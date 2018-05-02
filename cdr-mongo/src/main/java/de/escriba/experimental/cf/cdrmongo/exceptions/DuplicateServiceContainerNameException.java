package de.escriba.experimental.cf.cdrmongo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class DuplicateServiceContainerNameException extends ResponseStatusException{

    public DuplicateServiceContainerNameException(String orgName, String spaceName,String containerName) {
        super(HttpStatus.BAD_REQUEST, "Space already exists:"+orgName+"/"+spaceName+"/"+containerName);
    }
}
