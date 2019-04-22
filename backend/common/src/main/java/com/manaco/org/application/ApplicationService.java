package com.manaco.org.application;

import java.util.List;
import java.util.UUID;

import com.manaco.org.model.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Jhonatan Mamani
 */
@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    public UUID findApplicationIdByName(String name) {
        return applicationRepository.findIdByName(name);
    }

    public String findApplicationNameById(UUID id) {
        return applicationRepository.findNameById(id);
    }

    public List<Application> findAllApplications() {
        return applicationRepository.findAll();
    }
}
