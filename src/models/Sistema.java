package models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import persistencia.SerializadorJava;
import persistencia.SistemaException;

/**
 * Sistema.java - Construtores Corrigidos
 * 
 * CORREÇÃO PRINCIPAL: Ajustados os parâmetros dos construtores no método
 * criarDadosIniciais()
 * para corresponder exatamente às assinaturas definidas nas classes.
 */
public class Sistema {
    // ===== COLEÇÕES DE OBJETOS =====
    private List<Usuario> usuarios = new ArrayList<>();
    private List<Frequencia> frequencias = new ArrayList<>();

    // Arquivos para persistência binária
    private static final String ARQUIVO_USUARIOS = "usuarios.dat";
    private static final String ARQUIVO_FREQUENCIAS = "frequencias.dat";

    // Serializador para CSV
    private final SerializadorJava serializador;

    /**
     * Construtor: carrega dados persistidos (se existirem) e
     * já cria dados iniciais (caso não haja nada gravado).
     */
    public Sistema() {
        this.serializador = new SerializadorJava();
        System.out.println("🔧 Inicializando Sistema...");

        carregarUsuarios();
        carregarFrequencias();
        criarDadosIniciais();

        System.out.println("✅ Sistema inicializado com " + usuarios.size() +
                " usuários e " + frequencias.size() + " frequências");
    }

    // ====== CRUD de USUÁRIOS ======

    /**
     * Adiciona um usuário no sistema.
     * Lança SistemaException se o usuário for nulo ou se já existir outro
     * com mesmo CPF ou email.
     */
    public void adicionarUsuario(Usuario u) throws SistemaException {
        if (u == null) {
            throw new SistemaException("Usuario", "N/A", "Usuário inválido (nulo)");
        }

        // Verificar duplicidade de CPF
        boolean cpfExiste = usuarios.stream()
                .anyMatch(existing -> existing.getCpf().equals(u.getCpf()));
        if (cpfExiste) {
            throw new SistemaException("Usuario", u.getCpf(), "CPF já cadastrado");
        }

        // Verificar duplicidade de email
        boolean emailExiste = usuarios.stream()
                .anyMatch(existing -> existing.getEmail().equalsIgnoreCase(u.getEmail()));
        if (emailExiste) {
            throw SistemaException.emailJaCadastrado(u.getEmail());
        }

        usuarios.add(u);
        salvarUsuarios();
        System.out.println(" Usuário adicionado: " + u.getNome() + " (" + u.getTipoUsuario() + ")");
    }

    /**
     * Remove usuário pelo CPF. Lança SistemaException se não encontrar.
     */
    public void removerUsuario(String cpf) throws SistemaException {
        Usuario u = usuarios.stream()
                .filter(x -> x.getCpf().equals(cpf))
                .findFirst()
                .orElseThrow(() -> SistemaException.usuarioNaoEncontrado(cpf));
        usuarios.remove(u);
        salvarUsuarios();
        System.out.println(" Usuário removido: " + u.getNome());
    }

    /**
     * Busca usuário pelo CPF e retorna o objeto.
     * Lança SistemaException se não encontrar.
     */
    public Usuario buscarUsuario(String cpf) throws SistemaException {
        return usuarios.stream()
                .filter(x -> x.getCpf().equals(cpf))
                .findFirst()
                .orElseThrow(() -> SistemaException.usuarioNaoEncontrado(cpf));
    }

    /**
     * Busca usuário pelo email.
     */
    public Usuario buscarUsuarioPorEmail(String email) throws SistemaException {
        return usuarios.stream()
                .filter(x -> x.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElseThrow(() -> new SistemaException("Usuario", email, "Usuário não encontrado pelo email"));
    }

    /**
     * Retorna a lista atual de usuários (cópia defensiva).
     */
    public List<Usuario> listarUsuarios() {
        return new ArrayList<>(usuarios);
    }

    /**
     * Busca usuários por tipo específico.
     */
    public List<Usuario> buscarUsuariosPorTipo(Class<? extends Usuario> tipo) {
        return usuarios.stream()
                .filter(tipo::isInstance)
                .collect(Collectors.toList());
    }

    // ====== CRUD de FREQUÊNCIAS ======

    /**
     * Adiciona uma frequência.
     * Lança SistemaException se for nulo.
     */
    public void adicionarFrequencia(Frequencia f) throws SistemaException {
        if (f == null) {
            throw new SistemaException("Frequencia", "N/A", "Frequência inválida (nula)");
        }

        // Verificar se o aluno existe (opcional, mas recomendado)
        String matricula = f.getAlunoMatricula();
        boolean alunoExiste = usuarios.stream()
                .anyMatch(u -> u instanceof Aluno && ((Aluno) u).getMatricula().equals(matricula));

        if (!alunoExiste) {
            System.out.println(" Aviso: Frequência registrada para aluno não encontrado: " + matricula);
        }

        frequencias.add(f);
        salvarFrequencias();
        System.out.println(" Frequência adicionada: " + f.toString());
    }

    /**
     * Remove frequência pelo ID. Lança SistemaException se não encontrar.
     */
    public void removerFrequenciaPorId(long id) throws SistemaException {
        Frequencia f = frequencias.stream()
                .filter(x -> x.getId() == id)
                .findFirst()
                .orElseThrow(() -> SistemaException.frequenciaNaoEncontrada((int) id));
        frequencias.remove(f);
        salvarFrequencias();
        System.out.println(" Frequência removida: ID " + id);
    }

    /**
     * Busca lista de frequências de um determinado aluno (por matrícula).
     */
    public List<Frequencia> buscarFrequenciasPorAluno(String matricula) {
        return frequencias.stream()
                .filter(x -> x.getAlunoMatricula().equals(matricula))
                .collect(Collectors.toList());
    }

    /**
     * Busca lista de frequências de uma determinada disciplina.
     */
    public List<Frequencia> buscarFrequenciasPorDisciplina(String disciplina) {
        return frequencias.stream()
                .filter(x -> x.getDisciplina().equalsIgnoreCase(disciplina))
                .collect(Collectors.toList());
    }

    /**
     * Busca frequências por registrador (CPF).
     */
    public List<Frequencia> buscarFrequenciasPorRegistrador(String cpfRegistrador) {
        return frequencias.stream()
                .filter(x -> x.getRegistradoPorCpf().equals(cpfRegistrador))
                .collect(Collectors.toList());
    }

    /**
     * Retorna a lista atual de frequências (cópia defensiva).
     */
    public List<Frequencia> listarFrequencias() {
        return new ArrayList<>(frequencias);
    }

    // ====== PERSISTÊNCIA EM BINÁRIO (Serializable) ======

    /**
     * Salva a lista de usuários em arquivo binário.
     * MÉTODO TORNADO PÚBLICO para ser chamado do MainWindow.
     */
    public void salvarUsuarios() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(ARQUIVO_USUARIOS))) {
            oos.writeObject(usuarios);
            System.out.println(" Usuários gravados em arquivo binário (" + usuarios.size() + " registros)");
        } catch (Exception e) {
            System.err.println(" Erro ao salvar usuários: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Carrega a lista de usuários do arquivo binário.
     */
    @SuppressWarnings("unchecked")
    private void carregarUsuarios() {
        File arquivo = new File(ARQUIVO_USUARIOS);
        if (!arquivo.exists()) {
            usuarios = new ArrayList<>();
            System.out.println(" Arquivo de usuários não existe. Iniciando com lista vazia.");
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
            Object obj = ois.readObject();
            usuarios = (List<Usuario>) obj;
            System.out.println(" Usuários carregados de arquivo binário (" + usuarios.size() + " registros)");
        } catch (Exception e) {
            System.err.println(" Erro ao carregar usuários: " + e.getMessage());
            usuarios = new ArrayList<>();
        }
    }

    /**
     * Salva a lista de frequências em arquivo binário.
     * MÉTODO TORNADO PÚBLICO para ser chamado do MainWindow.
     */
    public void salvarFrequencias() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(ARQUIVO_FREQUENCIAS))) {
            oos.writeObject(frequencias);
            System.out.println(" Frequências gravadas em arquivo binário (" + frequencias.size() + " registros)");
        } catch (Exception e) {
            System.err.println(" Erro ao salvar frequências: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Carrega a lista de frequências do arquivo binário.
     */
    @SuppressWarnings("unchecked")
    private void carregarFrequencias() {
        File arquivo = new File(ARQUIVO_FREQUENCIAS);
        if (!arquivo.exists()) {
            frequencias = new ArrayList<>();
            System.out.println(" Arquivo de frequências não existe. Iniciando com lista vazia.");
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
            Object obj = ois.readObject();
            frequencias = (List<Frequencia>) obj;
            System.out.println(" Frequências carregadas de arquivo binário (" + frequencias.size() + " registros)");
        } catch (Exception e) {
            System.err.println(" Erro ao carregar frequências: " + e.getMessage());
            frequencias = new ArrayList<>();
        }
    }

    // ====== GERAÇÃO E LEITURA DE CSV (via SerializadorJava) ======

    /**
     * Gera um CSV de exemplo "dados.csv" para testar a importação via
     * lerDadosCSV().
     */
    public void criarArquivoCSVExemplo() {
        try {
            serializador.criarCSVExemplo();
            System.out.println(" Arquivo CSV de exemplo criado");
        } catch (Exception e) {
            System.err.println(" Erro ao criar CSV de exemplo: " + e.getMessage());
        }
    }

    /**
     * Lê o CSV ("dados.csv") e importa usuários e frequências para as listas.
     */
    public void lerDadosCSV() {
        try {
            int usuariosAntes = usuarios.size();
            int frequenciasAntes = frequencias.size();

            serializador.carregarUsuariosCSV(usuarios);
            serializador.carregarFrequenciasCSV(frequencias);

            int novosUsuarios = usuarios.size() - usuariosAntes;
            int novasFrequencias = frequencias.size() - frequenciasAntes;

            System.out.println("📥 Dados CSV importados: " + novosUsuarios +
                    " usuários, " + novasFrequencias + " frequências");

            // Salvar após importação
            salvarUsuarios();
            salvarFrequencias();

        } catch (Exception e) {
            System.err.println(" Erro ao ler dados CSV: " + e.getMessage());
        }
    }

    /**
     * Gera, a partir dos objetos em memória, novos arquivos CSV:
     * • usuarios.csv
     * • frequencias.csv
     * MÉTODO IMPLEMENTADO para ser chamado da interface.
     */
    public void exportarParaCSV() {
        try {
            serializador.salvarUsuariosCSV(usuarios);
            serializador.salvarFrequenciasCSV(frequencias);
            System.out.println(" Dados exportados para CSV com sucesso");
        } catch (Exception e) {
            System.err.println(" Erro ao exportar para CSV: " + e.getMessage());
            throw new RuntimeException("Erro na exportação CSV: " + e.getMessage(), e);
        }
    }

    // ====== DEMONSTRAÇÃO DE POLIMORFISMO ======

    public void demonstrarPolimorfismo() {
        System.out.println("\n=== 🎭 Demonstração Polimorfismo ===");
        for (Usuario u : usuarios) {
            System.out.println(" → Nome: " + u.getNome());
            System.out.println("   Tipo: " + u.getTipoUsuario());
            System.out.println("   Pode editar frequência? " + u.podeEditarFrequencia());
            System.out.println("   Pode gerenciar usuários? " + u.podeGerenciarUsuarios());
            System.out.println("   Permissões: " + String.join(", ", u.getPermissoes()));
            System.out.println("   Descrição Completa: " + u.getDescricaoCompleta());
            System.out.println("   Relatório Personalizado: " + u.gerarRelatorioPersonalizado());
            System.out.println("-----------------------------------");
        }
    }

    // ====== GERAÇÃO DE RELATÓRIOS ======

    /**
     * Gera um relatório geral (string) de todos os usuários.
     */
    public String gerarRelatorioGeralUsuarios() {
        StringBuilder sb = new StringBuilder("===  Relatório Geral de Usuários ===\n\n");
        sb.append(String.format("Total de usuários: %d\n", usuarios.size()));

        // Contadores por tipo
        long alunos = usuarios.stream().filter(u -> u instanceof Aluno).count();
        long professores = usuarios.stream().filter(u -> u instanceof Professor).count();
        long coordenadores = usuarios.stream().filter(u -> u instanceof Coordenador).count();
        long administradores = usuarios.stream().filter(u -> u instanceof Administrador).count();

        sb.append(String.format("  • Alunos: %d\n", alunos));
        sb.append(String.format("  • Professores: %d\n", professores));
        sb.append(String.format("  • Coordenadores: %d\n", coordenadores));
        sb.append(String.format("  • Administradores: %d\n\n", administradores));

        sb.append("DETALHES:\n");
        for (Usuario u : usuarios) {
            sb.append(String.format("ID:%d | %s | %s | Email:%s | Ativo:%s\n",
                    u.getId(), u.getTipoUsuario(), u.getNome(), u.getEmail(), u.isAtivo()));
        }
        return sb.toString();
    }

    /**
     * Gera um relatório de frequência por disciplina.
     */
    public String gerarRelatorioFrequenciasPorDisciplina(String disciplina) {
        StringBuilder sb = new StringBuilder(
                String.format("=== 📚 Relatório de Frequência: Disciplina %s ===\n\n", disciplina));

        List<Frequencia> filtradas = frequencias.stream()
                .filter(f -> f.getDisciplina().equalsIgnoreCase(disciplina))
                .collect(Collectors.toList());

        sb.append(String.format("Total de registros: %d\n\n", filtradas.size()));

        if (filtradas.isEmpty()) {
            sb.append("Nenhum registro encontrado para esta disciplina.\n");
        } else {
            // Estatísticas
            long presentes = filtradas.stream().filter(Frequencia::isPresente).count();
            long faltas = filtradas.size() - presentes;
            double percentualPresenca = filtradas.size() > 0 ? (presentes * 100.0 / filtradas.size()) : 0;

            sb.append(String.format("ESTATÍSTICAS:\n"));
            sb.append(String.format("  • Presenças: %d (%.1f%%)\n", presentes, percentualPresenca));
            sb.append(String.format("  • Faltas: %d (%.1f%%)\n\n", faltas, 100 - percentualPresenca));

            sb.append("DETALHES:\n");
            for (Frequencia f : filtradas) {
                sb.append(String.format(
                        "FreqID:%d | AlunoMat:%s | Data:%s | Status:%s | RegistradoPor:%s\n",
                        f.getId(),
                        f.getAlunoMatricula(),
                        f.getDataFormatada(),
                        f.getStatus(),
                        f.getRegistradoPorCpf()));
            }
        }
        return sb.toString();
    }

    /**
     * Gera relatório de frequências de um aluno específico.
     */
    public String gerarRelatorioFrequenciasAluno(String matricula) {
        StringBuilder sb = new StringBuilder(
                String.format("=== 🎓 Relatório de Frequências - Aluno %s ===\n\n", matricula));

        List<Frequencia> freqAluno = buscarFrequenciasPorAluno(matricula);

        if (freqAluno.isEmpty()) {
            sb.append("Nenhuma frequência registrada para este aluno.\n");
        } else {
            long presentes = freqAluno.stream().filter(Frequencia::isPresente).count();
            double percentual = (presentes * 100.0) / freqAluno.size();

            sb.append(String.format("Total de aulas: %d\n", freqAluno.size()));
            sb.append(String.format("Presenças: %d (%.1f%%)\n", presentes, percentual));
            sb.append(String.format("Faltas: %d (%.1f%%)\n\n", freqAluno.size() - presentes, 100 - percentual));

            // Agrupar por disciplina
            var porDisciplina = freqAluno.stream()
                    .collect(Collectors.groupingBy(Frequencia::getDisciplina));

            sb.append("POR DISCIPLINA:\n");
            for (var entry : porDisciplina.entrySet()) {
                String disc = entry.getKey();
                List<Frequencia> freqDisc = entry.getValue();
                long presDisc = freqDisc.stream().filter(Frequencia::isPresente).count();
                double percDisc = (presDisc * 100.0) / freqDisc.size();

                sb.append(String.format("  %s: %d aulas, %.1f%% presença\n",
                        disc, freqDisc.size(), percDisc));
            }
        }

        return sb.toString();
    }

    // ====== MÉTODO DE "DADOS INICIAIS" - CONSTRUTORES CORRIGIDOS ======

    /**
     * Cria alguns usuários e frequências para testar rapidamente.
     * Se dados já existirem (carregados do .dat), este método não altera nada.
     * 
     * CORREÇÃO PRINCIPAL: Ajustados os parâmetros dos construtores para
     * corresponder
     * exatamente às assinaturas definidas nas classes modelo.
     */
    public void criarDadosIniciais() {
        if (!usuarios.isEmpty() || !frequencias.isEmpty()) {
            System.out.println("🔧 Dados já existem. Pulando criação de dados iniciais.");
            return;
        }

        try {
            System.out.println("🔧 Criando dados iniciais...");

            // ===== CRIAR ALUNOS =====
            // Construtor Aluno: (int id, String nome, String email, String cpf, String
            // matricula, String curso, int semestre)
            adicionarUsuario(new Aluno(
                    1, // id
                    "Ana Costa", // nome
                    "ana@exemplo.com", // email
                    "12345678901", // cpf
                    "2024001", // matricula
                    "Engenharia", // curso
                    null, 3 // semestre
            ));

            adicionarUsuario(new Aluno(
                    2, // id
                    "Bruno Silva", // nome
                    "bruno@exemplo.com", // email
                    "23456789012", // cpf
                    "2024002", // matricula
                    "Medicina", // curso
                    null, 2 // semestre
            ));

            // ===== CRIAR PROFESSORES =====
            // Construtor Professor: (int id, String nome, String email, String cpf, String
            // area, String titulacao)
            adicionarUsuario(new Professor(
                    3, // id
                    "Dr. Carlos Mendes", // nome
                    "carlos@exemplo.com", // email
                    "34567890123", // cpf
                    "Matemática", // area
                    "Doutor" // titulacao
                    , null));

            adicionarUsuario(new Professor(
                    4, // id
                    "Profa. Diana Santos", // nome
                    "diana@exemplo.com", // email
                    "45678901234", // cpf
                    "Física", // area
                    "Mestre" // titulacao
                    , null));

            // ===== CRIAR COORDENADOR =====
            // Construtor Coordenador: (int id, String nome, String email, String cpf,
            // String curso)
            adicionarUsuario(new Coordenador(
                    5, // id
                    "João Silva", // nome
                    "joao@exemplo.com", // email
                    "56789012345", // cpf
                    "Ciência da Computação" // curso
                    , null));

            // ===== CRIAR ADMINISTRADOR =====
            // Construtor Administrador: (int id, String nome, String email, String cpf,
            // String nivelAcesso)
            adicionarUsuario(new Administrador(
                    6, // id
                    "Lucia Admin", // nome
                    "lucia@exemplo.com", // email
                    "67890123456", // cpf
                    "TOTAL" // nivelAcesso
                    , null));

            // ===== CRIAR FREQUÊNCIAS =====
            // Construtor Frequencia: (long id, String alunoMatricula, String disciplina,
            // LocalDate data, boolean presente, String registradoPorCpf)
            LocalDate hoje = LocalDate.now();

            adicionarFrequencia(new Frequencia(
                    1L, // id (long)
                    "2024001", // alunoMatricula
                    "Cálculo I", // disciplina
                    hoje, // data
                    true, // presente
                    "34567890123" // registradoPorCpf (Dr. Carlos)
            ));

            adicionarFrequencia(new Frequencia(
                    2L, // id (long)
                    "2024001", // alunoMatricula
                    "Física I", // disciplina
                    hoje.minusDays(1), // data
                    false, // presente
                    "45678901234" // registradoPorCpf (Profa. Diana)
            ));

            adicionarFrequencia(new Frequencia(
                    3L, // id (long)
                    "2024002", // alunoMatricula
                    "Anatomia", // disciplina
                    hoje, // data
                    true, // presente
                    "34567890123" // registradoPorCpf (Dr. Carlos)
            ));

            adicionarFrequencia(new Frequencia(
                    4L, // id (long)
                    "2024002", // alunoMatricula
                    "Anatomia", // disciplina
                    hoje.minusDays(2), // data
                    true, // presente
                    "34567890123" // registradoPorCpf (Dr. Carlos)
            ));

            System.out.println(" Dados iniciais criados com sucesso!");
            System.out.println(
                    "📊 Resumo: " + usuarios.size() + " usuários e " + frequencias.size() + " frequências criadas");

        } catch (SistemaException e) {
            System.err.println(" Erro ao criar dados iniciais: " + e.getDetalhesErro());
        }
    }

    // ====== MÉTODOS UTILITÁRIOS ======

    /**
     * Retorna estatísticas gerais do sistema.
     */
    public String getEstatisticas() {
        return String.format(
                "Sistema: %d usuários, %d frequências, %d alunos ativos",
                usuarios.size(),
                frequencias.size(),
                usuarios.stream().filter(u -> u instanceof Aluno && u.isAtivo()).count());
    }

    /**
     * Limpa todos os dados do sistema (CUIDADO!).
     */
    public void limparTodosDados() {
        usuarios.clear();
        frequencias.clear();
        salvarUsuarios();
        salvarFrequencias();
        System.out.println("🧹 Todos os dados foram limpos do sistema");
    }
}