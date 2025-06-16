package ui;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import static ui.UIConfig.*;

public class SideMenuPanel extends JPanel {

    private final JButton btnHome;
    private final JButton btnUsuarios;
    private final JButton btnDisciplinas;
    private final JButton btnRegistrar;
    private final JButton btnRelatorio;
    private final JButton btnConfig;
    private final JButton btnSair;

    public SideMenuPanel(ActionListener homeAction,
            ActionListener usuariosAction,
            ActionListener disciplinasAction,
            ActionListener registrarAction,
            ActionListener relatorioAction,
            ActionListener configAction,
            ActionListener sairAction) {

        setLayout(new GridLayout(7, 1, 8, 8));
        setBackground(COR_MENU);

        btnHome = criarBotao("Home", MOONSTONE, COR_TEXTO, MOONSTONE, homeAction);
        btnUsuarios = criarBotao("Usuários", COR_FUNDO, MOONSTONE, MOONSTONE, usuariosAction);
        btnDisciplinas = criarBotao("Disciplinas", COR_FUNDO, MOONSTONE, MOONSTONE, disciplinasAction);
        btnRegistrar = criarBotao("Registrar Frequência", COR_FUNDO, COR_DESTAQUE, COR_DESTAQUE, registrarAction);
        btnRelatorio = criarBotao("Relatórios", COR_FUNDO, MOONSTONE, MOONSTONE, relatorioAction);
        btnConfig = criarBotao("Configurações", COR_FUNDO, MOONSTONE, MOONSTONE, configAction);
        btnSair = criarBotao("Sair", Color.WHITE, COR_DESTAQUE, COR_DESTAQUE, sairAction);

        add(btnHome);
        add(btnUsuarios);
        add(btnDisciplinas);
        add(btnRegistrar);
        add(btnRelatorio);
        add(btnConfig);
        add(btnSair);
    }

    // Getters para acesso nos listeners (se precisar)
    public JButton getBtnHome() {
        return btnHome;
    }

    public JButton getBtnUsuarios() {
        return btnUsuarios;
    }

    public JButton getBtnDisciplinas() {
        return btnDisciplinas;
    }

    public JButton getBtnRegistrar() {
        return btnRegistrar;
    }

    public JButton getBtnRelatorio() {
        return btnRelatorio;
    }

    public JButton getBtnConfig() {
        return btnConfig;
    }

    public JButton getBtnSair() {
        return btnSair;
    }
}
