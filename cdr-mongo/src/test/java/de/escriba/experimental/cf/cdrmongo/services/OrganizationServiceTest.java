package de.escriba.experimental.cf.cdrmongo.services;

import de.escriba.experimental.cf.beans.cdr.OrganizationInfo;
import de.escriba.experimental.cf.beans.cdr.ServiceContainerInfo;
import de.escriba.experimental.cf.beans.cdr.SpaceInfo;
import de.escriba.experimental.cf.cdrmongo.exceptions.*;
import de.escriba.experimental.cf.cdrmongo.model.Organization;
import de.escriba.experimental.cf.cdrmongo.model.ServiceContainer;
import de.escriba.experimental.cf.cdrmongo.model.Space;
import de.escriba.experimental.cf.cdrmongo.repository.OrganizationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class OrganizationServiceTest {

    @Autowired
    private OrganizationService orgService;

    @Autowired
    private OrganizationRepository orgRepo;


    @Before
    public void cleanup() {
        StepVerifier.create(orgRepo.deleteAll()).verifyComplete();
    }

    @Test
    public void shouldCreateOrganizationFromOrgInfo() {
        OrganizationInfo orgInfo = new OrganizationInfo("mu.org", "Testorganisation")
                .tag("Junit", "Experimental");

        StepVerifier.create(
                orgService.createOrganization(orgInfo)
                        .flatMap(objId -> orgRepo.findById(objId))
                        .map(org -> org.getTags())
        ).expectNext(orgInfo.getTags())
                .verifyComplete();

        StepVerifier.create(
                orgService.findOrganizationByName("mu.org")
        ).consumeNextWith(
                organization -> {
                    assertThat(organization.getName()).isEqualTo(orgInfo.getName());
                    assertThat(organization.getDescription()).isEqualTo(orgInfo.getDescription());
                    assertThat(organization.getTags()).isEqualTo(orgInfo.getTags());
                    assertThat(organization.getSpaces()).isEmpty();
                }
        ).verifyComplete();
    }

    @Test
    public void shouldRefuseToCreateDuplicateNames() {
        OrganizationInfo orgInfo = new OrganizationInfo("mu.org", "Testorganisation")
                .tag("Junit", "Experimental");
        StepVerifier.create(
                orgService.createOrganization(orgInfo)
        ).consumeNextWith(
                obId -> {
                    assertThat(obId).isNotNull();
                }

        ).verifyComplete();

        OrganizationInfo orgInfo2 = new OrganizationInfo("mu.org", "Testorganisation 3")
                .tag("Experimental3");

        StepVerifier.create(orgService.createOrganization(orgInfo2))
                .expectError(DuplicateOrganizationNameException.class)
                .verify();
    }

    @Test
    public void shouldSignalOrganizationNotFoundException() {
        StepVerifier.create(orgService.findOrganizationByName("thisnamedoesnotexist"))
                .expectError(OrganizationNotFoundException.class)
                .verify();
    }

    @Test
    public void shouldFailOnUpdatingNonExostingOrganization() {
        OrganizationInfo orgInf = new OrganizationInfo("namedoesnotexiat", "New description");
        StepVerifier.create(orgService.updateOrganzation(orgInf))
                .expectError(OrganizationNotFoundException.class)
                .verify();
    }

    @Test
    public void shouldUpdateExistingOrganizations() {
        createTestOrganization(true);

        OrganizationInfo orgInf = new OrganizationInfo("mu.org", "NEW").tag("NEW");
        StepVerifier.create(orgService.updateOrganzation(orgInf))
                .consumeNextWith(o -> {
                })
                .verifyComplete();

        StepVerifier.create(orgRepo.findFirstByName("mu.org"))
                .consumeNextWith(o -> {
                    assertThat(o.getDescription()).isEqualTo(orgInf.getDescription());
                    assertThat(o.getTags()).isEqualTo(orgInf.getTags());
                    assertThat(o.getSpaces()).isNotNull();
                    assertThat(o.containsSpace("s1"));
                    assertThat(o.containsSpace("s2"));
                    assertThat(o.getSpaces().size()).isEqualTo(2);
                    assertThat(o.getSpaceByName("s1")).isPresent();
                    assertThat(o.getSpaceByName("s1").get().containsServiceContainerName("c1"));
                    assertThat(o.getSpaceByName("s1").get().containsServiceContainerName("c2"));
                    assertThat(o.getSpaceByName("s2")).isPresent();
                    assertThat(o.getSpaceByName("s2").get().containsServiceContainerName("c1"));
                })
                .verifyComplete();

    }

    @Test
    public void shouldFindAllOrganizations() {
        for (int i = 1; i <= 10; i++) {
            OrganizationInfo oi = new OrganizationInfo("org" + i, "descr" + i).tag("tag" + i);
            StepVerifier.create(orgService.createOrganization(oi))
                    .consumeNextWith(objId -> {
                    })
                    .verifyComplete();
        }

        Flux<Organization> all = orgService.findAll();
        StepVerifier.create(all.count())
                .expectNext(10L);

    }

    @Test
    public void shouldBeAbleToDeleteOrganizations() {
        createTestOrganization(false);
        StepVerifier.create(orgService.deleteOrganization("mu.org")
                .hasElement()
        ).expectNext(false).verifyComplete();
        assertThatOrganizationIsGone();

    }

    @Test
    public void shouldRefuseToDeleteNonEmptyOrganizations() {
        createTestOrganization(true);
        StepVerifier.create(orgService.deleteOrganization("mu.org"))
                .expectError(OrganizationNotEmptyException.class)
                .verify();
    }

    @Test
    public void shouldDeleteNonEmptyOrganizationsWithForceFlag() {
        createTestOrganization(true);
        StepVerifier.create(orgService.deleteOrganizationRecursively("mu.org")
                .hasElement()
        ).expectNext(false).verifyComplete();


        assertThatOrganizationIsGone();
    }

    @Test
    public void shouldRaiseErrorOnDeletingNonExistingOrg() {
        StepVerifier.create(orgService.deleteOrganization("notexisting"))
                .expectError(OrganizationNotFoundException.class)
                .verify();
    }

    @Test
    public void shouldRaiseErrorOnDeletingNonExistingOrgRecursively() {
        StepVerifier.create(orgService.deleteOrganizationRecursively("notexisting"))
                .expectError(OrganizationNotFoundException.class)
                .verify();
    }

    @Test
    public void shouldBeAbleToListSpaces() {
        createTestOrganization(true);
        StepVerifier.create(
                orgService.listSpaceInfosForOrganization("mu.org")
                        .count()
        )
                .expectNext(2L)
                .verifyComplete();

    }

    @Test
    public void listSpacesShouldRaiseErrorOnNonExistingOrganization() {
        createTestOrganization(false);
        StepVerifier.create(
                orgService.listSpaceInfosForOrganization("notfound")
        )
                .expectError(OrganizationNotFoundException.class)
                .verify();
    }

    @Test
    public void shouldBeAbleToRetrieveExtendedSpaceInfo() {
        createTestOrganization(true);
        StepVerifier.create(
                orgService.findSpaceInfo("mu.org", "s2")
        )
                .consumeNextWith(spaceInfo -> {
                    assertThat(spaceInfo.getName()).isEqualTo("s2");
                    assertThat(spaceInfo.getTags()).containsExactly("bar");
                    assertThat(spaceInfo.getServiceContainers()).isNotNull();
                    assertThat(spaceInfo.getServiceContainers()).isNotEmpty();

                })
                .verifyComplete();
    }

    @Test
    public void getSpaceInfoShouldRaiseErrorOnNonExistingSpace() {
        createTestOrganization(true);
        StepVerifier.create(
                orgService.findSpaceInfo("mu.org", "s77")
        )
                .expectError(SpaceNotFoundException.class)
                .verify();
    }

    @Test
    public void getSpaceInfoShouldRaiseErrorOnNonExistingOrganization() {
        StepVerifier.create(
                orgService.findSpaceInfo("mu.org-notexisting", "s77")
        )
                .expectError(OrganizationNotFoundException.class)
                .verify();

    }

    @Test
    public void shouldBeAbleToCreateSpaces() {
        createTestOrganization(false);
        SpaceInfo spaceInfo = new SpaceInfo("newSpace", "newDescr").tag("one", "two");

        StepVerifier.create(orgService.createSpace("mu.org", spaceInfo))
                .consumeNextWith(aVoid -> {
                })
                .verifyComplete();

        StepVerifier.create(orgService.findSpaceInfo("mu.org", "newSpace"))
                .consumeNextWith(spaceInfo1 -> {
                    assertThat(spaceInfo1.getName()).isEqualTo("newSpace");
                    assertThat(spaceInfo1.getDescription()).isEqualTo("newDescr");
                    assertThat(spaceInfo1.getTags()).containsExactlyInAnyOrder("one", "two");

                })
                .verifyComplete();

    }

    @Test
    public void shouldRefuseToOverwriteExistingSpaceOnCreate() {
        createTestOrganization(true);
        SpaceInfo spaceInfo = new SpaceInfo("s1", "newDescr").tag("one", "two");

        StepVerifier.create(orgService.createSpace("mu.org", spaceInfo))
                .expectError(DuplicateSpaceNameException.class)
                .verify();
    }

    @Test
    public void shouldRefuseToCreateSpaceInNotExistigOrg() {
        SpaceInfo spaceInfo = new SpaceInfo("s1", "newDescr").tag("one", "two");

        StepVerifier.create(orgService.createSpace("mu.org.NOTEXISTS", spaceInfo))
                .expectError(OrganizationNotFoundException.class)
                .verify();

    }


    @Test
    public void shouldBeAbleToUpdateSpaces() {
        createTestOrganization(true);

        SpaceInfo spc = new SpaceInfo("s1", "newDescr").tag("new1", "new2");
        StepVerifier.create(orgService.updateSpace("mu.org", spc))
                .consumeNextWith(objectId -> {
                })
                .verifyComplete();

        StepVerifier.create(
                orgService.findOrganizationByName("mu.org")
        ).consumeNextWith(organization -> {
            Space s = organization.getSpaceByName("s1").get();
            assertThat(s.getName()).isEqualTo("s1");
            assertThat(s.getDescription()).isEqualTo("newDescr");
            assertThat(s.getTags()).containsExactlyInAnyOrder("new1", "new2");
        }).verifyComplete();
    }

    @Test
    public void shouldRefuseToUpdateNotExistingSpace() {
        createTestOrganization(true);

        SpaceInfo spc = new SpaceInfo("s177777", "newDescr").tag("new1", "new2");
        StepVerifier.create(orgService.updateSpace("mu.org", spc))
                .expectError(SpaceNotFoundException.class)
                .verify();

    }

    @Test
    public void shouldBeAbleToRemoveSpaces() {
        createTestOrganization(true);
        StepVerifier.create(
                orgService.deleteSpace("mu.org", "s2")
        ).consumeNextWith(objectId -> {
        })
                .verifyComplete();

        StepVerifier.create(
                orgService.findOrganizationByName("mu.org")
        )
                .consumeNextWith(organization -> {
                    assertThat(organization.containsSpace("s2")).isFalse();
                    assertThat(organization.containsSpace("s1")).isTrue();

                })
                .verifyComplete();
    }

    @Test
    public void shouldRaiseErrorOnRemoveNonExistingSpace() {
        createTestOrganization(true);
        StepVerifier.create(
                orgService.deleteSpace("mu.org", "s9999999")
        )
                .expectError(SpaceNotFoundException.class)
                .verify();
    }

    @Test
    public void shouldBeAbleToCreateServiceContainer(){
        createTestOrganization(true);
        ServiceContainerInfo scInfo=new ServiceContainerInfo("c3","t3","Neuer Container")
                .tag("DEV");

        StepVerifier.create(
                orgService.createServiceContainer("mu.org","s1",scInfo)
        ).consumeNextWith(objectId -> {}).verifyComplete();

        StepVerifier.create(
                orgService.findSpaceInfo("mu.org","s1")
        ).consumeNextWith(xSp->{
            assertThat(xSp.getServiceContainers()).isNotNull();
            assertThat(xSp.getServiceContainers().size()).isEqualTo(3);
            ServiceContainerInfo scInf1=xSp.getServiceContainers().get("c3");
            assertThat(scInf1).isNotNull();
            assertThat(scInf1.getName()).isEqualTo(scInfo.getName());
            assertThat(scInf1.getDescription()).isEqualTo(scInfo.getDescription());
            assertThat(scInf1.getServiceType()).isEqualTo(scInfo.getServiceType());
            assertThat(scInf1.getTags()).isEqualTo(scInfo.getTags());
        }).verifyComplete();
    }

    @Test
    public void shouldRefuseToCreateServiceContainerInNonExistingOrg(){
        ServiceContainerInfo scInfo=new ServiceContainerInfo("c3","t3","Neuer Container")
                .tag("DEV");

        StepVerifier.create(
                orgService.createServiceContainer("mu.org-NICHTDA","s1",scInfo)
        ).expectError(OrganizationNotFoundException.class).verify();

    }

    @Test
    public void shouldRefuseToCreateServiceContainerInNonExistingSpace(){
        createTestOrganization(true);
        ServiceContainerInfo scInfo=new ServiceContainerInfo("c3","t3","Neuer Container")
                .tag("DEV");

        StepVerifier.create(
                orgService.createServiceContainer("mu.org","s1-NICHTDA",scInfo)
        ).expectError(SpaceNotFoundException.class).verify();

    }

    @Test
    public void shouldRefuseToCreateServiceContainerWIthDuplicateName(){
        createTestOrganization(true);
        ServiceContainerInfo scInfo=new ServiceContainerInfo("c2","t3","Neuer Container")
                .tag("DEV");

        StepVerifier.create(
                orgService.createServiceContainer("mu.org","s1",scInfo)
        ).expectError(DuplicateServiceContainerNameException.class).verify();
    }

    @Test
    public void shouldBeAbleToUpdateServiceContainer(){
        createTestOrganization(true);
        ServiceContainerInfo scInfo=new ServiceContainerInfo("c2","t3Update","Update Container")
                .tag("DEV");
        StepVerifier.create(
                orgService.updateServiceContainer("mu.org","s1",scInfo)
        ).consumeNextWith(objectId -> {}).verifyComplete();

        StepVerifier.create(
                orgService.findSpaceInfo("mu.org","s1")
        )
                .consumeNextWith(xSp->{
                    assertThat(xSp.getServiceContainers()).isNotNull();
                    ServiceContainerInfo sci=xSp.getServiceContainers().get("c2");
                    assertThat(sci).isNotNull();
                    assertThat(sci.getServiceType()).isEqualTo("t3Update");
                    assertThat(sci.getDescription()).isEqualTo("Update Container");
                    assertThat(sci.getTags()).containsExactly("DEV");
                }).verifyComplete();

    }

    @Test
    public void shouldRefuseToUpdateServiceContainerInNonExistingOrg(){
        ServiceContainerInfo scInfo=new ServiceContainerInfo("c2","t3Update","Update Container")
                .tag("DEV");
        StepVerifier.create(
                orgService.updateServiceContainer("mu.org-XXXXX","s1",scInfo)
        ).expectError(OrganizationNotFoundException.class).verify();

    }

    @Test
    public void shouldRefuseToUpdateServiceContainerInNonExistingSpace(){
        createTestOrganization(true);
        ServiceContainerInfo scInfo=new ServiceContainerInfo("c2","t3Update","Update Container")
                .tag("DEV");
        StepVerifier.create(
                orgService.updateServiceContainer("mu.org","s1XXX",scInfo)
        ).expectError(SpaceNotFoundException.class).verify();

    }

    @Test
    public void shouldRefuseToUpdateNonExistingServiceContainer(){
        createTestOrganization(true);
        ServiceContainerInfo scInfo=new ServiceContainerInfo("c2XXX","t3Update","Update Container")
                .tag("DEV");
        StepVerifier.create(
                orgService.updateServiceContainer("mu.org","s1",scInfo)
        ).expectError(ServiceContainerNotFoundException.class).verify();

    }

    public void shouldBeAbleToDeleteServiceContainer(){
        createTestOrganization(true);
        StepVerifier.create(
                orgService.deleteServiceContainer("mu.org","s1","c1")
        ).consumeNextWith(objectId -> {}).verifyComplete();

        StepVerifier.create(
                orgService.findSpaceInfo("mu.org","s1")
        ).consumeNextWith(xSpc->{
            assertThat(xSpc.getServiceContainers().containsKey("s1")).isFalse();
            assertThat(xSpc.getServiceContainers().containsKey("s2")).isTrue();
        }).verifyComplete();
    }

    @Test
    public void shouldRefuseToDeleteServiceContainerFromNonExistingOrg(){
        StepVerifier.create(
                orgService.deleteServiceContainer("mu.orgXXX","s1","c1")
        ).expectError(OrganizationNotFoundException.class).verify();
    }

    @Test
    public void shouldRefuseToRemoveServiceContainerFromNonExistingSpace(){
        createTestOrganization(true);
        StepVerifier.create(
                orgService.deleteServiceContainer("mu.org","s1XXX","c1")
        ).expectError(SpaceNotFoundException.class).verify();

    }

    @Test
    public void shouldRefuseToRemoveNonExistingServiceContainer(){
        createTestOrganization(true);
        StepVerifier.create(
                orgService.deleteServiceContainer("mu.org","s1","c1XXXX")
        ).expectError(ServiceContainerNotFoundException.class).verify();
    }


    @Test
    public void shouldBeAbleToLoadServiceContainerInfo(){
        createTestOrganization(true);
        StepVerifier.create(
                orgService.findServiceContainerInfo("mu.org","s2","c1")
        ).consumeNextWith(sci->{
            assertThat(sci.getName()).isEqualTo("c1");
            assertThat(sci.getServiceType()).isEqualTo("type1");
            assertThat(sci.getTags()).containsExactly("tag1");
        }).verifyComplete();
    }

    @Test
    public void shouldRasieErrorOnServiceContainerNotFound(){
        createTestOrganization(true);
        StepVerifier.create(
                orgService.findServiceContainerInfo("mu.org","s2","c22")
        ).expectError(ServiceContainerNotFoundException.class).verify();
    }


    private void createTestOrganization(boolean withContent) {
        Organization org = new Organization("mu.org", "OLD").tag("OLD");
        if (withContent) {
            org.addSpace(
                    new Space("s1").tag("foo")
                            .addContainer(
                                    new ServiceContainer("c1", "type1").tag("tag1"),
                                    new ServiceContainer("c2", "type2").tag("tag2")
                            )
            )
                    .addSpace(
                            new Space("s2").tag("bar")
                                    .addContainer(
                                            new ServiceContainer("c1", "type1").tag("tag1")
                                    )
                    );
        }

        StepVerifier.create(orgRepo.save(org))
                .consumeNextWith(o -> {
                })
                .verifyComplete();

    }

    private void assertThatOrganizationIsGone() {
        StepVerifier.create(orgRepo.findFirstByName("mu.org").hasElement())
                .expectNext(false)
                .verifyComplete();
    }
}
