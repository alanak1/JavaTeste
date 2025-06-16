package ui;

import java.awt.*;
import javax.swing.*;

/**
 * SideMenuPanel.java
 *
 * Painel lateral contendo botões de navegação:
 * - Home
 * - Registrar Frequência
 * - Relatórios
 * - Configurações
 * - Logout
 * - Sair
 *
 * Cada botão terá cor de fundo e fonte ajustadas.
 */
public class SideMenuPanel extends JPanel {
    private JButton btnHome;
    private JButton btnRegistro;
    private JButton btnRelatorio;
    private JButton btnConfig;
    private JButton btnLogout;
    private JButton btnSair;

    public SideMenuPanel() {
        setLayout(new GridLayout(6, 1, 5, 5));
        setPreferredSize(new Dimension(200, 0));
        setBackground(new Color(240, 240, 240)); // cinza claro

        btnHome = new JButton("Home");
        styleNavButton(btnHome, new Color(100, 149, 237)); // Cornflower Blue

        btnRegistro = new JButton("Registrar Frequência");
        styleNavButton(btnRegistro, new Color(255, 165, 0)); // Laranja

        btnRelatorio = new JButton("Relatórios");
        styleNavButton(btnRelatorio, new Color(100, 149, 237)); // Cornflower Blue

        btnConfig = new JButton("Configurações");
        styleNavButton(btnConfig, new Color(100, 149, 237)); // Cornflower Blue
        
        btnLogout = new JButton("Logout");
        styleNavButton(btnLogout, new Color(255, 140, 0)); // Dark Orange

        btnSair = new JButton("Sair");
        styleNavButton(btnSair, new Color(220, 53, 69)); // Vermelho tomate

        add(btnHome);
        add(btnRegistro);
        add(btnRelatorio);
        add(btnConfig);
        add(btnLogout);
        add(btnSair);
    }

    /**
     * Métodos auxiliares para estilizar botões de navegação de forma consistente.
     */
    private void styleNavButton(JButton btn, Color bgColor) {
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        btn.setOpaque(true);            // ESSENCIAL
        btn.setContentAreaFilled(true);
    }

    // Getters para que MainWindow possa "ouvir" cliques
    public JButton getBtnHome() {
        return btnHome;
    }
    public JButton getBtnRegistro() {
        return btnRegistro;
    }
    public JButton getBtnRelatorio() {
        return btnRelatorio;
    }
    public JButton getBtnConfig() {
        return btnConfig;
    }
    public JButton getBtnLogout() {
        return btnLogout;
    }
    public JButton getBtnSair() {
        return btnSair;
    }
}