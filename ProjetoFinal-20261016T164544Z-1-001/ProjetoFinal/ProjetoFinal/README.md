# Sistema de Estoque e Vendas (Java Swing + MySQL)

Projeto acadêmico em Java Swing para controlar o **estoque** de uma loja e registrar **vendas**,
com duas aplicações que compartilham o mesmo banco de dados MySQL.

## Objetivo

- Cadastrar, editar e excluir produtos e acompanhar a quantidade em estoque.
- Registrar vendas lendo o código de barras (ou o ID) do produto, com total e comprovante na tela.
- Consultar as vendas já feitas e ver os detalhes de cada uma.
- Deixar o banco pronto para uma futura aplicação **web** usar os mesmos dados.

## Visão geral

```
 ┌────────────────────┐                      ┌────────────────────┐
 │  App de ESTOQUE    │                      │   App de VENDAS    │
 │  (ProjetoFinal)    │                      │  (ProjetoVendas)   │
 │  • Produtos        │                      │  • Registrar venda │
 │  • Histórico de    │                      │  • Comprovante     │
 │    vendas (leitura)│                      │                    │
 └─────────┬──────────┘                      └─────────┬──────────┘
           │              JDBC / MySQL                 │
           └──────────►  banco estoque_db_prog  ◄──────┘
                         produto · venda · item_venda
```

## Como funciona

### App de estoque (`View.TelaPrincipal`)

Uma única janela (`TelaPrincipal`) troca entre telas (`JPanel`). Cada tela recebe a janela
no construtor e chama `mostrarInicial()`, `mostrarProdutos()` ou `mostrarVendas()` para navegar.

- **Tela inicial:** dois botões, *Estoque* e *Vendas*.
- **Produtos:** formulário com ID, código de barras, nome, preço, valor, fornecedor, quantidade e marca,
  mais a lista de produtos do banco.
  - Clicar em um item da lista leva os dados para os campos (modo edição).
  - **Salvar** cadastra um produto novo ou atualiza o selecionado.
  - **Excluir** apaga do banco e da lista (pede confirmação).
  - **Novo** limpa os campos e sai do modo edição.
  - **Voltar** retorna à tela inicial.
- **Vendas (histórico):** lista as vendas (nº, data, hora, total), da mais recente para a mais antiga.
  - Clicar em uma venda mostra os itens: produto, quantidade, valor unitário e subtotal.
  - Filtro por período (`dd/mm/aaaa`; campos em branco = sem limite), com o total das vendas listadas.
  - **Atualizar** recarrega do banco, mantendo filtro e venda aberta (as vendas nascem no outro app).

### App de vendas (`View.AppVendas`)

1. Digite o **código de barras ou o ID** e a quantidade; **Enter** (ou o leitor de código de barras) adiciona.
2. A lista mostra cada produto (com a quantidade) e o valor da linha; embaixo fica o **total**.
3. O mesmo produto adicionado de novo soma na mesma linha.
4. **Remover item** (ou a tecla Delete) retira o selecionado; **Cancelar venda** esvazia tudo.
5. **Finalizar venda** grava tudo em uma transação (venda + itens + baixa de estoque):
   ou grava tudo, ou nada. Em seguida mostra o **comprovante na tela**.

## Banco de dados

Banco `estoque_db_prog`. A tabela `produto` já existe (usada pelo estoque); o script `vendas.sql`
cria as duas tabelas de vendas.

| Tabela | Colunas principais |
|---|---|
| `produto` | id, codBarra, nome, preco, valor, fornecedor, marca, quantidade |
| `venda` | id, data_hora, total |
| `item_venda` | id, id_venda, id_produto, nome_produto, quantidade, valor_unitario, subtotal |

`item_venda` guarda uma **cópia do nome e do preço** da hora da venda, então o histórico não muda
se o produto for alterado depois.

## Como configurar e executar

**Requisitos:** Java 11 ou superior (o projeto de estoque está configurado para JDK 25), NetBeans,
MySQL e o driver *MySQL Connector/J* adicionado ao projeto.

1. Crie o banco e a tabela `produto`; depois rode o `vendas.sql` **uma vez**.
2. Em cada projeto, ajuste `DAO/Conexao.java` (URL, usuário e senha).
3. App de estoque: classe principal `View.TelaPrincipal`.
   App de vendas: classe principal `View.AppVendas`.
4. No app de vendas, confira `Util/Config.java`:
   - `COLUNA_PRECO_VENDA`: coluna de `produto` usada como preço de venda (`"preco"` ou `"valor"`);
   - `CONTROLAR_ESTOQUE`: confere e baixa o estoque ao vender (`true` ou `false`);
   - `NOME_LOJA`: título do comprovante.

## Conteúdo desta entrega (tela de vendas no app de estoque)

Copie as pastas de `src` para o `src` do projeto de estoque (o NetBeans pode pedir para criar o pacote `Util`).

| Arquivo | Situação | Função |
|---|---|---|
| `View/Vendas.java` | novo | Tela do histórico (feita em código) |
| `Controller/controllerVendas.java` | novo | Lógica da tela: filtro, lista, detalhes |
| `DAO/VendaDAO.java` | novo | Consultas de `venda` e `item_venda` |
| `Model/Venda.java`, `Model/ItemVenda.java` | novos | Dados de uma venda e de seus itens |
| `Util/Formatador.java` | novo | Dinheiro, data e hora no padrão brasileiro |
| `View/TelaPrincipal.java` | alterado | Novo método `mostrarVendas()` |
| `View/TelaInicial.java` e `.form` | alterados | O botão *Vendas* chama `mostrarVendas()` |
| `vendas.sql` | script | Cria as tabelas `venda` e `item_venda` |

## Decisões de projeto

- **Arquitetura em camadas:** `View` (telas), `Controller` (lógica), `DAO` (SQL) e `Model` (dados).
- **SQL só com `PreparedStatement`** (`?`), evitando SQL injection.
- **Transação na venda:** a baixa de estoque é atômica (`UPDATE ... WHERE quantidade >= ?`),
  o que impede estoque negativo mesmo com dois caixas vendendo ao mesmo tempo.
- **Chave estrangeira em `item_venda` → `produto`:** protege o histórico; consequência: um produto
  já vendido **não pode ser excluído** no estoque.
- **Data e hora em uma coluna** (`DATETIME`); `DATE()` e `TIME()` separam quando necessário.

## Pontos a melhorar

**Segurança e banco**
- A senha do MySQL está escrita no código e o usuário é `root`: usar arquivo de configuração fora do
  repositório e um usuário do banco com poucas permissões.
- Cada consulta abre e fecha uma conexão: usar um *pool* (ex.: HikariCP).
- Excluir um produto já vendido mostra o erro técnico do MySQL: trocar por mensagem amigável
  ou por um campo `ativo` (inativar em vez de excluir).
- Dois usuários editando o mesmo produto: vale quem salvar por último; uma coluna `versao`
  (bloqueio otimista) resolveria.

**Dados e validações**
- Código de barras sem restrição `UNIQUE`; preço e quantidade negativos não são barrados.
- As colunas `preco` e `valor` são ambíguas: renomear (ex.: `preco_custo` e `preco_venda`).
- O cadastro aceita `5,50` e `5.50`, mas não `1.234,50`.

**Interface**
- O acesso ao banco roda na thread da interface: com banco lento a tela trava. Usar `SwingWorker`.
- O histórico carrega todas as vendas (sem paginação) e não atualiza sozinho.

**Funcionalidades que faltam**
- Venda: forma de pagamento, troco, desconto, identificação do operador, cancelamento/estorno de
  venda já finalizada (devolvendo o estoque) e comprovante para impressão ou PDF.
- Histórico: busca por produto, relatórios (total por dia, mais vendidos) e exportação.

**Código**
- `Controller/controller.java` (vazio) e `DAO/TesteConexao.java` não são usados: remover.
- `Conexao`, `Formatador` e parte do `Model` existem nos dois projetos: criar uma biblioteca
  compartilhada (`.jar`) ou unir tudo em um só projeto (a tela de vendas já é um `JPanel`).
- Não há testes automatizados (JUnit); os testes feitos foram manuais.

**Versão web**
- Para a aplicação web, usar o mesmo banco com JPA/Hibernate ou uma API REST, versionando o esquema
  com Flyway/Liquibase para as duas aplicações não quebrarem uma à outra.

## Testes realizados e limitações

Foram feitos testes de ponta a ponta clicando nas telas reais, contra um MariaDB (compatível com
MySQL): cadastro, edição e exclusão, venda completa com comprovante, estoque insuficiente com
*rollback*, filtros por período (incluindo os limites do dia) e navegação entre telas.
**Não foram testados** em um MySQL de produção nem dentro do NetBeans.
