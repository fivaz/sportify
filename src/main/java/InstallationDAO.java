import java.io.IOException;
import java.sql.*;
import java.util.*;

/**
 * Cette classe contient les requêtes qui nous permettent de communiquer avec la base de données.
 * Elle permet l'insertion de tout nos objects "installation" qui ont comme sous-classe "Equipement".
 */
class InstallationDAO {

    private Connection connection;
    private final String TABLE = "table_sportify";

    private final String CREATE_TABLE_STATEMENT = "CREATE TABLE " + TABLE +
            " (" +
            "ID_SPORTIFY INTEGER NOT NULL," +
            "COMMUNE_SPORTIFY VARCHAR(128) NULL," +
            "NOM_INSTALLATION_SPORTIFY VARCHAR(128) NULL," +
            "CATEGORIE_INSTALLATION_SPORTIFY VARCHAR(128) NULL," +
            "NOM_EQUIPEMENT_SPORTIFY VARCHAR(128) NULL," +
            "CATEGORIE_EQUIPEMENT_SPORTIFY VARCHAR(128) NULL," +
            "NOMBRE_EQUIPEMENT_SPORTIFY INT NULL," +
            "ENVIRONNEMENT_SPORTIFY VARCHAR(128) NULL," +
            "IS_FAVORI_SPORTIFY INT DEFAULT 0," +
            "PRIMARY KEY (ID_SPORTIFY)" +
            ")";

    private final String FIND_ALL_STATEMENT = "SELECT * FROM " + TABLE;
    private final String FIND_WHERE_STATEMENT = FIND_ALL_STATEMENT + " WHERE ID_SPORTIFY = ?";
    private final String FIND_LIKE_STATEMENT = FIND_ALL_STATEMENT + " WHERE " +
            "COMMUNE_SPORTIFY LIKE ? OR " +
            "NOM_INSTALLATION_SPORTIFY LIKE ? OR " +
            "CATEGORIE_INSTALLATION_SPORTIFY LIKE ? OR " +
            "NOM_EQUIPEMENT_SPORTIFY LIKE ? OR " +
            "NOMBRE_EQUIPEMENT_SPORTIFY LIKE ?";
    private final String FIND_ALL_FAVORITES = FIND_ALL_STATEMENT + " WHERE IS_FAVORI_SPORTIFY = 1";
    private final String INSERT_STATEMENT = "INSERT INTO " + TABLE + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private final String TOGGLE_FAVORITE = "UPDATE " + TABLE + " SET IS_FAVORI_SPORTIFY = ? WHERE ID_SPORTIFY = ?";
    private final String DELETE_ALL_STATEMENT = "DELETE FROM " + TABLE;

    static final int NUMBER_OF_ROWS = 1382;

    /**
     * Constructeur
     */
    InstallationDAO() {
        connection = H2.getConnection();
        //vérifie si la base de données n'existe pas.
        if (!checkTableExists()) {
            //si tel est le cas, crée la base de données et la remplit avec les données.
            createTable();
            insertAll();
        }
        //si elle existe déjà, vérifie si les données ne sont pas sont correctes.
        else if (findAll().size() != NUMBER_OF_ROWS) {
            //si tel est le cas, créer efface toutes les données et les crée de nouveau.
            refreshData();
        }
    }

    /**
     * fonction qui vérifie si la base de données existe déjà
     * (testé via InstallationDAOTest#testConstructor)
     *
     * @return le résultat
     */
    private boolean checkTableExists() {
        try {
            Statement statement = connection.createStatement();
            statement.executeQuery(FIND_ALL_STATEMENT);
        } catch (SQLException e) {
            if (e.getErrorCode() == 42102) {
                return false;
            }
        }
        return true;
    }

    /**
     * fonction qui créer la table table_sportify
     * (testé via InstallationDAOTest#testConstructor)
     */
    private void createTable() {
        try {
            Statement statement = connection.createStatement();
            statement.execute(CREATE_TABLE_STATEMENT);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * C'est une instance d'Installation à partir d'un resultSet (SQL * FROM) sur la ligne d'une requête (resultSet.next())
     *
     * @param selectInstallationResultSet resultSet sur la ligne et avec toutes les tables
     * @throws SQLException retourne un erreur si la requete SQL n'a pas été correctement écrite.
     */
    private Installation installationBuilder(ResultSet selectInstallationResultSet) throws SQLException {

        int id = selectInstallationResultSet.getInt("ID_SPORTIFY");
        String name = selectInstallationResultSet.getString("NOM_INSTALLATION_SPORTIFY");
        String commune = selectInstallationResultSet.getString("COMMUNE_SPORTIFY");
        String category = selectInstallationResultSet.getString("CATEGORIE_INSTALLATION_SPORTIFY");
        String equipementName = selectInstallationResultSet.getString("NOM_EQUIPEMENT_SPORTIFY");
        String equipementCategory = selectInstallationResultSet.getString("CATEGORIE_EQUIPEMENT_SPORTIFY");
        int equipementQuantity = selectInstallationResultSet.getInt("NOMBRE_EQUIPEMENT_SPORTIFY");
        String environnement = selectInstallationResultSet.getString("ENVIRONNEMENT_SPORTIFY");
        boolean isFavorite = (selectInstallationResultSet.getInt("IS_FAVORI_SPORTIFY") == 1);

        Equipement equipement = new Equipement(equipementName, equipementCategory, equipementQuantity);

        return new Installation(id, commune, name, category, equipement, environnement, isFavorite);
    }

    /**
     * Fonction qui retourne la dernière ligne de la TABLE
     *
     * @return dernière ligne comme instance d'Installation
     */
    Installation getLast() {
        Installation installation = null;
        try {
            Statement statement = connection.createStatement();
            statement.execute(FIND_ALL_STATEMENT);
            ResultSet resultSet = statement.getResultSet();
            resultSet.last();

            installation = installationBuilder(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return installation;
    }

    /**
     * Permet d'insérer une nouveulle ligne (installation) à table_sportify
     *
     * @param installation objet de l'installation
     */
    void insert(Installation installation) {
        try {
            PreparedStatement statement = connection.prepareStatement(INSERT_STATEMENT);
            statement.setInt(1, installation.getId());
            statement.setString(2, installation.getCommune());
            statement.setString(3, installation.getName());
            statement.setString(4, installation.getCategory());
            statement.setString(5, installation.getEquipement().getName());
            statement.setString(6, installation.getEquipement().getCategory());
            statement.setInt(7, installation.getEquipement().getQuantity());
            statement.setString(8, installation.getEnvironment());
            statement.setBoolean(9, installation.getFavorite());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * retourne les lignes d'une requete sql en tant que list d'installations
     *
     * @param resultSet la requète sql
     * @return les installations retournées
     */
    private List<Installation> getRows(ResultSet resultSet) throws SQLException {
        List<Installation> installations = new ArrayList<>();

        while (resultSet.next())
            installations.add(installationBuilder(resultSet));

        return installations;
    }

    /**
     * Retourne une liste qui contient toutes les installations
     *
     * @return une liste avec toutes les installations
     */
    List<Installation> findAll() {
        return select(FIND_ALL_STATEMENT);
    }

    /**
     * Récupere une liste d'installations selon un texte donné.
     *
     * @param text pour trouver les installations avec une requête LIKE sur toutes les colonnes
     * @return l'installation trouvée ou null si rien n'a été trouvé.
     */
    List<Installation> findByLike(String text) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_LIKE_STATEMENT);
            preparedStatement.setString(1, "%" + text + "%");
            preparedStatement.setString(2, "%" + text + "%");
            preparedStatement.setString(3, "%" + text + "%");
            preparedStatement.setString(4, "%" + text + "%");
            preparedStatement.setString(5, "%" + text + "%");
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            return getRows(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Récupere une installation selon l'id donné.
     *
     * @param id l'id d'une l'installation
     * @return l'installation trouvée ou null si rien n'a été trouvé.
     */
    Installation findById(int id) {
        Installation installation = null;
        try {
            PreparedStatement statement = connection.prepareStatement(FIND_WHERE_STATEMENT);
            statement.setInt(1, id);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            resultSet.next();
            installation = installationBuilder(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return installation;
    }

    /**
     * Supprime toutes les données de table_sportify
     * (testé via InstallationDAOTest#testConstructor)
     */
    void clearTable() {
        try {
            Statement statement = connection.createStatement();
            statement.execute(DELETE_ALL_STATEMENT);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Insère chaque objet installation tant qu'il y en a
     * (testé via InstallationDAOTest#testConstructor)
     */
    private void insertAll() {
        try {
            List<Installation> installations = CSVtoListOfInstallations.convertCSVtoListOfInstallations("", "");
            for (Installation installation : installations)
                insert(installation);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Efface tous les objets et puis les insère de nouveau
     */
    void refreshData() {
        clearTable();
        insertAll();
    }

    /**
     * Permet de gérer le bouton favoris afin de l'activer et désactiver.
     *
     * @param installation l'id d'une l'installation
     * @return l'installation trouvée ou null si rien n'a été trouvé.
     */
    void toggleFavorite(Installation installation) {
        boolean oldFavorite = installation.getFavorite();
        boolean newFavorite = !oldFavorite;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(TOGGLE_FAVORITE);
            preparedStatement.setBoolean(1, newFavorite);
            preparedStatement.setInt(2, installation.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Récupere une installation selon l'id donné.
     *
     * @param sqlStatement reqûete SQL
     * @return l'installation trouvée ou null si rien n'a été trouvé.
     */
    private List<Installation> select(String sqlStatement) {
        try {
            Statement statement = connection.createStatement();
            statement.executeQuery(sqlStatement);
            ResultSet resultSet = statement.getResultSet();
            return getRows(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Recup la liste de tous les favoris.
     *
     * @return Les installations trouver en favoris et null si rien trouvé.
     */
    List<Installation> getFavIfAdded() {
        return select(FIND_ALL_FAVORITES);
    }
}


