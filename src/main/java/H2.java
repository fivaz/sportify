import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Class qui retourne/Crée la connection à la base de données interne
 */
class H2 {
    private static final String URL = "jdbc:h2:~/";
    private static final String DATABASE = "sportify";

    private static final String username = "sportify";
    private static final String PASSWORD = "";
    private static Connection dbConnection = null;

    /**
     * Singleton qui permet de creer une unique instance de connexion
     * @return l'instance de connexion
     */
    static Connection getConnection() {

        if (dbConnection == null) {
            try {
                dbConnection = DriverManager.getConnection(URL + DATABASE, username, PASSWORD);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dbConnection;
    }
}