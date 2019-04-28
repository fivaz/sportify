import javax.swing.*;
import java.awt.*;

/**
 * Fenêtre montrant les informations concernant une installation
 */
class InstallationInfoWindow extends JFrame {
    static final String FRM_NAME = "frmInstallationInfo";
    private static final String FRM_TITLE = "Informations supplémentaires";
    static final String LBL_COMMUNE_INSTALLATION_NAME = "lblInstallationCommune";
    private static final String LBL_COMMUNE_INSTALLATION_TITLE = "Commune de l'installation : ";
    static final String LBL_NAME_INSTALLATION_NAME = "lblInstallationName";
    private static final String LBL_NAME_INSTALLATION_TITLE = "Nom de l'installation  : ";
    static final String LBL_CATEGORY_INSTALLATION_NAME = "lblInstallationCategory";
    private static final String LBL_CATEGORY_INSTALLATION_TITLE = "Catégorie de l'installation  : ";
    static final String LBL_NAME_EQUIPEMENT_NAME = "lblEquipementName";
    private static final String LBL_NAME_EQUIPEMENT_TITLE = "Nom de l'équipement  : ";
    static final String LBL_CATEGORY_EQUIPEMENT_NAME = "lblEquipementCategory";
    private static final String LBL_CATEGORY_EQUIPEMENT_TITLE = "Catégorie de l'équipement  : ";
    static final String LBL_QUANTITY_EQUIPEMENT_NAME = "lblEquipementQuantity";
    private static final String LBL_QUANTITY_EQUIPEMENT_TITLE = "Nombre d'équipement  : ";
    static final String LBL_ENVIRONMENT_INSTALLATION_NAME = "lblInstallationEnvironment";
    private static final String LBL_ENVIRONMENT_INSTALLATION_TITLE = "Environnement : ";

    /**
     * Fonction permettant de configurer les infos de la fenêtre
     *
     * @param id l'identifiant de l'installation dont les informations seront afficher sur la fenêtre
     */
    InstallationInfoWindow(int id) {
        setTitle(FRM_TITLE);
        setName(FRM_NAME);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new GridLayout(7, 2));
        //Traitement des données pour l'affichage
        buildInfo(new InstallationDAO().findById(id));
        setVisible(true);
        pack();
    }

    /**
     * Fonction permettant la construction de l'affichage de la fenêtre secondaire selon une installation
     *
     * @param installation Instance de l'installation qui sera affichée
     */
    private void buildInfo(Installation installation) {
        add(new JLabel(LBL_COMMUNE_INSTALLATION_TITLE)).setFont(new Font("Courrier", Font.BOLD, 13));
        JLabel lblInstallationCommune = new JLabel(installation.getCommune());
        lblInstallationCommune.setName(LBL_COMMUNE_INSTALLATION_NAME);
        lblInstallationCommune.setFont(new Font("Courrier", Font.PLAIN, 13));
        add(lblInstallationCommune);

        add(new JLabel(LBL_NAME_INSTALLATION_TITLE)).setFont(new Font("Courrier", Font.BOLD, 13));
        JLabel lblInstallationName = new JLabel(installation.getName());
        lblInstallationName.setName(LBL_NAME_INSTALLATION_NAME);
        lblInstallationName.setFont(new Font("Courrier", Font.PLAIN, 13));
        add(lblInstallationName);

        add(new JLabel(LBL_CATEGORY_INSTALLATION_TITLE)).setFont(new Font("Courrier", Font.BOLD, 13));
        JLabel lblInstallationCategory = new JLabel(installation.getCategory());
        lblInstallationCategory.setName(LBL_CATEGORY_INSTALLATION_NAME);
        lblInstallationCategory.setFont(new Font("Courrier", Font.PLAIN, 13));
        add(lblInstallationCategory);

        add(new JLabel(LBL_NAME_EQUIPEMENT_TITLE)).setFont(new Font("Courrier", Font.BOLD, 13));
        JLabel lblEquipementName = new JLabel(installation.getEquipement().getName());
        lblEquipementName.setName(LBL_NAME_EQUIPEMENT_NAME);
        lblEquipementName.setFont(new Font("Courrier", Font.PLAIN, 13));
        add(lblEquipementName);

        add(new JLabel(LBL_CATEGORY_EQUIPEMENT_TITLE)).setFont(new Font("Courrier", Font.BOLD, 13));
        JLabel lblEquipementCategory = new JLabel(installation.getEquipement().getCategory());
        lblEquipementCategory.setName(LBL_CATEGORY_EQUIPEMENT_NAME);
        lblEquipementCategory.setFont(new Font("Courrier", Font.PLAIN, 13));
        add(lblEquipementCategory);

        add(new JLabel(LBL_QUANTITY_EQUIPEMENT_TITLE)).setFont(new Font("Courrier", Font.BOLD, 13));
        JLabel lblEquipementQuantity = new JLabel(String.valueOf(installation.getEquipement().getQuantity()));
        lblEquipementQuantity.setName(LBL_QUANTITY_EQUIPEMENT_NAME);
        lblEquipementQuantity.setFont(new Font("Courrier", Font.PLAIN, 13));
        add(lblEquipementQuantity);

        add(new JLabel(LBL_ENVIRONMENT_INSTALLATION_TITLE)).setFont(new Font("Courrier", Font.BOLD, 13));
        JLabel lblInstallationEnvironment = new JLabel(installation.getEnvironment());
        lblInstallationEnvironment.setName(LBL_ENVIRONMENT_INSTALLATION_NAME);
        lblInstallationEnvironment.setFont(new Font("Courrier", Font.PLAIN, 13));
        add(lblInstallationEnvironment);
    }
}
