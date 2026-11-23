/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.math.BigDecimal;

/**
 *
 * @author Discentes
 */
public class Produto {
    private int id; // vai ser gerado pelo banco
    
    private String codBarra;
    
    private String nome;
    
    private BigDecimal preco;
    
    private BigDecimal valor;
    
    private String fornecedor;
    
    private String marca; // provisório, mudar para Enum que pode ser adicionado pelo usuário

    private int quantidade; 

    public Produto(String codBarra, String nome, BigDecimal preco, BigDecimal valor, String fornecedor, String marca, int quantidade) {
        this.codBarra = codBarra;
        this.nome = nome;
        this.preco = preco;
        this.valor = valor;
        this.fornecedor = fornecedor;
        this.marca = marca;
        this.quantidade = quantidade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getCodBarra() {
        return codBarra;
    }

    public void setCodBarra(String codBarra) {
        this.codBarra = codBarra;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
    
    @Override
    public String toString() {
        return nome + " | Cód: " + codBarra + " | Marca: " + marca
            + " | Qtd: " + quantidade + " | Preço: R$ " + preco;
}
    
    
    
    
}
