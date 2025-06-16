package ui;

import java.awt.*;
import javax.swing.*;

/**
 * StatusBar.java
 *
 * Barra de status fixa na parte inferior da janela:
 * Exibe mensagens de contexto (por exemplo, "Você está em: Home").
 */
public class StatusBar extends JPanel {
    public JLabel lblStatus;

    public StatusBar() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(0, 25));
        setBackground(new Color(30, 30, 30)); // cinza escuro

        lblStatus = new JLabel("Pronto");
        lblStatus.setForeground(new Color(200, 200, 200)); // quase branco
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblStatus.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        add(lblStatus, BorderLayout.WEST);
    }

    /**
     * Atualiza a mensagem exibida na barra de status.
     */
    public void setStatus(String message) {
        lblStatus.setText(message);
    }
}