package Business.Team;

import Business.Item.Item;

/**
 * Clase que representa a un miembro de un equipo, con sus características como su ID, estrategia,
 * daño acumulado y estado de KO.
 */
public class Member {
    private int member_id;            // ID del miembro
    private String strategy;          // Estrategia del miembro
    private double accumulated_damage; // Daño acumulado
    private boolean knocked_out;      // Estado de KO
    private Item item;

    /**
     * Constructor para inicializar un miembro con un ID y una estrategia.
     *
     * @param member_id El ID del miembro.
     * @param strategy  La estrategia del miembro.
     */
    public Member(int member_id, String strategy) {
        this.member_id = member_id;
        this.strategy = strategy;
        this.accumulated_damage = 0.0;
        this.knocked_out = false;
        this.item = null;
    }

    /**
     * Obtiene el ID del miembro.
     *
     * @return El ID del miembro como un entero.
     */
    public int getMemberId() {
        return member_id;
    }

    /**
     * Obtiene la estrategia del miembro.
     *
     * @return La estrategia del miembro como una cadena de texto.
     */
    public String getStrategy() {
        return strategy;
    }

    /**
     * Obtiene el daño acumulado por el miembro.
     *
     * @return El daño acumulado como un número de tipo double.
     */
    public double getAccumulatedDamage() {
        return accumulated_damage;
    }

    /**
     * Establece el daño acumulado por el miembro.
     *
     * @param accumulated_damage El nuevo valor de daño acumulado.
     */
    public void setAccumulatedDamage(double accumulated_damage) {
        this.accumulated_damage = accumulated_damage;
    }

    /**
     * Obtiene el estado de KO del miembro (si está o no noqueado).
     *
     * @return true si el miembro está noqueado, false si no lo está.
     */
    public boolean isKnockedOut() {

        return knocked_out;
    }

    /**
     * Establece el estado de KO del miembro.
     *
     * @param knocked_out El estado de KO del miembro (true si está noqueado, false si no).
     */
    public void setKnockedOut(boolean knocked_out) {
        this.knocked_out = knocked_out;
    }
}
