package com.manaco.org.entries;

import com.manaco.org.common.model.Item;
import com.manaco.org.common.model.Ufv;
import com.manaco.org.common.repositories.UfvRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ufvs")
@CrossOrigin(origins = "*")
public class UfvController {

    @Autowired
    private UfvRepository ufvRepository;

    @GetMapping
    public Page<Ufv> get(@RequestParam(defaultValue = "0") int page) {
        return ufvRepository.findAll(new PageRequest(page, 4));
    }
}
