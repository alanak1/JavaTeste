package models;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Representa um registro de frequência de um aluno em uma data.
 * Requisito 9: associação entre classes via campos alunoMatricula e professorCpf
 * Requisito 5: possui 10+ atributos/métodos
 */
public class Frequencia implements Serializable {
    private static final long serialVersionUID = 1L;

    // ======= Atributos =======
    private int id;                   // identificador único da frequência
    private String alunoMatricula;    // “foreign key” para Aluno (identificado pela matrícula)
    private String disciplina;        // disciplina da aula
    private LocalDate data;           // data da aula
    private boolean presente;         // true = presente, false = faltou
    private String registradoPorCpf;  // CPF do usuário (Professor/Coordenador/Admin) que registrou

    private static final DateTimeFormatter DATE_FORMATTER =
        DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // ======= Construtor completo =======
    public Frequencia(int id,
                      String alunoMatricula,
                      String disciplina,
                      LocalDate data,
                      boolean presente,
                      String registradoPorCpf) {
        this.id = id;
        this.alunoMatricula = alunoMatricula;
        this.disciplina = disciplina;
        this.data = data;
        this.presente = presente;
        this.registradoPorCpf = registradoPorCpf;
    }

    // ======= Getters que o SerializadorJava espera =======
    public int getId() {
        return id;
    }

    public String getAlunoMatricula() {
        return alunoMatricula;
    }

    /**
     * Se o CSV/Serializador pedir um getAlunoId() int, converte matrícula em inteiro.
     */
    public int getAlunoId() {
        try {
            return Integer.parseInt(alunoMatricula);
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
     * Convenção JavaBeans: método “isPresente” para o boolean ‘presente’.
     */
    public boolean isPresente() {
        return presente;
    }

    public String getRegistradoPorCpf() {
        return registradoPorCpf;
    }

    /**
     * Se o SerializadorJava pedir “getRegistradoPorId()” em vez de CPF,
     * converter o CPF (sem pontuação) para inteiro.
     */
    public int getRegistradoPorId() {
        try {
            return Integer.parseInt(registradoPorCpf.replaceAll("\\D+", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    // ======= Métodos utilitários =======
    /**
     * Retorna “Presente” ou “Falta”.
     */
    public String getStatus() {
        return presente ? "Presente" : "Falta";
    }

    /**
     * Data formatada como “dd/MM/yyyy”.
     */
    public String getDataFormatada() {
        return (data != null ? data.format(DATE_FORMATTER) : "N/A");
    }

    @Override
    public String toString() {
        return String.format(
            "Freq[id=%d, aluno=%s, disc=%s, data=%s, pres=%s, regPor=%s]",
            id,
            alunoMatricula,
            disciplina,
            getDataFormatada(),
            getStatus(),
            registradoPorCpf
        );
    }
}
