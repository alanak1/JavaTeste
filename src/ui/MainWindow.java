package ui;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import models.Sistema;

/**
 * MainWindow.java
 *
 * Janela principal que reúne o “SideMenuPanel” (à esquerda),
 * o “ContentPanel” (no centro, com CardLayout) e a “StatusBar” (na parte inferior).
 */
public class MainWindow extends JFrame {
    private final SideMenuPanel sidePanel;
    private final ContentPanel contentPanel;
    private final StatusBar statusBar;
    private final Sistema sistema;

    public MainWindow() {
        super("Meu Sistema de Frequência");

        // 1) Inicializa o sistema e carrega dados iniciais
        this.sistema = new Sistema();                    // em Sistema(): chamar carregarDados() e criarDadosIniciais()
/*        // (Se quiser evitar exceções não-verificadas aqui, pode envolver em try/catch.)
        try {
            this.sistema.criarDadosIniciais();           // chamar criarDadosIniciais() manualmente, caso queira
        } catch (Exception ignore) {
            // caso já existam dados, não faz nada
        }
            */

        // 2) Configurações principais da JFrame
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(900, 600);
        setLocationRelativeTo(null);

        // 3) Cria cada painel e adiciona à BorderLayout
        sidePanel    = new SideMenuPanel();
        contentPanel = new ContentPanel(sistema);
        statusBar    = new StatusBar();

        add(sidePanel,    BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
        add(statusBar,    BorderLayout.SOUTH);

        // 4) Intercepta o fechamento para confirmar com o usuário
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitProcedure();
            }
        });

        // 5) Configurar ações dos botões laterais
        sidePanel.getBtnHome().addActionListener(e -> {
            contentPanel.showCard("HOME");
            statusBar.setStatus("Página Inicial");
        });
        sidePanel.getBtnRegistro().addActionListener(e -> {
            contentPanel.showCard("REGISTRO");
            statusBar.setStatus("Registrar Frequência");
        });
        sidePanel.getBtnRelatorio().addActionListener(e -> {
            contentPanel.showCard("RELATORIO");
            statusBar.setStatus("Relatório de Usuários");
        });
        sidePanel.getBtnConfig().addActionListener(e -> {
            contentPanel.showCard("CONFIG");
            statusBar.setStatus("Configurações");
        });
        sidePanel.getBtnSair().addActionListener(e -> exitProcedure());

        // 6) Exibe a janela
        setVisible(true);
    }

    /**
     * Pergunta ao usuário se realmente deseja sair antes de fechar o programa.
     * Ao confirmar, salva os dados em disco e finaliza a aplicação.
     */
    private void exitProcedure() {
        int resp = JOptionPane.showConfirmDialog(
            this,
            "Deseja realmente sair?",
            "Confirmação",
            JOptionPane.YES_NO_OPTION
        );
        if (resp == JOptionPane.YES_OPTION) {
            // Salvar estado antes de sair (serialização)
            try {
                sistema.salvarUsuarios();     // grava usuarios em “usuarios.dat”
                sistema.salvarFrequencias();  // grava frequências em “frequencias.dat”
            } catch (Exception ex) {
                // caso deseje exibir erro: JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
            System.exit(0);
        }
    }
}
