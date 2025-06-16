package ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import models.*;
import static ui.UIConfig.*;

/**
 * MainWindow.java - Versão Corrigida
 * 
 * Janela principal do sistema que adapta a interface 
 * de acordo com o tipo de usuário logado.
 */
public class MainWindow extends JFrame {
    private final Sistema sistema;
    private final Usuario usuarioLogado;
    private final ContentPanel contentPanel;
    private final SideMenuPanel sidePanel;
    private final StatusBar statusBar;

    public MainWindow(Usuario usuarioLogado) {
        super("Sistema de Frequência - " + usuarioLogado.getNome());
        this.usuarioLogado = usuarioLogado;
        this.sistema = new Sistema();

        // Configurações da janela
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        aplicarTamanhoPadrao(this);
        setLayout(new BorderLayout());
        getContentPane().setBackground(COR_FUNDO);

        // Criar componentes
        contentPanel = new ContentPanel(sistema, usuarioLogado);
        statusBar = new StatusBar();
        sidePanel = criarMenuPorPerfil();

        // Montar layout
        add(sidePanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);

        // Configurar eventos
        configurarEventos();

        // Status inicial
        statusBar.setStatus("Logado como: " + usuarioLogado.getNome() + " (" + usuarioLogado.getTipoUsuario() + ")");

        // Centralizar e exibir
        setLocationRelativeTo(null);
        setVisible(true);
        
        System.out.println("✅ Janela principal aberta para usuário: " + usuarioLogado.getNome());
    }

    /**
     * Cria o menu lateral baseado no perfil do usuário logado.
     */
    private SideMenuPanel criarMenuPorPerfil() {
        // Actions comuns
        ActionListener homeAction = e -> {
            contentPanel.showCard("HOME");
            statusBar.setStatus("Página Inicial");
        };

        ActionListener sairAction = e -> exitProcedure();

        // Actions específicas por perfil
        ActionListener usuariosAction = null;
        ActionListener disciplinasAction = null;
        ActionListener registrarAction = null;
        ActionListener relatorioAction = null;
        ActionListener configAction = null;

        // Configurar actions baseadas no perfil
        if (ehAdministrador()) {
            usuariosAction = e -> {
                contentPanel.showCard("USUARIOS");
                statusBar.setStatus("Gerenciamento de Usuários");
            };
            disciplinasAction = e -> {
                contentPanel.showCard("DISCIPLINAS");
                statusBar.setStatus("Gerenciamento de Disciplinas");
            };
            registrarAction = e -> {
                contentPanel.showCard("REGISTRO");
                statusBar.setStatus("Registro de Frequência");
            };
            relatorioAction = e -> {
                contentPanel.showCard("RELATORIO");
                statusBar.setStatus("Relatórios Gerais");
            };
            configAction = e -> {
                contentPanel.showCard("CONFIG");
                statusBar.setStatus("Configurações do Sistema");
            };
            
        } else if (ehProfessor() || ehCoordenador()) {
            registrarAction = e -> {
                contentPanel.showCard("REGISTRO");
                statusBar.setStatus("Registro de Frequência");
            };
            relatorioAction = e -> {
                contentPanel.showCard("RELATORIO");
                statusBar.setStatus("Relatórios de Frequência");
            };
            configAction = e -> {
                contentPanel.showCard("CONFIG");
                statusBar.setStatus("Configurações");
            };
            
        } else if (ehAluno()) {
            relatorioAction = e -> {
                contentPanel.showCard("RELATORIO");
                statusBar.setStatus("Minhas Frequências");
            };
            configAction = e -> {
                contentPanel.showCard("CONFIG");
                statusBar.setStatus("Configurações");
            };
        }

        // Criar menu com as actions apropriadas
        return new SideMenuPanel(
            homeAction,
            usuariosAction,
            disciplinasAction,
            registrarAction,
            relatorioAction,
            configAction,
            sairAction
        );
    }

    /**
     * Configura os listeners de eventos da janela.
     */
    private void configurarEventos() {
        // Evento de fechamento da janela
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitProcedure();
            }
        });

        // Atalhos de teclado
        configurarAtalhosTeclado();
    }

    /**
     * Configura atalhos de teclado para navegação rápida.
     */
    private void configurarAtalhosTeclado() {
        JRootPane rootPane = getRootPane();
        
        // Ctrl+H = Home
        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_DOWN_MASK), "home");
        rootPane.getActionMap().put("home", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contentPanel.showCard("HOME");
                statusBar.setStatus("Atalho: Home");
            }
        });

        // Ctrl+R = Registro (se disponível)
        if (ehProfessor() || ehCoordenador() || ehAdministrador()) {
            rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK), "registro");
            rootPane.getActionMap().put("registro", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    contentPanel.showCard("REGISTRO");
                    statusBar.setStatus("Atalho: Registro");
                }
            });
        }

        // Ctrl+T = Relatórios
        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_DOWN_MASK), "relatorio");
        rootPane.getActionMap().put("relatorio", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contentPanel.showCard("RELATORIO");
                statusBar.setStatus("Atalho: Relatórios");
            }
        });

        // Ctrl+Q = Sair
        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK), "sair");
        rootPane.getActionMap().put("sair", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exitProcedure();
            }
        });
    }

    /**
     * Procedimento de saída do sistema.
     * Pergunta ao usuário se deseja sair e salva os dados.
     */
    private void exitProcedure() {
        int resposta = JOptionPane.showConfirmDialog(
            this,
            "Deseja realmente sair do sistema?",
            "Confirmação de Saída",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (resposta == JOptionPane.YES_OPTION) {
            try {
                statusBar.setStatus("Salvando dados...");
                
                // Salvar dados do sistema
                sistema.salvarUsuarios();
                sistema.salvarFrequencias();
                
                System.out.println(" Dados salvos com sucesso");
                System.out.println(" Usuário " + usuarioLogado.getNome() + " saiu do sistema");
                
                // Fechar aplicação
                System.exit(0);
                
            } catch (Exception e) {
                System.err.println(" Erro ao salvar dados: " + e.getMessage());
                
                int respSemSalvar = JOptionPane.showConfirmDialog(
                    this,
                    "Erro ao salvar dados:\n" + e.getMessage() + 
                    "\n\nDeseja sair mesmo assim?",
                    "Erro ao Salvar",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.ERROR_MESSAGE
                );
                
                if (respSemSalvar == JOptionPane.YES_OPTION) {
                    System.exit(1);
                }
            }
        }
    }

    // ===== MÉTODOS DE VERIFICAÇÃO DE PERFIL =====

    /**
     * Verifica se o usuário logado é um Administrador.
     */
    private boolean ehAdministrador() {
        return usuarioLogado instanceof Administrador;
    }

    /**
     * Verifica se o usuário logado é um Professor.
     */
    private boolean ehProfessor() {
        return usuarioLogado instanceof Professor;
    }

    /**
     * Verifica se o usuário logado é um Coordenador.
     */
    private boolean ehCoordenador() {
        return usuarioLogado instanceof Coordenador;
    }

    /**
     * Verifica se o usuário logado é um Aluno.
     */
    private boolean ehAluno() {
        return usuarioLogado instanceof Aluno;
    }

    // ===== GETTERS PARA ACESSO AOS COMPONENTES =====

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public Sistema getSistema() {
        return sistema;
    }

    public ContentPanel getContentPanel() {
        return contentPanel;
    }

    public SideMenuPanel getSidePanel() {
        return sidePanel;
    }

    public StatusBar getStatusBar() {
        return statusBar;
    }

    /**
     * Atualiza a barra de status com uma mensagem temporária.
     */
    public void mostrarStatusTemporario(String mensagem, int segundos) {
        String statusOriginal = statusBar.lblStatus.getText();
        statusBar.setStatus(mensagem);
        
        Timer timer = new Timer(segundos * 1000, e -> statusBar.setStatus(statusOriginal));
        timer.setRepeats(false);
        timer.start();
    }

    /**
     * Força a atualização dos painéis (útil após mudanças nos dados).
     */
    public void atualizarInterface() {
        contentPanel.repaint();
        contentPanel.revalidate();
        System.out.println(" Interface atualizada");
    }
}