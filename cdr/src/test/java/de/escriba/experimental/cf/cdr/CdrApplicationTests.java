package de.escriba.experimental.cf.cdr;

import de.escriba.experimental.cf.cdr.model.ContextDefinitionEntity;
import de.escriba.experimental.cf.cdr.model.ContextSourceEntity;
import de.escriba.experimental.cf.cdr.repository.ContextDefinitionRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class CdrApplicationTests {

    @Autowired
    private ContextDefinitionRepository repo;

    @Test
    @Transactional
    public void contextLoads() {
        log.info("Running...");
        repo.findAll().forEach(e->{
            log.info("**** {}",e);
        });
    }

    @Test
    public void saveEntityAndExpectItCanBeLoaded() {
        ContextDefinitionEntity cDef1 = cretateEnty("eis-runtime", "Demo Environment Excel Demo", "E", "Demo");
        cDef1 = repo.save(cDef1);
        Long id = cDef1.getId();
        assertThat(id).isNotNull();

        ContextDefinitionEntity cDef2 = repo.findById(id).get();
        assertThat(cDef2.getServiceType()).isEqualTo("eis-runtime");
        assertThat(cDef2.getDescription()).contains("Excel Demo");
        assertThat(cDef2.getTags()).containsExactly("E", "Demo");
    }

    @Test
    public void saveAndDeleteEntityAndExpectItCannotBeFound() {
        ContextDefinitionEntity cDef1 = cretateEnty("eis-runtime", "Demo Environment Excel Demo", "E", "Demo");
        cDef1 = repo.save(cDef1);
        Long id = cDef1.getId();
        assertThat(id).isNotNull();

        repo.deleteById(id);
        Optional<ContextDefinitionEntity> cDef2 = repo.findById(id);
        assertThat(cDef2).isNotPresent();


    }

    @Test
    public void saveAndExpectThatTageCanBeUpdated() {
        ContextDefinitionEntity cDef1 = cretateEnty("eis-runtime", "Demo Environment Excel Demo", "E", "Demo");
        cDef1 = repo.save(cDef1);
        Long id = cDef1.getId();
        assertThat(id).isNotNull();

        ContextDefinitionEntity cDef2 = repo.findById(id).get();
        assertThat(cDef2.getTags()).containsExactly("E", "Demo");
        cDef2.description("Modified");
        cDef2.tags("Demo", "Q", "experimental");
        repo.save(cDef2);

        ContextDefinitionEntity cDef3 = repo.findById(id).get();
        assertThat(cDef3.getTags()).containsExactly("Demo", "Q", "experimental");
        assertThat(cDef3.getDescription()).isEqualTo("Modified");
    }


    @Test
    public void saveSomeEntitiesAndExpectWeCanFindByTags(){
        repo.deleteAll();
        ContextDefinitionEntity e1=cretateEnty("type1",null,"Demo","Tag4","Tag7");
        ContextDefinitionEntity e2=cretateEnty("type2",null,"Tag5","Tag7");
        ContextDefinitionEntity e3=cretateEnty("type3","descr3","Demo");
        ContextDefinitionEntity e4=cretateEnty("type4",null); // No tags at all
        repo.saveAll(Arrays.asList(e1,e2,e3,e4));

        List<ContextDefinitionEntity> demo=repo.findByTags("Demo");
        assertThat(demo.size()).isEqualTo(2);
        assertThat(demo.stream().map(ContextDefinitionEntity::getServiceType)).containsExactly("type1","type3");


    }

    @Test
    @Transactional
    public void saveEntityWithSourcesAndExpectThemToBeLoaded(){
        ContextDefinitionEntity cDef=cretateEnty("runtime","Test","Sources")
                .sources(
                        new ContextSourceEntity().role("env").source("THE ENV".getBytes(),"application/xml"),
                        new ContextSourceEntity().role("main").source("MAIN CONFIG".getBytes(),"text/plain")
                        );

        cDef=repo.save(cDef);
        Long id=cDef.getId();
        assertThat(id).isNotNull();

        cDef=repo.findById(id).get();
        assertThat(cDef.getSources().size()).isEqualTo(2);

        assertThat(cDef.getSources().stream().map(ContextSourceEntity::getSource).map(data->new String(data))).contains("THE ENV","MAIN CONFIG");
        assertThat(cDef.getSources().stream().map(ContextSourceEntity::getContentType)).contains("application/xml","text/plain");

    }

    private ContextDefinitionEntity cretateEnty(String serviceType, String description, String... tags) {
        return new ContextDefinitionEntity()
                .serviceType(serviceType)
                .description(description)
                .tags(tags);
    }

}
