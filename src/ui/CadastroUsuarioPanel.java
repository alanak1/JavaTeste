package ui;

import javax.swing.*;
import java.awt.*;
import models.*;

public class CadastroUsuarioPanel extends JPanel {
    public CadastroUsuarioPanel(Sistema sistema) {
        setLayout(new GridLayout(6, 2, 10, 10));
        setBorder(BorderFactory.createTitledBorder("Cadastro de Usuário"));

        JTextField nomeField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField senhaField = new JTextField();

        add(new JLabel("Nome:"));
        add(nomeField);

        add(new JLabel("Email:"));
        add(emailField);

        add(new JLabel("Senha:"));
        add(senhaField);

        // Adicione outros campos conforme sua modelagem (tipo, matrícula, etc.)

        JButton btnSalvar = new JButton("Salvar");
        add(btnSalvar);

        btnSalvar.addActionListener(e -> {
            // Exemplo simples - insira lógica de cadastro aqui
            JOptionPane.showMessageDialog(this, "Usuário cadastrado! (simulação)");
        });
    }
}
