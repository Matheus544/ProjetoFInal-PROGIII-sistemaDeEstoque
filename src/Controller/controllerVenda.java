package Controller;

import DAO.ProdutoDAO;
import DAO.VendaDAO;
import Model.ItemVenda;
import Model.Produto;
import Model.Venda;
import Util.Config;
import Util.Formatador;
import View.ComprovanteDialog;
import java.awt.Color;
import java.awt.Window;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

/**
 * Controla a venda em andamento: adicionar, remover, cancelar e finalizar.
 */
public class controllerVenda {

    private static final Color COR_OK = new Color(0, 120, 60);
    private static final Color COR_ERRO = new Color(190, 30, 30);

    private final JTextField jTextFieldCodigo;
    private final JSpinner jSpinnerQuantidade;
    private final JList<ItemVenda> jListItens;
    private final JLabel jLabelTotal;
    private final JLabel jLabelStatus;

    private final DefaultListModel<ItemVenda> modelo = new DefaultListModel<>();
    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    private final VendaDAO vendaDAO = new VendaDAO();

    public controllerVenda(JTextField jTextFieldCodigo, JSpinner jSpinnerQuantidade,
            JList<ItemVenda> jListItens, JLabel jLabelTotal, JLabel jLabelStatus) {
        this.jTextFieldCodigo = jTextFieldCodigo;
        this.jSpinnerQuantidade = jSpinnerQuantidade;
        this.jListItens = jListItens;
        this.jLabelTotal = jLabelTotal;
        this.jLabelStatus = jLabelStatus;

        this.jListItens.setModel(modelo);
        this.jListItens.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        atualizarTotal();
    }

    /** Registra o produto digitado (código de barras ou ID) com a quantidade escolhida. */
    public void adicionar() {
        String entrada = jTextFieldCodigo.getText().trim();
        if (entrada.isEmpty()) {
            erro("Digite o código de barras ou o ID do produto.");
            return;
        }

        int quantidade;
        try {
            jSpinnerQuantidade.commitEdit();   // garante que o número digitado foi aceito
            quantidade = ((Number) jSpinnerQuantidade.getValue()).intValue();
        } catch (ParseException e) {
            erro("Quantidade inválida.");
            return;
        }

        try {
            Produto produto = produtoDAO.buscarPorCodigoOuId(entrada);
            if (produto == null) {
                erro("Produto não encontrado: " + entrada);
                jTextFieldCodigo.selectAll();
                return;
            }
            if (produto.getPreco() == null) {
                erro("\"" + produto.getNome() + "\" está sem preço de venda cadastrado.");
                return;
            }

            ItemVenda existente = procurarItem(produto.getId());
            int jaNaVenda = existente == null ? 0 : existente.getQuantidade();

            if (Config.CONTROLAR_ESTOQUE && jaNaVenda + quantidade > produto.getEstoque()) {
                erro("Estoque insuficiente de \"" + produto.getNome() + "\". Disponível: " + produto.getEstoque()
                        + (jaNaVenda > 0 ? " (já há " + jaNaVenda + " na venda)" : "") + ".");
                return;
            }

            ItemVenda item;
            if (existente == null) {
                item = new ItemVenda(produto, quantidade);
                modelo.addElement(item);
            } else {
                existente.adicionarQuantidade(quantidade);     // mesmo produto: soma na linha existente
                modelo.set(modelo.indexOf(existente), existente);
                item = existente;
            }

            jListItens.ensureIndexIsVisible(modelo.indexOf(item));
            atualizarTotal();
            ok("Adicionado: " + produto.getNome() + " x" + quantidade);

            jTextFieldCodigo.setText("");
            jSpinnerQuantidade.setValue(1);
            focarCodigo();

        } catch (SQLException e) {
            erro("Erro ao consultar o banco: " + e.getMessage());
        }
    }

    /** Tira da venda o item selecionado na lista. */
    public void removerSelecionado() {
        int indice = jListItens.getSelectedIndex();
        if (indice < 0) {
            erro("Selecione um item da lista para remover.");
            return;
        }
        ItemVenda removido = modelo.remove(indice);
        atualizarTotal();
        ok("Removido: " + removido.getProduto().getNome());
        if (!modelo.isEmpty()) {
            jListItens.setSelectedIndex(Math.min(indice, modelo.size() - 1));
        }
    }

    /** Descarta a venda inteira (pede confirmação). */
    public void cancelar() {
        if (modelo.isEmpty()) {
            return;
        }
        int resposta = JOptionPane.showConfirmDialog(janela(),
                "Cancelar a venda atual e remover todos os itens?",
                "Cancelar venda", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (resposta == JOptionPane.YES_OPTION) {
            novaVenda();
        }
    }

    /** Grava a venda no banco, mostra o comprovante e começa uma venda nova. */
    public void finalizar() {
        if (modelo.isEmpty()) {
            erro("Adicione pelo menos um item antes de finalizar.");
            return;
        }

        List<ItemVenda> itens = Collections.list(modelo.elements());
        Venda venda = new Venda(itens, LocalDateTime.now().withNano(0));

        try {
            vendaDAO.salvar(venda);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(janela(),
                    "Não foi possível registrar a venda:\n" + e.getMessage()
                    + "\n\nA venda continua aberta e nada foi gravado.",
                    "Erro ao finalizar", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ComprovanteDialog.mostrar(janela(), venda);   // só na tela
        novaVenda();
        ok("Venda nº " + venda.getId() + " registrada.");
    }

    // ---------------------------------------------------------------- apoio

    private void novaVenda() {
        modelo.clear();
        atualizarTotal();
        jTextFieldCodigo.setText("");
        jSpinnerQuantidade.setValue(1);
        jLabelStatus.setText(" ");
        focarCodigo();
    }

    private ItemVenda procurarItem(int idProduto) {
        for (int i = 0; i < modelo.size(); i++) {
            if (modelo.get(i).getProduto().getId() == idProduto) {
                return modelo.get(i);
            }
        }
        return null;
    }

    private void atualizarTotal() {
        BigDecimal total = BigDecimal.ZERO.setScale(2);
        for (int i = 0; i < modelo.size(); i++) {
            total = total.add(modelo.get(i).getSubtotal());
        }
        jLabelTotal.setText("Total: " + Formatador.moeda(total));
    }

    public void focarCodigo() {
        jTextFieldCodigo.requestFocusInWindow();
    }

    private Window janela() {
        return SwingUtilities.getWindowAncestor(jListItens);
    }

    private void ok(String mensagem) {
        jLabelStatus.setForeground(COR_OK);
        jLabelStatus.setText(mensagem);
    }

    private void erro(String mensagem) {
        jLabelStatus.setForeground(COR_ERRO);
        jLabelStatus.setText(mensagem);
    }
}
