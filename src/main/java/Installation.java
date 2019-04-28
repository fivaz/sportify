import java.util.Objects;

class Installation {

    private int id;
    private String commune;
    private String name;
    private String category;
    private Equipement equipement;
    private String environment;
    private boolean isFavorite;

    Installation(int id, String commune, String name, String category, Equipement equipement, String environment, boolean isFavorite) {
        this.id = id;
        this.commune = commune;
        this.name = name;
        this.category = category;
        this.equipement = equipement;
        this.environment = environment;
        this.isFavorite = isFavorite;
    }

    int getId() {
        return id;
    }

    String getCommune() {
        return commune;
    }

    String getName() {
        return name;
    }

    String getCategory() {
        return category;
    }

    Equipement getEquipement() {
        return equipement;
    }

    String getEnvironment() {
        return environment;
    }

    @Override
    public String toString() {
        return "\nid : " + id + " - commune: " + commune + " - name: " + name + " - category: " + category + " - " + equipement + " - environement: " + environment;
    }

    /**
     * Code généré automatiquement
     * @param installation
     * @return
     */
    @Override
    public boolean equals(Object installation) {
        if (this == installation)
            return true;
        if (installation == null || getClass() != installation.getClass())
            return false;
        Installation that = (Installation) installation;
        return id == that.id &&
                isFavorite == that.isFavorite &&
                Objects.equals(commune, that.commune) &&
                Objects.equals(name, that.name) &&
                Objects.equals(category, that.category) &&
                Objects.equals(equipement, that.equipement) &&
                Objects.equals(environment, that.environment);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, commune, name, category, equipement, environment, isFavorite);
    }

    boolean getFavorite() {
        return isFavorite;
    }

    void toggleFavorite() {
        isFavorite = !isFavorite;
    }
}
