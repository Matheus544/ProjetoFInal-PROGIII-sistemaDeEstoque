package View;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Janela principal da aplicação de vendas (classe com o main).
 */
public class AppVendas extends JFrame {

    public AppVendas() {
        super("Vendas");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        TelaVenda tela = new TelaVenda();
        setContentPane(tela);
        pack();
        setMinimumSize(getSize());
        setLocationRelativeTo(null);

        SwingUtilities.invokeLater(tela::focarCodigo);
    }

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AppVendas.class.getName())
                    .log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(() -> new AppVendas().setVisible(true));
    }
}
