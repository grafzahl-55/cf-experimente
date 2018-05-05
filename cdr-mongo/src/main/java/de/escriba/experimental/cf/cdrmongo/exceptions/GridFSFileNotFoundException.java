package de.escriba.experimental.cf.cdrmongo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class GridFSFileNotFoundException extends HttpStatusCodeException {

    public GridFSFileNotFoundException(String fileId){
        super(HttpStatus.NOT_FOUND,"File not found. FileId:"+fileId);
    }
}
