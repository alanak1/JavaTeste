package ui;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.*;
import models.*;
import persistencia.SistemaException;
import static ui.UIConfig.*;

/**
 * ContentPanel.java - Versão Corrigida
 * 
 * Painel central que funciona como um CardLayout adaptado ao perfil do usuário:
 * - Card "HOME": tela de boas‐vindas
 * - Card "USUARIOS": gerenciamento de usuários (Admin)
 * - Card "DISCIPLINAS": gerenciamento de disciplinas (Admin/Coord)
 * - Card "REGISTRO": formulário para registrar frequência (Prof/Coord/Admin)
 * - Card "RELATORIO": listagem de relatórios
 * - Card "CONFIG": configurações
 * 
 * CORREÇÕES:
 * - Substituído sistema.getUsuarios() por sistema.listarUsuarios()
 * - Substituído sistema.getFrequencias() por sistema.listarFrequencias()
 * - Melhorada a interface de registro
 * - Adicionada validação de dados
 * - Melhorados os relatórios por perfil
 */
public class ContentPanel extends JPanel {
    private final CardLayout cardLayout;
    private final Sistema sistema;
    private Usuario usuarioLogado;

    // Cards disponíveis
    private JPanel homePanel, usuariosPanel, disciplinasPanel, registroPanel, relatorioPanel, configPanel;

    public ContentPanel(Sistema sistema, Usuario usuarioLogado) {
        this.sistema = sistema;
        this.usuarioLogado = usuarioLogado;
        cardLayout = new CardLayout();
        setLayout(cardLayout);
        setBackground(COR_FUNDO);

        criarPaineis();
        showCard("HOME");
    }

    /**
     * Cria todos os painéis baseados no perfil do usuário.
     */
    private void criarPaineis() {
        // Home sempre disponível
        homePanel = buildHomePanel();
        add(homePanel, "HOME");

        // Painéis baseados no perfil
        if (ehAdministrador()) {
            usuariosPanel = buildUsuariosPanel();
            disciplinasPanel = buildDisciplinasPanel();
            registroPanel = buildRegistroPanel();
            relatorioPanel = buildRelatorioPanel();
            configPanel = buildConfigPanel();

            add(usuariosPanel, "USUARIOS");
            add(disciplinasPanel, "DISCIPLINAS");
            add(registroPanel, "REGISTRO");
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
    }

    public void showCard(String name) {
        cardLayout.show(this, name);
    }

    public void setUsuarioLogado(Usuario usuario) {
        this.usuarioLogado = usuario;
        // Recriar painéis se necessário
        removeAll();
        criarPaineis();
        revalidate();
        repaint();
    }

    // === Métodos de verificação de tipo ===
    private boolean ehAdministrador() {
        return usuarioLogado instanceof Administrador;
    }

    private boolean ehProfessor() {
        return usuarioLogado instanceof Professor;
    }

    private boolean ehCoordenador() {
        return usuarioLogado instanceof Coordenador;
    }

    private boolean ehAluno() {
        return usuarioLogado instanceof Aluno;
    }

    // === CONSTRUÇÃO DOS PAINÉIS ===

    /**
     * Painel HOME - Boas-vindas personalizada.
     */
    private JPanel buildHomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COR_FUNDO);

        // Painel de boas-vindas
        JPanel welcomePanel = new JPanel(new GridBagLayout());
        welcomePanel.setBackground(COR_FUNDO);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Título principal
        JLabel lblTitle = new JLabel("Bem-vindo ao Sistema de Frequência", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(COR_TEXTO);
        gbc.gridx = 0;
        gbc.gridy = 0;
        welcomePanel.add(lblTitle, gbc);

        // Saudação personalizada
        String saudacao = String.format("Olá, %s!", usuarioLogado.getNome());
        JLabel lblSaudacao = new JLabel(saudacao, SwingConstants.CENTER);
        lblSaudacao.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lblSaudacao.setForeground(MOONSTONE);
        gbc.gridy = 1;
        welcomePanel.add(lblSaudacao, gbc);

        // Tipo de usuário
        String tipoInfo = String.format("Logado como: %s", usuarioLogado.getTipoUsuario());
        JLabel lblTipo = new JLabel(tipoInfo, SwingConstants.CENTER);
        lblTipo.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        lblTipo.setForeground(GUNMETAL);
        gbc.gridy = 2;
        welcomePanel.add(lblTipo, gbc);

        panel.add(welcomePanel, BorderLayout.CENTER);

        // Painel de estatísticas rápidas
        JPanel statsPanel = criarPainelEstatisticas();
        panel.add(statsPanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Cria painel com estatísticas rápidas do sistema.
     */
    private JPanel criarPainelEstatisticas() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 10, 10));
        panel.setBackground(MOONSTONE);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Total de usuários
        int totalUsuarios = sistema.listarUsuarios().size(); // CORRIGIDO: era getUsuarios()
        JLabel lblUsuarios = new JLabel(
                String.format("<html><center><b>%d</b><br>Usuários</center></html>", totalUsuarios));
        lblUsuarios.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblUsuarios.setForeground(Color.WHITE);
        lblUsuarios.setHorizontalAlignment(SwingConstants.CENTER);

        // Total de frequências
        int totalFrequencias = sistema.listarFrequencias().size(); // CORRIGIDO: era getFrequencias()
        JLabel lblFrequencias = new JLabel(
                String.format("<html><center><b>%d</b><br>Frequências</center></html>", totalFrequencias));
        lblFrequencias.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblFrequencias.setForeground(Color.WHITE);
        lblFrequencias.setHorizontalAlignment(SwingConstants.CENTER);

        // Estatística específica por tipo
        String estatEspecifica = obterEstatisticaEspecifica();
        JLabel lblEspecifica = new JLabel(String.format("<html><center>%s</center></html>", estatEspecifica));
        lblEspecifica.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblEspecifica.setForeground(Color.WHITE);
        lblEspecifica.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(lblUsuarios);
        panel.add(lblFrequencias);
        panel.add(lblEspecifica);

        return panel;
    }

    /**
     * Retorna estatística específica baseada no tipo de usuário.
     */
    private String obterEstatisticaEspecifica() {
        if (ehAluno() && usuarioLogado instanceof Aluno aluno) {
            List<Frequencia> minhasFreq = sistema.buscarFrequenciasPorAluno(aluno.getMatricula());
            long presencas = minhasFreq.stream().filter(Frequencia::isPresente).count();
            return String.format("<b>%d</b><br>Suas Presenças", presencas);
        } else if (ehProfessor()) {
            List<Frequencia> registradas = sistema.buscarFrequenciasPorRegistrador(usuarioLogado.getCpf());
            return String.format("<b>%d</b><br>Registradas por Você", registradas.size());
        } else {
            long alunos = sistema.listarUsuarios().stream().filter(u -> u instanceof Aluno).count();
            return String.format("<b>%d</b><br>Alunos", alunos);
        }
    }

    /**
     * Painel USUARIOS - Gerenciamento de usuários (Admin).
     */
    private JPanel buildUsuariosPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COR_FUNDO);

        // Título
        JLabel lblTitle = new JLabel("Gerenciamento de Usuários", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(COR_TEXTO);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panel.add(lblTitle, BorderLayout.NORTH);

        // Lista de usuários
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Consolas", Font.PLAIN, 12));
        area.setBackground(Color.WHITE);
        area.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        StringBuilder sb = new StringBuilder();
        sb.append("LISTA COMPLETA DE USUÁRIOS:\n");
        sb.append("=".repeat(50)).append("\n\n");

        List<Usuario> usuarios = sistema.listarUsuarios(); // CORRIGIDO: era getUsuarios()
        for (Usuario u : usuarios) {
            sb.append(String.format("ID: %d | Nome: %s\n", u.getId(), u.getNome()));
            sb.append(String.format("Tipo: %s | Email: %s\n", u.getTipoUsuario(), u.getEmail()));
            sb.append(String.format("CPF: %s | Ativo: %s\n", u.getCpf(), u.isAtivo() ? "Sim" : "Não"));

            // Informações específicas por tipo
            if (u instanceof Aluno aluno) {
                sb.append(String.format("Matrícula: %s | Curso: %s | Semestre: %d\n",
                        aluno.getMatricula(), aluno.getCurso(), aluno.getSemestre()));
            } else if (u instanceof Professor professor) {
                sb.append(String.format("Área: %s | Titulação: %s\n",
                        professor.getArea(), professor.getTitulacao()));
            } else if (u instanceof Coordenador coord) {
                sb.append(String.format("Curso Coordenado: %s\n", coord.getCurso()));
            } else if (u instanceof Administrador admin) {
                sb.append(String.format("Nível de Acesso: %s\n", admin.getNivelAcesso()));
            }

            sb.append("-".repeat(40)).append("\n");
        }

        area.setText(sb.toString());
        JScrollPane scroll = new JScrollPane(area);
        panel.add(scroll, BorderLayout.CENTER);

        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(COR_FUNDO);

        JButton btnAdicionar = criarBotao("Adicionar Usuário", COR_SUCESSO, Color.WHITE, COR_SUCESSO,
                e -> mostrarDialogAdicionarUsuario());
        JButton btnRemover = criarBotao("Remover Usuário", COR_DESTAQUE, Color.WHITE, COR_DESTAQUE,
                e -> mostrarDialogRemoverUsuario());
        JButton btnAtualizar = criarBotao("Atualizar Lista", MOONSTONE, Color.WHITE, MOONSTONE,
                e -> atualizarListaUsuarios());

        buttonPanel.add(btnAdicionar);
        buttonPanel.add(btnRemover);
        buttonPanel.add(btnAtualizar);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Painel DISCIPLINAS - Gerenciamento de disciplinas.
     */
    private JPanel buildDisciplinasPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COR_FUNDO);

        JLabel lblTitle = new JLabel("Gerenciamento de Disciplinas", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(COR_TEXTO);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panel.add(lblTitle, BorderLayout.NORTH);

        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Consolas", Font.PLAIN, 12));

        // Listar disciplinas únicas das frequências
        List<Frequencia> frequencias = sistema.listarFrequencias(); // CORRIGIDO: era getFrequencias()
        StringBuilder sb = new StringBuilder();
        sb.append("DISCIPLINAS NO SISTEMA:\n");
        sb.append("=".repeat(30)).append("\n\n");

        frequencias.stream()
                .map(Frequencia::getDisciplina)
                .distinct()
                .sorted()
                .forEach(disciplina -> {
                    long totalAulas = frequencias.stream()
                            .filter(f -> f.getDisciplina().equals(disciplina))
                            .count();
                    sb.append(String.format("• %s (%d registros)\n", disciplina, totalAulas));
                });

        if (frequencias.isEmpty()) {
            sb.append("Nenhuma disciplina encontrada.\n");
            sb.append("Registre algumas frequências para ver as disciplinas aqui.");
        }

        area.setText(sb.toString());
        panel.add(new JScrollPane(area), BorderLayout.CENTER);

        return panel;
    }

    /**
     * Painel REGISTRO - Formulário para registrar frequência.
     */
    private JPanel buildRegistroPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COR_FUNDO);

        // Título
        JLabel lblTitle = new JLabel("Registro de Frequência", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(COR_TEXTO);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panel.add(lblTitle, BorderLayout.NORTH);

        // Formulário
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MOONSTONE, 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Campos do formulário
        JTextField txtMatricula = new JTextField(15);
        JTextField txtDisciplina = new JTextField(15);
        JTextField txtData = new JTextField(12);
        txtData.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        JComboBox<String> cbPresente = new JComboBox<>(new String[] { "Presente", "Falta" });
        JTextArea txtObservacoes = new JTextArea(3, 15);

        // Layout do formulário
        int row = 0;

        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Matrícula do Aluno:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtMatricula, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Disciplina:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtDisciplina, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Data (dd/MM/yyyy):"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtData, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        formPanel.add(cbPresente, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(new JLabel("Observações:"), gbc);
        gbc.gridx = 1;
        JScrollPane scrollObs = new JScrollPane(txtObservacoes);
        formPanel.add(scrollObs, gbc);

        // Centralizar formulário
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(COR_FUNDO);
        centerPanel.add(formPanel);
        panel.add(centerPanel, BorderLayout.CENTER);

        // Botão registrar
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(COR_FUNDO);

        JButton btnRegistrar = criarBotao("Registrar Frequência", COR_DESTAQUE, Color.WHITE, COR_DESTAQUE,
                e -> registrarFrequencia(txtMatricula, txtDisciplina, txtData, cbPresente, txtObservacoes));

        buttonPanel.add(btnRegistrar);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Painel RELATORIO - Relatórios baseados no perfil.
     */
    private JPanel buildRelatorioPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COR_FUNDO);

        JLabel lblTitle = new JLabel("Relatórios de Frequência", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(COR_TEXTO);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panel.add(lblTitle, BorderLayout.NORTH);

        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Consolas", Font.PLAIN, 12));
        area.setBackground(Color.WHITE);

        StringBuilder sb = new StringBuilder();

        if (ehAdministrador() || ehCoordenador()) {
            sb.append(sistema.gerarRelatorioGeralUsuarios()).append("\n\n");
            sb.append("=".repeat(60)).append("\n\n");

            // Relatório de todas as frequências
            List<Frequencia> todasFreq = sistema.listarFrequencias(); // CORRIGIDO: era getFrequencias()
            sb.append("TODAS AS FREQUÊNCIAS:\n");
            for (Frequencia f : todasFreq) {
                sb.append(f.getDescricaoCompleta()).append("\n");
            }

        } else if (ehProfessor()) {
            sb.append("FREQUÊNCIAS REGISTRADAS POR VOCÊ:\n");
            sb.append("=".repeat(40)).append("\n\n");

            List<Frequencia> minhasFreq = sistema.buscarFrequenciasPorRegistrador(usuarioLogado.getCpf());
            for (Frequencia f : minhasFreq) {
                sb.append(f.getDescricaoCompleta()).append("\n");
            }

        } else if (ehAluno() && usuarioLogado instanceof Aluno aluno) {
            sb.append("SUAS FREQUÊNCIAS:\n");
            sb.append("=".repeat(20)).append("\n\n");

            List<Frequencia> minhasFreq = sistema.buscarFrequenciasPorAluno(aluno.getMatricula());
            for (Frequencia f : minhasFreq) {
                sb.append(f.getDescricaoCompleta()).append("\n");
            }

            // Estatísticas pessoais
            if (!minhasFreq.isEmpty()) {
                long presencas = minhasFreq.stream().filter(Frequencia::isPresente).count();
                double percentual = (presencas * 100.0) / minhasFreq.size();
                sb.append("\n").append("=".repeat(30)).append("\n");
                sb.append(String.format("ESTATÍSTICAS PESSOAIS:\n"));
                sb.append(String.format("Total de aulas: %d\n", minhasFreq.size()));
                sb.append(String.format("Presenças: %d (%.1f%%)\n", presencas, percentual));
                sb.append(String.format("Faltas: %d (%.1f%%)\n", minhasFreq.size() - presencas, 100 - percentual));
            }
        }

        area.setText(sb.toString());
        JScrollPane scroll = new JScrollPane(area);
        panel.add(scroll, BorderLayout.CENTER);

        // Botões de ação
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(COR_FUNDO);

        if (ehAdministrador() || ehCoordenador()) {
            JButton btnExportar = criarBotao("Exportar CSV", MOONSTONE, Color.WHITE, MOONSTONE,
                    e -> exportarRelatorio());
            buttonPanel.add(btnExportar);
        }

        JButton btnAtualizar = criarBotao("Atualizar", COR_SUCESSO, Color.WHITE, COR_SUCESSO,
                e -> atualizarRelatorio());
        buttonPanel.add(btnAtualizar);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Painel CONFIG - Configurações básicas.
     */
    private JPanel buildConfigPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COR_FUNDO);

        JLabel lblTitle = new JLabel("Configurações", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(COR_TEXTO);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panel.add(lblTitle, BorderLayout.NORTH);

        JPanel configContent = new JPanel(new GridLayout(4, 1, 10, 10));
        configContent.setBackground(COR_FUNDO);
        configContent.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // Informações do usuário
        JPanel userInfo = new JPanel(new GridLayout(4, 2, 5, 5));
        userInfo.setBorder(BorderFactory.createTitledBorder("Informações do Usuário"));
        userInfo.setBackground(Color.WHITE);

        userInfo.add(new JLabel("Nome:"));
        userInfo.add(new JLabel(usuarioLogado.getNome()));
        userInfo.add(new JLabel("Email:"));
        userInfo.add(new JLabel(usuarioLogado.getEmail()));
        userInfo.add(new JLabel("Tipo:"));
        userInfo.add(new JLabel(usuarioLogado.getTipoUsuario()));
        userInfo.add(new JLabel("CPF:"));
        userInfo.add(new JLabel(usuarioLogado.getCpf()));

        configContent.add(userInfo);

        // Ações disponíveis
        JPanel actions = new JPanel(new FlowLayout());
        actions.setBorder(BorderFactory.createTitledBorder("Ações"));
        actions.setBackground(Color.WHITE);

        if (ehAdministrador()) {
            JButton btnDemo = criarBotao("Permissoes", MOONSTONE, Color.WHITE, MOONSTONE,
                    e -> {
                        sistema.demonstrarPolimorfismo();
                        JOptionPane.showMessageDialog(panel, "Demonstração de permissões executada! Veja o console.");
                    });
            actions.add(btnDemo);
        }

        JButton btnLimpar = criarBotao("Limpar Console", GUNMETAL, Color.WHITE, GUNMETAL,
                e -> System.out.println("\n".repeat(50)));
        actions.add(btnLimpar);

        configContent.add(actions);

        panel.add(configContent, BorderLayout.CENTER);

        return panel;
    }

    // === MÉTODOS DE AÇÃO ===

    /**
     * Registra uma nova frequência.
     */
    private void registrarFrequencia(JTextField txtMatricula, JTextField txtDisciplina,
            JTextField txtData, JComboBox<String> cbPresente,
            JTextArea txtObservacoes) {
        try {
            String matricula = txtMatricula.getText().trim();
            String disciplina = txtDisciplina.getText().trim();
            String dataStr = txtData.getText().trim();
            boolean presente = cbPresente.getSelectedIndex() == 0;
            String observacoes = txtObservacoes.getText().trim();

            // Validações
            if (matricula.isEmpty() || disciplina.isEmpty() || dataStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos os campos obrigatórios devem ser preenchidos!",
                        "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Verificar se aluno existe
            boolean alunoExiste = sistema.listarUsuarios().stream() // CORRIGIDO: era getUsuarios()
                    .anyMatch(u -> u instanceof Aluno && ((Aluno) u).getMatricula().equals(matricula));

            if (!alunoExiste) {
                int resp = JOptionPane.showConfirmDialog(this,
                        "Aluno com matrícula '" + matricula + "' não encontrado.\nDeseja continuar mesmo assim?",
                        "Aluno Não Encontrado", JOptionPane.YES_NO_OPTION);
                if (resp != JOptionPane.YES_OPTION)
                    return;
            }

            // Converter data
            LocalDate data = LocalDate.parse(dataStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            // Gerar ID único
            long novoId = System.currentTimeMillis();

            // Criar frequência
            Frequencia frequencia = new Frequencia(novoId, matricula, disciplina, data, presente,
                    usuarioLogado.getCpf(), observacoes);

            sistema.adicionarFrequencia(frequencia);

            JOptionPane.showMessageDialog(this, "Frequência registrada com sucesso!",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            // Limpar campos
            txtMatricula.setText("");
            txtDisciplina.setText("");
            txtObservacoes.setText("");
            cbPresente.setSelectedIndex(0);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao registrar frequência:\n" + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Exporta relatório para CSV.
     */
    private void exportarRelatorio() {
        try {
            sistema.exportarParaCSV();
            JOptionPane.showMessageDialog(this, "Relatório exportado para CSV com sucesso!",
                    "Exportação Concluída", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao exportar: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Atualiza a lista de usuários (reconstrói o painel).
     */
    private void atualizarListaUsuarios() {
        if (usuariosPanel != null) {
            remove(usuariosPanel);
            usuariosPanel = buildUsuariosPanel();
            add(usuariosPanel, "USUARIOS");
            showCard("USUARIOS");
        }
    }

    /**
     * Atualiza o relatório (reconstrói o painel).
     */
    private void atualizarRelatorio() {
        if (relatorioPanel != null) {
            remove(relatorioPanel);
            relatorioPanel = buildRelatorioPanel();
            add(relatorioPanel, "RELATORIO");
            showCard("RELATORIO");
        }
    }

    /**
     * Mostra dialog para adicionar usuário (placeholder).
     */
    private void mostrarDialogAdicionarUsuario() {
        JOptionPane.showMessageDialog(this, "Funcionalidade de adicionar usuário em desenvolvimento.",
                "Em Desenvolvimento", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Mostra dialog para remover usuário (placeholder).
     */
    private void mostrarDialogRemoverUsuario() {
        String cpf = JOptionPane.showInputDialog(this, "Digite o CPF do usuário a ser removido:");
        if (cpf != null && !cpf.trim().isEmpty()) {
            try {
                sistema.removerUsuario(cpf.trim());
                JOptionPane.showMessageDialog(this, "Usuário removido com sucesso!",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                atualizarListaUsuarios();
            } catch (SistemaException e) {
                JOptionPane.showMessageDialog(this, "Erro ao remover usuário:\n" + e.getDetalhesErro(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}