import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Classe qui permet de transformer un fichier csv en une liste d'installation.
 */
class CSVtoListOfInstallations {

    private static final String CSV_FILE = "bdd.csv";
    private static final String CSV_DELIMITER = ";";

    /**
     * Convertit une installation en tableau en une instance d'Installation
     *
     * @param array tableau de string
     * @return object Installation
     */
    private static Installation convertArrayToInstallation(String[] array) {

        int installationId = Integer.valueOf(array[0]);
        String installationCommune = array[1];
        String installationName = array[2];
        String installationCategory = array[3];
        String equipementName = array[4];
        String equipementCategory = array[5];
        int equipementQuantity = Integer.valueOf(array[6]);
        String installationEnvironment = array[7];
        boolean installationIsFavorite = Boolean.valueOf(array[8]);

        Equipement equipement = new Equipement(equipementName, equipementCategory, equipementQuantity);

        return new Installation(installationId, installationCommune, installationName, installationCategory, equipement, installationEnvironment, installationIsFavorite);
    }

    /**
     * Fonction qui convertit le fichier csv en liste d'installation.
     *
     * @param fichierCsv   nom du fichier Csv
     * @param csvDelimiter élément qui sépare les instances dans la liste.
     * @return liste d'installations
     * @throws IOException retourne un erreur si le fichier n'existe pas.
     */
    static List<Installation> convertCSVtoListOfInstallations(String fichierCsv, String csvDelimiter) throws IOException {

        if (fichierCsv.isEmpty())
            fichierCsv = CSVtoListOfInstallations.CSV_FILE;
        if (csvDelimiter.isEmpty())
            csvDelimiter = CSVtoListOfInstallations.CSV_DELIMITER;

        List<Installation> installations = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(fichierCsv));
        String line;
        while ((line = br.readLine()) != null) {
            String[] info = Arrays.copyOf(line.split(csvDelimiter), 9);
            info[8] = "0";
            installations.add(convertArrayToInstallation(info));
        }
        return installations;
    }
}
