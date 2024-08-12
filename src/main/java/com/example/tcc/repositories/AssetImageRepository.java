package com.example.tcc.repositories;

import com.example.tcc.models.AssetImageModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetImageRepository extends CrudRepository<AssetImageModel, Long> {

}