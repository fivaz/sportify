import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Fenêtre montrant les favoris
 */
class InstallationFavWindow extends JFrame {
    static final String FRM_NAME = "frmInstallationFav";
    static final String LBL_NAME_INSTALLATION_NAME = "lblInstallationName";
    static final String LBL_COMMUNE_INSTALLATION_NAME = "lblInstallationCommune";
    static final String BTN_FAV_SUPP = "btnSupFav";
    static final String FRM_FAV_TITLE = "Fenêtre des favoris";
    static final String FONT = "Courrier";
    List<Installation> favIfAdded;

    /**
     * Fonction permettant de configurer les infos de la fenêtre des favoris
     */
    InstallationFavWindow() {
        setTitle(FRM_FAV_TITLE);
        setName(FRM_NAME);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new GridLayout(0, 1));

        InstallationDAO dao = new InstallationDAO();
        favIfAdded = dao.getFavIfAdded();

        buildFavWindow();
        setVisible(true);
        pack();
    }

    /**
     * Fonction permettant la construction de l'affichage de la fenêtre des favoris selon une installation
     */
    private void buildFavWindow() {
        for (Installation installation : favIfAdded) {
            JPanel pnlLigne = new JPanel();

            pnlLigne.setBackground(Color.WHITE);
            JLabel lblCommune = new JLabel(installation.getCommune());
            lblCommune.setFont(new Font(FONT, Font.BOLD, 13));
            JLabel lblEquipement = new JLabel(installation.getEquipement().getName());

            JLabel lblInstallationCommune = new JLabel(installation.getCommune());
            lblInstallationCommune.setName(LBL_COMMUNE_INSTALLATION_NAME);
            JLabel lblInstallationName = new JLabel(installation.getName());
            lblInstallationName.setName(LBL_NAME_INSTALLATION_NAME);

            JButton btnSupFav = new JButton("Supprimer le favoris ");
            btnSupFav.setName(BTN_FAV_SUPP);
            btnSupFav.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    InstallationDAO dao = new InstallationDAO();
                    dao.toggleFavorite(installation);
                    btnSupFav.setEnabled(false);
                    remove(pnlLigne);
                    repaint();
                    revalidate();
                }
            });
            pnlLigne.add(lblInstallationCommune);
            pnlLigne.add(lblInstallationName);
            pnlLigne.add(btnSupFav);
            add(pnlLigne);
        }
    }
}
