package View;

import java.awt.BorderLayout;
import java.awt.Container;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class TelaPrincipal extends JFrame {

    public TelaPrincipal() {
        setTitle("Gerenciamento de Estoque");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        mostrarInicial();   
    }

    
    public void trocarTela(JPanel tela) {
        Container conteudo = getContentPane();
        conteudo.removeAll();
        conteudo.add(tela, BorderLayout.CENTER);
        conteudo.revalidate();
        conteudo.repaint();
        pack();                      
        setLocationRelativeTo(null);
    }


    public void mostrarInicial() {
        trocarTela(new TelaInicial(this));
    }

    
    public void mostrarProdutos() {
        trocarTela(new Produtos(this));
    }

    public static void main(String[] args) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaPrincipal.class.getName())
                    .log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(() -> new TelaPrincipal().setVisible(true));
    }
}
