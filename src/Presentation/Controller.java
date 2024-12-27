package Presentation;

import Business.Character.Character;
import Business.Character.CharacterManager;
import Business.Combat.CombatManager;
import Business.Item.Item;
import Business.Item.ItemManager;
import Business.Stats.Stats;
import Business.Stats.StatsManager;
import Business.Team.Member;
import Business.Team.Team;
import Business.Team.TeamManager;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Controlador principal que gestiona las interacciones entre la lógica del negocio y la interfaz de usuario.
 */
public class Controller {

    private final CharacterManager characterManager;
    private final TeamManager teamManager;
    private final ItemManager itemManager;
    private final StatsManager statsManager;
    private final CombatManager combatManager;

    /**
     * Constructor del controlador que inicializa los manejadores de las diferentes entidades.
     */
    public Controller() {
        this.characterManager = new CharacterManager();
        this.teamManager = new TeamManager();
        this.itemManager = new ItemManager();
        this.statsManager = new StatsManager();
        this.combatManager = new CombatManager();
    }

    /**
     * Inicia el programa y muestra el mensaje de bienvenida.
     */
    public void startProgram() {
        UIManager uiManager = new UIManager();
        uiManager.showStartingMessage(this);
    }

    /**
     * Valida que los archivos de persistencia estén accesibles.
     *
     * @return true si todos los archivos son accesibles, false si algún archivo no lo es.
     */
    public boolean validatePersistence() {
        boolean okFiles = true;

        String[] filePaths = {
                "Data/characters.json",
                "Data/teams.json",
                "Data/stats.json",
                "Data/items.json"
        };

        for (String path : filePaths) {
            if (!new File(path).exists()) {
                System.out.println("Error: the file " + path + " can't be accessed.");
                okFiles = false;
            }
        }

        return okFiles;
    }

    /**
     * Maneja las opciones del menú principal.
     *
     * @param option La opción seleccionada por el usuario.
     * @param controller El controlador que gestiona las interacciones.
     * @throws IOException Si ocurre un error al acceder a los archivos.
     * @throws JSONException Si ocurre un error al procesar el archivo JSON.
     */
    public void mainMenuOption(int option, Controller controller) throws IOException, JSONException {
        String charactersPath = "Data/characters.json";
        String teamsPath = "Data/teams.json";
        String itemsPath = "Data/items.json";

        UIManager uiManager = new UIManager();
        switch (option) {
            case 1 -> {
                List<String> characterNames = characterManager.getCharacterNames();
                uiManager.displayCharacterNames(characterNames);

                int choice = uiManager.promptChoice(characterNames.size(), controller);
                if (choice > 0) { // Ignoramos la opción "0" para salir
                    Character character = characterManager.getCharacterByIndex(choice - 1); // Ajustar índice
                    List<String> teamNames = teamManager.getTeamsForCharacter(character);
                    uiManager.displayCharacterDetails(character.getDetails(), teamNames);
                }
            }

            case 2 -> {
                List<String> teamNames = teamManager.getTeamNames(teamsPath);
                uiManager.showTeamManagementMenu(this, teamNames, charactersPath);

                int choice = uiManager.promptChoice(teamNames.size(), controller);
                if (choice > 0) {
                    String teamName = teamNames.get(choice - 1);
                    List<String> memberDetails = getTeamMembersDetails(teamName);
                    List<String> stats = getStatsForTeam(teamName);
                    uiManager.displayTeamDetails(teamName, controller, memberDetails, stats);
                }
                if (choice == 0) {
                    uiManager.displayMainMenu(controller);
                }
            }

            case 3 -> {
                List<String> itemNames = itemManager.getItemNames(itemsPath);
                uiManager.displayItemNames(itemNames);

                int choice = uiManager.promptChoice(itemNames.size(), controller);
                if (choice >= 0) {
                    Item item = itemManager.getItemByIndex(choice, itemsPath);
                    uiManager.displayItemDetails(item.getDetails());
                }
            }

            case 4 -> {
                // Cargar datos necesarios
                List<String> teamNames = teamManager.getTeamNames(teamsPath);
                if (teamNames.isEmpty()) {
                    System.out.println("No teams available. Please create teams first.");
                    return;
                }

                List<Team> teams = teamManager.loadTeams();
                List<Character> characters = characterManager.loadCharacters();
                List<Item> items = itemManager.loadItems();

                uiManager.displayAvailableTeams(teamNames);

                int team1Index = uiManager.getTeamChoice(teamNames.size(), 1);
                int team2Index = uiManager.getTeamChoice(teamNames.size(), 2);

                // Inicializar combate
                Team team1 = teams.get(team1Index);
                Team team2 = teams.get(team2Index);

                combatManager.initializeTeams(team1, team2, items, characters);
            }

            case 5 -> {
                System.out.println("\nWe hope to see you again!");
                System.exit(0);
            }
            default -> System.out.println("Please enter a valid option [1-5]");
        }
    }

    /**
     * Espera a que el usuario presione la tecla ENTER para continuar.
     */
    public void pressEnterKeyToContinue() {
        UIManager uiManager = new UIManager();
        Controller controller = new Controller();
        System.out.println("\n<Press ENTER key to continue...>");

        try {
            new Scanner(System.in).nextLine();
            uiManager.displayMainMenu(controller);
        } catch (Exception e) {
            System.out.println("Error while waiting for key press.");
        }
    }

    /*************************************TEAMS***************************************************/

    /**
     * Crea un nuevo equipo con los personajes seleccionados.
     *
     * @param teamName El nombre del nuevo equipo.
     * @param members Los members del team.
     * @return true si el equipo fue creado con éxito, false en caso contrario.
     * @throws IOException Si ocurre un error al escribir en el archivo.
     * @throws JSONException Si ocurre un error al procesar el archivo JSON.
     */
    public boolean createTeam(String teamName, List<Member> members) throws IOException, JSONException {
        boolean teamCreated = false;

        teamCreated = teamManager.createTeam(teamName, members);

        if (!teamCreated) {
            System.out.println("Team name " + teamName + " is not available. Please try again!");
            UIManager uiManager = new UIManager();
            Controller controller = new Controller();
            uiManager.createTeamPrompt(controller);
        }

        return teamCreated;
    }

    /**
     * Elimina un equipo por su nombre.
     *
     * @param teamName El nombre del equipo a eliminar.
     * @return true si el equipo fue eliminado correctamente, false si no fue eliminado.
     */
    public boolean deleteTeam(String teamName) {
        Scanner scanner = new Scanner(System.in);
        char option;

        System.out.println("Are you sure you want to delete " + teamName + "? (y/n)");
        option = scanner.next().charAt(0);

        if (option == 'y' || option == 'Y') {
            try {
                return teamManager.deleteTeam(teamName);
            } catch (IOException e) {
                System.err.println("Error while deleting team: " + e.getMessage());
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Obtiene los detalles de los miembros de un equipo.
     *
     * @param teamName El nombre del equipo.
     * @return Una lista con los detalles de los miembros del equipo.
     * @throws IOException Si ocurre un error al leer el archivo de equipos.
     */
    public List<String> getTeamMembersDetails(String teamName) throws IOException {
        Team team = teamManager.getTeamByName(teamName);
        int i = 0;

        if (team == null) {
            System.out.println("Error: Team not found.");
            return new ArrayList<>();
        }

        List<String> memberDetails = new ArrayList<>();

        for (Member member : team.getTeamMembers()) {
            if (member != null) {
                Character memberCharacter = characterManager.getCharacter(member.getMemberId());
                if (memberCharacter != null) {
                    String memberDetail = "\tCharacter #" + (i + 1) + ": " + memberCharacter.getName() + " (" + member.getStrategy().toUpperCase() + ")";
                    memberDetails.add(memberDetail);
                    i++;
                } else {
                    memberDetails.add("Not assigned. Error in getTeamMembersDetails");
                }
            }
        }

        return memberDetails;
    }

    /**
     * Obtiene las estadísticas de un equipo.
     *
     * @param teamName El nombre del equipo.
     * @return Una lista con las estadísticas del equipo.
     * @throws IOException Si ocurre un error al leer el archivo de estadísticas.
     * @throws JSONException Si ocurre un error al procesar el archivo JSON.
     */
    public List<String> getStatsForTeam(String teamName) throws IOException, JSONException {
        Stats stats = statsManager.getStatsByTeamName(teamName);
        List<String> teamStats = new ArrayList<>();

        if (stats != null) {
            teamStats.add(stats.getDetails());
        } else {
            List<Stats> statsList = statsManager.loadStats();
            stats = statsManager.createNewStats(teamName, statsList);
            teamStats.add(stats.getDetails());
        }

        return teamStats;
    }

    /**
     * Solicita al usuario que elija un equipo a partir de un número de opciones disponibles.
     * La entrada del usuario se valida para asegurarse de que esté dentro del rango de equipos válidos.
     *
     * @param teamCount El número total de equipos disponibles para seleccionar.
     * @return El índice (basado en 0) del equipo seleccionado por el usuario.
     *         Si el usuario elige una opción inválida, el método sigue solicitando la entrada hasta que sea válida.
     */
    public int getTeamChoice(int teamCount) {
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt() - 1;

        while (choice < 0 || choice >= teamCount) {
            System.out.println("Invalid choice. Try again.");
            choice = scanner.nextInt() - 1;
        }

        return choice;
    }

    /**
     * 
     * @param teamName
     * @return
     * @throws IOException
     */
	public boolean isTeamNameValid(String teamName) throws IOException {
		return this.teamManager.isTeamNameValid(teamName);
	}

    /**
     *
     * @param info
     * @return
     */
    public Character findCharacter(String info) {
        try {
            int idNumerico = Integer.parseInt(info);
            return this.characterManager.getCharacter(idNumerico);
        } catch (Exception ignored) {
            return this.characterManager.getCharacter(info);
        }
    }
}
