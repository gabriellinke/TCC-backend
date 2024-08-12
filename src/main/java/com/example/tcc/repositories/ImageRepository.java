package com.example.tcc.repositories;

import com.example.tcc.models.ImageModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends CrudRepository<ImageModel, Long> {

}