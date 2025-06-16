package ui;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import static ui.UIConfig.*;

public class SideMenuPanel extends JPanel {

    public SideMenuPanel(ActionListener homeAction,
            ActionListener registrarAction,
            ActionListener relatorioAction,
            ActionListener configAction,
            ActionListener sairAction) {

        setLayout(new GridLayout(6, 1, 8, 8));
        setBackground(COR_MENU);

        // Botões padronizados da UIConfig
        JButton btnHome = criarBotao("Home", MOONSTONE, COR_TEXTO, MOONSTONE, homeAction);
        JButton btnRegistrar = criarBotao("Registrar Frequência", COR_FUNDO, COR_DESTAQUE, COR_DESTAQUE,
                registrarAction);
        JButton btnRelatorio = criarBotao("Relatórios", COR_FUNDO, MOONSTONE, MOONSTONE, relatorioAction);
        JButton btnConfig = criarBotao("Configurações", COR_FUNDO, MOONSTONE, MOONSTONE, configAction);
        JButton btnSair = criarBotao("Sair", Color.WHITE, COR_DESTAQUE, COR_DESTAQUE, sairAction);

        // Adiciona os botões ao painel
        add(btnHome);
        add(btnRegistrar);
        add(btnRelatorio);
        add(btnConfig);
        add(new JLabel()); // espaço extra
        add(btnSair);
    }
}
