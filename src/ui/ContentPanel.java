package ui;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import models.*;

public class ContentPanel extends JPanel {
    private final CardLayout cardLayout;
    private final Sistema sistema;
    private Usuario usuarioLogado;

    // Cards
    private JPanel homePanel, registroPanel, usuariosPanel, disciplinasPanel, relatorioPanel, configPanel;

    public ContentPanel(Sistema sistema, Usuario usuarioLogado) {
        this.sistema = sistema;
        this.usuarioLogado = usuarioLogado;
        cardLayout = new CardLayout();
        setLayout(cardLayout);

        // Sempre disponível
        homePanel = buildHomePanel();
        add(homePanel, "HOME");

        // Acesso por perfil
        if (ehAdministrador()) {
            usuariosPanel = buildUsuariosPanel();
            disciplinasPanel = buildDisciplinasPanel();
            relatorioPanel = buildRelatorioPanel();
            configPanel = buildConfigPanel();
            add(usuariosPanel, "USUARIOS");
            add(disciplinasPanel, "DISCIPLINAS");
            add(relatorioPanel, "RELATORIO");
            add(configPanel, "CONFIG");
        } else if (ehProfessor() || ehCoordenador()) {
            registroPanel = buildRegistroPanel();
            relatorioPanel = buildRelatorioPanel();
            configPanel = buildConfigPanel();
            add(registroPanel, "REGISTRO");
            add(relatorioPanel, "RELATORIO");
            add(configPanel, "CONFIG");
        } else if (ehAluno()) {
            relatorioPanel = buildRelatorioPanel();
            configPanel = buildConfigPanel();
            add(relatorioPanel, "RELATORIO");
            add(configPanel, "CONFIG");
        }
        showCard("HOME");
    }

    public void showCard(String name) {
        cardLayout.show(this, name);
    }

    public void setUsuarioLogado(Usuario usuario) {
        this.usuarioLogado = usuario;
    }

    // === Métodos de verificação de tipo ===
    private boolean ehAdministrador() { return usuarioLogado instanceof Administrador; }
    private boolean ehProfessor()     { return usuarioLogado instanceof Professor; }
    private boolean ehCoordenador()   { return usuarioLogado instanceof Coordenador; }
    private boolean ehAluno()         { return usuarioLogado instanceof Aluno; }

    // --- Painéis por perfil ---

    private JPanel buildHomePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        JLabel lbl = new JLabel("Bem-vindo ao Sistema de Frequência", SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lbl.setForeground(new Color(40, 40, 40));
        panel.add(lbl);
        return panel;
    }

    private JPanel buildUsuariosPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel lbl = new JLabel("Gerenciar Usuários", SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        panel.add(lbl, BorderLayout.NORTH);
        JTextArea area = new JTextArea();
        area.setEditable(false);
        StringBuilder sb = new StringBuilder();
        for (Usuario u : sistema.listarUsuarios()) {
            sb.append(u.getNome()).append(" (").append(u.getTipoUsuario()).append(")\n");
        }
        area.setText(sb.toString());
        panel.add(new JScrollPane(area), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildDisciplinasPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel lbl = new JLabel("Gerenciar Disciplinas", SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        panel.add(lbl, BorderLayout.NORTH);
        JTextArea area = new JTextArea("Funcionalidade em construção...");
        area.setEditable(false);
        panel.add(area, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildRegistroPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        JLabel lblTitle = new JLabel("Registrar Frequência");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(lblTitle, gbc);

        gbc.gridwidth = 1;

        gbc.gridy++;
        panel.add(new JLabel("Matrícula do Aluno:"), gbc);
        gbc.gridx = 1;
        JTextField txtMatricula = new JTextField(12);
        panel.add(txtMatricula, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Disciplina:"), gbc);
        gbc.gridx = 1;
        JTextField txtDisciplina = new JTextField(12);
        panel.add(txtDisciplina, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Data (dd/MM/yyyy):"), gbc);
        gbc.gridx = 1;
        JTextField txtData = new JTextField(12);
        panel.add(txtData, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Presente? (s/n):"), gbc);
        gbc.gridx = 1;
        JTextField txtPresente = new JTextField(2);
        panel.add(txtPresente, gbc);

        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        JButton btnRegistrar = new JButton("Registrar");
        panel.add(btnRegistrar, gbc);

        btnRegistrar.addActionListener(e -> {
            try {
                String matricula = txtMatricula.getText().trim();
                String disciplina = txtDisciplina.getText().trim();
                LocalDate data = LocalDate.parse(txtData.getText().trim(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                boolean presente = txtPresente.getText().trim().equalsIgnoreCase("s");
                JOptionPane.showMessageDialog(panel, "Frequência registrada para matrícula " + matricula);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Erro: " + ex.getMessage());
            }
        });
        return panel;
    }

    private JPanel buildRelatorioPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel lbl = new JLabel("Relatórios de Frequência", SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        panel.add(lbl, BorderLayout.NORTH);

        JTextArea area = new JTextArea();
        area.setEditable(false);

        StringBuilder sb = new StringBuilder();
        if (ehAdministrador() || ehCoordenador()) {
            sb.append("=== Relatório de TODAS as frequências ===\n\n");
            for (Frequencia f : sistema.listarFrequencias()) {
                sb.append(String.format("Aluno: %s | Disciplina: %s | Data: %s | Presente: %s\n",
                        f.getAlunoMatricula(), f.getDisciplina(), f.getDataFormatada(), f.isPresente() ? "Sim" : "Não"));
            }
        } else if (ehProfessor()) {
            sb.append("=== Relatório de Frequências das suas turmas ===\n\n");
            sb.append("(Em construção)\n");
        } else if (ehAluno() && usuarioLogado instanceof Aluno aluno) {
            sb.append("=== Suas Frequências ===\n\n");
            for (Frequencia f : sistema.listarFrequencias()) {
                if (f.getAlunoMatricula().equals(aluno.getMatricula())) {
                    sb.append(String.format("Disciplina: %s | Data: %s | Presente: %s\n",
                            f.getDisciplina(), f.getDataFormatada(), f.isPresente() ? "Sim" : "Não"));
                }
            }
        }
        area.setText(sb.toString());
        panel.add(new JScrollPane(area), BorderLayout.CENTER);

        if (ehAdministrador() || ehCoordenador()) {
            JButton btnExportar = new JButton("Exportar para CSV");
            btnExportar.addActionListener(e -> {
                sistema.exportarParaCSV();
                JOptionPane.showMessageDialog(panel, "Relatório exportado com sucesso!");
            });
            panel.add(btnExportar, BorderLayout.SOUTH);
        }

        return panel;
    }

    private JPanel buildConfigPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel lbl = new JLabel("Configurações", SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.ITALIC, 18));
        panel.add(lbl, BorderLayout.CENTER);
        return panel;
    }
}
