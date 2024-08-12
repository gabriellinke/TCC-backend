package com.example.tcc.repositories;

import com.example.tcc.models.FileAssetModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileAssetRepository extends CrudRepository<FileAssetModel, Long> {

}