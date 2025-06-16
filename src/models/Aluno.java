package models;

import java.util.ArrayList;
import java.util.List;

/**
 * Subclasse de Usuario que representa um aluno.
 * Requisito 6: herança (Aluno → Usuario)
 * Requisito 4: implementa métodos abstratos
 * Requisito 5: possui 10+ atributos/métodos
 */
public class Aluno extends Usuario {
    private static final long serialVersionUID = 1L;

    // ======= Atributos específicos =======
    private String matricula;
    private String curso;
    private int semestre;
    private List<Frequencia> frequencias;

    // ======= Construtores =======
    /**
     * Construtor vazio (pode ser usado em desserialização ou reflexão).
     */
    public Aluno() {
        super();
        this.frequencias = new ArrayList<>();
    }

    /**
     * Construtor completo para Aluno.
     * @param id          ID numérico do aluno
     * @param nome        nome completo
     * @param email       e-mail
     * @param cpf         CPF
     * @param matricula   matrícula (string)
     * @param curso       curso de graduação
     * @param semestre    semestre atual
     */
public Aluno(int id, String nome, String email, String cpf, String senha, String matricula, String curso, int ano) {
        super(id, nome, email, cpf, senha);
        this.matricula = matricula;
        this.curso = curso;
        this.semestre = semestre;
        this.frequencias = new ArrayList<>();
    }

    // ======= Implementação dos métodos abstratos de Usuario =======
    @Override
    public String getTipoUsuario() {
        return "Aluno";
    }

    @Override
    public String[] getPermissoes() {
        return new String[] { "VISUALIZAR_FREQUENCIA", "GERAR_RELATORIO" };
    }

    @Override
    public boolean podeEditarFrequencia() {
        // Aluno não edita frequência de outros alunos
        return false;
    }

    @Override
    public boolean podeGerenciarUsuarios() {
        // Aluno não tem permissão para gerenciar usuários
        return false;
    }

    @Override
    public String gerarRelatorioPersonalizado() {
        // Retorna um resumo de presença do aluno
        return String.format("Relatório de Presença do Aluno %s (Mat: %s)", nome, matricula);
    }

    // ======= Getters e Setters específicos de Aluno =======
    public String getMatricula() {
        return matricula;
    }
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getCurso() {
        return curso;
    }
    public void setCurso(String curso) {
        this.curso = curso;
    }

    public int getSemestre() {
        return semestre;
    }
    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }

    public List<Frequencia> getFrequencias() {
        return new ArrayList<>(frequencias);
    }
    public void setFrequencias(List<Frequencia> frequencias) {
        this.frequencias = (frequencias != null ? frequencias : new ArrayList<>());
    }

    // ======= Métodos auxiliares que o SerializadorJava pode chamar =======
    /**
     * Se o SerializadorJava espera um método getAlunoId() retornando int,
     * podemos converter a matrícula (caso seja numérica) para int aqui.
     */
    public int getAlunoId() {
        try {
            return Integer.parseInt(matricula);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
