import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Classe qui teste les méthodes de la classe {@link CSVtoListOfInstallations}
 */
public class CSVtoListOfInstallationsTest {

    /**
     * Test le retour de la fonction à savoir qu'il correspond bien aux résutlats attendus.
     * @throws IOException retourne un erreur si le fichier n'existe pas.
     */
    @Test
    public void testConvertion_HappyPath() throws IOException {
        List<Installation> installations = CSVtoListOfInstallations.convertCSVtoListOfInstallations("bdd.csv", ";");
        assertEquals(1382,installations.size());
    }

    /**
     * Test si le tableau retourné n'est pas vide.
     * @throws IOException retourne un erreur si le fichier n'existe pas.
     */
    @Test
    public void testArrayNotEmpty() throws IOException {
        List<Installation> installations = CSVtoListOfInstallations.convertCSVtoListOfInstallations("bdd.csv", ";");
        assertFalse(installations.isEmpty());
    }

    /**
     * Test si les délimitations du tableau existent dans le fichier.
     * @throws IOException retourne un erreur si la délimitation n'existe pas.
     */
    @Test(expected = java.util.regex.PatternSyntaxException.class)
    public void testArrayWrongDelimiter() throws IOException {
        CSVtoListOfInstallations.convertCSVtoListOfInstallations("bdd.csv", "***");
    }

    /**
     * Test si le fichier n'est pas trouvé.
     * @throws IOException retourne un erreur si le fichier n'existe pas.
     */
    @Test(expected = FileNotFoundException.class)
    public void wrongFile() throws IOException {
        CSVtoListOfInstallations.convertCSVtoListOfInstallations("bddd.csv", ";");
    }
}
