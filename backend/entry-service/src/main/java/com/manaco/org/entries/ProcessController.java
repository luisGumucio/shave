package com.manaco.org.entries;

import com.manaco.org.model.Process;
import com.manaco.org.model.TransactionOption;
import com.manaco.org.repositories.ProccessRepository;
import com.manaco.org.repositories.UfvRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/process")
@CrossOrigin(origins = "*")
public class ProcessController {

    @Autowired
    private ProccessRepository proccessRepository;

    @GetMapping
    public Page<Process> get(@RequestParam(defaultValue = "0") int page) {
        return proccessRepository.findAll(new PageRequest(page, 10));
    }

    @GetMapping(path = "/{identifier}")
    public Process getDate(@PathVariable String identifier) {
        return proccessRepository.findByNumberProcessAndIsActiveAndTransactionOption(1, true, TransactionOption.REPUESTOS);
    }
}
