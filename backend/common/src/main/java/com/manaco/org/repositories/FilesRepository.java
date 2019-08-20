package com.manaco.org.repositories;

import com.manaco.org.model.FileUpload;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FilesRepository extends MongoRepository<FileUpload, String> {
}
