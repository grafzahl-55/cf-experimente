package de.escriba.experimental.cf.cdrmongo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class OrganizationNotFoundException extends ResponseStatusException{

    public OrganizationNotFoundException(String orgName) {
        super(HttpStatus.NOT_FOUND, "Organization not found:"+orgName);
    }
}
