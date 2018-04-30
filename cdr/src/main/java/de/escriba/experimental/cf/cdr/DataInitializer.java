package de.escriba.experimental.cf.cdr;

import de.escriba.experimental.cf.cdr.model.ApplicationInstanceEntity;
import de.escriba.experimental.cf.cdr.model.ContextDefinitionEntity;
import de.escriba.experimental.cf.cdr.model.ContextDeploymentEntity;
import de.escriba.experimental.cf.cdr.model.ContextSourceEntity;
import de.escriba.experimental.cf.cdr.repository.ApplicationInstanceRepository;
import de.escriba.experimental.cf.cdr.repository.ContextDefinitionRepository;
import de.escriba.experimental.cf.cdr.repository.ContextDeploymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class DataInitializer implements CommandLineRunner {

    private final ContextDefinitionRepository repository;
    private final ApplicationInstanceRepository appRepo;
    private final ContextDeploymentRepository deployRepo;

    private final String[] serviceTypes={
        "runtime",
        "file-adapter",
        "rest-adapter",
        "jdbc-adapter",
        "authorization-server"
    };

    private final String[] tags={
            "Demo",
            "Development",
            "Experimental",
            "Stable",
            "Excel",
            "Standalone",
            "Jdbc-Backend",
            "Legalsuite"
    };

    private Random random=new Random();


    @Override
    @Transactional
    public void run(String... args){
        if( repository.count()==0L ){
            log.info("Leeres Repository vorgefunden: Generiere Testdaten.");

            for (int i=0; i<10; i++){
                ContextDefinitionEntity ent=new ContextDefinitionEntity()
                        .serviceType(randomServiceType())
                        .description("Test Dataset "+i)
                        .name("CDEF-"+i);
                ent.setTags(createRandomTags());
                byte[] main=("MAIN-"+i).getBytes();
                byte[] env=("ENV-"+i).getBytes();
                ent.sources(
                        new ContextSourceEntity().role("env").source(env,"text/plain"),
                        new ContextSourceEntity().role("main").source(main,"text/plain")
                );
                repository.save(ent);
                log.info("Neu: {}",ent);
            }

            // Create some app instances
            appRepo.save(new ApplicationInstanceEntity("eis-runtime","runtime","svr-escsf","E"));
            appRepo.save(new ApplicationInstanceEntity("authorization-serve","authotization-server","svr-escsf","E"));
            appRepo.save(new ApplicationInstanceEntity("fa","file-adapter","svr-escsf","E"));
            appRepo.save(new ApplicationInstanceEntity("sql-ada","jdbc-adapter","svr-escsf","E"));
            appRepo.save(new ApplicationInstanceEntity("eis-http","http-adapter","svr-escsf","E"));

            appRepo.findAll().forEach(appInstance->{
                List<ContextDefinitionEntity> defs=repository.findByServiceType(appInstance.getServiceType());
                if( !defs.isEmpty() ){
                    ContextDefinitionEntity cDef=defs.get(0);
                    ContextDeploymentEntity deployment=new ContextDeploymentEntity(appInstance,cDef).description("Deployed during autostart");
                    deployRepo.save(deployment);
                    log.info("Service deployed {}-->{}",cDef,appInstance);
                }
            });
        }
    }

    private String randomServiceType(){
        return serviceTypes[random.nextInt(serviceTypes.length)];
    }

    private List<String> createRandomTags(){
        int num=1+random.nextInt(3);
        Set<String> selected=new HashSet<>();
        for(int i=0; i<num; i++){
            int index=random.nextInt(tags.length);
            selected.add(tags[index]);
        }
        return new ArrayList<>(selected);
    }
}
