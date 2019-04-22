/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.manaco.org.repositories;

import com.manaco.org.model.Proccess;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author lucho
 */
public interface ProccessRepository extends JpaRepository<Proccess, Integer>{
    Proccess findByNumberProcessAndActiveIn(int id, boolean active);
}
