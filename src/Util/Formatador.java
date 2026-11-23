package Util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Formatação de dinheiro, data e hora no padrão brasileiro.
 */
public final class Formatador {

    private static final Locale PT_BR = Locale.forLanguageTag("pt-BR");
    private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm:ss");

    private Formatador() {
    }

    /** 1234.5 -> "R$ 1.234,50" */
    public static String moeda(BigDecimal valor) {
        DecimalFormat df = new DecimalFormat("#,##0.00", DecimalFormatSymbols.getInstance(PT_BR));
        return "R$ " + df.format(valor);
    }

    public static String data(LocalDateTime dataHora) {
        return DATA.format(dataHora);
    }

    public static String hora(LocalDateTime dataHora) {
        return HORA.format(dataHora);
    }
}
