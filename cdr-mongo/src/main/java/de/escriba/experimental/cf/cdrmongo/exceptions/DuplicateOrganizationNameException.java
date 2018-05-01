package de.escriba.experimental.cf.cdrmongo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class DuplicateOrganizationNameException extends ResponseStatusException{

    public DuplicateOrganizationNameException(String orgName) {
        super(HttpStatus.BAD_REQUEST, "Organization already exists:"+orgName);
    }
}
