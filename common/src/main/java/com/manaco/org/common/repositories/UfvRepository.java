package com.manaco.org.common.repositories;


import com.manaco.org.common.model.Ufv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


@Service
public interface UfvRepository extends JpaRepository<Ufv, Integer> {

    Ufv findByCreationDate(LocalDate creationDate);
}

