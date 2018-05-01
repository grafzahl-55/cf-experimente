package de.escriba.experimental.cf.cdrmongo.services;

import de.escriba.experimental.cf.beans.cdr.OrganizationInfo;
import de.escriba.experimental.cf.beans.cdr.SpaceInfo;
import de.escriba.experimental.cf.cdrmongo.exceptions.*;
import de.escriba.experimental.cf.cdrmongo.model.Organization;
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
        Organization org = new Organization(orgInfo.getName(), orgInfo.getDescription())
                .tag(orgInfo.getTags());

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
                    org.setDescription(orgInfo.getDescription());
                    org.setTags(orgInfo.getTags());
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

    public Mono<ObjectId> createSpace(String organizationName,SpaceInfo spaceInfo) {
        assert spaceInfo.getName()!=null;
        return
                findOrganizationByName(organizationName)
                        .flatMap(org->{
                            if( org.containsSpace(spaceInfo.getName()) ){
                                throw new DuplicateSpaceNameException(organizationName, spaceInfo.getName());
                            } else {
                                org.addSpace(
                                        new Space(spaceInfo.getName(),spaceInfo.getDescription()).tag(spaceInfo.getTags())
                                );
                                return orgRepo.save(org);
                            }
                        }).map(organization -> {return organization.getId(); });

    }

    public Mono<ObjectId> updateSpace(String organizationName, SpaceInfo spaceInfo) {
        assert spaceInfo.getName()!=null;
        return findOrganizationByName(organizationName)
                .flatMap(org->{
                    Space spc=org.getSpaceByName(spaceInfo.getName())
                            .orElseThrow(()->new SpaceNotFoundException(organizationName, spaceInfo.getName()));
                    spc.setDescription(spaceInfo.getDescription());
                    spc.setTags(spaceInfo.getTags());
                    return orgRepo.save(org);
                }).map(organization -> organization.getId());

    }

    public Mono<ObjectId> deleteSpace(String organizationName, String spaceName){
        assert spaceName!=null;
        return findOrganizationByName(organizationName)
                .flatMap(org->{
                    if(org.containsSpace(spaceName)){
                        org.removeSpace(spaceName);
                    } else {
                        throw new SpaceNotFoundException(organizationName, spaceName);
                    }
                    return orgRepo.save(org);
                }).map(org->org.getId());
    }

    public Flux<SpaceInfo> listSpaceInfosForOrganization(String organizationName) {
        return findOrganizationByName(organizationName)
                .flatMapIterable(OrganizationService::createSpaceInfos);
    }

    public Mono<SpaceInfo> getSpaceInfo(String organizationName, String spaceName) {
        return findOrganizationByName(organizationName)
                .map(
                        org -> {
                            return org.getSpaceByName(spaceName)
                                    .map(OrganizationService::createSpaceInfo)
                                    .orElseThrow(() -> new SpaceNotFoundException(organizationName, spaceName));
                        }
                );
    }



    private static Iterable<? extends SpaceInfo> createSpaceInfos(Organization org) {
        if (org.getSpaces() == null) {
            return Collections.emptyList();
        } else {
            return org.getSpaces().values().stream()
                    .map(OrganizationService::createSpaceInfo).collect(Collectors.toList());
        }
    }

    private static SpaceInfo createSpaceInfo(Space spc) {
        return new SpaceInfo(spc.getName(), spc.getDescription()).tag(spc.getTags());
    }



}



