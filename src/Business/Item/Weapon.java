package Business.Item;

public class Weapon extends Item {
    /**
     * Constructor para inicializar un ítem con sus atributos específicos.
     *
     * @param item_id    Identificador único del ítem.
     * @param item_name  Nombre del ítem.
     * @param power      Poder del ítem (daño o defensa).
     * @param durability Durabilidad del ítem.
     */
    public Weapon(int item_id, String item_name, int power, int durability) {
        super(item_id, item_name, power, durability);
    }
}
