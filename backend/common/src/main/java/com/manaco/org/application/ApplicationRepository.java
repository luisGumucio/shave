package com.manaco.org.application;

import java.util.UUID;

import com.manaco.org.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * JPA - Spring Repository interface to enable persistence on
 * {@link Application}.
 *
 * @author Jhonatan Mamani
 */
@Repository
public interface ApplicationRepository extends JpaRepository<Application, UUID> {

    @Query(value = "SELECT name FROM Application app where app.id = ?1")
    String findNameById(UUID id);

    @Query(value = "SELECT id FROM Application app where app.name = ?1")
    UUID findIdByName(String name);
}
