package Util;

import Model.ItemVenda;
import Model.Venda;

/**
 * Monta o texto do comprovante (só para exibir na tela).
 */
public final class Comprovante {

    private static final int LARGURA = 40;

    private Comprovante() {
    }

    public static String gerar(Venda venda) {
        String duplo = "=".repeat(LARGURA);
        String simples = "-".repeat(LARGURA);
        StringBuilder sb = new StringBuilder();

        sb.append(duplo).append('\n');
        sb.append(centralizar(Config.NOME_LOJA)).append('\n');
        sb.append(centralizar("COMPROVANTE DE VENDA")).append('\n');
        sb.append(duplo).append('\n');
        sb.append("Venda nº: ").append(venda.getId()).append('\n');
        sb.append("Data: ").append(Formatador.data(venda.getDataHora()))
          .append("   Hora: ").append(Formatador.hora(venda.getDataHora())).append('\n');
        sb.append(simples).append('\n');

        for (ItemVenda item : venda.getItens()) {
            sb.append(item.getProduto().getNome()).append('\n');
            String detalhe = "  " + item.getQuantidade() + " x " + Formatador.moeda(item.getValorUnitario());
            sb.append(duasColunas(detalhe, Formatador.moeda(item.getSubtotal()))).append('\n');
        }

        sb.append(simples).append('\n');
        sb.append(duasColunas("TOTAL", Formatador.moeda(venda.getTotal()))).append('\n');
        sb.append(duplo).append('\n');
        sb.append(centralizar("Obrigado pela preferência!")).append('\n');
        return sb.toString();
    }

    /** Texto à esquerda e valor encostado na direita. */
    private static String duasColunas(String esquerda, String direita) {
        int espacos = Math.max(1, LARGURA - esquerda.length() - direita.length());
        return esquerda + " ".repeat(espacos) + direita;
    }

    private static String centralizar(String texto) {
        int margem = Math.max(0, (LARGURA - texto.length()) / 2);
        return " ".repeat(margem) + texto;
    }
}
