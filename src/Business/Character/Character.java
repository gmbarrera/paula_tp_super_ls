package Business.Character;

import Business.Item.Item;

/**
 * Crea un personaje con un ID, nombre y peso.
 */
public class Character {
    private int id;
    private String name;
    private int weight;
    private double accumulatedDamage;
    private Item weapon;
    private Item armor;
    private boolean KO;

    /**
     * Constructor: crea una nueva instancia de Character.
     *
     * @param id    el identificador único del personaje.
     * @param name  el nombre del personaje.
     * @param weight el peso del personaje en kilogramos.
     */
    public Character(int id, String name, int weight) {
        this.id = id;
        this.name = name;
        this.weight = weight;
    }

    /**
     * Obtiene el ID del personaje.
     *
     * @return el identificador único del personaje.
     */
    public int getId() {
        return id;
    }

    /**
     * Obtiene el nombre del personaje.
     *
     * @return el nombre del personaje.
     */
    public String getName() {
        return name;
    }

    /**
     * Obtiene el peso del personaje.
     *
     * @return el peso del personaje en kilogramos.
     */
    public int getWeight() {
        return weight;
    }

    public double getAccumulatedDamage() {
        return accumulatedDamage;
    }

    /**
     * Obtiene una representación en texto con los detalles del personaje.
     *
     * @return una cadena con los detalles del personaje, incluyendo ID, nombre y peso.
     */
    public String getDetails() {
        return "\tID: " + id + "\n\tNAME: " + name + "\n\tWEIGHT: " + weight + " kg";
    }

    public void takeDamage(double damage) {
        this.accumulatedDamage += damage; // Accumulate damage
    }

    public boolean isEliminated() {
        return this.accumulatedDamage > 1; // Check if eliminated
    }

    public void setWeapon(Item weapon) {
        this.weapon = weapon;
    }

    public void setArmor(Item armor) {
        this.armor = armor;
    }

    public Item getWeapon() {
        return weapon;
    }

    public Item getArmor() {
        return armor;
    }

    public String toString() {
        return this.id + " " + this.name;
    }

}
