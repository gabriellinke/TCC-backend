package com.example.tcc.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AssetInfoResponseDto {
    private Long id;
    private String tombo;
    private String tombo_antigo;
    private String descricao;
    private String estado_conservacao;
    private String situacao;
    private String local;
    private String responsavel;

    public String getTomboAntigo() {
        return tombo_antigo;
    }

    public String getEstadoConservacao() {
        return estado_conservacao;
    }
}
