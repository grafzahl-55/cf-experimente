package de.escriba.experimental.cf.cdrmongo.controller;

import de.escriba.experimental.cf.cdrmongo.exceptions.GridFsReadException;
import de.escriba.experimental.cf.cdrmongo.services.GridFSService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class FileDownloadController {

    private final GridFSService gridFs;

    private DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();

    @GetMapping(value = "/api/files/{fileId}")
    public Mono<GridFsResource> downloadFile(@PathVariable String fileId, ServerWebExchange exchange) {
        GridFsResource res = gridFs.download(fileId);
        if (res == null) {
            exchange.getResponse()
                    .setStatusCode(HttpStatus.NOT_FOUND);
            return Mono.empty();

        } else {
            try {
                exchange.getResponse()
                        .setStatusCode(HttpStatus.OK);
                exchange.getResponse().getHeaders()
                        .setContentLength(res.contentLength());
                exchange.getResponse().getHeaders()
                        .setContentType(MediaType.parseMediaType(res.getContentType()));

                return Mono.just(res);


            } catch (IOException io) {
                throw new GridFsReadException(fileId, io);
            }
        }
    }


}


