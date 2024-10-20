package com.example.tcc.controllers;

import com.example.tcc.models.PatrimonioModel;
import com.example.tcc.responses.PatrimonioResponseDto;
import com.example.tcc.services.PatrimonioSearchService;
import com.example.tcc.util.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patrimonio")
@RequiredArgsConstructor
public class PatrimonioController {
    private final PatrimonioSearchService patrimonioSearchService;

    @GetMapping
    public ResponseEntity<?> retrieve(
        @RequestParam(value = "tombo", required = false) String assetNumber,
        @RequestParam(value = "tombo_antigo", required = false) String formerAssetNumber
    ) {
        try {
            PatrimonioModel asset;
            if(assetNumber != null) {
                asset = patrimonioSearchService.getPatrimonioByAssetNumber(assetNumber);
            } else if(formerAssetNumber != null) {
                asset = patrimonioSearchService.getPatrimonioByFormerAssetNumber(formerAssetNumber);
            } else {
                throw new Exception("Nenhum tombo ou tombo antigo foi fornecido");
            }
            if(asset == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(new PatrimonioResponseDto(
                asset.getTombo(),
                asset.getTomboAntigo(),
                asset.getDescricao(),
                asset.getResponsavel()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage()));
        }
    }
}