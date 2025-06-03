package models;

import java.io.Serializable;

/**
 * Classe abstrata base para todos os tipos de usuário do sistema.
 * Requisito 3: classe abstrata
 * Requisito 4: métodos abstratos
 * Requisito 1: encapsulamento (atributos protegidos e getters/setters)
 */
public abstract class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;

    // ======= Atributos (encapsulados) =======
    protected int id;           
    protected String nome;
    protected String email;
    protected String cpf;
    protected boolean ativo;

    // ======= Construtores =======
    /**
     * Construtor completo. Usa-se este quando for criar um usuário com ID explícito.
     */
    public Usuario(int id, String nome, String email, String cpf) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.ativo = true;
    }

    /**
     * Construtor vazio (pode ser usado pelo Serializador via reflexão).
     */
    public Usuario() {
        // Mantém ativo = true por padrão, mas pode ser ajustado depois via setAtivo()
    }

    // ======= Métodos abstratos que as subclasses DEVEM implementar =======
    public abstract String getTipoUsuario();
    public abstract String[] getPermissoes();
    public abstract boolean podeEditarFrequencia();
    public abstract boolean podeGerenciarUsuarios();
    public abstract String gerarRelatorioPersonalizado();

    // ======= Método concretamente implementado (sobrescritura possível) =======
    /**
     * Retorna uma descrição geral do usuário, incluindo seu tipo e e-mail.
     * Requisito 7: método sobrescrito (as subclasses podem @Override se quiserem).
     */
    public String getDescricaoCompleta() {
        return String.format("%s (%s) - %s", nome, getTipoUsuario(), email);
    }

    // ======= Getters e Setters =======
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public boolean isAtivo() {
        return ativo;
    }
    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public String toString() {
        return String.format("%s (ID: %d) - %s", nome, id, getTipoUsuario());
    }
}
