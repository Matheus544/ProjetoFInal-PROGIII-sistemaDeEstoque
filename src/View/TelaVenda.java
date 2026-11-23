package View;

import Controller.controllerVenda;
import Model.ItemVenda;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

/**
 * Tela de vendas (feita em código, sem o editor visual do NetBeans).
 * É um JPanel, então também pode ser colocada dentro de outra janela.
 */
public class TelaVenda extends JPanel {

    private final JTextField jTextFieldCodigo = new JTextField(18);
    private final JSpinner jSpinnerQuantidade = new JSpinner(new SpinnerNumberModel(1, 1, 9999, 1));
    private final JButton jButtonAdicionar = new JButton("Adicionar");
    private final JLabel jLabelStatus = new JLabel(" ");

    private final JList<ItemVenda> jListItens = new JList<>();
    private final JLabel jLabelTotal = new JLabel();

    private final JButton jButtonRemover = new JButton("Remover item");
    private final JButton jButtonCancelar = new JButton("Cancelar venda");
    private final JButton jButtonFinalizar = new JButton("Finalizar venda");

    private final controllerVenda c;

    public TelaVenda() {
        montarTela();
        c = new controllerVenda(jTextFieldCodigo, jSpinnerQuantidade, jListItens, jLabelTotal, jLabelStatus);
        ligarEventos();
    }

    private void montarTela() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // ---- topo: código/ID, quantidade e botão Adicionar
        JPanel entrada = new JPanel(new GridBagLayout());
        entrada.setBorder(BorderFactory.createTitledBorder("Registrar produto"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 6, 6, 6);
        g.gridy = 0;

        g.gridx = 0;
        entrada.add(new JLabel("Código de barras ou ID:"), g);
        g.gridx = 1; g.weightx = 1; g.fill = GridBagConstraints.HORIZONTAL;
        jTextFieldCodigo.setFont(jTextFieldCodigo.getFont().deriveFont(16f));
        entrada.add(jTextFieldCodigo, g);
        g.gridx = 2; g.weightx = 0; g.fill = GridBagConstraints.NONE;
        entrada.add(new JLabel("Qtd:"), g);
        g.gridx = 3;
        jSpinnerQuantidade.setPreferredSize(new Dimension(70, 28));
        entrada.add(jSpinnerQuantidade, g);
        g.gridx = 4;
        entrada.add(jButtonAdicionar, g);

        jLabelStatus.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));
        JPanel topo = new JPanel(new BorderLayout(0, 4));
        topo.add(entrada, BorderLayout.NORTH);
        topo.add(jLabelStatus, BorderLayout.CENTER);
        add(topo, BorderLayout.NORTH);

        // ---- centro: lista de itens (produto e valor)
        jListItens.setCellRenderer(new ItemVendaRenderer());
        jListItens.setFixedCellHeight(34);
        JScrollPane rolagem = new JScrollPane(jListItens);
        rolagem.setBorder(BorderFactory.createTitledBorder("Itens da venda"));
        rolagem.setPreferredSize(new Dimension(620, 320));
        add(rolagem, BorderLayout.CENTER);

        // ---- rodapé: total e botões
        jLabelTotal.setFont(jLabelTotal.getFont().deriveFont(Font.BOLD, 28f));
        jLabelTotal.setHorizontalAlignment(SwingConstants.RIGHT);

        jButtonFinalizar.setFont(jButtonFinalizar.getFont().deriveFont(Font.BOLD, 15f));
        jButtonFinalizar.setBackground(new Color(0, 128, 96));
        jButtonFinalizar.setForeground(Color.WHITE);
        jButtonFinalizar.setPreferredSize(new Dimension(190, 44));

        JPanel esquerda = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        esquerda.add(jButtonRemover);
        esquerda.add(jButtonCancelar);

        JPanel botoes = new JPanel(new BorderLayout());
        botoes.add(esquerda, BorderLayout.WEST);
        botoes.add(jButtonFinalizar, BorderLayout.EAST);

        JPanel rodape = new JPanel(new BorderLayout(0, 8));
        rodape.add(jLabelTotal, BorderLayout.NORTH);
        rodape.add(botoes, BorderLayout.CENTER);
        add(rodape, BorderLayout.SOUTH);
    }

    private void ligarEventos() {
        jButtonAdicionar.addActionListener(e -> c.adicionar());
        jTextFieldCodigo.addActionListener(e -> c.adicionar());   // Enter (leitor de código de barras)
        ((JSpinner.DefaultEditor) jSpinnerQuantidade.getEditor()).getTextField()
                .addActionListener(e -> c.adicionar());           // Enter depois de digitar a quantidade

        jButtonRemover.addActionListener(e -> c.removerSelecionado());
        jButtonCancelar.addActionListener(e -> c.cancelar());
        jButtonFinalizar.addActionListener(e -> c.finalizar());

        // tecla Delete na lista também remove o item selecionado
        jListItens.getInputMap(JComponent.WHEN_FOCUSED)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "remover");
        jListItens.getActionMap().put("remover", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                c.removerSelecionado();
            }
        });
    }

    /** Coloca o cursor no campo de código (chamado ao abrir a janela). */
    public void focarCodigo() {
        c.focarCodigo();
    }
}
