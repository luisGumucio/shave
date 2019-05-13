/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.manaco.org.repositories;

import com.manaco.org.model.Process;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author lucho
 */
@Transactional
public interface ProccessRepository extends JpaRepository<Process, Integer>{
    Process findByNumberProcessAndIsActiveIn(int id, boolean active);
}
