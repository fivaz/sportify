import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Classe qui permet la création d'un panneau d'installation
 */
class InstallationRow extends JPanel {

    static final String BTN_INFO_NAME = "btnInfo";
    private static final String BTN_INFO_TITLE = "Infos";
    static final String BTN_FAV_NAME = "btnFav";
    private static final String BTN_FAV_TITLE = "Ajouter en favoris";

    InstallationRow(Installation installation) {
        setBackground(Color.WHITE);
        setMaximumSize(new Dimension(10000, 30));
        JLabel lblCommune = new JLabel(installation.getCommune());
        JLabel lblEquipement = new JLabel(installation.getName());
        JButton btnInfo = new JButton(BTN_INFO_TITLE);
        btnInfo.setName(BTN_INFO_NAME);
        btnInfo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new InstallationInfoWindow(installation.getId());
            }
        });

        JButton btnFav = new JButton(BTN_FAV_TITLE);
        btnFav.setName(BTN_FAV_NAME);
        boolean isFavorite = installation.getFavorite();
        btnFav.setEnabled(!isFavorite);
        btnFav.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                InstallationDAO dao = new InstallationDAO();
                dao.toggleFavorite(installation);
                btnFav.setEnabled(false);
                //new InstallationFavWindow(installation.getId());
            }
        });


        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        JPanel pnlInfoCommune = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlInfoCommune.setBackground(Color.WHITE);
        JPanel pnlInfoEquipement = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlInfoEquipement.setBackground(Color.WHITE);
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlButtons.setBackground(Color.WHITE);

        pnlInfoCommune.add(lblCommune);
        pnlInfoEquipement.add(lblEquipement);

        pnlButtons.add(btnInfo);
        pnlButtons.add(btnFav);

        add(pnlInfoCommune);
        add(pnlInfoEquipement);
        add(pnlButtons);
    }
}
