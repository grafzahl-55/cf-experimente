package de.escriba.experimental.cf.cdr;

import de.escriba.experimental.cf.cdr.model.ContextDefinitionEntity;
import de.escriba.experimental.cf.cdr.model.ContextSourceEntity;
import de.escriba.experimental.cf.cdr.repository.ContextDefinitionRepository;
import de.escriba.experimental.cf.cdr.repository.ContextDeploymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
//@ActiveProfiles("mysql")
public class CdrApplicationTests {

    @Autowired
    private ContextDefinitionRepository repo;
    @Autowired
    private ContextDeploymentRepository deployRepo;

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
        ContextDefinitionEntity cDef1 = createEntity("eis-runtime", "Demo Environment Excel Demo", "E", "Demo");
        cDef1 = repo.save(cDef1);
        Long id = cDef1.getId();
        assertThat(id).isNotNull();

        ContextDefinitionEntity cDef2 = repo.findById(id).get();
        assertThat(cDef2.getServiceType()).isEqualTo("eis-runtime");
        assertThat(cDef2.getDescription()).contains("Excel Demo");
        assertThat(cDef2.getTags()).contains("E", "Demo");
    }

    @Test
    public void saveAndDeleteEntityAndExpectItCannotBeFound() {
        ContextDefinitionEntity cDef1 = createEntity("eis-runtime", "Demo Environment Excel Demo", "E", "Demo");
        cDef1 = repo.save(cDef1);
        Long id = cDef1.getId();
        assertThat(id).isNotNull();

        repo.deleteById(id);
        Optional<ContextDefinitionEntity> cDef2 = repo.findById(id);
        assertThat(cDef2).isNotPresent();


    }

    @Test
    public void saveAndExpectThatTageCanBeUpdated() {
        ContextDefinitionEntity cDef1 = createEntity("eis-runtime", "Demo Environment Excel Demo", "E", "Demo");
        cDef1 = repo.save(cDef1);
        Long id = cDef1.getId();
        assertThat(id).isNotNull();

        ContextDefinitionEntity cDef2 = repo.findById(id).get();
        assertThat(cDef2.getTags()).contains("E", "Demo");
        cDef2.description("Modified");
        cDef2.tags("Demo", "Q", "experimental");
        repo.save(cDef2);

        ContextDefinitionEntity cDef3 = repo.findById(id).get();
        assertThat(cDef3.getTags()).contains("Demo", "Q", "experimental");
        assertThat(cDef3.getDescription()).isEqualTo("Modified");
    }


    @Test
    public void saveSomeEntitiesAndExpectWeCanFindByTags(){
        deployRepo.deleteAll();
        repo.deleteAll();
        ContextDefinitionEntity e1= createEntity("type1",null,"Demo","Tag4","Tag7");
        ContextDefinitionEntity e2= createEntity("type2",null,"Tag5","Tag7");
        ContextDefinitionEntity e3= createEntity("type3","descr3","Demo");
        ContextDefinitionEntity e4= createEntity("type4",null); // No tags at all
        repo.saveAll(Arrays.asList(e1,e2,e3,e4));

        List<ContextDefinitionEntity> demo=repo.findByTags("Demo");
        assertThat(demo.size()).isEqualTo(2);
        assertThat(demo.stream().map(ContextDefinitionEntity::getServiceType)).contains("type1","type3");


    }

    @Test
    @Transactional
    public void saveEntityWithSourcesAndExpectThemToBeLoaded(){
        ContextDefinitionEntity cDef= createEntity("runtime","Test","Sources")
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

    private int counter=0;

    private ContextDefinitionEntity createEntity(String serviceType, String description, String... tags) {
        return new ContextDefinitionEntity()
                .serviceType(serviceType)
                .name(serviceType+"TEST"+(counter++))
                .description(description)
                .tags(tags);
    }

}
