package Model;

import java.math.BigDecimal;

/**
 * Um produto dentro da venda, com a quantidade e o preço da hora em que foi adicionado.
 */
public class ItemVenda {

    private final Produto produto;
    private int quantidade;
    private final BigDecimal valorUnitario;

    public ItemVenda(Produto produto, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.valorUnitario = produto.getPreco(); // congela o preço no momento da venda
    }

    public Produto getProduto() { return produto; }
    public int getQuantidade() { return quantidade; }
    public BigDecimal getValorUnitario() { return valorUnitario; }

    public void adicionarQuantidade(int mais) {
        this.quantidade += mais;
    }

    public BigDecimal getSubtotal() {
        return valorUnitario.multiply(BigDecimal.valueOf(quantidade));
    }
}
