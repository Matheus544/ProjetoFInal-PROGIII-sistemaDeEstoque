package View;

import Model.ItemVenda;
import Util.Formatador;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

/**
 * Desenha cada linha da lista: produto à esquerda e valor à direita.
 */
public class ItemVendaRenderer extends JPanel implements ListCellRenderer<ItemVenda> {

    private final JLabel nome = new JLabel();
    private final JLabel valor = new JLabel();

    public ItemVendaRenderer() {
        super(new BorderLayout(12, 0));
        setOpaque(true);
        setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
        nome.setFont(nome.getFont().deriveFont(Font.PLAIN, 16f));
        valor.setFont(valor.getFont().deriveFont(Font.BOLD, 16f));
        add(nome, BorderLayout.CENTER);
        add(valor, BorderLayout.EAST);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends ItemVenda> list, ItemVenda item,
            int index, boolean selecionado, boolean foco) {

        nome.setText(item.getProduto().getNome() + "  x" + item.getQuantidade());
        valor.setText(Formatador.moeda(item.getSubtotal()));

        // new Color(...) copia a cor: o Nimbus ignora cores "UIResource" ao pintar renderizadores
        Color fundo = new Color((selecionado ? list.getSelectionBackground() : list.getBackground()).getRGB());
        Color texto = new Color((selecionado ? list.getSelectionForeground() : list.getForeground()).getRGB());
        setBackground(fundo);
        nome.setForeground(texto);
        valor.setForeground(texto);
        return this;
    }
}
