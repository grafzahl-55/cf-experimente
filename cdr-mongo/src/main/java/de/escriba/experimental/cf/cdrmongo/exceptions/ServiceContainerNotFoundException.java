package de.escriba.experimental.cf.cdrmongo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ServiceContainerNotFoundException extends ResponseStatusException{

    public ServiceContainerNotFoundException(String orgName, String spaceName, String containerName) {

        super(HttpStatus.NOT_FOUND, "ServiceContainer not found:"+orgName+"/"+spaceName+"/"+containerName);
    }
}
