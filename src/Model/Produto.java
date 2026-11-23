package Model;

import java.math.BigDecimal;

/**
 * Produto do estoque, como a tela de vendas precisa dele (somente leitura).
 */
public class Produto {

    private final int id;
    private final String codBarra;
    private final String nome;
    private final BigDecimal preco;   // preço de venda
    private final int estoque;        // quantidade disponível

    public Produto(int id, String codBarra, String nome, BigDecimal preco, int estoque) {
        this.id = id;
        this.codBarra = codBarra;
        this.nome = nome;
        this.preco = preco;
        this.estoque = estoque;
    }

    public int getId() { return id; }
    public String getCodBarra() { return codBarra; }
    public String getNome() { return nome; }
    public BigDecimal getPreco() { return preco; }
    public int getEstoque() { return estoque; }
}
