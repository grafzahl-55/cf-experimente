package de.escriba.experimental.cf.cdrmongo.controller;

import de.escriba.experimental.cf.cdrmongo.services.GridFSService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.ByteArrayInputStream;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureWebTestClient
public class FileDownloadControllerTest {


    @Autowired
    WebTestClient webClient;

    @Autowired
    GridFSService gridFs;

    @Test
    public void shouldDeliverExistingFiles(){
        byte[] testdata=new byte[1234];
        Random rnd=new Random();
        rnd.nextBytes(testdata);

        String fileId=gridFs.upload(new ByteArrayInputStream(testdata),MediaType.APPLICATION_JSON);
        assertThat(fileId).isNotEmpty();

        webClient.get().uri("/api/files/{fileId}",fileId)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Resource.class)
                .consumeWith(result->{
                    try {
                        Resource resource = result.getResponseBody();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        IOUtils.copy(resource.getInputStream(), baos);
                        baos.close();
                        byte[] readData=baos.toByteArray();
                        assertThat(readData).isEqualTo(testdata);
                    } catch(Exception e){
                        throw new RuntimeException(e);
                    }

                });
    }

    @Test
    public void shouldSet404StatusOnFileNotFound(){
        webClient.get().uri("/api/files/{id}","thisIdDoesNotExist")
                .exchange()
                .expectStatus().isNotFound();
    }

}
