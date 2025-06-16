import ui.LoginWindow;

/**
 * TesteCompleto.java
 * 
 * Classe principal para executar o sistema completo com interface gráfica.
 * Inicia com a tela de login.
 */
public class TesteCompleto {
    public static void main(String[] args) {
        // Configura o Look and Feel do sistema
        try {
            javax.swing.UIManager.setLookAndFeel(
                javax.swing.UIManager.getSystemLookAndFeelClassName()
            );
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException e) {
            System.err.println("Erro ao configurar Look and Feel: " + e.getMessage());
        }

        // Inicia a aplicação na Event Dispatch Thread
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LoginWindow loginWindow = new LoginWindow();
                loginWindow.setVisible(true);
            }
        });
    }
}