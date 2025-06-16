package models;

/**
 * Subclasse de Usuario que representa um administrador.
 * Corrigida para:
 * • Implementar TODOS os métodos abstratos herdados de Usuario
 * • Ter um construtor (int, String, String, String, String)
 */
public class Administrador extends Usuario {
    private static final long serialVersionUID = 1L;

    // ======= Atributos específicos =======
    private String nivelAcesso;

    // ======= Construtores =======
    /**
     * Construtor vazio (pode ser chamado pelo Serializador, se necessário).
     */
    public Administrador() {
        super();
    }

    /**
     * Construtor que será usado em Sistema.criarDadosIniciais(…):
     * (int id, String nome, String email, String cpf, String nivelAcesso)
     */
    public Administrador(int id, String nome, String email, String cpf, String senha, String nivelAcesso) {
        super(id, nome, email, cpf, senha);
        this.nivelAcesso = nivelAcesso;
    }

    // ======= Implementação COMPLETA de todos os métodos abstratos de Usuario
    // =======
    @Override
    public String getTipoUsuario() {
        return "Administrador";
    }

    @Override
    public String[] getPermissoes() {
        return new String[] { "CRIAR_USUARIO", "EXCLUIR_USUARIO", "VER_TODOS_RELATORIOS" };
    }

    @Override
    public boolean podeEditarFrequencia() {
        // Administrador pode editar qualquer frequência
        return true;
    }

    @Override
    public boolean podeGerenciarUsuarios() {
        // Administrador gerencia usuários do sistema
        return true;
    }

    @Override
    public String gerarRelatorioPersonalizado() {
        // Exemplo de relatório personalizado para Administrador
        return String.format("Relatório Geral de Usuários (Nível de Acesso: %s)", nivelAcesso);
    }

    // ======= Getters e setters específicos de Administrador =======
    public String getNivelAcesso() {
        return nivelAcesso;
    }

    public void setNivelAcesso(String nivelAcesso) {
        this.nivelAcesso = nivelAcesso;
    }
}
