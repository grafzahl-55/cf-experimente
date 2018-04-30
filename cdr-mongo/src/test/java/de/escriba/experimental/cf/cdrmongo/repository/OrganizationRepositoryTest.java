package de.escriba.experimental.cf.cdrmongo.repository;

import de.escriba.experimental.cf.cdrmongo.model.Organization;
import de.escriba.experimental.cf.cdrmongo.model.Space;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.BeforeTransaction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import javax.xml.ws.Holder;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataMongoTest
public class OrganizationRepositoryTest {

    @Autowired
    private OrganizationRepository orgRepo;

    @Before
    @After
    public void cleanupRepo(){
        this.orgRepo.deleteAll()
                .subscribe();
    }


    /**
     * @see {@link https://github.com/simonbasle-demos/reactor-by-example}
     */
    @Test
    public void shouldStoreAndReloadOrganization(){
        Organization org=new Organization("Dev-Org","Organization for the developers")
                .tag("Dev","Berlin")
                .addSpace(new Space("dev-space","Development").tag("Dev"))
                .addSpace(new Space("ci-space","Continuaous Integration").tag("CI"));
        Mono<Organization> saveResult=orgRepo.save(org);

        StepVerifier.create(saveResult)
                .consumeNextWith(loadedOrg->{
                    assertThat(loadedOrg).isNotNull();
                    assertThat(loadedOrg.getId()).isNotNull();
                    assertThat(loadedOrg.getName()).isEqualTo(org.getName());
                    assertThat(loadedOrg.getDescription()).isEqualTo(org.getDescription());
                    assertThat(loadedOrg.getTags()).isEqualTo(org.getTags());
                    assertThat(loadedOrg.getSpaces()).containsOnlyKeys("dev-space","ci-space");
                })
                .expectComplete()
                .verify();

    }

    @Test
    public void shouldPermitRemoveOrganizations(){
        Organization org=new Organization("Dev-Org","Organization for the developers")
                .tag("Dev","Berlin")
                .addSpace(new Space("dev-space","Development").tag("Dev"))
                .addSpace(new Space("ci-space","Continuaous Integration").tag("CI"));

        Organization org2=new Organization("Demo-Org","Organizatreion for the Demo Solutions")
                .tag("Demo","Berlin")
                .addSpace(new Space("demo-space","Demo Space").tag("Demo"));

        List<ObjectId> ids=new ArrayList<>();

        Flux<Organization> save=orgRepo.saveAll(Flux.just(org,org2));
        StepVerifier.create(save)
                .consumeNextWith(o->ids.add(o.getId()))
                .consumeNextWith(o->ids.add(o.getId()))
                .expectComplete()
                .verify();

        ObjectId id1=ids.get(0);
        ObjectId id2=ids.get(1);

        StepVerifier.create(orgRepo.deleteById(id1))
                .expectComplete()
                .verify();

        StepVerifier.create(orgRepo.existsById(id1))
                .expectNext(false)
                .verifyComplete();

        StepVerifier.create(orgRepo.existsById(id2))
                .expectNext(true)
                .verifyComplete();

    }

    @Test
    public void shouldAllowAddingSpaces(){
        Organization org=new Organization("Dev-Org","Organization for the developers")
                .tag("Dev","Berlin")
                .addSpace(new Space("dev-space","Development").tag("Dev"))
                .addSpace(new Space("ci-space","Continuaous Integration").tag("CI"));

        Holder<ObjectId> h=new Holder<>();
        StepVerifier.create(orgRepo.save(org))
                .consumeNextWith(o->h.value=o.getId())
                .verifyComplete();
        assertThat(h.value).isNotNull();

        // Add a space
        StepVerifier.create(orgRepo.findById(h.value))
                .consumeNextWith(o->{
                    o.addSpace(new Space("temp","temp"));
                    orgRepo.save(o).subscribe();
                }).verifyComplete();

        StepVerifier.create(orgRepo.findById(h.value))
                .consumeNextWith(o->{
                    assertThat(o.getSpaces()).isNotNull();
                    assertThat(o.getSpaces()).containsKey("temp");
                })
                .verifyComplete();

    }
    @Test
    public void shouldAllowRemovingSpaces(){
        Organization org=new Organization("Dev-Org","Organization for the developers")
                .tag("Dev","Berlin")
                .addSpace(new Space("dev-space","Development").tag("Dev"))
                .addSpace(new Space("ci-space","Continuaous Integration").tag("CI"));

        Holder<ObjectId> h=new Holder<>();
        StepVerifier.create(orgRepo.save(org))
                .consumeNextWith(o->h.value=o.getId())
                .verifyComplete();
        assertThat(h.value).isNotNull();

        // Add a space
        StepVerifier.create(orgRepo.findById(h.value))
                .consumeNextWith(o->{
                    o.removeSpace("ci-space");
                    orgRepo.save(o).subscribe();
                }).verifyComplete();

        StepVerifier.create(orgRepo.findById(h.value))
                .consumeNextWith(o->{
                    assertThat(o.getSpaces()).isNotNull();
                    assertThat(o.getSpaces()).containsOnlyKeys("dev-space");
                })
                .verifyComplete();

    }
}
