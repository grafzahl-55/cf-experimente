package de.escriba.experimental.cf.cdr;

import de.escriba.experimental.cf.cdr.model.ContextDefinitionEntity;
import de.escriba.experimental.cf.cdr.model.ContextSourceEntity;
import de.escriba.experimental.cf.cdr.repository.ContextDefinitionRepository;
import de.escriba.experimental.cf.cdr.repository.ContextDeploymentRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class UploadDownloadTest {


    @Autowired
    private TestRestTemplate rt;

    @Autowired
    private ContextDefinitionRepository repo;
    @Autowired
    private ContextDeploymentRepository deployRepo;

    private Long id;

    @Before
    @Transactional
    public void setup(){
        deployRepo.deleteAll();
        repo.deleteAll();
        ContextDefinitionEntity cDef=new ContextDefinitionEntity()
                .serviceType("runtime").description("Test").name("eis-runtime-TEST123")
                .sources(
                    new ContextSourceEntity().role("env").source("ENV".getBytes(),"text/plain"),
                    new ContextSourceEntity().role("main").source("MAIN".getBytes(),"text/plain")
                );
        cDef=repo.save(cDef);
        id=cDef.getId();
    }

    @Test
    public void downloadEnvAndExpectENV() {
        String s=rt.getForObject(sourceUrl(id,"env"), String.class);
        assertThat(s).isEqualTo("ENV");
        s=rt.getForObject(sourceUrl(id,"main"), String.class);
        assertThat(s).isEqualTo("MAIN");
    }

    @Test
    public void downloadUnknownContextAndExpect404(){
        Long id2=20000L;
        assertThat(repo.findById(id2).isPresent()).isFalse();

        ResponseEntity<String> resp=rt.exchange(sourceUrl(id2,"main"),HttpMethod.GET,new HttpEntity<>(""),String.class);
        assertThat(resp.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    public void downloadUnknownRoleAndExpect404(){

        ResponseEntity<String> resp=rt.exchange(sourceUrl(id,"foo"),HttpMethod.GET,new HttpEntity<>(""),String.class);
        assertThat(resp.getStatusCodeValue()).isEqualTo(404);
    }


    @Test
    public void uploadNewRoleAndExpectItInRepo(){
        String body="{'foo':'bar'}";
        HttpHeaders hdr=new HttpHeaders();
        hdr.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> ent=new HttpEntity<>(body,hdr);
        ResponseEntity<String> resp=rt.exchange(sourceUrl(id,"foo"),HttpMethod.PUT,ent,String.class);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        assertThat(rt.getForObject(sourceUrl(id,"foo"), String.class)).isEqualTo(body);

        rt.delete(sourceUrl(id,"foo"));

        resp=rt.exchange(sourceUrl(id,"foo"),HttpMethod.GET,new HttpEntity<>(""),String.class);
        assertThat(resp.getStatusCodeValue()).isEqualTo(404);

    }


    @Test
    public void expextThatDeletUnknownSourceYields404(){
        Long id2=20000L;
        assertThat(repo.findById(id2).isPresent()).isFalse();
        ResponseEntity<String> resp=rt.exchange(sourceUrl(id2,"main"),HttpMethod.DELETE,new HttpEntity<>(""),String.class);
        assertThat(resp.getStatusCodeValue()).isEqualTo(404);
        resp=rt.exchange(sourceUrl(id,"notexisting"),HttpMethod.DELETE,new HttpEntity<>(""),String.class);
        assertThat(resp.getStatusCodeValue()).isEqualTo(404);

    }


    private String sourceUrl(Long id, String role){
        //return String.format("/sources/%d/%s",id,role);
        return String.format("/api/contexts/%d/sources/%s",id,role);
    }
}
