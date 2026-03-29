package br.com.technosou.core.model;

import java.util.Optional;

public record PosicaoGeografica(Double latitude, Double longitude) {

    public static Optional<PosicaoGeografica> fromStrings(String latBruta, String lonBruta) {
        if (latBruta == null || lonBruta == null || latBruta.isBlank() || lonBruta.isBlank()) {
            return Optional.empty();
        }

        try {
            double lat = Double.parseDouble(latBruta.replace(",", "."));
            double lon = Double.parseDouble(lonBruta.replace(",", "."));
            return Optional.of(new PosicaoGeografica(lat, lon));

        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}