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
 * Classe principal de controle do sistema.
 */
public class Sistema {
    // Coleções
    private List<Usuario> usuarios = new ArrayList<>();
    private List<Frequencia> frequencias = new ArrayList<>();
    private final SerializadorJava serializador = new SerializadorJava();

    // Arquivos para persistência binária
    private static final String ARQUIVO_USUARIOS = "usuarios.dat";
    private static final String ARQUIVO_FREQUENCIAS = "frequencias.dat";

    // Construtor
    public Sistema() {
        carregarUsuarios();
        carregarFrequencias();
        criarDadosIniciais();
    }

    // CRUD de Usuários (SEM salvar automático!)
    public void adicionarUsuario(Usuario u) throws SistemaException {
        if (u == null) throw new SistemaException("Usuario", "N/A", "Usuário inválido (nulo)");
        boolean emailExiste = usuarios.stream().anyMatch(existing -> existing.getEmail().equalsIgnoreCase(u.getEmail()));
        if (emailExiste) throw SistemaException.emailJaCadastrado(u.getEmail());
        usuarios.add(u);
        // Não salva automaticamente!
    }

    public void removerUsuario(String cpf) throws SistemaException {
        Usuario u = usuarios.stream().filter(x -> x.getCpf().equals(cpf))
            .findFirst().orElseThrow(() -> SistemaException.usuarioNaoEncontrado(cpf));
        usuarios.remove(u);
        // Não salva automaticamente!
    }

    public Usuario buscarUsuario(String cpf) throws SistemaException {
        return usuarios.stream().filter(x -> x.getCpf().equals(cpf))
            .findFirst().orElseThrow(() -> SistemaException.usuarioNaoEncontrado(cpf));
    }

    public List<Usuario> listarUsuarios() {
        return new ArrayList<>(usuarios);
    }

    // CRUD de Frequências (SEM salvar automático!)
    public void adicionarFrequencia(Frequencia f) throws SistemaException {
        if (f == null) throw new SistemaException("Frequencia", "N/A", "Frequência inválida (nula)");
        frequencias.add(f);
        // Não salva automaticamente!
    }

    public void removerFrequenciaPorId(int id) throws SistemaException {
        Frequencia f = frequencias.stream().filter(x -> x.getId() == id)
            .findFirst().orElseThrow(() -> SistemaException.frequenciaNaoEncontrada(id));
        frequencias.remove(f);
        // Não salva automaticamente!
    }

    public List<Frequencia> buscarFrequenciasPorAluno(String matricula) {
        return frequencias.stream().filter(x -> x.getAlunoMatricula().equals(matricula))
            .collect(Collectors.toList());
    }

    public List<Frequencia> buscarFrequenciasPorDisciplina(String disciplina) {
        return frequencias.stream().filter(x -> x.getDisciplina().equalsIgnoreCase(disciplina))
            .collect(Collectors.toList());
    }

    public List<Frequencia> listarFrequencias() {
        return new ArrayList<>(frequencias);
    }

    // Persistência em binário (apenas quando o usuário quiser!)
    public void salvarUsuarios() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO_USUARIOS))) {
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
            System.err.println("⚠️ Nenhum arquivo de usuários foi encontrado ou houve erro: " + e.getMessage());
            usuarios = new ArrayList<>();
        }
    }

    public void salvarFrequencias() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO_FREQUENCIAS))) {
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
            System.err.println("⚠️ Nenhum arquivo de frequências foi encontrado ou houve erro: " + e.getMessage());
            frequencias = new ArrayList<>();
        }
    }

    // Exportação/Importação de CSV (apenas quando você quiser)
    public void criarArquivoCSVExemplo() { serializador.criarCSVExemplo(); }

    public void lerDadosCSV() {
        serializador.carregarUsuariosCSV(usuarios);
        serializador.carregarFrequenciasCSV(frequencias);
    }

    public void exportarParaCSV() {
        serializador.salvarUsuariosCSV(usuarios);
        serializador.salvarFrequenciasCSV(frequencias);
    }

    // Polimorfismo (exemplo)
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

    // Relatórios
    public String gerarRelatorioGeralUsuarios() {
        StringBuilder sb = new StringBuilder("=== Relatório Geral de Usuários ===\n");
        for (Usuario u : usuarios) {
            sb.append(String.format("ID:%d | %s | Email:%s | Ativo:%s\n", u.getId(), u.getTipoUsuario(), u.getEmail(), u.isAtivo()));
        }
        return sb.toString();
    }

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

    // Dados iniciais (só adiciona se lista vazia)
    public void criarDadosIniciais() {
        if (!usuarios.isEmpty()) return;
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

    // Autenticação
    public Usuario autenticarUsuario(String email, String senha) {
        for (Usuario u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(email) && u.getSenha().equals(senha)) {
                return u;
            }
        }
        return null;
    }
}
