package View;

import Model.Venda;
import Util.Comprovante;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.KeyEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

/**
 * Janela que mostra o comprovante da venda (apenas na tela).
 */
public class ComprovanteDialog extends JDialog {

    private ComprovanteDialog(Window dono, Venda venda) {
        super(dono, "Comprovante de venda", ModalityType.APPLICATION_MODAL);

        String texto = Comprovante.gerar(venda);
        int linhas = (int) texto.lines().count();

        JTextArea area = new JTextArea(texto, Math.min(linhas + 1, 28), 42);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        area.setEditable(false);
        area.setMargin(new java.awt.Insets(10, 12, 10, 12));
        area.setCaretPosition(0);

        JButton fechar = new JButton("Fechar");
        fechar.addActionListener(e -> dispose());

        JPanel rodape = new JPanel();
        rodape.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        rodape.add(fechar);

        setLayout(new BorderLayout());
        add(new JScrollPane(area), BorderLayout.CENTER);
        add(rodape, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(fechar);                       // Enter fecha
        getRootPane().registerKeyboardAction(e -> dispose(),          // Esc fecha
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

        pack();
        setLocationRelativeTo(dono);
    }

    public static void mostrar(Window dono, Venda venda) {
        new ComprovanteDialog(dono, venda).setVisible(true);
    }
}
