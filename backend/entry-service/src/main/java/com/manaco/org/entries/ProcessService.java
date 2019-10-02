package com.manaco.org.entries;

import com.manaco.org.model.TransactionOption;
import com.manaco.org.repositories.ProccessRepository;
import com.manaco.org.model.Process;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.manaco.org.model.TransactionOption.SALDO_INITIAL_REPUESTOS;

@Service
public class ProcessService {
    @Autowired
    private ProccessRepository processRepository;

    public List<Process> findAll() {
        return processRepository.findAll();
    }

    public Process createProcess(Process process) {
        return processRepository.save(process);
    }

    public Process findByNumberProcessAndIsActive(Integer number, boolean active) {
        return processRepository.findByNumberProcessAndIsActive(number, active);
    }

//    public Process findByNumberProcessAndIsActiveAndTransactionOption(Integer number,
//                                                                      boolean active, TransactionOption transactionOption) {
//        return processRepository.findByNumberProcessAndIsActiveAndTransactionOption(number, active, transactionOption);
//    }
}

