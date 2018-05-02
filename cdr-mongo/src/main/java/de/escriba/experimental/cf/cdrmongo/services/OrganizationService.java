package de.escriba.experimental.cf.cdrmongo.services;

import de.escriba.experimental.cf.beans.cdr.ExtendedSpaceInfo;
import de.escriba.experimental.cf.beans.cdr.OrganizationInfo;
import de.escriba.experimental.cf.beans.cdr.ServiceContainerInfo;
import de.escriba.experimental.cf.beans.cdr.SpaceInfo;
import de.escriba.experimental.cf.cdrmongo.exceptions.*;
import de.escriba.experimental.cf.cdrmongo.model.Converters;
import de.escriba.experimental.cf.cdrmongo.model.Organization;
import de.escriba.experimental.cf.cdrmongo.model.ServiceContainer;
import de.escriba.experimental.cf.cdrmongo.model.Space;
import de.escriba.experimental.cf.cdrmongo.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Zugang zum {@link de.escriba.experimental.cf.cdrmongo.repository.OrganizationRepository}
 */
@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository orgRepo;
    private final Converters converters;

    public Flux<Organization> findAll() {
        return orgRepo.findAll();
    }

    public Mono<Organization> findOrganizationByName(String organizationName) {
        assert organizationName != null;
        return orgRepo.findFirstByName(organizationName).switchIfEmpty(
                Mono.error(new OrganizationNotFoundException(organizationName))
        );
    }

    public Mono<ObjectId> createOrganization(OrganizationInfo orgInfo) {
        assert orgInfo.getName() != null;
        Organization org = new Organization(orgInfo.getName());
        converters.patch(orgInfo, org);

        return orgRepo.save(org)
                .map(o -> o.getId())
                .doOnError(DuplicateKeyException.class,
                        e -> {
                            throw new DuplicateOrganizationNameException(orgInfo.getName());
                        }
                );
    }

    public Mono<Organization> updateOrganzation(OrganizationInfo orgInfo) {
        return findOrganizationByName(orgInfo.getName())
                .flatMap(org -> {
                    converters.patch(orgInfo, org);
                    return orgRepo.save(org);
                });
    }


    public Mono<Void> deleteOrganization(String organizationName) {
        return findOrganizationByName(organizationName)
                .flatMap(org -> {
                    if (org.getSpaces() != null && !org.getSpaces().isEmpty()) {
                        throw new OrganizationNotEmptyException(organizationName);
                    }
                    return orgRepo.deleteById(org.getId());
                });

    }


    public Mono<Void> deleteOrganizationRecursively(String organizationName) {
        return findOrganizationByName(organizationName)
                .flatMap(
                        org -> orgRepo.delete(org)
                );
    }

    public Mono<ObjectId> createSpace(String organizationName, SpaceInfo spaceInfo) {
        assert spaceInfo.getName() != null;
        return
                findOrganizationByName(organizationName)
                        .flatMap(org -> {
                            if (org.containsSpace(spaceInfo.getName())) {
                                throw new DuplicateSpaceNameException(organizationName, spaceInfo.getName());
                            } else {
                                Space space = new Space(spaceInfo.getName());
                                converters.patch(spaceInfo, space);
                                org.addSpace(space);
                                return orgRepo.save(org);
                            }
                        }).map(organization -> {
                    return organization.getId();
                });

    }

    public Mono<ObjectId> updateSpace(String organizationName, SpaceInfo spaceInfo) {
        assert spaceInfo.getName() != null;
        return findOrganizationByName(organizationName)
                .flatMap(org -> {
                    Space spc = org.getSpaceByName(spaceInfo.getName())
                            .orElseThrow(() -> new SpaceNotFoundException(organizationName, spaceInfo.getName()));
                    converters.patch(spaceInfo, spc);
                    return orgRepo.save(org);
                }).map(organization -> organization.getId());

    }

    public Mono<ObjectId> deleteSpace(String organizationName, String spaceName) {
        assert spaceName != null;
        return findOrganizationByName(organizationName)
                .flatMap(org -> {
                    if (org.containsSpace(spaceName)) {
                        org.removeSpace(spaceName);
                    } else {
                        throw new SpaceNotFoundException(organizationName, spaceName);
                    }
                    return orgRepo.save(org);
                }).map(org -> org.getId());
    }

    public Flux<ExtendedSpaceInfo> listSpaceInfosForOrganization(String organizationName) {
        return findOrganizationByName(organizationName)
                .flatMapIterable(this::createSpaceInfos);
    }

    public Mono<ExtendedSpaceInfo> findSpaceInfo(String organizationName, String spaceName) {
        return findOrganizationByName(organizationName)
                .map(
                        org -> {
                            return org.getSpaceByName(spaceName)
                                    .map(converters::toExtendedInfo)
                                    .orElseThrow(() -> new SpaceNotFoundException(organizationName, spaceName));
                        }
                );
    }

    public Mono<ServiceContainerInfo> findServiceContainerInfo(String organizationName, String spaceName, String serviceContainerName) {
        assert serviceContainerName != null;
        return findSpaceInfo(organizationName, spaceName)
                .map(
                        extendedSpaceInfo -> {
                            if (extendedSpaceInfo.getServiceContainers() == null ||
                                    !extendedSpaceInfo.getServiceContainers().containsKey(serviceContainerName)) {
                                throw new ServiceContainerNotFoundException(organizationName, spaceName, serviceContainerName);
                            } else {
                                return extendedSpaceInfo.getServiceContainers().get(serviceContainerName);
                            }
                        }
                );
    }

    public Mono<ObjectId> createServiceContainer(String organizationName, String spaceName, ServiceContainerInfo scInfo) {
        assert scInfo.getName() != null;
        return findOrganizationByName(organizationName)
                .flatMap(
                        organization -> {
                            if (organization.containsSpace(spaceName)) {
                                Space space = organization.getSpaces().get(spaceName);
                                if (space.containsServiceContainerName(scInfo.getName())) {
                                    throw new DuplicateServiceContainerNameException(organizationName, spaceName, scInfo.getName());
                                } else {
                                    ServiceContainer sc = new ServiceContainer(scInfo.getName(), scInfo.getServiceType());
                                    converters.patch(scInfo, sc);
                                    space.addContainer(sc);
                                }
                            } else {
                                throw new SpaceNotFoundException(organizationName, spaceName);
                            }

                            return orgRepo.save(organization);
                        }
                ).map(o -> o.getId());
    }

    public Mono<ObjectId> updateServiceContainer(String organizationName, String spaceName, ServiceContainerInfo scInfo) {
        assert scInfo.getName() != null;
        return findOrganizationByName(organizationName)
                .flatMap(
                        organization -> {
                            if (organization.containsSpace(spaceName)) {
                                Space space = organization.getSpaces().get(spaceName);
                                if (space.containsServiceContainerName(scInfo.getName())) {
                                    ServiceContainer sc = space.getServiceContainers().get(scInfo.getName());
                                    converters.patch(scInfo, sc);
                                } else {
                                    throw new ServiceContainerNotFoundException(organizationName, spaceName, scInfo.getName());
                                }
                            } else {
                                throw new SpaceNotFoundException(organizationName, spaceName);
                            }

                            return orgRepo.save(organization);
                        }
                ).map(o -> o.getId());
    }

    public Mono<ObjectId> deleteServiceContainer(String organizationName, String spaceName, String containerName) {
        assert containerName != null;
        return findOrganizationByName(organizationName)
                .flatMap(
                        organization -> {
                            if (organization.containsSpace(spaceName)) {
                                Space space = organization.getSpaces().get(spaceName);
                                if (space.containsServiceContainerName(containerName)) {
                                    space.getServiceContainers().remove(containerName);
                                } else {
                                    throw new ServiceContainerNotFoundException(organizationName, spaceName, containerName);
                                }
                            } else {
                                throw new SpaceNotFoundException(organizationName, spaceName);
                            }

                            return orgRepo.save(organization);
                        }
                ).map(o -> o.getId());
    }

    private Iterable<? extends ExtendedSpaceInfo> createSpaceInfos(Organization org) {
        if (org.getSpaces() == null) {
            return Collections.emptyList();
        } else {
            return org.getSpaces().values().stream()
                    .map(converters::toExtendedInfo).collect(Collectors.toList());
        }
    }


}



