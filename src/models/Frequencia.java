package models;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Frequencia.java - Versão Corrigida
 * 
 * Representa um registro de frequência de um aluno em uma data.
 * Requisito 9: associação entre classes via campos alunoMatricula e
 * registradoPorCpf
 * Requisito 5: possui 10+ atributos/métodos
 * 
 * CORREÇÕES:
 * - ID alterado para long para consistência com removerFrequenciaPorId()
 * - Melhorada validação de dados
 * - Adicionados métodos utilitários
 * - Implementado equals() e hashCode()
 */
public class Frequencia implements Serializable {
    private static final long serialVersionUID = 1L;

    // ===== ATRIBUTOS =====
    private long id; // ALTERADO: int → long para consistência
    private String alunoMatricula; // "foreign key" para Aluno (identificado pela matrícula)
    private String disciplina; // disciplina da aula
    private LocalDate data; // data da aula
    private boolean presente; // true = presente, false = faltou
    private String registradoPorCpf; // CPF do usuário (Professor/Coordenador/Admin) que registrou
    private String observacoes; // NOVO: campo para observações adicionais

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // ===== CONSTRUTORES =====

    /**
     * Construtor vazio para serialização.
     */
    public Frequencia() {
        this.data = LocalDate.now();
        this.presente = true;
        this.observacoes = "";
    }

    /**
     * Construtor completo.
     */
    public Frequencia(long id,
            String alunoMatricula,
            String disciplina,
            LocalDate data,
            boolean presente,
            String registradoPorCpf) {
        this(id, alunoMatricula, disciplina, data, presente, registradoPorCpf, "");
    }

    /**
     * Construtor completo com observações.
     */
    public Frequencia(long id,
            String alunoMatricula,
            String disciplina,
            LocalDate data,
            boolean presente,
            String registradoPorCpf,
            String observacoes) {
        this.id = id;
        this.alunoMatricula = validarString(alunoMatricula, "Matrícula do aluno");
        this.disciplina = validarString(disciplina, "Disciplina");
        this.data = validarData(data);
        this.presente = presente;
        this.registradoPorCpf = validarString(registradoPorCpf, "CPF do registrador");
        this.observacoes = observacoes != null ? observacoes : "";
    }

    // ===== MÉTODOS DE VALIDAÇÃO =====

    private String validarString(String valor, String nomeCampo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(nomeCampo + " não pode ser nulo ou vazio");
        }
        return valor.trim();
    }

    private LocalDate validarData(LocalDate data) {
        if (data == null) {
            throw new IllegalArgumentException("Data não pode ser nula");
        }
        // Opcional: validar se a data não é muito antiga ou futura
        LocalDate hoje = LocalDate.now();
        if (data.isAfter(hoje.plusDays(7))) {
            throw new IllegalArgumentException("Data não pode ser mais de 7 dias no futuro");
        }
        if (data.isBefore(hoje.minusYears(2))) {
            throw new IllegalArgumentException("Data não pode ser mais de 2 anos no passado");
        }
        return data;
    }

    // ===== GETTERS QUE O SERIALIZADORJAVA ESPERA =====

    public long getId() { // ALTERADO: int → long
        return id;
    }

    public String getAlunoMatricula() {
        return alunoMatricula;
    }

    /**
     * Se o CSV/Serializador pedir um getAlunoId() int, converte matrícula em
     * inteiro.
     */
    public int getAlunoId() {
        try {
            // Remove caracteres não numéricos e converte
            String numerico = alunoMatricula.replaceAll("\\D+", "");
            return numerico.isEmpty() ? 0 : Integer.parseInt(numerico);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public String getDisciplina() {
        return disciplina;
    }

    public LocalDate getData() {
        return data;
    }

    /**
     * Convenção JavaBeans: método "isPresente" para o boolean 'presente'.
     */
    public boolean isPresente() {
        return presente;
    }

    public String getRegistradoPorCpf() {
        return registradoPorCpf;
    }

    public String getObservacoes() {
        return observacoes;
    }

    /**
     * Se o SerializadorJava pedir "getRegistradoPorId()" em vez de CPF,
     * converter o CPF (sem pontuação) para inteiro.
     */
    public int getRegistradoPorId() {
        try {
            String numerico = registradoPorCpf.replaceAll("\\D+", "");
            return numerico.isEmpty() ? 0 : Integer.parseInt(numerico);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    // ===== SETTERS =====

    public void setId(long id) {
        this.id = id;
    }

    public void setAlunoMatricula(String alunoMatricula) {
        this.alunoMatricula = validarString(alunoMatricula, "Matrícula do aluno");
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = validarString(disciplina, "Disciplina");
    }

    public void setData(LocalDate data) {
        this.data = validarData(data);
    }

    public void setPresente(boolean presente) {
        this.presente = presente;
    }

    public void setRegistradoPorCpf(String registradoPorCpf) {
        this.registradoPorCpf = validarString(registradoPorCpf, "CPF do registrador");
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes != null ? observacoes : "";
    }

    // ===== MÉTODOS UTILITÁRIOS =====

    /**
     * Retorna "Presente" ou "Falta".
     */
    public String getStatus() {
        return presente ? "Presente" : "Falta";
    }

    /**
     * Retorna status com emoji.
     */
    public String getStatusComEmoji() {
        return presente ? " Presente" : " Falta";
    }

    /**
     * Data formatada como "dd/MM/yyyy".
     */
    public String getDataFormatada() {
        return (data != null ? data.format(DATE_FORMATTER) : "N/A");
    }

    /**
     * Data formatada personalizada.
     */
    public String getDataFormatada(DateTimeFormatter formatter) {
        return (data != null ? data.format(formatter) : "N/A");
    }

    /**
     * Retorna o dia da semana da frequência.
     */
    public String getDiaSemana() {
        if (data == null)
            return "N/A";

        return switch (data.getDayOfWeek()) {
            case MONDAY -> "Segunda-feira";
            case TUESDAY -> "Terça-feira";
            case WEDNESDAY -> "Quarta-feira";
            case THURSDAY -> "Quinta-feira";
            case FRIDAY -> "Sexta-feira";
            case SATURDAY -> "Sábado";
            case SUNDAY -> "Domingo";
        };
    }

    /**
     * Verifica se a frequência é de hoje.
     */
    public boolean ehDeHoje() {
        return data != null && data.equals(LocalDate.now());
    }

    /**
     * Verifica se a frequência é da semana atual.
     */
    public boolean ehDaSemanaAtual() {
        if (data == null)
            return false;

        LocalDate hoje = LocalDate.now();
        LocalDate inicioSemana = hoje.minusDays(hoje.getDayOfWeek().getValue() - 1);
        LocalDate fimSemana = inicioSemana.plusDays(6);

        return !data.isBefore(inicioSemana) && !data.isAfter(fimSemana);
    }

    /**
     * Verifica se a frequência é do mês atual.
     */
    public boolean ehDoMesAtual() {
        if (data == null)
            return false;

        LocalDate hoje = LocalDate.now();
        return data.getYear() == hoje.getYear() && data.getMonth() == hoje.getMonth();
    }

    /**
     * Calcula quantos dias se passaram desde a data da frequência.
     */
    public long getDiasDesdeFrequencia() {
        if (data == null)
            return -1;
        return java.time.temporal.ChronoUnit.DAYS.between(data, LocalDate.now());
    }

    /**
     * Retorna uma descrição completa da frequência.
     */
    public String getDescricaoCompleta() {
        return String.format(
                "%s - %s em %s (%s) - Registrado por %s%s",
                getDataFormatada(),
                getStatusComEmoji(),
                disciplina,
                getDiaSemana(),
                registradoPorCpf,
                (observacoes.isEmpty() ? "" : " - Obs: " + observacoes));
    }

    // ===== MÉTODOS DE COMPARAÇÃO =====

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        Frequencia that = (Frequencia) obj;
        return id == that.id &&
                presente == that.presente &&
                Objects.equals(alunoMatricula, that.alunoMatricula) &&
                Objects.equals(disciplina, that.disciplina) &&
                Objects.equals(data, that.data) &&
                Objects.equals(registradoPorCpf, that.registradoPorCpf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, alunoMatricula, disciplina, data, presente, registradoPorCpf);
    }

    /**
     * Compara duas frequências para ordenação (por data, depois por disciplina).
     */
    public int compareTo(Frequencia outra) {
        if (outra == null)
            return 1;

        // Primeiro por data (mais recente primeiro)
        if (this.data != null && outra.data != null) {
            int compareData = outra.data.compareTo(this.data);
            if (compareData != 0)
                return compareData;
        }

        // Depois por disciplina
        if (this.disciplina != null && outra.disciplina != null) {
            int compareDisc = this.disciplina.compareToIgnoreCase(outra.disciplina);
            if (compareDisc != 0)
                return compareDisc;
        }

        // Por último por matrícula do aluno
        if (this.alunoMatricula != null && outra.alunoMatricula != null) {
            return this.alunoMatricula.compareToIgnoreCase(outra.alunoMatricula);
        }

        return 0;
    }

    // ===== MÉTODOS DE CONVERSÃO =====

    @Override
    public String toString() {
        return String.format(
                "Freq[id=%d, aluno=%s, disc=%s, data=%s, pres=%s, regPor=%s]",
                id,
                alunoMatricula,
                disciplina,
                getDataFormatada(),
                getStatus(),
                registradoPorCpf);
    }

    /**
     * Converte para string no formato CSV.
     */
    public String toCSV() {
        return String.format(
                "%d;%s;%s;%s;%b;%s;%s",
                id,
                alunoMatricula,
                disciplina,
                getDataFormatada(),
                presente,
                registradoPorCpf,
                observacoes.replace(";", ",") // Evitar quebra do CSV
        );
    }

    /**
     * Converte para formato JSON simples.
     */
    public String toJSON() {
        return String.format(
                "{\"id\":%d,\"aluno\":\"%s\",\"disciplina\":\"%s\",\"data\":\"%s\",\"presente\":%b,\"registrador\":\"%s\",\"observacoes\":\"%s\"}",
                id,
                alunoMatricula,
                disciplina,
                data != null ? data.toString() : "null",
                presente,
                registradoPorCpf,
                observacoes.replace("\"", "\\\""));
    }

    // ===== MÉTODOS ESTÁTICOS UTILITÁRIOS =====

    /**
     * Cria uma frequência de presença para hoje.
     */
    public static Frequencia criarPresencaHoje(long id, String matricula, String disciplina, String registrador) {
        return new Frequencia(id, matricula, disciplina, LocalDate.now(), true, registrador);
    }

    /**
     * Cria uma frequência de falta para hoje.
     */
    public static Frequencia criarFaltaHoje(long id, String matricula, String disciplina, String registrador) {
        return new Frequencia(id, matricula, disciplina, LocalDate.now(), false, registrador);
    }

    /**
     * Valida se uma string pode ser uma matrícula válida.
     */
    public static boolean isMatriculaValida(String matricula) {
        return matricula != null &&
                matricula.trim().length() >= 4 &&
                matricula.matches(".*\\d+.*"); // Deve conter pelo menos um número
    }

    /**
     * Valida se uma string pode ser um CPF válido (formato básico).
     */
    public static boolean isCpfValido(String cpf) {
        if (cpf == null)
            return false;
        String numerosCpf = cpf.replaceAll("\\D+", "");
        return numerosCpf.length() == 11;
    }

    /**
     * Gera um ID único baseado em timestamp (para testes).
     */
    public static long gerarIdUnico() {
        return System.currentTimeMillis();
    }
}