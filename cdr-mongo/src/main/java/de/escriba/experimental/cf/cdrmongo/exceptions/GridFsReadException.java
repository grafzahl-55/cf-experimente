package de.escriba.experimental.cf.cdrmongo.exceptions;

import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

public class GridFsReadException extends ResponseStatusException {

    public GridFsReadException(Object id, IOException ioEx){
        super(HttpStatus.INTERNAL_SERVER_ERROR,
                "Read error on ObjectID "+id,
                ioEx);
    }
}
