package br.com.technosou.core.model;

import com.fasterxml.jackson.annotation.JsonValue;

public record Velocidade(
        @JsonValue
        Double valor
) {

    public static Velocidade fromString(String valorBruto) {
        if (valorBruto == null || valorBruto.isBlank()) {
            return new Velocidade(0.0);
        }

        try {
            return new Velocidade(Double.parseDouble(valorBruto.replace(",", ".")));
        } catch (NumberFormatException e) {
            return new Velocidade(0.0);
        }
    }
}