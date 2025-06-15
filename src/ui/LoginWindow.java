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
    }
    
    private void initComponents() {
        txtEmail = new JTextField(20);
        txtSenha = new JPasswordField(20);
        btnLogin = new JButton("Entrar");
        btnCancelar = new JButton("Cancelar");
        
        // Estilizar botões
        btnLogin.setBackground(new Color(100, 149, 237));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setFocusPainted(false);
        
        btnCancelar.setBackground(new Color(220, 53, 69));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancelar.setFocusPainted(false);
    }
    
    private void setupLayout() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Painel do título
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.WHITE);
        JLabel lblTitle = new JLabel("Sistema de Frequência");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(45, 45, 45));
        JLabel lblSubtitle = new JLabel("Faça login para continuar");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitle.setForeground(new Color(100, 100, 100));
        
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(lblTitle);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(lblSubtitle);
        
        // Painel do formulário
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Email
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Email:"), gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(txtEmail, gbc);
        
        // Senha (por simplicidade, vamos usar o CPF como senha)
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Senha (CPF):"), gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(txtSenha, gbc);
        
        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnCancelar);
        
        // Adicionar tudo ao painel principal
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Painel de informações
        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(new Color(240, 240, 240));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel lblInfo = new JLabel("<html><center>Use as credenciais de um usuário cadastrado.<br>" +
                                "Exemplo: ana@exemplo.com / 33333333333</center></html>");
        lblInfo.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblInfo.setForeground(new Color(100, 100, 100));
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
            SwingUtilities.invokeLater(() -> {
                new MainWindow(usuarioFinal).setVisible(true);
            });
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
    
    public void mostrarCredenciaisExemplo() {
        String info = "=== Usuários cadastrados para teste ===\n\n";
        for (Usuario u : sistema.listarUsuarios()) {
            info += String.format("%s (%s)\n  Email: %s\n  Senha: %s\n\n", 
                u.getNome(), u.getTipoUsuario(), u.getEmail(), u.getCpf());
        }
        
        JTextArea textArea = new JTextArea(info);
        textArea.setEditable(false);
        textArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        
        JOptionPane.showMessageDialog(this, scrollPane, 
            "Credenciais de Teste", JOptionPane.INFORMATION_MESSAGE);
    }
}