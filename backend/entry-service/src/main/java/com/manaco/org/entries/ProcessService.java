package com.manaco.org.entries;

import com.manaco.org.repositories.ProccessRepository;
import com.manaco.org.model.Process;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcessService {
    @Autowired
    private ProccessRepository processRepository;

    public List<Process> findAll() {
        return processRepository.findAll();
    }

    public Process createProcess(Process process) {
//        Process current = processRepository.findByNumberAndActiveIn(process.getNumber(), process.getActive());
//        if (current == null) {
//            return  processRepository.save(process);
//        }
//        throw new ProcessIlegalException("failed to create");

        return processRepository.save(process);
    }
}

