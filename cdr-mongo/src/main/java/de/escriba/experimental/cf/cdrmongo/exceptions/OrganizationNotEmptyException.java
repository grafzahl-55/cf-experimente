package de.escriba.experimental.cf.cdrmongo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class OrganizationNotEmptyException extends ResponseStatusException {
    public OrganizationNotEmptyException(String organizationName) {
        super(HttpStatus.BAD_REQUEST, "Organization not empty:"+organizationName);
    }
}
