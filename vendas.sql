-- Tabelas do sistema de vendas (rodar UMA vez, no mesmo banco do estoque).
USE estoque_db_prog;

CREATE TABLE venda (
    id        INT PRIMARY KEY AUTO_INCREMENT,
    data_hora DATETIME      NOT NULL,   -- data e horário da venda
    total     DECIMAL(10,2) NOT NULL
);

CREATE TABLE item_venda (
    id             INT PRIMARY KEY AUTO_INCREMENT,
    id_venda       INT           NOT NULL,
    id_produto     INT           NOT NULL,
    nome_produto   VARCHAR(100)  NOT NULL,   -- cópia do nome na hora da venda
    quantidade     INT           NOT NULL,
    valor_unitario DECIMAL(10,2) NOT NULL,   -- preço na hora da venda
    subtotal       DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_item_venda   FOREIGN KEY (id_venda)   REFERENCES venda(id),
    CONSTRAINT fk_item_produto FOREIGN KEY (id_produto) REFERENCES produto(id)
);
