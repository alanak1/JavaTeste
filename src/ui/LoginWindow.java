package ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import models.Sistema;
import models.Usuario;

/**
 * LoginWindow.java
 * 
 * Tela de login do sistema. Valida as credenciais do usuário
 * antes de permitir acesso ao sistema principal.
 */
public class LoginWindow extends JFrame {
    private JTextField txtEmail;
    private JPasswordField txtSenha;
    private JButton btnLogin;
    private JButton btnCancelar;
    private final Sistema sistema;

    public LoginWindow() {
        super("Sistema de Frequência - Login");
        this.sistema = new Sistema();

        initComponents();
        setupLayout();
        setupActions();

        // Configurações da janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(UIConfig.COR_FUNDO);
    }

    private void initComponents() {
        txtEmail = new JTextField(20);
        txtSenha = new JPasswordField(20);

        btnLogin = criarBotaoEstilizado("Entrar", UIConfig.COR_DESTAQUE, Color.WHITE, UIConfig.COR_DESTAQUE);
        btnCancelar = criarBotaoEstilizado("Cancelar", UIConfig.GUNMETAL, Color.WHITE, UIConfig.GUNMETAL);
    }

    private JButton criarBotaoEstilizado(String texto, Color corFundo, Color corTexto, Color corBorda) {
        JButton btn = new JButton(texto);
        btn.setBackground(corFundo);
        btn.setForeground(corTexto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(corBorda, 1));
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        return btn;
    }

    private void setupLayout() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIConfig.COR_FUNDO);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Painel do título
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(UIConfig.COR_FUNDO);
        JLabel lblTitle = new JLabel("Sistema de Frequência");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(UIConfig.COR_TEXTO);
        JLabel lblSubtitle = new JLabel("Faça login para continuar");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitle.setForeground(UIConfig.GUNMETAL);

        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(lblTitle);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(lblSubtitle);

        // Painel do formulário
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(UIConfig.COR_FUNDO);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setForeground(UIConfig.COR_TEXTO);
        formPanel.add(lblEmail, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(txtEmail, gbc);

        // Senha
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel lblSenha = new JLabel("Senha (CPF):");
        lblSenha.setForeground(UIConfig.COR_TEXTO);
        formPanel.add(lblSenha, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(txtSenha, gbc);

        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(UIConfig.COR_FUNDO);
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnCancelar);


        // Adicionar tudo ao painel principal
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Painel de informações
        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(UIConfig.MOONSTONE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel lblInfo = new JLabel("<html><center>Use as credenciais de um usuário cadastrado.<br>" +
                                "Exemplo: admin@exemplo.com / 123456789</center></html>");
        lblInfo.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblInfo.setForeground(UIConfig.COR_TEXTO);
        infoPanel.add(lblInfo);

        // Layout final
        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.SOUTH);
    }

    private void setupActions() {
        // Ação do botão Login
        btnLogin.addActionListener(e -> fazerLogin());
        

        // Ação do botão Cancelar
        btnCancelar.addActionListener(e -> System.exit(0));

        // Enter no campo senha também faz login
        txtSenha.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    fazerLogin();
                }
            }
        });

        // Enter no campo email move para senha
        txtEmail.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    txtSenha.requestFocus();
                }
            }
        });
    }

    private void fazerLogin() {
        String email = txtEmail.getText().trim();
        String senha = new String(txtSenha.getPassword()).trim();

        if (email.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Por favor, preencha todos os campos!",
                "Atenção",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validar credenciais
        Usuario usuarioLogado = null;
        for (Usuario u : sistema.listarUsuarios()) {
            // Exemplo: senha = CPF
            if (u.getEmail().equalsIgnoreCase(email) && u.getCpf().equals(senha)) {
                usuarioLogado = u;
                break;
            }
        }

        if (usuarioLogado != null) {
            // Login bem-sucedido
            final Usuario usuarioFinal = usuarioLogado; // Variável final para usar no lambda
            JOptionPane.showMessageDialog(this,
                "Bem-vindo, " + usuarioLogado.getNome() + "!",
                "Login bem-sucedido",
                JOptionPane.INFORMATION_MESSAGE);

            // Fechar tela de login e abrir tela principal
            this.dispose();
            SwingUtilities.invokeLater(() -> new MainWindow(usuarioFinal).setVisible(true));
        } else {
            // Login falhou
            JOptionPane.showMessageDialog(this,
                "Email ou senha incorretos!",
                "Erro de Login",
                JOptionPane.ERROR_MESSAGE);
            txtSenha.setText("");
            txtSenha.requestFocus();
        }
    }

    // Opcional: método para mostrar credenciais de exemplo
    public void mostrarCredenciaisExemplo() {
        StringBuilder info = new StringBuilder("=== Usuários cadastrados para teste ===\n\n");
        for (Usuario u : sistema.listarUsuarios()) {
            info.append(String.format("%s (%s)\n  Email: %s\n  Senha: %s\n\n",
                u.getNome(), u.getTipoUsuario(), u.getEmail(), u.getCpf()));
        }

        JTextArea textArea = new JTextArea(info.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Consolas", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        JOptionPane.showMessageDialog(this, scrollPane,
            "Credenciais de Teste", JOptionPane.INFORMATION_MESSAGE);
    }
}
