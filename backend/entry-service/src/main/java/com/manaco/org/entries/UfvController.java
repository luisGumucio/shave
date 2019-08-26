package com.manaco.org.entries;

import com.manaco.org.model.Ufv;
import com.manaco.org.repositories.UfvRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/ufvs")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class UfvController {

    @Autowired
    private UfvRepository ufvRepository;

    @GetMapping
    public Page<Ufv> get(@RequestParam(defaultValue = "0") int page) {
        return ufvRepository.findAll(new PageRequest(page, 10));
    }

    @GetMapping(path = "/fecha/{date}")
    public ResponseEntity<Ufv> getDate(@PathVariable String date) {
        Ufv ufv = ufvRepository.findByCreationDate(LocalDate.parse(date));
        if(ufv == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(ufv);
    }
}
