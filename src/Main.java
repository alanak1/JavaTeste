import javax.swing.*;
import ui.LoginWindow;

/**
 * Main.java - Versão Corrigida Final
 * 
 * Classe principal que inicializa o Sistema de Frequência.
 * CORREÇÃO: Removidas exceções desnecessárias do configurarLookAndFeel()
 */
public class Main {
    
    public static void main(String[] args) {
        System.out.println(" Iniciando Sistema de Frequência...");
        
        // Configurar Look and Feel do sistema
        configurarLookAndFeel();
        
        // Configurar propriedades do Swing
        configurarSwing();
        
        // Inicializar aplicação na EDT (Event Dispatch Thread)
        SwingUtilities.invokeLater(() -> {
            try {
                System.out.println("🔑 Abrindo tela de login...");
                
                // Criar e exibir a tela de login
                LoginWindow loginWindow = new LoginWindow();
                loginWindow.setVisible(true);
                
                System.out.println(" Sistema inicializado com sucesso!");
                
            } catch (Exception e) {
                System.err.println(" Erro ao inicializar a aplicação:");
                e.printStackTrace();
                
                // Exibir erro em dialog para o usuário
                JOptionPane.showMessageDialog(
                    null,
                    "Erro ao inicializar o sistema:\n" + e.getMessage(),
                    "Erro de Inicialização",
                    JOptionPane.ERROR_MESSAGE
                );
                
                System.exit(1);
            }
        });
    }
    
    /**
     * Configura o Look and Feel do sistema operacional.
     * CORREÇÃO: Simplificado para evitar exceções "nunca lançadas"
     */
    private static void configurarLookAndFeel() {
        try {
            // Usar o Look and Feel nativo do sistema operacional
            UIManager.setLookAndFeel(UIManager.getLookAndFeel());
            System.out.println(" Look and Feel configurado: " + UIManager.getLookAndFeel().getName());
            
        } catch (Exception e) {
            // Captura qualquer exceção relacionada ao Look and Feel
            System.err.println(" Erro ao configurar Look and Feel: " + e.getMessage());
            System.err.println(" Usando Look and Feel padrão do Java");
        }
    }
    
    /**
     * Configura propriedades globais do Swing.
     */
    private static void configurarSwing() {
        // Habilitar antialiasing para texto
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        
        // Configurar comportamento padrão dos componentes
        UIManager.put("Button.defaultButtonFollowsFocus", Boolean.TRUE);
        UIManager.put("Table.gridColor", UIManager.getColor("Label.foreground"));
        
        // Configurar tooltips
        ToolTipManager.sharedInstance().setInitialDelay(500);
        ToolTipManager.sharedInstance().setDismissDelay(10000);
        
        System.out.println("✅ Propriedades do Swing configuradas");
    }
    
    /**
     * Método para debug - mostra informações do sistema.
     */
    public static void mostrarInfoSistema() {
        System.out.println("\n=== INFORMAÇÕES DO SISTEMA ===");
        System.out.println("Java Version: " + System.getProperty("java.version"));
        System.out.println("Java Vendor: " + System.getProperty("java.vendor"));
        System.out.println("OS Name: " + System.getProperty("os.name"));
        System.out.println("OS Version: " + System.getProperty("os.version"));
        System.out.println("User Name: " + System.getProperty("user.name"));
        System.out.println("User Home: " + System.getProperty("user.home"));
        System.out.println("Working Directory: " + System.getProperty("user.dir"));
        System.out.println("Look and Feel: " + UIManager.getLookAndFeel().getName());
        System.out.println("==============================\n");
    }
}