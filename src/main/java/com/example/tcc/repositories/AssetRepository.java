package com.example.tcc.repositories;

import com.example.tcc.models.AssetModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetRepository extends CrudRepository<AssetModel, Long> {

}