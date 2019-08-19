package com.manaco.org.repositories;


import com.manaco.org.model.Ufv;
//import org.springframework.data.jpa.repository.JpaRepository;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;


//@Service
public interface UfvRepository extends MongoRepository<Ufv, String> {

    Ufv findByCreationDate(LocalDate creationDate);
}

