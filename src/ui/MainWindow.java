package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import models.Sistema;
import models.Usuario;

import static ui.UIConfig.*;

public class MainWindow extends JFrame {
    private final Sistema sistema;
    private final Usuario usuarioLogado;
    private final ContentPanel contentPanel;
    private final SideMenuPanel sidePanel;

    public MainWindow(Usuario usuarioLogado) {
        super("Sistema de Frequência");
        this.usuarioLogado = usuarioLogado;
        this.sistema = new Sistema();

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        aplicarTamanhoPadrao(this);
        setLayout(new BorderLayout());

        // Crie o painel central e o menu lateral já passando o usuário
        contentPanel = new ContentPanel(sistema, usuarioLogado);

        // Actions dos botões do menu
        ActionListener homeAction = e -> contentPanel.showCard("HOME");
        ActionListener usuariosAction = e -> contentPanel.showCard("USUARIOS");
        ActionListener disciplinasAction = e -> contentPanel.showCard("DISCIPLINAS");
        ActionListener registrarAction = e -> contentPanel.showCard("REGISTRAR");
        ActionListener relatorioAction = e -> contentPanel.showCard("RELATORIO");
        ActionListener configAction = e -> contentPanel.showCard("CONFIG");
        ActionListener exportarAction = e -> {
            sistema.exportarParaCSV();
            JOptionPane.showMessageDialog(this, "Exportação concluída!");
        };
        ActionListener sairAction = e -> exitProcedure();

        // Menu lateral dinâmico conforme o usuário
        sidePanel = new SideMenuPanel(
            usuarioLogado,
            homeAction,
            registrarAction,
            relatorioAction,
            configAction,
            usuariosAction,
            disciplinasAction,
            exportarAction,
            sairAction
        );

        add(sidePanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        // Confirma ao sair
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitProcedure();
            }
        });

        setVisible(true);
    }

    private void exitProcedure() {
        int resp = JOptionPane.showConfirmDialog(
            this,
            "Deseja realmente sair?",
            "Confirmação",
            JOptionPane.YES_NO_OPTION
        );
        if (resp == JOptionPane.YES_OPTION) {
            sistema.salvarUsuarios();
            sistema.salvarFrequencias();
            System.exit(0);
        }
    }
}
