package Presentation;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Clase UIManager responsable de interactuar con el usuario,
 * mostrando menús y gestionando las entradas relacionadas con personajes,
 * equipos, ítems y combate.
 */
public class UIManager {

    private final Scanner scanner;

    /**
     * Inicializa la instancia de UIManager con un objeto Scanner
     * para manejar las entradas del usuario.
     */
    public UIManager() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Muestra el mensaje de inicio del programa y verifica los archivos locales
     * antes de continuar con la ejecución.
     *
     * @param controller El objeto Controller que gestiona la lógica de la aplicación.
     */
    public void showStartingMessage(Controller controller) {
        System.out.println("""
                  ____                          _     ____     ____            _\s
                 / ___| _   _ _ __   ___ _ __  | |   / ___|   | __ ) _ __ ___ | |
                 \\___ \\| | | | '_ \\ / _ \\ '__| | |   \\___ \\   |  _ \\| '__/ _ \\| |
                  ___) | |_| | |_) |  __/ |    | |___ ___) |  | |_) | | | (_) |_|
                 |____/ \\__,_| .__/ \\___|_|    |_____|____( ) |____/|_|  \\___/(_)
                             |_|                          |/                    \s
                """);
        System.out.println("Welcome to Super LS, Bro! Simulator.");
        System.out.println("\nVerifying local files... ");

        if (controller.validatePersistence()) {
            System.out.println("Files OK.\nStarting program...\n");
            displayMainMenu(controller);
        } else {
            System.out.println("Shutting down...");
            System.exit(0);
        }
    }

    /**
     * Muestra el menú principal con opciones para listar personajes,
     * gestionar equipos, listar ítems, simular combates y salir del programa.
     *
     * @param controller El objeto Controller que gestiona la lógica de la aplicación.
     */
    public void displayMainMenu(Controller controller) {
        System.out.println("""
                            \t1) List Characters
                            \t2) Manage Teams
                            \t3) List Items
                            \t4) Simulate Combat
                            
                            \t5) Exit""");

        System.out.print("\nChoose an option: ");
        try {
            int option = scanner.nextInt();
            scanner.nextLine();
            controller.mainMenuOption(option, controller);
        } catch (InputMismatchException e) {
            System.out.println("Please enter a valid number [1-5]");
            scanner.nextLine();
            displayMainMenu(controller);
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Solicita al usuario que elija una opción dentro de un rango permitido.
     *
     * @param max_index El índice máximo de opciones disponibles.
     * @param controller El objeto Controller que maneja la lógica de la aplicación.
     * @return La opción seleccionada por el usuario.
     */
    public int promptChoice(int max_index, Controller controller) {
        System.out.print("\nChoose an option: ");
        int choice = scanner.nextInt();

        if (choice < 0 || choice > max_index) {
            System.out.println("Please enter a valid number [0-" + max_index + "]");
        }
        if (choice == 0) {
            displayMainMenu(controller);
        }

        return choice;
    }

    /***********************************************CHARACTER***********************************************************/

    /**
     * Muestra una lista de nombres de personajes.
     *
     * @param character_names Una lista de nombres de personajes para mostrar.
     */
    public void displayCharacterNames(List<String> character_names) {
        for (int i = 0; i < character_names.size(); i++) {
            System.out.println("\t" + (i + 1) + ") " + character_names.get(i));
        }
        System.out.println("\n\t0) Back");
    }

    /**
     * Muestra los detalles de un personaje junto con los nombres de los equipos a los que pertenece.
     *
     * @param character_details Una cadena con los detalles del personaje.
     * @param team_names Una lista de nombres de equipos a los que pertenece el personaje.
     */
    public void displayCharacterDetails(String character_details, List<String> team_names) {
        Controller controller = new Controller();
        System.out.println("\n");
        System.out.println(character_details);

        System.out.println("\tTEAMS:");
        if (team_names != null) {
            for (String team_name : team_names) {
                System.out.println("\t\t" + "- " + team_name);
            }
        } else {
            System.out.println("\tThis character does not belong to any team");
        }

        controller.pressEnterKeyToContinue();
    }

    /***********************************************TEAMS***********************************************************/
    /**
     * Muestra el menú de gestión de equipos con opciones para crear,
     * listar, eliminar equipos o regresar al menú principal.
     *
     * @param controller El objeto Controller que maneja la lógica de la aplicación.
     * @param team_names Una lista de nombres de equipos.
     * @param filepath La ruta al archivo donde se almacenan los datos de equipos.
     */
    public void showTeamManagementMenu(Controller controller, List<String> team_names, String filepath) {
        System.out.println("""
                \nTeam Management Menu:
                \t1) Create Team
                \t2) List Teams
                \t3) Delete a Team
                \n\t4) Back""");

        System.out.print("\nChoose an option: ");

        try {
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> createTeamPrompt(controller, filepath);
                case 2 -> displayTeamNames(team_names);
                case 3 -> deleteTeam(controller);
                case 4 -> displayMainMenu(controller);
                default -> {
                    System.out.println("Invalid option. Please enter a valid option [1-3].");
                    showTeamManagementMenu(controller, team_names, filepath);
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Please enter a valid number.");
            scanner.nextLine();
            showTeamManagementMenu(controller, team_names, filepath);
        }
    }

    /**
     * Muestra una lista de nombres de equipos.
     *
     * @param team_names Una lista de nombres de equipos para mostrar.
     */
    public void displayTeamNames(List<String> team_names) {
        int i;

        for (i = 0; i < team_names.size(); i++) {
            System.out.println("\t" + (i + 1) + ") " + team_names.get(i));
        }

        System.out.println("\n\t0) Back");
    }

    /**
     * Muestra los detalles de un equipo, incluyendo su nombre, miembros y estadísticas.
     *
     * @param team_name     El nombre del equipo cuyos detalles se mostrarán.
     * @param controller    El objeto Controller que gestiona la lógica de la aplicación.
     * @param member_details Una lista con los detalles de los miembros del equipo.
     * @param stats         Una lista con las estadísticas del equipo.
     */
    public void displayTeamDetails(String team_name, Controller controller, List<String> member_details, List<String> stats) {
        System.out.println("\tTeam name: " + team_name + "\n");

        if (member_details.isEmpty() || stats.isEmpty()) {
            System.out.println("Error in displayTeamDetails");
        } else {
            for (String detail : member_details) {
                System.out.println(detail);
            }

            for (String stat : stats) {
                System.out.println(stat);
            }
        }

        controller.pressEnterKeyToContinue();
    }

    /**
     * Solicita al usuario los datos necesarios para crear un equipo.
     *
     * @param controller El objeto Controller que gestiona la lógica de la aplicación.
     * @param filepath La ruta del archivo donde se almacenan los datos de los equipos.
     */
    public void createTeamPrompt(Controller controller, String filepath) {
    	// Solicitar el nombre del team
    	String teamName = "";
    	
        try {
			do {
				System.out.print("Please enter the team's name: ");
			    teamName = scanner.nextLine();
			} while (!controller.isTeamNameValid(teamName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        List<String> characterIds = new ArrayList<>();

        // Solicitar los miembros del team.
        for (int i=0; i<4; i++) {
        	String id = "";
        	
        	do {
	        	System.out.println("Please enter the id or name for character: ");
	        	id = scanner.nextLine();
        	}
        	while (!controller.isCharacterValid(id));
        	
        	characterIds.add(id);
        }
        
        
        // Crear el team
        try {
            boolean teamCreated = controller.createTeam(teamName, characterIds);
            if (teamCreated) {
                System.out.printf("Team '%s' has been successfully created!\n", teamName);
                displayMainMenu(controller);
            } else {
                System.out.println("Error creating the team. Please try again.");
            }
        } catch (IOException e) {
            System.out.println("Error creating team: " + e.getMessage());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Solicita al usuario el nombre de un equipo para eliminarlo.
     *
     * @param controller El objeto Controller que gestiona la lógica de la aplicación.
     */
    public void deleteTeam(Controller controller) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nEnter the name of the team to delete: ");
        String team_name = scanner.nextLine();

        boolean success = controller.deleteTeam(team_name);
        if (success) {
            System.out.println("The team '" + team_name + "' has been successfully deleted.");
        } else {
            System.out.println("Team " + team_name + " could not be deleted.");
            displayMainMenu(controller);
        }
    }

    /***********************************************ITEMS**********************************************************/
    /**
     * Muestra una lista de nombres de ítems.
     *
     * @param item_names Una lista de nombres de ítems a mostrar.
     */
    public void displayItemNames(List<String> item_names) {
        System.out.println("Items:");
        for (int i = 0; i < item_names.size(); i++) {
            System.out.println("\t" + (i + 1) + ") " + item_names.get(i));
        }
        System.out.println("\n\t0) Back");
    }

    /**
     * Muestra los detalles de un ítem.
     *
     * @param item_details Una cadena con los detalles del ítem.
     */
    public void displayItemDetails(String item_details) {
        Controller controller = new Controller();
        System.out.println("\n");
        System.out.println(item_details);
        controller.pressEnterKeyToContinue();
    }

    /**********************************************COMBAT***********************************************************/
    /**
     * Muestra la lista de equipos disponibles para la simulación.
     * Imprime en consola los nombres de los equipos con un índice numerado.
     *
     * @param teamNames Una lista de cadenas que contiene los nombres de los equipos disponibles.
     */
    public void displayAvailableTeams(List<String> teamNames) {
        System.out.println("\nStarting simulation...\nLooking for available teams...\n");
        for (int i = 0; i < teamNames.size(); i++) {
            System.out.println("\t" + (i + 1) + ") " + teamNames.get(i));
        }
        System.out.println("\n");
    }

    /**
     * Solicita al usuario que elija un equipo de una lista numerada, con un índice válido dentro de un rango.
     * La entrada del usuario se valida para asegurarse de que esté dentro de los índices disponibles.
     *
     * @param maxIndex El índice máximo permitido para la selección (el número total de equipos).
     * @param team_num El número del equipo que se debe elegir (usado para el mensaje de solicitud).
     * @return El índice (basado en 0) del equipo seleccionado por el usuario.
     *         Si el usuario elige una opción inválida, el método sigue solicitando la entrada hasta que sea válida.
     */
    public int getTeamChoice(int maxIndex, int team_num) {
        System.out.print("Choose team #" + team_num + ": ");
        int choice = scanner.nextInt() - 1;
        while (choice < 0 || choice >= maxIndex) {
            System.out.println("Invalid choice. Try again.");
            System.out.print("Choose team #" + team_num + ": ");
            choice = scanner.nextInt() - 1;
        }
        return choice;
    }

    /**
     * Muestra los detalles de una ronda de combate en la consola.
     * Se usa para mostrar el resultado de las acciones tomadas durante la ronda.
     *
     * @param roundDetails Detalles de la ronda, como los ataques realizados y los cambios en el estado de los miembros.
     */
    public void displayCombatRound(String roundDetails) {
        System.out.println(roundDetails);
    }

    /**
     * Muestra el mensaje final del combate, indicando qué equipo ha ganado o si ha sido un empate.
     *
     * @param winnerMessage El mensaje que indica quién es el ganador del combate o si fue un empate.
     */
    public void displayCombatWinner(String winnerMessage) {
        System.out.println(winnerMessage);
    }

}
