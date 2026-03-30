package br.com.technosou.core.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.jboss.logging.Logger;

public final class DataHoraUtils {

    private static final Logger LOG = Logger.getLogger(DataHoraUtils.class);
    private static final ZoneId FUSO_HORARIO_RIO = ZoneId.of("America/Sao_Paulo");

    private DataHoraUtils() {
    }

    public static LocalDateTime converterTimestamp(String timestampEmMilissegundos) {
        if (timestampEmMilissegundos == null || timestampEmMilissegundos.isBlank()) {
            return null;
        }

        try {
            long epochMilli = Long.parseLong(timestampEmMilissegundos);
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), FUSO_HORARIO_RIO);
        } catch (NumberFormatException e) {
            LOG.warn("Falha ao converter timestamp: " + timestampEmMilissegundos);
            return null;
        }
    }
}