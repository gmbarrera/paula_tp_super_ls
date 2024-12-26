package Business.Team;

import java.util.List;

/**
 * Clase que representa a un equipo, con su nombre y una lista de miembros.
 */
public class Team {
    private final String teamName;        // Nombre del equipo
    private final List<Member> teamMembers; // Miembros del equipo

    /**
     * Constructor para inicializar un equipo con su nombre y los miembros.
     *
     * @param teamName     El nombre del equipo.
     * @param teamMembers  La lista de miembros del equipo.
     */
    public Team(String teamName, List<Member> teamMembers) {
        this.teamName = teamName;
        this.teamMembers = teamMembers;
    }

    /**
     * Obtiene el nombre del equipo.
     *
     * @return El nombre del equipo como una cadena de texto.
     */
    public String getTeamName() {
        return teamName;
    }

    /**
     * Obtiene la lista de miembros del equipo.
     *
     * @return La lista de miembros del equipo.
     */
    public List<Member> getTeamMembers() {
        return teamMembers;
    }
}
