package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import models.Sistema;
import models.Frequencia;
import models.Usuario;
import models.Aluno;
import models.Professor;
import models.Administrador;
import models.Coordenador;
import persistencia.SistemaException;

/**
 * ContentPanel.java
 *
 * Painel central que funciona como um CardLayout:
 * - Card "HOME": tela de boas‐vindas
 * - Card "REGISTRO": formulário para registrar frequência
 * - Card "RELATORIO": listagem de relatórios de usuários
 * - Card "CONFIG": placeholder de configurações
 */
public class ContentPanel extends JPanel {
    private final CardLayout cardLayout;
    private final Sistema sistema;
    private Usuario usuarioLogado = null;  // Inicializar como null para evitar erros

    // Componentes do card REGISTRO
    private JTextField txtRegCpf;
    private JTextField txtAlunoId;
    private JTextField txtCourseCode;
    private JTextField txtDate;
    private JTextField txtPresent;

    @SuppressWarnings("LeakingThisInConstructor")
    public ContentPanel(Sistema sistema) {
        this.sistema = sistema;
        cardLayout = new CardLayout();
        setLayout(cardLayout);
        setBackground(new Color(250, 250, 250));

        // Adiciona cada "card"
        add(buildHomePanel(),    "HOME");
        add(buildRegistroPanel(),"REGISTRO");
        add(buildRelatorioPanel(),"RELATORIO");
        add(buildConfigPanel(),  "CONFIG");

        // Exibe "HOME" por padrão
        cardLayout.show(this, "HOME");
    }
    
    public void setUsuarioLogado(Usuario usuario) {
        this.usuarioLogado = usuario;
    }

    /** Painel HOME (boas‐vindas) **/
    private JPanel buildHomePanel() {
        JPanel home = new JPanel(new BorderLayout());
        home.setBackground(Color.WHITE);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);

        JLabel lbl = new JLabel("Bem‐vindo ao Sistema de Frequência");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lbl.setForeground(new Color(45, 45, 45));
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(lbl);
        centerPanel.add(Box.createVerticalGlue());

        home.add(centerPanel, BorderLayout.CENTER);
        return home;
    }

    /** Painel REGISTRO de Frequência **/
    private JPanel buildRegistroPanel() {
        JPanel registro = new JPanel(new GridBagLayout());
        registro.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.EAST;

        // Linha 1: CPF do registrador (Professor ou Administrador)
        gbc.gridx = 0;
        gbc.gridy = 0;
        registro.add(new JLabel("CPF do Registrador:"), gbc);

        gbc.gridx = 1;
        txtRegCpf = new JTextField(15);
        registro.add(txtRegCpf, gbc);

        // Linha 2: ID do estudante
        gbc.gridx = 0;
        gbc.gridy = 1;
        registro.add(new JLabel("ID do Estudante (int):"), gbc);

        gbc.gridx = 1;
        txtAlunoId = new JTextField(10);
        registro.add(txtAlunoId, gbc);

        // Linha 3: Código da disciplina
        gbc.gridx = 0;
        gbc.gridy = 2;
        registro.add(new JLabel("Código da Disciplina:"), gbc);

        gbc.gridx = 1;
        txtCourseCode = new JTextField(10);
        registro.add(txtCourseCode, gbc);

        // Linha 4: Data (dd/MM/yyyy)
        gbc.gridx = 0;
        gbc.gridy = 3;
        registro.add(new JLabel("Data (dd/MM/yyyy):"), gbc);

        gbc.gridx = 1;
        txtDate = new JTextField(10);
        registro.add(txtDate, gbc);

        // Linha 5: Presente? (s / n)
        gbc.gridx = 0;
        gbc.gridy = 4;
        registro.add(new JLabel("Presente? (s / n)"), gbc);

        gbc.gridx = 1;
        txtPresent = new JTextField(2);
        registro.add(txtPresent, gbc);

        // Botão "Registrar"
        JButton btnSubmit = new JButton("Registrar");
        btnSubmit.setBackground(new Color(100, 149, 237));
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnSubmit.setFocusPainted(false);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        registro.add(btnSubmit, gbc);

        // Ação do botão "Registrar"
        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarFrequencia();
            }
        });

        return registro;
    }
    
    /** Método para registrar frequência **/
    private void registrarFrequencia() {
        // Verificar se o usuário logado tem permissão
        if (usuarioLogado != null && !usuarioLogado.podeEditarFrequencia()) {
            JOptionPane.showMessageDialog(
                this,
                "Você não tem permissão para registrar frequência!",
                "Acesso Negado",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        
        try {
            String regCpf = txtRegCpf.getText().trim();
            // Se o campo estiver vazio, usar o CPF do usuário logado
            if (regCpf.isEmpty() && usuarioLogado != null) {
                regCpf = usuarioLogado.getCpf();
            }
            // 1) Busca o usuário pelo CPF. Se não existir, lança SistemaException.
            Usuario registrador = sistema.buscarUsuario(regCpf);

            // 2) Verifica se é Professor, Administrador ou Coordenador
            if (!(registrador instanceof Professor) && 
                !(registrador instanceof Administrador) && 
                !(registrador instanceof Coordenador)) {
                JOptionPane.showMessageDialog(
                    this,
                    "Somente Professor, Administrador ou Coordenador pode registrar frequência!",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            // 3) Lê ID do aluno
            int alunoId;
            try {
                alunoId = Integer.parseInt(txtAlunoId.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                    this,
                    "ID de aluno inválido (não é inteiro)!",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // 4) Encontra o Aluno pela lista de usuários
            Aluno chosenAluno = null;
            for (Usuario u : sistema.listarUsuarios()) {
                if (u instanceof Aluno) {
                    Aluno st = (Aluno) u;
                    if (st.getId() == alunoId) {
                        chosenAluno = st;
                        break;
                    }
                }
            }
            if (chosenAluno == null) {
                JOptionPane.showMessageDialog(
                    this,
                    "Aluno de ID " + alunoId + " não encontrado!",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // 5) Código da disciplina
            String courseCode = txtCourseCode.getText().trim();

            // 6) Data
            String dateStr = txtDate.getText().trim();
            LocalDate date;
            try {
                date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                    this,
                    "Formato de data inválido! Use dd/MM/yyyy",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // 7) Presença
            boolean isPresent = txtPresent.getText().trim().equalsIgnoreCase("s");

            // 8) Gera ID da frequência (tamanho atual + 1)
            int newId = sistema.listarFrequencias().size() + 1;

            // 9) Cria o objeto Frequencia e adiciona via adicionarFrequencia()
            Frequencia f = new Frequencia(
                newId,
                chosenAluno.getMatricula(),
                courseCode,
                date,
                isPresent,
                regCpf
            );
            sistema.adicionarFrequencia(f);

            JOptionPane.showMessageDialog(
                this,
                "Frequência registrada com sucesso!",
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE
            );
            clearRegistroFields();
        } catch (SistemaException ex) {
            JOptionPane.showMessageDialog(
                this,
                "Erro ao registrar frequência:\n" + ex.getDetalhesErro(),
                "Erro",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /** Limpa campos **/
    private void clearRegistroFields() {
        txtRegCpf.setText("");
        txtAlunoId.setText("");
        txtCourseCode.setText("");
        txtDate.setText("");
        txtPresent.setText("");
    }

    /** Painel RELATÓRIO de Usuários **/
    private JPanel buildRelatorioPanel() {
        JPanel rel = new JPanel(new BorderLayout());
        rel.setBackground(Color.WHITE);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        textArea.setForeground(new Color(45, 45, 45));
        textArea.setBackground(new Color(245, 245, 245));

        // Monta relatório geral de todos os usuários
        StringBuilder sb = new StringBuilder();
        sb.append("--- Relatório Geral de Usuários ---\n\n");
        for (Usuario u : sistema.listarUsuarios()) {
            sb.append(u.toString()).append("\n");
        }
        textArea.setText(sb.toString());

        JScrollPane scroll = new JScrollPane(textArea);
        rel.add(scroll, BorderLayout.CENTER);
        return rel;
    }

    /** Painel CONFIGURAÇÕES (placeholder) **/
    private JPanel buildConfigPanel() {
        JPanel cfg = new JPanel(new BorderLayout());
        cfg.setBackground(Color.WHITE);

        JLabel lbl = new JLabel("Painel de Configurações (em construção)", SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.ITALIC, 18));
        lbl.setForeground(new Color(100, 100, 100));

        cfg.add(lbl, BorderLayout.CENTER);
        return cfg;
    }

    /** Exibe um card ("HOME", "REGISTRO", "RELATORIO" ou "CONFIG") **/
    public void showCard(String name) {
        cardLayout.show(this, name);
        
        // Se for a tela de registro e temos usuário logado, preencher o CPF
        if ("REGISTRO".equals(name) && usuarioLogado != null && usuarioLogado.podeEditarFrequencia()) {
            txtRegCpf.setText(usuarioLogado.getCpf());
            txtRegCpf.setEditable(false);
        }
    }
}