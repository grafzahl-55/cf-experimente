package de.escriba.experimental.cf.cdrmongo.services;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class GridFsServiceTest {

    @Autowired
    private GridFSService gridFs;

    private DataBufferFactory factory = new DefaultDataBufferFactory();

    @Test
    public void shouldProvideUploadAndDownload() throws Exception {
        //byte[] rawData="Hallo, Berlin".getBytes();
        Random rnd = new Random();
        byte[] rawData = new byte[12000];
        rnd.nextBytes(rawData);

        InputStream in = new ByteArrayInputStream(rawData);
        String oid = gridFs.upload(in, MediaType.APPLICATION_OCTET_STREAM);

        GridFsResource fileData = gridFs.download(oid);
        assertThat(fileData).isNotNull();
        assertThat(fileData.getContentType()).isEqualTo(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        IOUtils.copy(fileData.getInputStream(), baos);
        baos.close();
        assertThat(baos.toByteArray()).isEqualTo(rawData);

    }

    @Test
    public void shouldAllowDeletionOfUploads() {
        Random rnd = new Random();
        byte[] rawData = new byte[12000];
        rnd.nextBytes(rawData);

        InputStream in = new ByteArrayInputStream(rawData);
        String oid = gridFs.upload(in, MediaType.APPLICATION_OCTET_STREAM);

        GridFsResource fileData = gridFs.download(oid);
        assertThat(fileData).isNotNull();

        gridFs.delete(oid);
        fileData = gridFs.download(oid);
        assertThat(fileData).isNull();

    }


}
