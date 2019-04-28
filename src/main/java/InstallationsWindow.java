import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Classe qui permet de créer la fenêtre principale de l'application.
 */
class InstallationsWindow extends JFrame {

    private final JScrollPane scrollPane;
    private final JPanel pnlInstallations;
    private List<Installation> listOfInstallations;
    private final InstallationDAO dao;
    static final String PNL_INSTALLATIONS_NAME = "pnlInstallations";
    static final String TXF_SEARCH_NAME = "txfSearch";
    private static final String FRM_TITLE = "Sportify";
    static final String BTN_FAV_LIST_NAME = "btnFavList";
    private static String BTN_FAV_LIST_TITLE = "Liste des favoris ";

    InstallationsWindow() {
        dao = new InstallationDAO();
        setTitle(FRM_TITLE);
        getContentPane().setBackground(Color.WHITE);
        pnlInstallations = new JPanel();
        scrollPane = new JScrollPane(pnlInstallations);
        pnlInstallations.setName(PNL_INSTALLATIONS_NAME);

        JTextField txfSearch = new JTextField(50);
        txfSearch.setMaximumSize(txfSearch.getPreferredSize());
        txfSearch.setName(TXF_SEARCH_NAME);

        setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        pnlInstallations.setLayout(new BoxLayout(pnlInstallations, BoxLayout.PAGE_AXIS));
        pnlInstallations.setBackground(Color.WHITE);

        JPanel pblHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pblHeader.setBackground(Color.WHITE);
        JButton btnListFav = new JButton(BTN_FAV_LIST_TITLE);
        btnListFav.setName(BTN_FAV_LIST_NAME);

        btnListFav.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new InstallationFavWindow();
            }
        });

        pblHeader.add(txfSearch);
        pblHeader.add(btnListFav);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        loadInstallations();
        updateInstallations();

        txfSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterInstallations(txfSearch.getText());
            }
        });

        pblHeader.setMaximumSize(new Dimension(10000, 40));
        add(pblHeader);
        add(scrollPane);

        setVisible(true);
        pack();
    }

    /**
     * Permet de filtrer la recherche sur les installations.
     *
     * @param text mot d'entrée de la recherche.
     */
    private void filterInstallations(String text) {
        listOfInstallations = dao.findByLike(text);
        updateInstallations();
    }

    /**
     * Permet de remplir la liste d'installations avec toutes les installations de la base de données
     */
    private void loadInstallations() {
        listOfInstallations = new InstallationDAO().findAll();
    }

    /**
     * Met à jour l'affichage des panneaux d'installations selon la liste d'installations
     */
    private void updateInstallations() {
        pnlInstallations.removeAll();
        for (Installation installation : listOfInstallations)
            pnlInstallations.add(buildRow(installation));
        JPanel pnlRest = new JPanel();
        pnlRest.setBackground(Color.WHITE);
        pnlRest.setMinimumSize(new Dimension(10000, 10000));
        pnlInstallations.add(pnlRest);
        scrollPane.repaint();
        scrollPane.revalidate();
    }

    /**
     * Construit une InstallationRow selon une installation.
     *
     * @param installation une instance d'installation
     * @return installationRow créée.
     */
    private static InstallationRow buildRow(Installation installation) {
        return new InstallationRow(installation);
    }
}
