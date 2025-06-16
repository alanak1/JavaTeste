package persistencia;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import models.*;

/**
 * Classe responsável por ler/gravar CSV e converter em objetos,
 * além de exportar listas em CSV legível.
 * Requisito 13: Leitura de CSV
 * Requisito 14: Geração de CSV a partir de objetos
 */
public class SerializadorJava {
    private static final String CSV_USUARIOS = "usuarios.csv";
    private static final String CSV_FREQUENCIAS = "frequencias.csv";
    private static final String CSV_EXEMPLO = "dados.csv";

    private static final DateTimeFormatter DATE_FORMATTER =
        DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // =====================================================================================
    // 1) Cria um CSV de exemplo “dados.csv”
    // =====================================================================================
    public void criarCSVExemplo() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(CSV_EXEMPLO))) {
            // 1ª parte: Usuários
            pw.println("Tipo;Nome;Email;CPF;Matricula;Curso;Semestre");
            pw.println("Aluno;Pedro Silva;pedro@ex.com;11111111111;20231001;Medicina;2");
            pw.println("Professor;Maria Souza;maria@ex.com;22222222222;Saude;Mestre");
            pw.println("Administrador;Ana Admin;anaadmin@ex.com;33333333333;TOTAL");
            pw.println("Coordenador;Lucas Costa;lucas@ex.com;44444444444;Engenharia");
            pw.println();

            // 2ª parte: Frequências
            pw.println("Frequencia;ID;AlunoMatricula;Disciplina;Data;Presente;RegistradoPor");
            pw.println("Frequencia;1;20231001;Anatomia;01/03/2023;true;22222222222");
            pw.println("Frequencia;2;20231001;Fisiologia;02/03/2023;false;22222222222");
            pw.println("Frequencia;3;20231002;Anatomia;01/03/2023;true;22222222222");

            System.out.println("✅ CSV de exemplo \"" + CSV_EXEMPLO + "\" criado.");
        } catch (Exception e) {
            System.err.println("❌ Erro ao criar CSV de exemplo: " + e.getMessage());
        }
    }

    // =====================================================================================
    // 2) Carregar usuários de CSV
    //    → Recebe uma List<Usuario> chamada “usuarios” e adiciona nela
    // =====================================================================================
    public void carregarUsuariosCSV(List<Usuario> usuarios) {
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_EXEMPLO))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;

                String[] campos = linha.split(";");
                String tipo = campos[0].trim();
                // Se não for um tipo reconhecido, pulamos
                if (!tipo.equalsIgnoreCase("Aluno")
                    && !tipo.equalsIgnoreCase("Professor")
                    && !tipo.equalsIgnoreCase("Administrador")
                    && !tipo.equalsIgnoreCase("Coordenador")) {
                    continue;
                }

                // Processa a linha e gera o objeto Usuario com base no tipo
                Usuario u = processarLinhaUsuario(campos, usuarios.size() + 1);
                if (u != null) {
                    usuarios.add(u);  // → usa o parâmetro “usuarios” exatamente
                    System.out.println("📥 Usuário importado: " + u.getDescricaoCompleta());
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Erro ao carregar usuários de CSV: " + e.getMessage());
        }
    }

    /**
     * Leitura do conteúdo da linha de CSV para criar instância de Usuario.
     * Recebe também “novoId” (por ex., size da lista + 1) para atribuir o ID corretamente.
     * IMPORTANTE: senha não é importada pelo CSV, é definida como padrão ("senha123").
     */
    private Usuario processarLinhaUsuario(String[] campos, int novoId) {
        try {
            String tipo = campos[0].trim();
            String nome = campos[1].trim();
            String email = campos[2].trim();
            String cpf = campos[3].trim();

            // Senha padrão para todos os usuários importados de CSV
            String senhaPadrao = "senha123";

            switch (tipo) {
                case "Aluno":
                    // campos[4]=matricula, campos[5]=curso, campos[6]=semestre
                    String matricula = campos[4].trim();
                    String curso = campos[5].trim();
                    int semestre = Integer.parseInt(campos[6].trim());
                    return new Aluno(novoId, nome, email, cpf, senhaPadrao, matricula, curso, semestre);

                case "Professor":
                    // campos[4]=area, campos[5]=titulacao
                    String area = campos[4].trim();
                    String titulacao = campos[5].trim();
                    return new Professor(novoId, nome, email, cpf, senhaPadrao, area, titulacao);

                case "Administrador":
                    // campos[4]=nivelAcesso
                    String nivel = campos[4].trim();
                    return new Administrador(novoId, nome, email, cpf, senhaPadrao, nivel);

                case "Coordenador":
                    // campos[4]=curso
                    String cursoCoord = campos[4].trim();
                    return new Coordenador(novoId, nome, email, cpf, senhaPadrao, cursoCoord);

                default:
                    return null;
            }
        } catch (Exception e) {
            System.err.println("⚠️ Linha de usuário inválida (CSV): " + e.getMessage());
            return null;
        }
    }

    // =====================================================================================
    // 3) Salvar usuários em “usuarios.csv”
    //    → Recebe uma List<Usuario> chamada “usuarios” e percorre-a
    //    IMPORTANTE: nunca exporta campo senha!
    // =====================================================================================
    public void salvarUsuariosCSV(List<Usuario> usuarios) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(CSV_USUARIOS))) {
            // Cabeçalho
            pw.println("Tipo;ID;Nome;Email;CPF;Extra01;Extra02;Extra03");

            for (Usuario u : usuarios) {
                String tipo = u.getTipoUsuario();
                int id = u.getId();
                String nome = u.getNome();
                String email = u.getEmail();
                String cpf = u.getCpf();
                String extra = "";

                switch (tipo) {
                    case "Aluno":
                        Aluno a = (Aluno) u;
                        extra = a.getMatricula() + ";" + a.getCurso() + ";" + a.getSemestre();
                        break;
                    case "Professor":
                        Professor p = (Professor) u;
                        extra = p.getArea() + ";" + p.getTitulacao();
                        break;
                    case "Administrador":
                        Administrador ad = (Administrador) u;
                        extra = ad.getNivelAcesso();
                        break;
                    case "Coordenador":
                        Coordenador c = (Coordenador) u;
                        extra = c.getCurso();
                        break;
                }

                // Não exporta campo senha!
                pw.println(
                    String.format("%s;%d;%s;%s;%s;%s",
                                  tipo, id, nome, email, cpf, extra)
                );
            }

            System.out.println("✅ Arquivo \"" + CSV_USUARIOS + "\" gerado.");
        } catch (Exception e) {
            System.err.println("❌ Erro ao salvar usuários em CSV: " + e.getMessage());
        }
    }

    // =====================================================================================
    // 4) Carregar frequências de CSV
    //    → Recebe uma List<Frequencia> chamada “frequencias” e adiciona nela
    // =====================================================================================
    public void carregarFrequenciasCSV(List<Frequencia> frequencias) {
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_EXEMPLO))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;

                String[] campos = linha.split(";");
                // Só processa se o prefixo for exatamente “Frequencia”
                if (!campos[0].equalsIgnoreCase("Frequencia")) continue;

                Frequencia f = processarLinhaFrequencia(campos);
                if (f != null) {
                    frequencias.add(f);
                    System.out.println("📥 Frequência importada: " + f.toString());
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Erro ao carregar frequências de CSV: " + e.getMessage());
        }
    }

    /**
     * Constrói um objeto Frequencia a partir de uma linha CSV
     * campos[1] = id
     * campos[2] = alunoMatricula
     * campos[3] = disciplina
     * campos[4] = data (dd/MM/yyyy)
     * campos[5] = presente (true/false)
     * campos[6] = registradoPorCpf
     */
    private Frequencia processarLinhaFrequencia(String[] campos) {
        try {
            int id = Integer.parseInt(campos[1].trim());
            String alunoMat = campos[2].trim();
            String disciplina = campos[3].trim();
            LocalDate data = LocalDate.parse(campos[4].trim(), DATE_FORMATTER);
            boolean presente = Boolean.parseBoolean(campos[5].trim());
            String regPor = campos[6].trim();

            return new Frequencia(id, alunoMat, disciplina, data, presente, regPor);
        } catch (Exception e) {
            System.err.println("⚠️ Linha de frequência inválida (CSV): " + e.getMessage());
            return null;
        }
    }

    // =====================================================================================
    // 5) Salvar frequências em “frequencias.csv”
    //    → Recebe uma List<Frequencia> chamada “frequencias” e percorre-a
    // =====================================================================================
    public void salvarFrequenciasCSV(List<Frequencia> frequencias) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(CSV_FREQUENCIAS))) {
            pw.println("ID;AlunoMatricula;Disciplina;Data;Presente;RegistradoPor");

            for (Frequencia f : frequencias) {
                pw.println(
                    String.format("%d;%s;%s;%s;%b;%s",
                        f.getId(),
                        f.getAlunoMatricula(),
                        f.getDisciplina(),
                        f.getDataFormatada(),
                        f.isPresente(),
                        f.getRegistradoPorCpf()
                    )
                );
            }

            System.out.println("✅ Arquivo \"" + CSV_FREQUENCIAS + "\" gerado.");
        } catch (Exception e) {
            System.err.println("❌ Erro ao salvar frequências em CSV: " + e.getMessage());
        }
    }
}
