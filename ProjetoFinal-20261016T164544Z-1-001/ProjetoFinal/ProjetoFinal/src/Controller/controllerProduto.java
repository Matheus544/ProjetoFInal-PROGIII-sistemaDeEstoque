
package Controller;

import DAO.ProdutoDAO;
import Model.Produto;
import java.math.BigDecimal;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import java.sql.SQLException;

public class controllerProduto {
    JTextField jTextFieldCodbarra, jTextFieldNome, jTextFieldPreco, jTextFieldValor, jTextFieldFornecedor, jTextFieldQuantidade, jTextFieldMarca, jTextFieldID;

    JList jListProdutos ;

    DefaultListModel<Produto> modelo = new DefaultListModel<>();

    ProdutoDAO dao = new ProdutoDAO();

    // Produto que está sendo editado agora
    Produto produtoEmEdicao = null;

    public controllerProduto(JTextField jTextFieldCodbarra, JTextField jTextFieldNome, JTextField jTextAreaPreco,
            JTextField jTextAreaValor, JTextField jTextFieldFornecedor, JTextField jTextFieldQuantidade, JTextField jTextFieldMarca,
            JList jListProdutos, JTextField jTextFieldID){

        this.jTextFieldCodbarra = jTextFieldCodbarra;
        this.jTextFieldNome = jTextFieldNome;
        this.jTextFieldPreco = jTextAreaPreco;
        this.jTextFieldValor = jTextAreaValor;
        this.jTextFieldFornecedor = jTextFieldFornecedor;
        this.jTextFieldQuantidade = jTextFieldQuantidade;
        this.jTextFieldMarca = jTextFieldMarca;
        this.jListProdutos = jListProdutos;
        this.jTextFieldID = jTextFieldID;

        this.jListProdutos.setModel(modelo);

        // só um item pode ficar selecionado por vez
        this.jListProdutos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // quando o usuário clica em um item da lista os dados vão para os campos
        this.jListProdutos.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selecionar();
            }
        });

        carregar();

    }

    /** Joga o produto selecionado na lista para os campos */
    public void selecionar() {
        Produto p = (Produto) jListProdutos.getSelectedValue();
        if (p == null) {
            return; 
        }
        produtoEmEdicao = p;
        jTextFieldID.setText(String.valueOf(p.getId()));
        jTextFieldCodbarra.setText(p.getCodBarra());
        jTextFieldNome.setText(p.getNome());
        jTextFieldPreco.setText(p.getPreco() == null ? "" : p.getPreco().toPlainString());
        jTextFieldValor.setText(p.getValor() == null ? "" : p.getValor().toPlainString());
        jTextFieldFornecedor.setText(p.getFornecedor());
        jTextFieldQuantidade.setText(String.valueOf(p.getQuantidade()));
        jTextFieldMarca.setText(p.getMarca());
    }

   
    public void salvar(){
        try {
            String codBarra = jTextFieldCodbarra.getText().trim();
            String nome = jTextFieldNome.getText().trim();
            BigDecimal preco = new BigDecimal(jTextFieldPreco.getText().trim().replace(",", "."));
            BigDecimal valor = new BigDecimal(jTextFieldValor.getText().trim().replace(",", "."));
            String fornecedor = jTextFieldFornecedor.getText().trim();
            int quantidade = Integer.parseInt(jTextFieldQuantidade.getText().trim());
            String marca = jTextFieldMarca.getText().trim();

            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Informe o nome do produto.");
                return;
            }

            Produto produto = new Produto(
                    codBarra,
                    nome,
                    preco,
                    valor,
                    fornecedor,
                    marca,
                    quantidade
            );

            if (produtoEmEdicao == null) {
                
                dao.inserir(produto);          // grava e preenche o id
                modelo.addElement(produto);
            } else {
               
                produto.setId(produtoEmEdicao.getId());
                if (dao.atualizar(produto)) {
                    
                    modelo.set(modelo.indexOf(produtoEmEdicao), produto);
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Este produto não existe mais no banco. A lista será recarregada.",
                            "Produto não encontrado", JOptionPane.WARNING_MESSAGE);
                    carregar();
                }
            }
            limpar();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "Preço, valor e quantidade precisam ser números válidos.",
                    "Dados inválidos", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Erro ao salvar no banco: " + e.getMessage(),
                    "Erro de banco", JOptionPane.ERROR_MESSAGE);
        }
    }

   
    public void excluir() {
        Produto p = (Produto) jListProdutos.getSelectedValue();
        if (p == null) {
            JOptionPane.showMessageDialog(null, "Selecione um produto na lista para excluir.");
            return;
        }

        int resposta = JOptionPane.showConfirmDialog(null,
                "Excluir o produto \"" + p.getNome() + "\"?\nEsta ação não pode ser desfeita.",
                "Confirmar exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (resposta != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            dao.excluir(p.getId());   
            modelo.removeElement(p);             
            limpar();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Erro ao excluir no banco: " + e.getMessage(),
                    "Erro de banco", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    public void limpar() {
        produtoEmEdicao = null;
        jTextFieldID.setText("");
        jTextFieldCodbarra.setText("");
        jTextFieldNome.setText("");
        jTextFieldPreco.setText("");
        jTextFieldValor.setText("");
        jTextFieldMarca.setText("");
        jTextFieldQuantidade.setText("");
        jTextFieldFornecedor.setText("");
        jListProdutos.clearSelection();
    }

    private void carregar() {
        try {
            modelo.clear();
            for (Produto p : dao.listar()) {
                modelo.addElement(p);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Erro ao carregar produtos: " + e.getMessage(),
                    "Erro de banco", JOptionPane.ERROR_MESSAGE);
        }
    }

}
