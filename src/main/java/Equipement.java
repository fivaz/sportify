/**
 * Classe de la création de l'objet Equipement
 */
class Equipement {

    private String name;
    private String category;
    private int quantity;

    Equipement(String name, String category, int quantity){
        this.name = name;
        this.category = category;
        this.quantity = quantity;
    }

    String getName() {
        return name;
    }

    String getCategory() {
        return category;
    }

    int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "equipement name: "+name+" - equipement category: "+category+" - equipement quantity: "+quantity;
    }
}

