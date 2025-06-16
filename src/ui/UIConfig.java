package ui;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

public class UIConfig {
    public static final int LARGURA_PADRAO = 900;
    public static final int ALTURA_PADRAO = 600;

    public static final Color ASH_GRAY      = Color.decode("#BAC1B8");
    public static final Color MOONSTONE     = Color.decode("#58A4B0");
    public static final Color DARK_SPRING   = Color.decode("#0C7C59");
    public static final Color GUNMETAL      = Color.decode("#2B303A");
    public static final Color CHILI_RED     = Color.decode("#D64933");

    public static final Color COR_FUNDO     = ASH_GRAY;
    public static final Color COR_MENU      = MOONSTONE;
    public static final Color COR_DESTAQUE  = CHILI_RED;
    public static final Color COR_TEXTO     = GUNMETAL;
    public static final Color COR_SUCESSO   = DARK_SPRING;

    /**
     * Cria um JButton padronizado com cor, fonte, borda e ação.
     */
    public static JButton criarBotao(String texto, Color corFundo, Color corTexto, Color corBorda, ActionListener acao) {
        JButton btn = new JButton(texto);
        btn.setBackground(corFundo);
        btn.setForeground(corTexto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setBorder(BorderFactory.createLineBorder(corBorda, 2));
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        if (acao != null) btn.addActionListener(acao);
        return btn;
    }

    /**
     * Define o tamanho padrão e centraliza a janela na tela.
     */
    public static void aplicarTamanhoPadrao(JFrame frame) {
        frame.setSize(LARGURA_PADRAO, ALTURA_PADRAO);
        frame.setLocationRelativeTo(null); // centraliza
    }
}
