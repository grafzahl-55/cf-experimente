package de.escriba.experimental.cf.cdrmongo.services;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GridFSService {

    final private GridFsOperations gridFsOperations;
    private DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();

    public String upload(InputStream dataStream, MediaType contentType) {
        String fileName = UUID.randomUUID().toString();
        gridFsOperations.store(dataStream, fileName, contentType.toString());
        return fileName;
    }

    public GridFsResource download(String fileId) {
        return gridFsOperations.getResource(fileId);
    }

    public void delete(String fileId) {
        Query q = new Query(Criteria.where("filename").is(fileId));
        gridFsOperations.delete(q);
    }


}
