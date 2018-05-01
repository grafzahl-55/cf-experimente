package de.escriba.experimental.cf.cdrmongo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class SpaceNotFoundException extends ResponseStatusException{

    public SpaceNotFoundException(String orgName, String spaceName) {

        super(HttpStatus.NOT_FOUND, "Space not found:"+orgName+"/"+spaceName);
    }
}
