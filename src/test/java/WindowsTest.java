import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.swing.*;

import static org.junit.Assert.*;

/**
 * Classe comportant tous les tests sur l'interface graphique de la fenetre {@link InstallationsWindow}
 */
public class WindowsTest extends AssertJSwingJUnitTestCase {
    private FrameFixture window;

    /**
     * Fonction de setup pour l'appel de la classe
     */
    @Override
    protected void onSetUp() {
        new InstallationDAO().refreshData();

        InstallationsWindow frame = GuiActionRunner.execute(InstallationsWindow::new);
        window = new FrameFixture(robot(), frame);
        window.show();
    }

    @BeforeClass
    public static void refreshDatabase() {
        new InstallationDAO().refreshData();
    }

    /**
     * Fonction permettant de retourner le nombre de panneaux affichés actuellement
     *
     * @return nombre de panneaux
     */
    private int countInstallationPanels() {
        JPanel installationsPanel = window.panel(InstallationsWindow.PNL_INSTALLATIONS_NAME).target();
        return installationsPanel.getComponents().length -1;
    }

    /**
     * Fonction permettant de chercher un texte dans la fenêtre
     *
     * @param text Chaine de caractère à rechercher
     */
    private void search(String text) {
        window.textBox(InstallationsWindow.TXF_SEARCH_NAME).enterText(text);
    }

    /**
     * Test permettant de compter le nombre total d'installation
     */
    @Test
    public void checkAllInstallations() {
        assertEquals(InstallationDAO.NUMBER_OF_ROWS, countInstallationPanels());
    }

    /**
     * Test permettant de compter le nombre d'installation afficher correctement à l'écran
     */
    @Test
    public void checkFilterInstallationNameHappyPath() {
        search("Signal");
        assertEquals(7, countInstallationPanels());
    }

    /**
     * Test permettant de tester qu'il n'y a aucun retour en cas d'une mauvaise saisie
     */
    @Test
    public void checkFilterInstallationNameSadPath() {
        search("Signal1");
        assertEquals(0, countInstallationPanels());
    }

    /**
     * Test permettant de vérifier les informations supplémentaires selon un élément cliqué
     */
    @Test
    public void testBtnInfo_HappyPath() {
        search("Patinoire saisonnière de ");
        window.button(InstallationRow.BTN_INFO_NAME).click();
        FrameFixture infoWindow = WindowFinder.findFrame(InstallationInfoWindow.FRM_NAME).using(robot());

        infoWindow.label(InstallationInfoWindow.LBL_COMMUNE_INSTALLATION_NAME).requireText("CAROUGE");
        infoWindow.label(InstallationInfoWindow.LBL_NAME_INSTALLATION_NAME).requireText("Patinoire saisonnière de la place de Sardaigne");
        infoWindow.label(InstallationInfoWindow.LBL_CATEGORY_INSTALLATION_NAME).requireText("Patinoire");
        infoWindow.label(InstallationInfoWindow.LBL_NAME_EQUIPEMENT_NAME).requireText("Patinoire");
        infoWindow.label(InstallationInfoWindow.LBL_CATEGORY_EQUIPEMENT_NAME).requireText("patinoire");
        infoWindow.label(InstallationInfoWindow.LBL_QUANTITY_EQUIPEMENT_NAME).requireText("1");
        infoWindow.label(InstallationInfoWindow.LBL_ENVIRONMENT_INSTALLATION_NAME).requireText("extérieur");
    }

    /**
     * Test permettant d'ajouter un favoris et de vérifier qu'il soit bien présent dans la liste des favoris
     */
    @Test
    public void testAddFav() {
        search("Patinoire saisonnière de ");
        window.button(InstallationRow.BTN_FAV_NAME).click();
        window.button(InstallationsWindow.BTN_FAV_LIST_NAME).click();
        FrameFixture infoWindow = WindowFinder.findFrame(InstallationFavWindow.FRM_NAME).using(robot());
        infoWindow.label(InstallationFavWindow.LBL_NAME_INSTALLATION_NAME).requireText("Patinoire saisonnière de la place de Sardaigne");
        InstallationDAO dao = new InstallationDAO();
        Installation installation = dao.findById(20);
        dao.toggleFavorite(installation);
    }

    /**
     * Test permettant d'ajouter un favoris et de le supprimer en cliquant sur le boutton de suppression et vérifier dans la BDD si la suppression a été correctement effectuée
     */
    @Test
    public void testSupFav() {
        search("Patinoire saisonnière de ");
        window.button(InstallationRow.BTN_FAV_NAME).click();
        window.button(InstallationsWindow.BTN_FAV_LIST_NAME).click();
        FrameFixture favWindow = WindowFinder.findFrame(InstallationFavWindow.FRM_NAME).using(robot());
        favWindow.label(InstallationFavWindow.LBL_NAME_INSTALLATION_NAME).requireText("Patinoire saisonnière de la place de Sardaigne");
        favWindow.button(InstallationFavWindow.BTN_FAV_SUPP).click();
        InstallationDAO dao = new InstallationDAO();
        Installation installation = dao.findById(20);
        assertFalse(installation.getFavorite());
    }

}