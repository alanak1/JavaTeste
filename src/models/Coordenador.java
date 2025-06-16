package models;

import java.util.ArrayList;
import java.util.List;

/**
 * Subclasse de Usuario que representa um coordenador.
 * Requisito 6: herança (Coordenador → Usuario)
 * Requisito 4: implementa métodos abstratos
 * Requisito 5: possui 10+ atributos/métodos
 */
public class Coordenador extends Usuario {
    private static final long serialVersionUID = 1L;

    // ======= Atributos específicos =======
    private String curso;
    private List<String> disciplinasGerenciadas;

    // ======= Construtores =======
    public Coordenador() {
        super();
        this.disciplinasGerenciadas = new ArrayList<>();
    }

    /**
     * Construtor completo para Coordenador.
     * 
     * @param id    ID numérico do coordenador
     * @param nome  nome completo
     * @param email e-mail
     * @param cpf   CPF
     * @param curso nome do curso que coordena
     */
    public Coordenador(int id, String nome, String email, String cpf, String senha, String areaCoordenacao) {
        super(id, nome, email, cpf, senha);
        this.curso = curso;
        this.disciplinasGerenciadas = new ArrayList<>();
    }

    // ======= Implementação dos métodos abstratos de Usuario =======
    @Override
    public String getTipoUsuario() {
        return "Coordenador";
    }

    @Override
    public String[] getPermissoes() {
        return new String[] { "GERENCIAR_DISCIPLINAS", "GERAR_RELATORIO_DISCIPLINA" };
    }

    @Override
    public boolean podeEditarFrequencia() {
        // Coordenador pode editar frequência de alunos do seu curso
        return true;
    }

    @Override
    public boolean podeGerenciarUsuarios() {
        // Coordenador pode gerenciar usuários (alunos/professores) do seu curso
        return true;
    }

    @Override
    public String gerarRelatorioPersonalizado() {
        // Retorna um relatório fictício por curso
        return String.format("Relatório de Curso %s - Coordenador %s", curso, nome);
    }

    // ======= Getters e Setters específicos de Coordenador =======
    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public List<String> getDisciplinasGerenciadas() {
        return new ArrayList<>(disciplinasGerenciadas);
    }

    public void setDisciplinasGerenciadas(List<String> disciplinasGerenciadas) {
        this.disciplinasGerenciadas = (disciplinasGerenciadas != null
                ? disciplinasGerenciadas
                : new ArrayList<>());
    }

    // ======= Métodos auxiliares =======
    public boolean podeGerenciarDisciplina(String disciplina) {
        return disciplinasGerenciadas.contains(disciplina);
    }

    public int getCoordenadorId() {
        try {
            return Integer.parseInt(cpf.replaceAll("\\D+", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
