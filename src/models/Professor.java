package models;

import java.util.ArrayList;
import java.util.List;

/**
 * Subclasse de Usuario que representa um professor.
 * Requisito 6: herança (Professor → Usuario)
 * Requisito 4: implementa métodos abstratos
 * Requisito 5: possui 10+ atributos/métodos
 */
public class Professor extends Usuario {
    private static final long serialVersionUID = 1L;

    // ======= Atributos específicos =======
    private String area;
    private String titulacao;
    private List<String> disciplinas;

    // ======= Construtores =======
    public Professor() {
        super();
        this.disciplinas = new ArrayList<>();
    }

    /**
     * Construtor completo para Professor.
     * @param id          ID numérico do professor
     * @param nome        nome completo
     * @param email       e-mail
     * @param cpf         CPF
     * @param area        área de atuação
     * @param titulacao   titulação acadêmica
     */
    public Professor(int id, String nome, String email, String cpf, String senha, String area, String titulacao) {
    super(id, nome, email, cpf, senha);
    this.area = area;
    this.titulacao = titulacao;
    this.disciplinas = new ArrayList<>();
}
    // ======= Implementação dos métodos abstratos de Usuario =======
    @Override
    public String getTipoUsuario() {
        return "Professor";
    }

    @Override
    public String[] getPermissoes() {
        return new String[] { "VALIDAR_FREQUENCIA", "VISUALIZAR_ALUNOS" };
    }

    @Override
    public boolean podeEditarFrequencia() {
        // Professor pode lançar ou editar frequência de alunos
        return true;
    }

    @Override
    public boolean podeGerenciarUsuarios() {
        // Professor não gerencia usuários do sistema
        return false;
    }

    @Override
    public String gerarRelatorioPersonalizado() {
        // Retorna um relatório fictício de suas disciplinas
        return String.format("Relatório de Aulas do Professor %s (Área: %s)", nome, area);
    }

    // ======= Getters e Setters específicos de Professor =======
    public String getArea() {
        return area;
    }
    public void setArea(String area) {
        this.area = area;
    }

    public String getTitulacao() {
        return titulacao;
    }
    public void setTitulacao(String titulacao) {
        this.titulacao = titulacao;
    }

    public List<String> getDisciplinas() {
        return new ArrayList<>(disciplinas);
    }
    public void setDisciplinas(List<String> disciplinas) {
        this.disciplinas = (disciplinas != null ? disciplinas : new ArrayList<>());
    }

    // ======= Métodos auxiliares que o SerializadorJava pode chamar =======
    /**
     * Se o SerializadorJava pedir getTipoUsuario(), já existe acima.
     * Se ele pedir getProfessorId(), aqui poderia converter CPF em int (se quiser):
     */
    public int getProfessorId() {
        try {
            return Integer.parseInt(cpf.replaceAll("\\D+", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
