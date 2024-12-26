package Business.Stats;

import Persistance.StatsJSONDAO;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;

/**
 * Clase encargada de gestionar las estadísticas de los equipos.
 * Proporciona métodos para cargar, actualizar y guardar estadísticas.
 */
public class StatsManager {

    /**
     * Carga las estadísticas desde un archivo JSON y las parsea en una lista de objetos Stats.
     *
     * @param filePath Ruta del archivo JSON desde el que se leerán las estadísticas.
     * @return Una lista de objetos Stats que representan las estadísticas de los equipos.
     * @throws IOException Si hay un error al leer el archivo.
     */
    public List<Stats> loadStats(String filePath) throws IOException {
        StatsJSONDAO statsDAO = new StatsJSONDAO();
        return statsDAO.readAndParseStats(filePath);
    }

    /**
     * Busca las estadísticas de un equipo a partir de su nombre.
     *
     * @param team_name El nombre del equipo cuyas estadísticas se desean obtener.
     * @return El objeto Stats del equipo correspondiente o null si no se encuentra.
     * @throws IOException Si hay un error al leer el archivo de estadísticas.
     */
    public Stats getStatsByTeamName(String team_name) throws IOException {
        List<Stats> allStats = loadStats("Data/stats.json");

        for (Stats stats : allStats) {
            if (stats.getTeamName().equalsIgnoreCase(team_name)) {
                return stats;
            }
        }
        return null;
    }

    /**
     * Crea nuevas estadísticas para un equipo con valores iniciales de 0 y las guarda en el archivo.
     *
     * @param team_name El nombre del equipo al que se le crearán las estadísticas.
     * @param statsList La lista de estadísticas existente a la que se añadirá el nuevo objeto Stats.
     * @param filePath Ruta del archivo JSON donde se guardarán las estadísticas.
     * @return El nuevo objeto Stats creado para el equipo.
     * @throws IOException Si hay un error al leer o guardar el archivo de estadísticas.
     * @throws JSONException Si hay un error en el formato JSON al guardar las estadísticas.
     */
    public Stats createNewStats(String team_name, List<Stats> statsList, String filePath) throws IOException, JSONException {
        Stats newStats = new Stats(team_name, 0, 0, 0, 0);
        statsList.add(newStats);

        StatsJSONDAO statsDAO = new StatsJSONDAO();
        statsDAO.saveStats(statsList, filePath);

        return newStats;
    }

    /**
     * Actualiza las estadísticas de un equipo incrementando las partidas jugadas, las victorias y los KOs realizados y recibidos.
     *
     * @param stats El objeto Stats que contiene las estadísticas del equipo.
     * @param isWin Indica si el equipo ganó el partido (true) o no (false).
     * @param KO_done El número de KOs realizados en el partido.
     * @param KO_received El número de KOs recibidos en el partido.
     */
    public void updateStats(Stats stats, boolean isWin, int KO_done, int KO_received) {
        // Incrementar partidas jugadas
        stats.addGamesPlayed();

        // Incrementar victorias
        if (isWin) {
            stats.addVictory();
        }

        // Incrementar KOs realizados y recibidos
        stats.addKODone();
        stats.addKOReceived();
    }

    /**
     * Guarda las estadísticas actualizadas de todos los equipos en el archivo JSON.
     *
     * @param statsList La lista de objetos Stats que contienen las estadísticas de los equipos.
     * @param filePath Ruta del archivo JSON donde se guardarán las estadísticas.
     * @throws IOException Si hay un error al leer o guardar el archivo de estadísticas.
     * @throws JSONException Si hay un error en el formato JSON al guardar las estadísticas.
     */
    public void saveStats(List<Stats> statsList, String filePath) throws IOException, JSONException {
        StatsJSONDAO statsDAO = new StatsJSONDAO();
        statsDAO.saveStats(statsList, filePath);
    }

    /**
     * Incrementa el número de partidas jugadas en las estadísticas del equipo.
     *
     * @param stats El objeto Stats que contiene las estadísticas del equipo.
     */
    public void incrementGamesPlayed(Stats stats) {
        stats.addGamesPlayed();
    }

    /**
     * Incrementa el número de victorias en las estadísticas del equipo.
     *
     * @param stats El objeto Stats que contiene las estadísticas del equipo.
     */
    public void incrementGamesWon(Stats stats) {
        stats.addVictory();
    }

    /**
     * Acumula el número de KOs realizados y recibidos en las estadísticas del equipo.
     *
     * @param stats El objeto Stats que contiene las estadísticas del equipo.
     * @param KO_done El número de KOs realizados en el partido.
     * @param KO_received El número de KOs recibidos en el partido.
     */
    public void accumulateKOs(Stats stats, int KO_done, int KO_received) {
        stats.addKODone();
        stats.addKOReceived();
    }
}
