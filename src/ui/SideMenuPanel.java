package ui;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import static ui.UIConfig.*;

/**
 * SideMenuPanel.java - Versão Corrigida
 * 
 * Painel de menu lateral que se adapta ao perfil do usuário.
 * Só cria botões para funcionalidades que o usuário tem acesso.
 */
public class SideMenuPanel extends JPanel {

    // Botões do menu (podem ser null se não aplicáveis ao perfil)
    private final JButton btnHome;
    private final JButton btnUsuarios;
    private final JButton btnDisciplinas;
    private final JButton btnRegistrar;
    private final JButton btnRelatorio;
    private final JButton btnConfig;
    private final JButton btnSair;

    /**
     * Construtor que cria o menu baseado nas actions fornecidas.
     * Actions nulas resultam em botões não criados.
     */
    public SideMenuPanel(ActionListener homeAction,
            ActionListener usuariosAction,
            ActionListener disciplinasAction,
            ActionListener registrarAction,
            ActionListener relatorioAction,
            ActionListener configAction,
            ActionListener sairAction) {

        // Configuração do painel
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(COR_MENU);
        setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        setPreferredSize(new Dimension(220, 0));

        // === SEÇÃO PRINCIPAL ===
        adicionarSecao("NAVEGAÇÃO");

        // Botão Home (sempre presente)
        btnHome = criarBotaoMenu("🏠 Início", MOONSTONE, COR_TEXTO, homeAction);
        add(btnHome);
        adicionarEspacamento();

        // === SEÇÃO GESTÃO (apenas para Admin/Coordenador) ===
        boolean temGestao = usuariosAction != null || disciplinasAction != null;
        if (temGestao) {
            adicionarSecao("GESTÃO");
        }

        // Botão Usuários (apenas Administradores)
        if (usuariosAction != null) {
            btnUsuarios = criarBotaoMenu("👥 Usuários", COR_FUNDO, MOONSTONE, usuariosAction);
            add(btnUsuarios);
            adicionarEspacamento();
        } else {
            btnUsuarios = null;
        }

        // Botão Disciplinas (Admin e Coordenador)
        if (disciplinasAction != null) {
            btnDisciplinas = criarBotaoMenu(" Disciplinas", COR_FUNDO, MOONSTONE, disciplinasAction);
            add(btnDisciplinas);
            adicionarEspacamento();
        } else {
            btnDisciplinas = null;
        }

        // === SEÇÃO OPERAÇÕES ===
        boolean temOperacoes = registrarAction != null || relatorioAction != null;
        if (temOperacoes) {
            adicionarSecao("OPERAÇÕES");
        }

        // Botão Registrar Frequência (Professor, Coordenador, Admin)
        if (registrarAction != null) {
            btnRegistrar = criarBotaoMenu(" Registrar", COR_DESTAQUE, Color.WHITE, registrarAction);
            btnRegistrar.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Destaque especial
            add(btnRegistrar);
            adicionarEspacamento();
        } else {
            btnRegistrar = null;
        }

        // Botão Relatórios (todos os usuários)
        if (relatorioAction != null) {
            btnRelatorio = criarBotaoMenu("📊 Relatórios", COR_FUNDO, MOONSTONE, relatorioAction);
            add(btnRelatorio);
            adicionarEspacamento();
        } else {
            btnRelatorio = null;
        }

        // === SEÇÃO SISTEMA ===
        adicionarSecao("SISTEMA");

        // Botão Configurações (se disponível)
        if (configAction != null) {
            btnConfig = criarBotaoMenu("⚙️ Configurações", COR_FUNDO, MOONSTONE, configAction);
            add(btnConfig);
            adicionarEspacamento();
        } else {
            btnConfig = null;
        }

        // Espaçador flexível para empurrar o botão Sair para baixo
        add(Box.createVerticalGlue());

        // Botão Sair (sempre presente no final)
        if (sairAction != null) {
            btnSair = criarBotaoMenu(" Sair", Color.WHITE, COR_DESTAQUE, sairAction);
            btnSair.setFont(new Font("Segoe UI", Font.BOLD, 13));
            add(btnSair);
        } else {
            btnSair = null;
        }
    }

    /**
     * Cria um botão de menu estilizado.
     */
    private JButton criarBotaoMenu(String texto, Color corFundo, Color corTexto, ActionListener action) {
        JButton botao = criarBotao(texto, corFundo, corTexto, corFundo.darker(), action);
        botao.setAlignmentX(Component.CENTER_ALIGNMENT);
        botao.setMaximumSize(new Dimension(200, 40));
        botao.setPreferredSize(new Dimension(200, 40));

        // Efeito hover
        Color corOriginal = botao.getBackground();
        Color corHover = corOriginal.brighter();

        botao.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (botao.isEnabled()) {
                    botao.setBackground(corHover);
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botao.setBackground(corOriginal);
            }
        });

        return botao;
    }

    /**
     * Adiciona uma seção com título no menu.
     */
    private void adicionarSecao(String titulo) {
        // Espaçamento antes da seção (exceto para a primeira)
        if (getComponentCount() > 0) {
            add(Box.createVerticalStrut(15));
        }

        // Label da seção
        JLabel lblSecao = new JLabel(titulo);
        lblSecao.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblSecao.setForeground(GUNMETAL);
        lblSecao.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(lblSecao);

        // Linha separadora
        JSeparator separador = new JSeparator();
        separador.setForeground(GUNMETAL);
        separador.setMaximumSize(new Dimension(200, 1));
        add(separador);

        add(Box.createVerticalStrut(8));
    }

    /**
     * Adiciona espaçamento pequeno entre botões.
     */
    private void adicionarEspacamento() {
        add(Box.createVerticalStrut(5));
    }

    // ===== GETTERS PARA ACESSO AOS BOTÕES =====

    public JButton getBtnHome() {
        return btnHome;
    }

    public JButton getBtnUsuarios() {
        return btnUsuarios;
    }

    public JButton getBtnDisciplinas() {
        return btnDisciplinas;
    }

    public JButton getBtnRegistrar() {
        return btnRegistrar;
    }

    public JButton getBtnRelatorio() {
        return btnRelatorio;
    }

    public JButton getBtnConfig() {
        return btnConfig;
    }

    public JButton getBtnSair() {
        return btnSair;
    }

    // ===== MÉTODOS UTILITÁRIOS =====

    /**
     * Habilita ou desabilita um botão específico.
     */
    public void habilitarBotao(String nomeBotao, boolean habilitado) {
        JButton botao = switch (nomeBotao.toLowerCase()) {
            case "home" -> btnHome;
            case "usuarios" -> btnUsuarios;
            case "disciplinas" -> btnDisciplinas;
            case "registrar" -> btnRegistrar;
            case "relatorio" -> btnRelatorio;
            case "config" -> btnConfig;
            case "sair" -> btnSair;
            default -> null;
        };

        if (botao != null) {
            botao.setEnabled(habilitado);
            // Ajustar aparência visual quando desabilitado
            if (!habilitado) {
                botao.setBackground(ASH_GRAY);
                botao.setForeground(GUNMETAL.brighter());
            }
        }
    }

    /**
     * Destaca um botão como ativo.
     */
    public void destacarBotaoAtivo(String nomeBotao) {
        // Primeiro, resetar todos os botões
        resetarDestaqueBotoes();

        // Depois destacar o botão específico
        JButton botao = switch (nomeBotao.toLowerCase()) {
            case "home" -> btnHome;
            case "usuarios" -> btnUsuarios;
            case "disciplinas" -> btnDisciplinas;
            case "registrar" -> btnRegistrar;
            case "relatorio" -> btnRelatorio;
            case "config" -> btnConfig;
            default -> null;
        };

        if (botao != null) {
            botao.setBackground(DARK_SPRING);
            botao.setForeground(Color.WHITE);
            botao.setBorder(BorderFactory.createLineBorder(DARK_SPRING.darker(), 2));
        }
    }

    /**
     * Remove o destaque de todos os botões.
     */
    private void resetarDestaqueBotoes() {
        JButton[] botoes = { btnHome, btnUsuarios, btnDisciplinas, btnRegistrar, btnRelatorio, btnConfig };

        for (JButton botao : botoes) {
            if (botao != null) {
                // Restaurar cores originais baseadas no tipo do botão
                if (botao == btnHome) {
                    botao.setBackground(MOONSTONE);
                    botao.setForeground(COR_TEXTO);
                } else if (botao == btnRegistrar) {
                    botao.setBackground(COR_DESTAQUE);
                    botao.setForeground(Color.WHITE);
                } else {
                    botao.setBackground(COR_FUNDO);
                    botao.setForeground(MOONSTONE);
                }
                botao.setBorder(BorderFactory.createLineBorder(botao.getBackground().darker(), 1));
            }
        }
    }

    /**
     * Retorna informações sobre quais botões estão disponíveis.
     */
    public String getInfoBotoes() {
        StringBuilder info = new StringBuilder("Botões disponíveis: ");
        if (btnHome != null)
            info.append("Home ");
        if (btnUsuarios != null)
            info.append("Usuários ");
        if (btnDisciplinas != null)
            info.append("Disciplinas ");
        if (btnRegistrar != null)
            info.append("Registrar ");
        if (btnRelatorio != null)
            info.append("Relatório ");
        if (btnConfig != null)
            info.append("Config ");
        if (btnSair != null)
            info.append("Sair");
        return info.toString();
    }
}