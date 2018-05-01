package de.escriba.experimental.cf.cdrmongo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class DuplicateSpaceNameException extends ResponseStatusException{

    public DuplicateSpaceNameException(String orgName, String spaceName) {
        super(HttpStatus.BAD_REQUEST, "Space already exists:"+orgName+"/"+spaceName);
    }
}
