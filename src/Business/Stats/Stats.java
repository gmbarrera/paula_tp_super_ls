package Business.Stats;

/**
 * Clase que representa las estadísticas de un equipo en términos de partidas jugadas,
 * victorias, KOs realizados y recibidos.
 */
public class Stats {
    private String team_name;  // Nombre del equipo
    private int games_played;  // Número de partidas jugadas
    private int games_won;     // Número de partidas ganadas
    private int KO_done;       // Número de KOs realizados
    private int KO_received;   // Número de KOs recibidos

    /**
     * Constructor para inicializar las estadísticas de un equipo.
     *
     * @param team_name    Nombre del equipo.
     * @param games_played Número de partidas jugadas.
     * @param games_won    Número de partidas ganadas.
     * @param KO_done      Número de KOs realizados.
     * @param KO_received  Número de KOs recibidos.
     */
    public Stats(String team_name, int games_played, int games_won, int KO_done, int KO_received) {
        this.team_name = team_name;
        this.games_played = games_played;
        this.games_won = games_won;
        this.KO_done = KO_done;
        this.KO_received = KO_received;
    }

    /**
     * Obtiene el nombre del equipo asociado a estas estadísticas.
     *
     * @return El nombre del equipo como una cadena de texto.
     */
    public String getTeamName() {
        return team_name;
    }

    /**
     * Obtiene el número total de partidas jugadas por el equipo.
     *
     * @return El número de partidas jugadas como un entero.
     */
    public int getGamesPlayed() {
        return games_played;
    }

    /**
     * Obtiene el número total de partidas ganadas por el equipo.
     *
     * @return El número de partidas ganadas como un entero.
     */
    public int getGamesWon() {
        return games_won;
    }

    /**
     * Obtiene el número total de KOs realizados por el equipo.
     *
     * @return El número de KOs realizados como un entero.
     */
    public int getKODone() {
        return KO_done;
    }

    /**
     * Obtiene el número total de KOs recibidos por el equipo.
     *
     * @return El número de KOs recibidos como un entero.
     */
    public int getKOReceived() {
        return KO_received;
    }

    /**
     * Incrementa el número de victorias del equipo en 1.
     */
    public void addVictory() {
        games_won++;
    }

    /**
     * Incrementa el número de KOs realizados por el equipo en 1.
     */
    public void addKODone() {
        KO_done++;
    }

    /**
     * Incrementa el número de KOs recibidos por el equipo en 1.
     */
    public void addKOReceived() {
        KO_received++;
    }

    /**
     * Incrementa el número de partidas jugadas por el equipo en 1.
     */
    public void addGamesPlayed() {
        games_played++;
    }

    /**
     * Calcula la tasa de victorias del equipo en porcentaje.
     *
     * @param games_played El número total de partidas jugadas.
     * @param games_won    El número total de partidas ganadas.
     * @return El porcentaje de victorias del equipo.
     */
    public float winRate(float games_played, float games_won) {
        if (games_played == 0) {
            return 0;
        } else {
            return (games_won / games_played) * 100;
        }
    }

    public String getDetails() {
        return "\n\tCombats played: " + getGamesPlayed() + "\n\tCombats won: " + getGamesWon() + "\n\tWin rate: " + winRate(games_played, games_won) + "%" + "\n\tKOs done: " + getKODone() + "\n\tKOs recieved: " + getKOReceived() + "\n";
    }
}
