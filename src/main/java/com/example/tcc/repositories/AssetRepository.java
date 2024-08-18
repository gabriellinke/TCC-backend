package com.example.tcc.repositories;

import com.example.tcc.models.AssetModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

@Repository
public interface AssetRepository extends CrudRepository<AssetModel, Long> {

    @Query(value="SELECT a.responsible FROM assets a " +
                "JOIN files_assets fa ON " +
                "a.id = fa.asset_id WHERE fa.file_id = :fileId", nativeQuery = true)
    List<String> findAssetResponsiblesByFileId(Long fileId);
}