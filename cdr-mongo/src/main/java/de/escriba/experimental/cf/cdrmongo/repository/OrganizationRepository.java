package de.escriba.experimental.cf.cdrmongo.repository;

import de.escriba.experimental.cf.cdrmongo.model.Organization;
import org.bson.types.ObjectId;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface OrganizationRepository extends ReactiveCrudRepository<Organization, ObjectId> {

}
