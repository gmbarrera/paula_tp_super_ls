package Business.Item;

/**
 * Representa un ítem que puede ser utilizado en combate, ya sea como arma o armadura.
 */
public class Item {

    private int item_id;
    private String item_name;
    private String item_class; // Puede ser "Weapon" o "Armor".
    private int power;
    private int durability;

    /**
     * Constructor para inicializar un ítem con sus atributos específicos.
     *
     * @param item_id     Identificador único del ítem.
     * @param item_name   Nombre del ítem.
     * @param item_class  Clase del ítem ("Weapon" o "Armor").
     * @param power       Poder del ítem (daño o defensa).
     * @param durability  Durabilidad del ítem.
     */
    public Item(int item_id, String item_name, String item_class, int power, int durability) {
        this.item_id = item_id;
        this.item_name = item_name;
        this.item_class = item_class;
        this.power = power;
        this.durability = durability;
    }

    /**
     * Obtiene el identificador único del ítem.
     *
     * @return El ID del ítem.
     */
    public int getId() {
        return item_id;
    }

    /**
     * Obtiene el nombre del ítem.
     *
     * @return El nombre del ítem.
     */
    public String getName() {
        return item_name;
    }

    /**
     * Obtiene la clase del ítem (Weapon o Armor).
     *
     * @return La clase del ítem.
     */
    public String getItemClass() {
        return item_class;
    }

    /**
     * Obtiene el poder del ítem.
     *
     * @return El valor de poder del ítem.
     */
    public int getPower() {
        return power;
    }

    /**
     * Obtiene la durabilidad del ítem.
     *
     * @return El valor de durabilidad del ítem.
     */
    public int getDurability() {
        return durability;
    }

    /**
     * Obtiene una representación detallada del ítem como cadena de texto.
     *
     * @return Los detalles del ítem, incluyendo ID, nombre, clase, poder y durabilidad.
     */
    public String getDetails() {
        return "\tID: " + item_id +
                "\n\tNAME: " + item_name +
                "\n\tCLASS: " + item_class +
                "\n\tPOWER: " + power +
                "\n\tDURABILITY: " + durability;
    }

    // Method to check if the item is a weapon
     public boolean isWeapon() {
        return "Weapon".equalsIgnoreCase(this.item_class);
     }

     // Method to check if the item is armor
     public boolean isArmor() {
        return "Armor".equalsIgnoreCase(this.item_class);
    }

    public void decreaseDurability() {
        this.durability -= 1;

    }
}
