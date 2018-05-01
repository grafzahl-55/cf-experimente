package de.escriba.experimental.cf.cdrmongo.repository;

import de.escriba.experimental.cf.cdrmongo.model.Organization;
import org.bson.types.ObjectId;
import org.reactivestreams.Publisher;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrganizationRepository extends Repository<Organization, ObjectId> {

    // Kopiert von ReactiveCrudRepository
    /**
     * Saves a given entity. Use the returned instance for further operations as the save operation might have changed the
     * entity instance completely.
     *
     * @param entity must not be {@literal null}.
     * @return {@link Mono} emitting the saved entity.
     * @throws IllegalArgumentException in case the given {@code entity} is {@literal null}.
     */
     Mono<Organization> save(Organization entity);

    /**
     * Saves all given entities.
     *
     * @param entities must not be {@literal null}.
     * @return {@link Flux} emitting the saved entities.
     * @throws IllegalArgumentException in case the given {@link Iterable} {@code entities} is {@literal null}.
     */
    Flux<Organization> saveAll(Iterable<Organization> entities);

    /**
     * Saves all given entities.
     *
     * @param entityStream must not be {@literal null}.
     * @return {@link Flux} emitting the saved entities.
     * @throws IllegalArgumentException in case the given {@code Publisher} {@code entityStream} is {@literal null}.
     */
    Flux<Organization> saveAll(Publisher<Organization> entityStream);

    /**
     * Retrieves an entity by its id.
     *
     * @param id must not be {@literal null}.
     * @return {@link Mono} emitting the entity with the given id or {@link Mono#empty()} if none found.
     * @throws IllegalArgumentException in case the given {@code id} is {@literal null}.
     */
    Mono<Organization> findById(ObjectId id);

    /**
     * Retrieves an entity by its id supplied by a {@link Publisher}.
     *
     * @param id must not be {@literal null}. Uses the first emitted element to perform the find-query.
     * @return {@link Mono} emitting the entity with the given id or {@link Mono#empty()} if none found.
     * @throws IllegalArgumentException in case the given {@link Publisher} {@code id} is {@literal null}.
     */
    Mono<Organization> findById(Publisher<ObjectId> id);

    /**
     * Returns whether an entity with the id exists.
     *
     * @param id must not be {@literal null}.
     * @return {@link Mono} emitting {@literal true} if an entity with the given id exists, {@literal false} otherwise.
     * @throws IllegalArgumentException in case the given {@code id} is {@literal null}.
     */
    Mono<Boolean> existsById(ObjectId id);

    /**
     * Returns whether an entity with the given id, supplied by a {@link Publisher}, exists. Uses the first emitted
     * element to perform the exists-query.
     *
     * @param id must not be {@literal null}.
     * @return {@link Mono} emitting {@literal true} if an entity with the given id exists, {@literal false} otherwise
     * @throws IllegalArgumentException in case the given {@link Publisher} {@code id} is {@literal null}.
     */
    Mono<Boolean> existsById(Publisher<ObjectId> id);

    /**
     * Returns all instances of the type.
     *
     * @return {@link Flux} emitting all entities.
     */
    Flux<Organization> findAll();

    /**
     * Returns all instances with the given IDs.
     *
     * @param ids must not be {@literal null}.
     * @return {@link Flux} emitting the found entities.
     * @throws IllegalArgumentException in case the given {@link Iterable} {@code ids} is {@literal null}.
     */
    Flux<Organization> findAllById(Iterable<ObjectId> ids);

    /**
     * Returns all instances of the type with the given IDs supplied by a {@link Publisher}.
     *
     * @param idStream must not be {@literal null}.
     * @return {@link Flux} emitting the found entities.
     * @throws IllegalArgumentException in case the given {@link Publisher} {@code idStream} is {@literal null}.
     */
    Flux<Organization> findAllById(Publisher<ObjectId> idStream);

    /**
     * Returns the number of entities available.
     *
     * @return {@link Mono} emitting the number of entities.
     */
    Mono<Long> count();

    /**
     * Deletes the entity with the given id.
     *
     * @param id must not be {@literal null}.
     * @return {@link Mono} signaling when operation has completed.
     * @throws IllegalArgumentException in case the given {@code id} is {@literal null}.
     */
    Mono<Void> deleteById(ObjectId id);

    /**
     * Deletes the entity with the given id supplied by a {@link Publisher}.
     *
     * @param id must not be {@literal null}.
     * @return {@link Mono} signaling when operation has completed.
     * @throws IllegalArgumentException in case the given {@link Publisher} {@code id} is {@literal null}.
     */
    Mono<Void> deleteById(Publisher<ObjectId> id);

    /**
     * Deletes a given entity.
     *
     * @param entity must not be {@literal null}.
     * @return {@link Mono} signaling when operation has completed.
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     */
    Mono<Void> delete(Organization entity);

    /**
     * Deletes the given entities.
     *
     * @param entities must not be {@literal null}.
     * @return {@link Mono} signaling when operation has completed.
     * @throws IllegalArgumentException in case the given {@link Iterable} {@code entities} is {@literal null}.
     */
    Mono<Void> deleteAll(Iterable<? extends ObjectId> entities);

    /**
     * Deletes the given entities supplied by a {@link Publisher}.
     *
     * @param entityStream must not be {@literal null}.
     * @return {@link Mono} signaling when operation has completed.
     * @throws IllegalArgumentException in case the given {@link Publisher} {@code entityStream} is {@literal null}.
     */
    Mono<Void> deleteAll(Publisher<? extends ObjectId> entityStream);

    /**
     * Deletes all entities managed by the repository.
     *
     * @return {@link Mono} signaling when operation has completed.
     */
    Mono<Void> deleteAll();


    // Custom

    Mono<Organization> findFirstByName(String name);

    @Query("{'tags': {$eq : ?0}}")
    Flux<Organization> findByTag(String tag);
}
