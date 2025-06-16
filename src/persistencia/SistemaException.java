package persistencia;

/**
 * Exceção customizada para tratar erros de negócio do Sistema.
 * Requisito 11: Exception customizada
 */
public class SistemaException extends Exception  {
    private static final long serialVersionUID = 1;

    private String tipoEntidade;
    private String identificador;
    private String codigoErro;

    // ======= Construtores =======
    public SistemaException(String tipoEntidade, String identificador, String codigoErro) {
        super(String.format("[%s:%s] %s", tipoEntidade, identificador, codigoErro));
        this.tipoEntidade = tipoEntidade;
        this.identificador = identificador;
        this.codigoErro = codigoErro;
    }

    // ======= Getters básicos =======
    public String getTipoEntidade() {
        return tipoEntidade;
    }

    public String getIdentificador() {
        return identificador;
    }

    public String getCodigoErro() {
        return codigoErro;
    }


    /**
     * Retorna a mensagem completa em formato legível.
     */
    public String getMensagemFormatada() {
        return String.format("Entidade: %s | ID/Campo: %s | Erro: %s",
                tipoEntidade, identificador, codigoErro);
    }

    /**
     * Método para compatibilidade com o código que espera getDetalhesErro()
     */
    public String getDetalhesErro() {
        return getMensagemFormatada();
    }

    // ======= Métodos estáticos “factory” para criar exceções comuns =======
    public static SistemaException usuarioNaoEncontrado(String cpf) {
        return new SistemaException("Usuario", cpf, "Usuário não encontrado");
    }

    public static SistemaException emailJaCadastrado(String email) {
        return new SistemaException("Usuario", email, "Email já cadastrado");
    }

    public static SistemaException frequenciaNaoEncontrada(int id) {
        return new SistemaException("Frequencia", String.valueOf(id), "Frequência não encontrada");
    }

    public static SistemaException dadosInvalidos(String campo) {
        return new SistemaException("Dados", campo, "Dado inválido");
    }
}
