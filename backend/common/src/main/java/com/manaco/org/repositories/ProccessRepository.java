/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.manaco.org.repositories;

import com.manaco.org.model.Process;
import com.manaco.org.model.TransactionOption;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author lucho
 */
@Transactional
public interface ProccessRepository extends MongoRepository<Process, String> {
    Process findByNumberProcessAndIsActive(Integer number, boolean active);
    Process findByNumberProcessAndIsActiveAndTransactionOption(Integer number, boolean active,
                                                               TransactionOption option);

    Process findByIsActive(boolean b);
}
