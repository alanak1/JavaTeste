package ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import models.Sistema;
import models.Usuario;

/**
 * MainWindow.java
 *
 * Janela principal que reúne o "SideMenuPanel" (à esquerda),
 * o "ContentPanel" (no centro, com CardLayout) e a "StatusBar" (na parte
 * inferior).
 */
public class MainWindow extends JFrame {
    private final SideMenuPanel sidePanel;
    private final ContentPanel contentPanel;
    private final StatusBar statusBar;
    private final Sistema sistema;
    private final Usuario usuarioLogado;

    // Construtor vazio para testes (usa o primeiro usuário como padrão)
    public MainWindow() {
        this(null);
    }

    public MainWindow(Usuario usuarioLogado) {
        super("Sistema de Frequência - " + (usuarioLogado != null ? usuarioLogado.getNome() : "Não logado"));

        this.usuarioLogado = usuarioLogado;
        // 1) Inicializa o sistema e carrega dados iniciais
        this.sistema = new Sistema();

        // 2) Configurações principais da JFrame
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(900, 600);
        setLocationRelativeTo(null);

        // 3) Cria cada painel e adiciona à BorderLayout
        sidePanel = new SideMenuPanel();
        contentPanel = new ContentPanel(sistema);
        contentPanel.setUsuarioLogado(usuarioLogado); // Passa o usuário logado
        statusBar = new StatusBar();

        add(sidePanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);

        // Atualizar status bar com informações do usuário
        statusBar.setStatus("Logado como: " + usuarioLogado.getNome() +
                " (" + usuarioLogado.getTipoUsuario() + ")");

        // 4) Intercepta o fechamento para confirmar com o usuário
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitProcedure();
            }
        });

        // 5) Configurar ações dos botões laterais
        sidePanel.getBtnHome().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contentPanel.showCard("HOME");
                statusBar.setStatus("Página Inicial");
            }
        });

        sidePanel.getBtnRegistro().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contentPanel.showCard("REGISTRO");
                statusBar.setStatus("Registrar Frequência");
            }
        });

        sidePanel.getBtnRelatorio().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contentPanel.showCard("RELATORIO");
                statusBar.setStatus("Relatório de Usuários");
            }
        });

        sidePanel.getBtnConfig().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contentPanel.showCard("CONFIG");
                statusBar.setStatus("Configurações");
            }
        });

        sidePanel.getBtnLogout().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int resp = JOptionPane.showConfirmDialog(
                        MainWindow.this,
                        "Deseja fazer logout?",
                        "Confirmação",
                        JOptionPane.YES_NO_OPTION);
                if (resp == JOptionPane.YES_OPTION) {
                    // Salvar estado antes de fazer logout
                    MainWindow.this.dispose();
                    javax.swing.SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            new ui.LoginWindow().setVisible(true);
                        }
                    });
                }
            }
        });

        sidePanel.getBtnSair().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exitProcedure();
            }
        });

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
                JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.YES_OPTION) {
            // Salvar estado antes de sair (serialização)
            try {
                sistema.salvarUsuarios(); // grava usuarios em "usuarios.dat"
                sistema.salvarFrequencias(); // grava frequências em "frequencias.dat"
            } catch (Exception ex) {
                // caso deseje exibir erro: JOptionPane.showMessageDialog(this, ex.getMessage(),
                // "Erro", JOptionPane.ERROR_MESSAGE);
            }
            System.exit(0);
        }
    }
}