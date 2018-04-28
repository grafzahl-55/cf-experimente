package de.escriba.experimental.cf.cdr;

import de.escriba.experimental.cf.cdr.model.ContextDefinitionEntity;
import de.escriba.experimental.cf.cdr.model.ContextSourceEntity;
import de.escriba.experimental.cf.cdr.repository.ContextDefinitionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class DataInitializer implements CommandLineRunner {

    private final ContextDefinitionRepository repository;

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
    public void run(String... args) throws Exception {
        if( repository.count()==0L ){
            log.info("Leeres Repository vorgefunden: Generiere Testdaten.");
            for (int i=0; i<10; i++){
                ContextDefinitionEntity ent=new ContextDefinitionEntity()
                        .serviceType(randomServiceType())
                        .description("Test Dataset "+i);
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
