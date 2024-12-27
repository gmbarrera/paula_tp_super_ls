package Business.Team;

import Business.Character.Character;
import Business.Character.CharacterManager;
import Business.Stats.Stats;

import Persistance.JSON.StatsJSONDAO;
import Persistance.TeamDAO;
import Persistance.JSON.TeamJSONDAO;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Clase encargada de gestionar las operaciones relacionadas con los equipos.
 */

public class TeamManager {

    private final TeamDAO teamDAO;
    private final CharacterManager characterManager;
    private final StatsJSONDAO statsDAO;
    private String filePath;

    /**
     * Constructor del TeamManager.
     * Inicializa los DAOs necesarios para manejar los equipos, personajes y estadÃ­sticas.
     */
    public TeamManager() {
        this.teamDAO = new TeamJSONDAO();
        this.characterManager = new CharacterManager();
        this.statsDAO = new StatsJSONDAO();
        this.filePath = "Data/teams.json";
    }

    /**
     * Carga la lista de equipos desde un archivo JSON.
     *
     * @return Lista de objetos Team.
     * @throws IOException Si ocurre un error al leer el archivo.
     */
    public List<Team> loadTeams() throws IOException {
        return this.teamDAO.getTeams();
    }

    /**
     * Obtiene los nombres de los equipos desde el archivo JSON.
     *
     * @param filePath Ruta del archivo que contiene los equipos.
     * @return Lista de nombres de equipos.
     * @throws IOException Si ocurre un error al leer el archivo.
     */
    public List<String> getTeamNames(String filePath) throws IOException {
        List<Team> teams = teamDAO.getTeams();
        List<String> teamNames = new ArrayList<>();
        for (Team team : teams) {
            teamNames.add(team.getTeamName());
        }
        return teamNames;
    }

    /**
     * Obtiene un equipo por su Ã­ndice desde el archivo JSON.
     *
     * @param index Ã�ndice del equipo en la lista.
     * @param filePath Ruta del archivo que contiene los equipos.
     * @return El equipo correspondiente al Ã­ndice.
     * @throws IOException Si ocurre un error al leer el archivo.
     * @throws IndexOutOfBoundsException Si el Ã­ndice no es vÃ¡lido.
     */
    public Team getTeamByIndex(int index, String filePath) throws IOException {
        List<Team> teams = teamDAO.getTeams();
        if (index >= 0 && index < teams.size()) {
            return teams.get(index);
        } else {
            throw new IndexOutOfBoundsException("Invalid team index. Please try again!");
        }
    }

    /**
     * Obtiene un equipo por su nombre.
     *
     * @param teamName Nombre del equipo.
     * @return El equipo correspondiente al nombre, o null si no se encuentra.
     */
    public Team getTeamByName(String teamName) {
        List<Team> teams = teamDAO.getTeams();
        for (Team team : teams) {
            if (team.getTeamName().equalsIgnoreCase(teamName)) {
                return team;
            }
        }
        return null;
    }

    /**
     * Obtiene las estadÃ­sticas de un equipo por su nombre.
     *
     * @param teamName Nombre del equipo.
     * @return Las estadÃ­sticas del equipo, o null si no se encuentran.
     */
    public Stats getStatsByTeamName(String teamName) {
        List<Stats> allStats = statsDAO.readAndParseStats("Data/stats.json");
        for (Stats stats : allStats) {
            if (stats.getTeamName().equalsIgnoreCase(teamName)) {
                return stats;
            }
        }
        return null;
    }

    /**
     * Crea un nuevo equipo con los miembros proporcionados.
     *
     * @param teamName Nombre del nuevo equipo.
     * @return true si el equipo fue creado exitosamente, false si hubo algÃºn error.
     * @throws IOException Si ocurre un error al leer o escribir los archivos.
     */
    public boolean createTeam(String teamName, List<Member> members) throws IOException {
        Team newTeam = new Team(teamName, members);

        try {
            teamDAO.createTeam(newTeam);

            Stats stats = new Stats(teamName, 0, 0, 0, 0);  // Inicializamos con 0 para todos los valores
            List<Stats> statsList = new ArrayList<>();
            statsList.add(stats);
            statsDAO.saveStats(statsList); // Guardar la lista de estadÃ­sticas
            return true;

        } catch (JSONException e) {
            System.err.println("Error while saving team: " + e.getMessage());
            return false;
        }
    }

    /**
     * Verifica si el nombre del equipo es vÃ¡lido (no existe previamente).
     *
     * @param teamName Nombre del equipo.
     * @return true si el nombre del equipo es vÃ¡lido, false si ya existe.
     * @throws IOException Si ocurre un error al leer el archivo.
     */
    public boolean isTeamNameValid(String teamName) throws IOException {
        List<Team> existingTeams = new TeamJSONDAO().getTeams();

        for (Team team : existingTeams) {
            if (team.getTeamName().equalsIgnoreCase(teamName)) {
                return false;
            }
        }
        return !teamName.isBlank();  // Se evita que se ingrese un nombre vacio
    }

    /**
     * Elimina un equipo por su nombre.
     *
     * @param teamName Nombre del equipo a eliminar.
     * @return true si el equipo fue eliminado exitosamente, false si no existÃ­a.
     * @throws IOException Si ocurre un error al leer o escribir los archivos.
     */
    public boolean deleteTeam(String teamName) throws IOException {
        String filePath = "Data/teams.json";

        List<Team> teams = teamDAO.getTeams();
        Team teamToDelete = null;

        for (Team team : teams) {
            if (team.getTeamName().equalsIgnoreCase(teamName)) {
                teamToDelete = team;
                break;
            }
        }

        if (teamToDelete == null) {
            System.out.println("Error: The team '" + teamName + "' does not exist.");
            return false;
        }

        return teamDAO.deleteTeam(teamToDelete);
    }

    /**
     * Obtiene los equipos a los que pertenece un personaje especÃ­fico.
     *
     * @param character El personaje del cual se desean obtener los equipos.
     * @return Lista de nombres de equipos a los que pertenece el personaje.
     */
    public List<String> getTeamsForCharacter(Character character) {
        List<Team> teams = teamDAO.getTeams();
        List<String> teamNames = new ArrayList<>();

        for (Team team : teams) {
            for (Member member : team.getTeamMembers()) {
                if (member.getMemberId() == character.getId()) {
                    teamNames.add(team.getTeamName());
                    break;
                }
            }
        }

        return teamNames;
    }

    /**
     * Actualiza el daÃ±o acumulado de un miembro y verifica si debe ser eliminado (K.O.).
     *
     * @param member El miembro cuyo daÃ±o se actualizarÃ¡.
     * @param damage El daÃ±o que se le aplica al miembro.
     */
    public void updateMemberDamage(Member member, double damage) {
        // Actualizar el daÃ±o acumulado del miembro
        double newAccumulatedDamage = member.getAccumulatedDamage() + damage;
        member.setAccumulatedDamage(newAccumulatedDamage);
        Character character = characterManager.getCharacter(member.getMemberId());

        // Verificar si el miembro debe ser eliminado
        boolean knockedOut = isMemberKnockedOut(member);
        if (knockedOut) {
            System.out.println(character.getName() + " has been knocked out!");
        }
    }

    /**
     * Determina si un miembro ha sido eliminado (K.O.) basÃ¡ndose en el daÃ±o acumulado.
     *
     * @param member El miembro a verificar.
     * @return true si el miembro fue eliminado, false si no.
     */
    public boolean isMemberKnockedOut(Member member) {
        Random random = new Random();
        double knockOutValue = random.nextInt(200) + 1; // Generar nÃºmero entre 1 y 200
        knockOutValue /= 100; // Dividir entre 100

        double accumulatedDamage = member.getAccumulatedDamage();
        if (knockOutValue > accumulatedDamage) {
            member.setKnockedOut(true); // Marcar al miembro como eliminado
            return true; // El miembro estÃ¡ eliminado
        }

        return member.isKnockedOut(); // Devolver el estado actual si no fue eliminado
    }

    /**
     * Genera un resumen del estado de los miembros de un equipo.
     *
     * @param team El equipo del cual se desea generar el resumen.
     * @return Lista de cadenas con el estado de cada miembro.
     */
    public List<String> getTeamSummary(Team team) {
        List<String> summary = new ArrayList<>();

        for (Member member : team.getTeamMembers()) {
            Character character = characterManager.getCharacter(member.getMemberId());
            String status = member.isKnockedOut() ? "K.O." : String.format("%.2f%% damage", member.getAccumulatedDamage() * 100);
            summary.add(character.getName() + " - " + status);
        }
        return summary;
    }
}
