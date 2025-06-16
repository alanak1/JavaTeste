package models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import persistencia.SerializadorJava;
import persistencia.SistemaException;

/**
 * Classe principal de controle do sistema:
 * • Gerencia listas de Usuários e de Frequências
 * • Lança e trata SistemaException
 * • Faz persistência (Serializable) em .dat
 * • Geração e leitura de CSV (via SerializadorJava)
 */
public class Sistema {
    // ===== COLEÇÕES DE OBJETOS =====
    private List<Usuario> usuarios = new ArrayList<>();
    private List<Frequencia> frequencias = new ArrayList<>();
    private SerializadorJava serializador = new SerializadorJava();

    // Arquivos para persistência binária
    private static final String ARQUIVO_USUARIOS = "usuarios.dat";
    private static final String ARQUIVO_FREQUENCIAS = "frequencias.dat";

    /**
     * Construtor: carrega dados persistidos (se existirem) e
     * já cria dados iniciais (caso não haja nada gravado).
     */
    public Sistema() {
        carregarUsuarios();
        carregarFrequencias();
        criarDadosIniciais();
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
        // Verificar duplicidade de email (regra de negócio exemplo)
        boolean emailExiste = usuarios.stream()
                .anyMatch(existing -> existing.getEmail().equalsIgnoreCase(u.getEmail()));
        if (emailExiste) {
            throw SistemaException.emailJaCadastrado(u.getEmail());
        }
        usuarios.add(u);
        salvarUsuarios();
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
     * Retorna a lista atual de usuários (cópia defensiva).
     */
    public List<Usuario> listarUsuarios() {
        return new ArrayList<>(usuarios);
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
        frequencias.add(f);
        salvarFrequencias();
    }

    /**
     * Remove frequência pelo ID. Lança SistemaException se não encontrar.
     */
    public void removerFrequenciaPorId(int id) throws SistemaException {
        Frequencia f = frequencias.stream()
                .filter(x -> x.getId() == id)
                .findFirst()
                .orElseThrow(() -> SistemaException.frequenciaNaoEncontrada(id));
        frequencias.remove(f);
        salvarFrequencias();
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
     * Retorna a lista atual de frequências (cópia defensiva).
     */
    public List<Frequencia> listarFrequencias() {
        return new ArrayList<>(frequencias);
    }

    // ====== PERSISTÊNCIA EM BINÁRIO (Serializable) ======

    public void salvarUsuarios() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(ARQUIVO_USUARIOS))) {
            oos.writeObject(usuarios);
            System.out.println("✅ Usuários gravados em arquivo binário.");
        } catch (Exception e) {
            System.err.println("❌ Erro ao salvar usuários: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void carregarUsuarios() {
        File arquivo = new File(ARQUIVO_USUARIOS);
        if (!arquivo.exists()) {
            usuarios = new ArrayList<>();
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
            Object obj = ois.readObject();
            usuarios = (List<Usuario>) obj;
            System.out.println("✅ Usuários carregados de arquivo binário.");
        } catch (Exception e) {
            System.err.println("⚠️ Nenhum arquivo de usuários foi encontrado ou houve erro: "
                    + e.getMessage());
            usuarios = new ArrayList<>();
        }
    }

    public void salvarFrequencias() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(ARQUIVO_FREQUENCIAS))) {
            oos.writeObject(frequencias);
            System.out.println("✅ Frequências gravadas em arquivo binário.");
        } catch (Exception e) {
            System.err.println("❌ Erro ao salvar frequências: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void carregarFrequencias() {
        File arquivo = new File(ARQUIVO_FREQUENCIAS);
        if (!arquivo.exists()) {
            frequencias = new ArrayList<>();
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
            Object obj = ois.readObject();
            frequencias = (List<Frequencia>) obj;
            System.out.println("✅ Frequências carregadas de arquivo binário.");
        } catch (Exception e) {
            System.err.println("⚠️ Nenhum arquivo de frequências foi encontrado ou houve erro: "
                    + e.getMessage());
            frequencias = new ArrayList<>();
        }
    }

    // ====== GERAÇÃO E LEITURA DE CSV (via SerializadorJava) ======

    /**
     * Gera um CSV de exemplo “dados.csv” para testar a importação via lerDadosCSV().
     */
    public void criarArquivoCSVExemplo() {
        serializador.criarCSVExemplo();
    }

    /**
     * Lê o CSV (“dados.csv”) e importa usuários e frequências para as listas.
     */
    public void lerDadosCSV() {
        serializador.carregarUsuariosCSV(usuarios);
        serializador.carregarFrequenciasCSV(frequencias);
    }

    /**
     * Gera, a partir dos objetos em memória, novos arquivos CSV:
     * • usuarios.csv
     * • frequencias.csv
     */
    public void exportarParaCSV() {
        serializador.salvarUsuariosCSV(usuarios);
        serializador.salvarFrequenciasCSV(frequencias);
    }

    // ====== DEMONSTRAÇÃO DE POLIMORFISMO (opcional) ======

    public void demonstrarPolimorfismo() {
        System.out.println("=== Demonstração Polimorfismo ===");
        for (Usuario u : usuarios) {
            System.out.println(" → Nome: " + u.getNome());
            System.out.println("   Tipo: " + u.getTipoUsuario());
            System.out.println("   Pode editar frequência? " + u.podeEditarFrequencia());
            System.out.println("   Permissões: " + String.join(", ", u.getPermissoes()));
            System.out.println("   Descrição Completa: " + u.getDescricaoCompleta());
            System.out.println("-----------------------------------");
        }
    }

    // ====== GERAÇÃO DE RELATÓRIOS ======

    /**
     * Gera um relatório geral (string) de todos os usuários.
     */
    public String gerarRelatorioGeralUsuarios() {
        StringBuilder sb = new StringBuilder("=== Relatório Geral de Usuários ===\n");
        for (Usuario u : usuarios) {
            sb.append(String.format("ID:%d | %s | Email:%s | Ativo:%s\n",
                    u.getId(), u.getTipoUsuario(), u.getEmail(), u.isAtivo()));
        }
        return sb.toString();
    }

    /**
     * Gera um relatório de frequência por disciplina.
     */
    public String gerarRelatorioFrequenciasPorDisciplina(String disciplina) {
        StringBuilder sb = new StringBuilder(
                String.format("=== Relatório de Frequência: Disciplina %s ===\n", disciplina));
        List<Frequencia> filtradas = frequencias.stream()
                .filter(f -> f.getDisciplina().equalsIgnoreCase(disciplina))
                .collect(Collectors.toList());
        for (Frequencia f : filtradas) {
            sb.append(String.format(
                    "FreqID:%d | AlunoMat:%s | Data:%s | Status:%s | RegistradoPor:%s\n",
                    f.getId(),
                    f.getAlunoMatricula(),
                    f.getDataFormatada(),
                    f.getStatus(),
                    f.getRegistradoPorCpf()));
        }
        return sb.toString();
    }

    // ====== MÉTODO DE “DADOS INICIAIS” (exemplo) ======

    /**
     * Cria alguns usuários e frequências para testar rapidamente.
     * Se dados já existirem (carregados do .dat), este método não altera nada.
     */
public void criarDadosIniciais() {
    if (!usuarios.isEmpty()) {
        // Se já houver usuário cadastrado, não faz nada
        return;
    }
    try {
        System.out.println("🔧 Criando Administrador inicial...");
        adicionarUsuario(new Administrador(
                1,
                "Administrador",
                "admin@exemplo.com",
                "123456789",
                "123456789",    // senha
                "TOTAL"          // nível de acesso
        ));
        System.out.println("✅ Administrador inicial criado!");
    } catch (SistemaException e) {
        System.err.println("❌ Erro ao criar Administrador inicial: " + e.getDetalhesErro());
    }
}

    // ====== AUTENTICAÇÃO ======

    public Usuario autenticarUsuario(String email, String senha) {
        for (Usuario u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(email) && u.getSenha().equals(senha)) {
                return u;
            }
        }
        return null;
    }
}
