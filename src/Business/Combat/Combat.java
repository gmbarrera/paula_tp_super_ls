package Business.Combat;

import Business.Item.Item;
import Business.Stats.Stats;
import Business.Team.Team;

import java.util.List;

/**
 * Representa un combate entre dos equipos, incluyendo sus estadísticas y objetos utilizados.
 */
public class Combat {
    private final Team team1;
    private final Team team2;
    private final Stats statsTeam1;
    private final Stats statsTeam2;

    /**
     * Crea una nueva instancia de Combat.
     *
     * @param team1      el primer equipo participante.
     * @param team2      el segundo equipo participante.
     * @param statsTeam1 las estadísticas del primer equipo.
     * @param statsTeam2 las estadísticas del segundo equipo.
     */
    public Combat(Team team1, Team team2, Stats statsTeam1, Stats statsTeam2) {
        this.team1 = team1;
        this.team2 = team2;
        this.statsTeam1 = statsTeam1;
        this.statsTeam2 = statsTeam2;
    }

    /**
     * Obtiene el primer equipo participante en el combate.
     *
     * @return el objeto {@link Team} correspondiente al primer equipo.
     */
    public Team getTeam1() {
        return team1;
    }

    /**
     * Obtiene el segundo equipo participante en el combate.
     *
     * @return el objeto {@link Team} correspondiente al segundo equipo.
     */
    public Team getTeam2() {
        return team2;
    }

    /**
     * Obtiene las estadísticas del primer equipo.
     *
     * @return el objeto {@link Stats} con las estadísticas del primer equipo.
     */
    public Stats getStatsTeam1() {
        return statsTeam1;
    }

    /**
     * Obtiene las estadísticas del segundo equipo.
     *
     * @return el objeto {@link Stats} con las estadísticas del segundo equipo.
     */
    public Stats getStatsTeam2() {
        return statsTeam2;
    }


}
