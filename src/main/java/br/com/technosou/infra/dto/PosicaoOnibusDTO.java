package br.com.technosou.infra.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PosicaoOnibusDTO(
        @JsonProperty("ordem")
        String ordem,

        @JsonProperty("latitude")
        String latitude,

        @JsonProperty("longitude")
        String longitude,

        @JsonProperty("datahora")
        String dataHora,

        @JsonProperty("velocidade")
        String velocidade,

        @JsonProperty("linha")
        String linha,

        @JsonProperty("datahoraenvio")
        String dataHoraEnvio,

        @JsonProperty("datahoraservidor")
        String dataHoraServidor
) {}