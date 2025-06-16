package persistencia;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * SistemaException.java - Versão Corrigida
 * 
 * Exceção customizada para tratar erros de negócio do Sistema.
 * Requisito 11: Exception customizada
 * 
 * CORREÇÕES:
 * - Adicionado método getDetalhesErro() que estava sendo chamado mas não existia
 * - Melhorado o formato das mensagens de erro
 * - Adicionados novos métodos factory para diferentes tipos de erro
 * - Incluído timestamp para rastreamento
 */
public class SistemaException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;

    // ===== ATRIBUTOS =====
    private String tipoEntidade;
    private String identificador;
    private String codigoErro;
    private LocalDateTime timestamp;
    private String detalhesAdicionais;

    // ===== CONSTRUTORES =====
    
    /**
     * Construtor principal da exceção.
     */
    public SistemaException(String tipoEntidade, String identificador, String codigoErro) {
        super(String.format("[%s:%s] %s", tipoEntidade, identificador, codigoErro));
        this.tipoEntidade = tipoEntidade;
        this.identificador = identificador;
        this.codigoErro = codigoErro;
        this.timestamp = LocalDateTime.now();
        this.detalhesAdicionais = "";
    }

    /**
     * Construtor com detalhes adicionais.
     */
    public SistemaException(String tipoEntidade, String identificador, String codigoErro, String detalhesAdicionais) {
        this(tipoEntidade, identificador, codigoErro);
        this.detalhesAdicionais = detalhesAdicionais != null ? detalhesAdicionais : "";
    }

    /**
     * Construtor com causa raiz.
     */
    public SistemaException(String tipoEntidade, String identificador, String codigoErro, Throwable causa) {
        super(String.format("[%s:%s] %s", tipoEntidade, identificador, codigoErro), causa);
        this.tipoEntidade = tipoEntidade;
        this.identificador = identificador;
        this.codigoErro = codigoErro;
        this.timestamp = LocalDateTime.now();
        this.detalhesAdicionais = causa != null ? causa.getMessage() : "";
    }

    // ===== GETTERS BÁSICOS =====
    
    public String getTipoEntidade() {
        return tipoEntidade;
    }

    public String getIdentificador() {
        return identificador;
    }

    public String getCodigoErro() {
        return codigoErro;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getDetalhesAdicionais() {
        return detalhesAdicionais;
    }

    // ===== MÉTODOS DE FORMATAÇÃO =====

    /**
     * Retorna a mensagem completa em formato legível.
     */
    public String getMensagemFormatada() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("🚫 ERRO DO SISTEMA\n"));
        sb.append(String.format("   Entidade: %s\n", tipoEntidade));
        sb.append(String.format("   Identificador: %s\n", identificador));
        sb.append(String.format("   Código do Erro: %s\n", codigoErro));
        sb.append(String.format("   Data/Hora: %s\n", 
                 timestamp.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))));
        
        if (!detalhesAdicionais.isEmpty()) {
            sb.append(String.format("   Detalhes: %s\n", detalhesAdicionais));
        }
        
        return sb.toString();
    }

    /**
     * MÉTODO ADICIONADO: Retorna detalhes completos do erro para logging/debug.
     * Este método estava sendo chamado no código mas não existia.
     */
    public String getDetalhesErro() {
        return getMensagemFormatada();
    }

    /**
     * Retorna uma versão resumida do erro.
     */
    public String getMensagemResumo() {
        return String.format("%s [%s]: %s", tipoEntidade, identificador, codigoErro);
    }

    /**
     * Retorna apenas a mensagem do erro sem formatação.
     */
    public String getMensagemSimples() {
        return codigoErro;
    }

    // ===== MÉTODOS ESTÁTICOS "FACTORY" PARA CRIAR EXCEÇÕES COMUNS =====

    /**
     * Cria exceção para usuário não encontrado.
     */
    public static SistemaException usuarioNaoEncontrado(String cpf) {
        return new SistemaException("Usuario", cpf, "Usuário não encontrado");
    }

    /**
     * Cria exceção para email já cadastrado.
     */
    public static SistemaException emailJaCadastrado(String email) {
        return new SistemaException("Usuario", email, "Email já cadastrado no sistema");
    }

    /**
     * Cria exceção para CPF já cadastrado.
     */
    public static SistemaException cpfJaCadastrado(String cpf) {
        return new SistemaException("Usuario", cpf, "CPF já cadastrado no sistema");
    }

    /**
     * Cria exceção para frequência não encontrada.
     */
    public static SistemaException frequenciaNaoEncontrada(int id) {
        return new SistemaException("Frequencia", String.valueOf(id), "Frequência não encontrada");
    }

    /**
     * Cria exceção para frequência não encontrada (versão long).
     */
    public static SistemaException frequenciaNaoEncontrada(long id) {
        return new SistemaException("Frequencia", String.valueOf(id), "Frequência não encontrada");
    }

    /**
     * Cria exceção para dados inválidos.
     */
    public static SistemaException dadosInvalidos(String campo) {
        return new SistemaException("Dados", campo, "Dado inválido ou formato incorreto");
    }

    /**
     * Cria exceção para dados inválidos com detalhes.
     */
    public static SistemaException dadosInvalidos(String campo, String detalhes) {
        return new SistemaException("Dados", campo, "Dado inválido", detalhes);
    }

    /**
     * Cria exceção para acesso negado.
     */
    public static SistemaException acessoNegado(String operacao, String usuario) {
        return new SistemaException("Seguranca", usuario, 
                                  "Acesso negado para operação: " + operacao);
    }

    /**
     * Cria exceção para arquivo não encontrado.
     */
    public static SistemaException arquivoNaoEncontrado(String nomeArquivo) {
        return new SistemaException("Arquivo", nomeArquivo, "Arquivo não encontrado");
    }

    /**
     * Cria exceção para erro de persistência.
     */
    public static SistemaException erroPersistencia(String operacao, Throwable causa) {
        return new SistemaException("Persistencia", operacao, 
                                  "Erro na operação de persistência", causa);
    }

    /**
     * Cria exceção para erro de validação.
     */
    public static SistemaException erroValidacao(String campo, String valorInvalido, String regra) {
        return new SistemaException("Validacao", campo, 
                                  String.format("Valor '%s' não atende à regra: %s", valorInvalido, regra));
    }

    /**
     * Cria exceção para operação não permitida.
     */
    public static SistemaException operacaoNaoPermitida(String operacao, String motivo) {
        return new SistemaException("Operacao", operacao, 
                                  "Operação não permitida: " + motivo);
    }

    /**
     * Cria exceção para conflito de dados.
     */
    public static SistemaException conflitosDados(String entidade, String identificador, String detalhes) {
        return new SistemaException("Conflito", identificador, 
                                  String.format("Conflito na entidade %s: %s", entidade, detalhes));
    }

    // ===== MÉTODOS UTILITÁRIOS =====

    /**
     * Verifica se é um erro de um tipo específico.
     */
    public boolean ehTipoErro(String tipo) {
        return tipoEntidade != null && tipoEntidade.equalsIgnoreCase(tipo);
    }

    /**
     * Verifica se é um erro crítico que deve parar o sistema.
     */
    public boolean ehErroCritico() {
        return ehTipoErro("Sistema") || ehTipoErro("Persistencia") || ehTipoErro("Seguranca");
    }

    /**
     * Verifica se é um erro de validação de dados.
     */
    public boolean ehErroValidacao() {
        return ehTipoErro("Dados") || ehTipoErro("Validacao");
    }

    /**
     * Verifica se é um erro de entidade não encontrada.
     */
    public boolean ehErroNaoEncontrado() {
        return codigoErro != null && 
               (codigoErro.contains("não encontrado") || codigoErro.contains("não encontrada"));
    }

    /**
     * Retorna um código numérico para categorização do erro.
     */
    public int getCodigoNumerico() {
        return switch (tipoEntidade.toLowerCase()) {
            case "usuario" -> 1000;
            case "frequencia" -> 2000;
            case "dados", "validacao" -> 3000;
            case "persistencia", "arquivo" -> 4000;
            case "seguranca" -> 5000;
            case "operacao" -> 6000;
            case "conflito" -> 7000;
            default -> 9000; // Erro genérico
        };
    }

    // ===== OVERRIDE DE MÉTODOS DA CLASSE PAI =====

    @Override
    public String toString() {
        return getMensagemResumo();
    }

    /**
     * Método para log detalhado (inclui stack trace se houver causa).
     */
    public String toStringDetalhado() {
        StringBuilder sb = new StringBuilder();
        sb.append(getMensagemFormatada());
        
        if (getCause() != null) {
            sb.append("\n   Causa raiz: ").append(getCause().getMessage());
        }
        
        return sb.toString();
    }

    /**
     * Método para converter para JSON simples (para APIs, se necessário).
     */
    public String toJson() {
        return String.format(
            "{\"tipo\":\"%s\",\"id\":\"%s\",\"erro\":\"%s\",\"timestamp\":\"%s\"}",
            tipoEntidade,
            identificador, 
            codigoErro.replace("\"", "\\\""),
            timestamp.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        );
    }
}