package ui;

import models.*;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ContentPanel extends JPanel {
    private CardLayout cardLayout;
    private Sistema sistema;
    private Usuario usuarioLogado;

    // Painéis principais
    private JPanel homePanel, usuariosPanel, disciplinasPanel, registrarPanel, relatorioPanel, configPanel;

    public ContentPanel(Sistema sistema, Usuario usuarioLogado) {
        this.sistema = sistema;
        this.usuarioLogado = usuarioLogado;

        cardLayout = new CardLayout();
        setLayout(cardLayout);

        // Home sempre aparece
        homePanel = buildHomePanel();
        add(homePanel, "HOME");

        // Adiciona painéis por perfil
        if (ehAdministrador()) {
            usuariosPanel = buildUsuariosPanel();
            disciplinasPanel = buildDisciplinasPanel();
            relatorioPanel = buildRelatorioPanel();
            configPanel = buildConfigPanel();

            add(usuariosPanel, "USUARIOS");
            add(disciplinasPanel, "DISCIPLINAS");
            add(relatorioPanel, "RELATORIO");
            add(configPanel, "CONFIG");
        } else if (ehCoordenador()) {
            registrarPanel = buildRegistrarPanel();
            relatorioPanel = buildRelatorioPanel();
            configPanel = buildConfigPanel();

            add(registrarPanel, "REGISTRAR");
            add(relatorioPanel, "RELATORIO");
            add(configPanel, "CONFIG");
        } else if (ehProfessor()) {
            registrarPanel = buildRegistrarPanel();
            relatorioPanel = buildRelatorioPanel();
            configPanel = buildConfigPanel();

            add(registrarPanel, "REGISTRAR");
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

    public void showCard(String card) {
        cardLayout.show(this, card);
    }

    // =====================
    // Métodos utilitários para tipo de usuário
    // =====================
    private boolean ehAdministrador() {
        return usuarioLogado.getTipoUsuario().equalsIgnoreCase("Administrador");
    }
    private boolean ehProfessor() {
        return usuarioLogado.getTipoUsuario().equalsIgnoreCase("Professor");
    }
    private boolean ehCoordenador() {
        return usuarioLogado.getTipoUsuario().equalsIgnoreCase("Coordenador");
    }
    private boolean ehAluno() {
        return usuarioLogado.getTipoUsuario().equalsIgnoreCase("Aluno");
    }

    // =====================
    // Home
    // =====================
    private JPanel buildHomePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        JLabel lbl = new JLabel("Bem-vindo ao Sistema de Frequência", SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lbl.setForeground(new Color(40, 40, 40));
        panel.add(lbl);
        return panel;
    }

    // =====================
    // Painel de usuários (somente Admin)
    // =====================
    private JPanel buildUsuariosPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel lbl = new JLabel("Gerenciar Usuários", SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        panel.add(lbl, BorderLayout.NORTH);

        JTextArea area = new JTextArea();
        area.setEditable(false);

        StringBuilder sb = new StringBuilder();
        sb.append("=== Lista de Usuários ===\n\n");
        for (Usuario u : sistema.listarUsuarios()) {
            sb.append(String.format("%s (%s)\n", u.getNome(), u.getTipoUsuario()));
            sb.append("Email: " + u.getEmail() + "\n");
            sb.append("-----------------------------\n");
        }
        area.setText(sb.toString());
        panel.add(new JScrollPane(area), BorderLayout.CENTER);

        // Aqui você pode adicionar botões: Adicionar/Remover usuário, etc.

        return panel;
    }

    // =====================
    // Painel de disciplinas (somente Admin)
    // =====================
    private JPanel buildDisciplinasPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel lbl = new JLabel("Gerenciar Disciplinas", SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        panel.add(lbl, BorderLayout.NORTH);

        // Adapte para listar/editar disciplinas (crie uma lista, JTable, etc.)
        JTextArea area = new JTextArea("Funcionalidade em construção...");
        area.setEditable(false);
        panel.add(area, BorderLayout.CENTER);

        return panel;
    }

    // =====================
    // Registrar frequência (Professor, Coordenador)
    // =====================
    private JPanel buildRegistrarPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        JLabel lblTitle = new JLabel("Registrar Frequência");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(lblTitle, gbc);

        gbc.gridwidth = 1;

        // Campos exemplo
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

        // Ação do botão de registrar frequência (exemplo)
        btnRegistrar.addActionListener(e -> {
            try {
                String matricula = txtMatricula.getText().trim();
                String disciplina = txtDisciplina.getText().trim();
                LocalDate data = LocalDate.parse(txtData.getText().trim(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                boolean presente = txtPresente.getText().trim().equalsIgnoreCase("s");

                // Você pode validar e lançar a frequência pelo sistema aqui
                // sistema.adicionarFrequencia(...);

                JOptionPane.showMessageDialog(panel, "Frequência registrada para matrícula " + matricula);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Erro: " + ex.getMessage());
            }
        });

        return panel;
    }

    // =====================
    // Relatórios (Todos: Admin vê tudo, Coordenador vê todos, Professor vê só turmas, Aluno vê só ele)
    // =====================
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
            // Supondo que você tenha algum método para buscar as frequências das turmas do professor
            sb.append("=== Relatório de Frequências das suas turmas ===\n\n");
            // Implemente lógica aqui
            sb.append("(Em construção)\n");
        } else if (ehAluno()) {
            sb.append("=== Suas Frequências ===\n\n");
            for (Frequencia f : sistema.listarFrequencias()) {
                if (f.getAlunoMatricula().equals(((Aluno) usuarioLogado).getMatricula())) {
                    sb.append(String.format("Disciplina: %s | Data: %s | Presente: %s\n",
                            f.getDisciplina(), f.getDataFormatada(), f.isPresente() ? "Sim" : "Não"));
                }
            }
        }

        area.setText(sb.toString());
        panel.add(new JScrollPane(area), BorderLayout.CENTER);

        // Se for admin ou coordenador, pode adicionar botão para exportar relatório:
        if (ehAdministrador() || ehCoordenador()) {
            JButton btnExportar = new JButton("Exportar para CSV");
            btnExportar.addActionListener(e -> {
                sistema.exportarParaCSV(); // Chame o método correto
                JOptionPane.showMessageDialog(panel, "Relatório exportado com sucesso!");
            });
            panel.add(btnExportar, BorderLayout.SOUTH);
        }

        return panel;
    }

    // =====================
    // Configurações (todos têm acesso)
    // =====================
    private JPanel buildConfigPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel lbl = new JLabel("Configurações", SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.ITALIC, 18));
        panel.add(lbl, BorderLayout.CENTER);
        return panel;
    }
}
