package ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
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

        // Painel central adaptado ao perfil
        contentPanel = new ContentPanel(sistema, usuarioLogado);

        // Actions (cada ação mostra um card existente, ou exporta/sai)
        ActionListener homeAction       = e -> contentPanel.showCard("HOME");
        ActionListener usuariosAction   = e -> contentPanel.showCard("USUARIOS");
        ActionListener disciplinasAction= e -> contentPanel.showCard("DISCIPLINAS");
        ActionListener registrarAction  = e -> contentPanel.showCard("REGISTRAR");
        ActionListener relatorioAction  = e -> contentPanel.showCard("RELATORIO");
        ActionListener configAction     = e -> contentPanel.showCard("CONFIG");
        ActionListener exportarAction   = e -> {
            sistema.exportarParaCSV();
            JOptionPane.showMessageDialog(this, "Exportação concluída!");
        };
        ActionListener sairAction       = e -> exitProcedure();

        // Painel lateral
        sidePanel = new SideMenuPanel(homeAction, usuariosAction, disciplinasAction, registrarAction, relatorioAction, configAction, sairAction);

        add(sidePanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

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
