import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Classe qui test les méthodes de la classe {@link InstallationDAO}
 */
public class InstallationDAOTest {

    private static InstallationDAO dao;

    @BeforeClass
    public static void setUp() {
        //Connection à la base de données local
        dao = new InstallationDAO();
    }

    /**
     * Test si nous arrivons à insérer un object installation correctement
     */
    @Test
    public void testInsertInstallation() {

        final int ID_SPORTIFY = 10050;
        final String EQUIPEMENT_NAME = "15000";
        final String CATEGORIE_EQUIPEMENT_SPORTIFY = "equipement 1";
        final String CATEGORIE_INSTALLATION_SPORTIFY = "categorie 1";
        final String ENVIRONNEMENT_SPORTIFY = "Environnement 1";
        final String NOM_INSTALLATION_SPORTIFY = "Instalation 1";
        final String COMMUNE_SPORTIFY = "100";
        final int NOMBRE_EQUIPEMENT_SPORTIFY = 100;
        final boolean IS_FAVORITE_SPORTIFY = false;

        final Equipement EQUIPEMENT = new Equipement(EQUIPEMENT_NAME, CATEGORIE_EQUIPEMENT_SPORTIFY, NOMBRE_EQUIPEMENT_SPORTIFY);

        Installation expectedInstallation = new Installation(ID_SPORTIFY, COMMUNE_SPORTIFY, NOM_INSTALLATION_SPORTIFY, CATEGORIE_INSTALLATION_SPORTIFY, EQUIPEMENT, ENVIRONNEMENT_SPORTIFY, IS_FAVORITE_SPORTIFY);

        dao.clearTable();
        dao.insert(expectedInstallation);
        Installation gotInstallation = dao.getLast();

        assertEquals(expectedInstallation.getId(), gotInstallation.getId());
        assertEquals(expectedInstallation.getName(), gotInstallation.getName());
        assertEquals(expectedInstallation.getCommune(), gotInstallation.getCommune());
        assertEquals(expectedInstallation.getCategory(), gotInstallation.getCategory());
        assertEquals(expectedInstallation.getEnvironment(), gotInstallation.getEnvironment());
    }

    /**
     * Test si on arrive a supprimer tous les objects de notre table
     */
    @Test
    public void testConstructor() throws SQLException {
        Connection connection = H2.getConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate("DROP TABLE IF EXISTS table_sportify");
        dao = new InstallationDAO();
        // Test des méthodes internes privées suivantes :
        // - Teste checkDatabaseExists() si la base de données existe
        // - Teste createTable() la création des tables
        // - Teste insertAll() l'insertion des données
        assertEquals(InstallationDAO.NUMBER_OF_ROWS, dao.findAll().size());
    }

    /**
     * Test si nous insérons toutes nos installations dans la table
     */
    @Test
    public void testInsertDataCSV() {
        dao.refreshData();
        //il faudrait avoir 1382 lignes
        assertEquals(1382, dao.findAll().size());
    }

    /**
     * Test si on trouve le bon élément pour l'id n°1
     */
    @Test
    public void testFindById() {
        String EQUIPEMENT_NAME = "Installations d'athlétisme";
        String EQUIPEMENT_CATEGORY = "Terrain";
        int EQUIPEMENT_QUANTITY = 1;
        int ID_SPORTIFY = 1;
        String INSTALLATION_COMMUNE = "BERNEX";
        String INSTALLATION_NAME = "Zone sportive du Signal";
        String INSTALLATION_CATEGORY = "zone de sport";
        String INSTALLATION_ENVIRONNEMENT = "extérieur";
        boolean IS_FAVORITE = false;

        Equipement equipement = new Equipement(EQUIPEMENT_NAME, EQUIPEMENT_CATEGORY, EQUIPEMENT_QUANTITY);
        Installation expectedInstallation = new Installation(ID_SPORTIFY, INSTALLATION_COMMUNE, INSTALLATION_NAME, INSTALLATION_CATEGORY, equipement, INSTALLATION_ENVIRONNEMENT, IS_FAVORITE);

        dao.refreshData();
        Installation actualInstallation = dao.findById(1);

        assertEquals(expectedInstallation.getId(), actualInstallation.getId());
        assertEquals(expectedInstallation.getName(), actualInstallation.getName());
        assertEquals(expectedInstallation.getCategory(), actualInstallation.getCategory());
        assertEquals(expectedInstallation.getCommune(), actualInstallation.getCommune());
    }

    /**
     * Test le nombre de données selectionnées pour le texte en majuscule voulu.
     */
    @Test
    public void testMajNumberSearchedData() {
        dao.refreshData();
        assertEquals(38, dao.findByLike("BERNEX").size());
    }

    /**
     * Test le nombre de données selectionnées pour le texte en minuscule voulu.
     */
    @Test
    public void testMinNumberSearchedData() {
        dao.refreshData();
        assertEquals(4, dao.findByLike("Bernex").size());
    }

    /**
     * Test le nombre de données selectionnées pour le texte sans la fin voulu.
     */
    @Test
    public void testNumberSearchedData1() {
        dao.refreshData();
        assertEquals(38, dao.findByLike("BERNE").size());
    }

    /**
     * Test le nombre de données selectionnées pour le texte sans le début voulu.
     */
    @Test
    public void testNumberSearchedData2() {
        dao.refreshData();
        assertEquals(38, dao.findByLike("ERNEX").size());
    }

    /**
     * Test le nombre de données selectionnées pour le texte faux voulu.
     */
    @Test
    public void testNumberSearchDataWrongName() {
        dao.refreshData();
        assertEquals(0, dao.findByLike("BERNEXX").size());
    }

    /**
     * Test le nombre de données selectionnées pour le texte qui se retrouve dans plusieurs résultats différent.
     */
    @Test
    public void testNumberSearchDataAvu() {
        dao.refreshData();
        assertEquals(23, dao.findByLike("AVU").size());
    }

    /**
     * Test la liste d'installation trouvé avec la méthode "findAll()"
     * La liste retourné par la méthode est la liste de tout nos objets dans la basse de donnée
     * Elle est comparé avec la liste du fichier "bdd.csv"
     *
     * @throws IOException
     */
    @Test
    public void testFindAll() throws IOException {
        List<Installation> listeInstallationsExcepted = CSVtoListOfInstallations.convertCSVtoListOfInstallations("bdd.csv", ";");
        dao.refreshData();
        List<Installation> listeInstallationsActual = dao.findAll();
        for (int i = 0; i < listeInstallationsExcepted.size(); i++) {
            assertEquals(listeInstallationsExcepted.get(i).getId(), listeInstallationsActual.get(i).getId());
            assertEquals(listeInstallationsExcepted.get(i).getName(), listeInstallationsActual.get(i).getName());
            assertEquals(listeInstallationsExcepted.get(i).getCategory(), listeInstallationsActual.get(i).getCategory());
            assertEquals(listeInstallationsExcepted.get(i).getCommune(), listeInstallationsActual.get(i).getCommune());
        }
    }

    /**
     * Test l'integer trouvé avec la méthode "countAll()"
     */
    @Test
    public void testCountAll() {
        dao.refreshData();
        int nbActual = dao.findAll().size();
        assertEquals(1382, nbActual);
    }

    /**
     * Test la méthode après vider la table
     */
    @Test
    public void testCountAllAfterClear() {
        dao.clearTable();
        int nbActual = dao.findAll().size();
        assertEquals(0, nbActual);
    }

    /**
     * Test la méthode FindByLike avec "TC Bernex" passer en paramètre.
     */
    @Test
    public void testFindBylikeTCBernex() {
        dao.refreshData();

        List<Installation> listeInstallationExcepted = new ArrayList<>();
        listeInstallationExcepted.add(dao.findById(2));
        listeInstallationExcepted.add(dao.findById(3));
        listeInstallationExcepted.add(dao.findById(4));

        List<Installation> listInstallationActual = dao.findByLike("TC Bernex");
        for (int i = 0; i < listeInstallationExcepted.size(); i++) {
            assertEquals(listeInstallationExcepted.get(i).getId(), listInstallationActual.get(i).getId());
            assertEquals(listeInstallationExcepted.get(i).getName(), listInstallationActual.get(i).getName());
            assertEquals(listeInstallationExcepted.get(i).getCategory(), listInstallationActual.get(i).getCategory());
            assertEquals(listeInstallationExcepted.get(i).getCommune(), listInstallationActual.get(i).getCommune());
            assertEquals(listeInstallationExcepted.get(i).getEquipement().getCategory(), listInstallationActual.get(i).getEquipement().getCategory());
        }
    }

    /**
     * fonction qui teste la méthode d'ajouter ou supprimer une installation favorite
     */
    @Test
    public void testToggleFavorite() {
        Installation installationExpected = dao.findById(1);
        dao.toggleFavorite(installationExpected);
        Installation installationActual = dao.findById(1);
        assertNotEquals(installationExpected.getFavorite(), installationActual.getFavorite());
    }

    /**
     * fonction qui teste la méthode récupere une liste d'installations favorites
     */
    @Test
    public void testGetFavIfAdded() {
        int favoritesNumberAvant = dao.getFavIfAdded().size();
        int favoritesNumberExpected;

        Installation installation = dao.findById(58);

        if (installation.getFavorite())
            favoritesNumberExpected = favoritesNumberAvant - 1;
        else
            favoritesNumberExpected = favoritesNumberAvant + 1;

        dao.toggleFavorite(installation);
        int favoritesNumberApres = dao.getFavIfAdded().size();

        assertEquals(favoritesNumberExpected, favoritesNumberApres);
    }
}