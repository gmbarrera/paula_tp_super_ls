package Business.Combat;

import Business.Character.Character;
import Business.Character.CharacterManager;
import Business.Item.Item;
import Business.Stats.Stats;
import Business.Stats.StatsManager;
import Business.Team.Member;
import Business.Team.Team;
import Business.Team.TeamManager;
import Persistance.JSON.CharacterJSONDAO;
import Persistance.JSON.TeamJSONDAO;
import Presentation.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Clase encargada de gestionar el combate y llevar a cabo las operaciones necesarias
 */

public class CombatManager {

    // *** ATRIBUTOS ***
    private StatsManager statsManager;
    private TeamManager teamManager;
    private CharacterManager characterManager;
    private TeamJSONDAO teamDAO;
    private CharacterJSONDAO characterDAO;

    /**
     * Constructor de la clase CombatManager. Inicializa los gestores de estadísticas, equipos, personajes e ítems.
     */
    public CombatManager() {
        this.statsManager = new StatsManager();
        this.teamDAO = new TeamJSONDAO();
        this.characterDAO = new CharacterJSONDAO();
        this.teamManager = new TeamManager();
        this.characterManager = new CharacterManager();
    }

    /**
     * Inicializa los equipos con ítems y personajes asignados.
     * @param team1 El primer equipo.
     * @param team2 El segundo equipo.
     * @param items Lista de ítems disponibles.
     * @param characters Lista de personajes disponibles.
     */
    public void initializeTeams(Team team1, Team team2, List<Item> items, List<Character> characters) throws IOException {
        this.teamManager = new TeamManager();
        System.out.println("\nInitializing teams...\n");

        System.out.println("Team #1 – " + team1.getTeamName());
        List<String> team1Details = assignItemsForCombat(team1, items, characters);
        team1Details.forEach(System.out::println);

        System.out.println("\nTeam #2 – " + team2.getTeamName());
        List<String> team2Details = assignItemsForCombat(team2, items, characters);
        team2Details.forEach(System.out::println);

        System.out.println("\nCombat ready!\n<Press any key to continue...>");
        new Scanner(System.in).nextLine();

        Stats statsTeam1 = statsManager.getStatsByTeamName(team1.getTeamName());
        Stats statsTeam2 = statsManager.getStatsByTeamName(team2.getTeamName());

        Combat combat = new Combat(team1, team2, statsTeam1, statsTeam2); // Adjust as necessary
        executeCombat(combat);
    }

    /**
     * Asigna ítems aleatorios a los miembros de un equipo para el combate.
     * @param team El equipo a asignar ítems.
     * @param availableItems Los ítems disponibles para asignar.
     * @param characters Los personajes disponibles para asignar ítems.
     * @return Una lista de detalles sobre los ítems asignados.
     */
    public List<String> assignItemsForCombat(Team team, List<Item> availableItems, List<Character> characters) {
        List<String> assignedItemsDetails = new ArrayList<>();
        Random random = new Random();
        Character character = null;

        for (Member member : team.getTeamMembers()) {
            for (Character c : characters) {
                if (c.getId() == member.getMemberId()) {
                    character = c;
                    break;
                }
            }

            // Initialize weapon and armor to null
            Item weapon = null;
            Item armor = null;

            // Randomly assign one weapon and one armor
            boolean weaponAssigned = false;
            boolean armorAssigned = false;

            while (!weaponAssigned || !armorAssigned || weapon == null) {
                Item item = availableItems.get(random.nextInt(availableItems.size()));
                if (item.isWeapon() && !weaponAssigned && character != null) { // Check if the item is a weapon
                    weapon = item; // Assign as weapon
                    weaponAssigned = true; // Mark weapon as assigned
                    character.setWeapon(weapon);
                }
                if (item.isArmor() && !armorAssigned && character != null) { // Check if the item is armor
                    armor = item; // Assign as armor
                    armorAssigned = true; // Mark armor as assigned
                    character.setArmor(armor);
                }
            }

            // If no armor was assigned, create a default armor item
            if (armor == null) {
                armor = new Item(0, "No armor", "Armor", 0, 0); // Create a default armor item with zero value
                character.setArmor(armor);
            }
            if (weapon == null) {
                weapon = new Item(0, "No weapon", "Weapon", 0, 0); // Create a default weapon item with zero value
                character.setWeapon(weapon);
            }

            // Format details
            String itemDetail = "\t- " + character.getName() + "\n\t\tWeapon: " + character.getWeapon().getName() + "\n\t\tArmor: " + character.getArmor().getName() + "\n";
            assignedItemsDetails.add(itemDetail);
        }

        return assignedItemsDetails;
    }


    /**
     * Ejecuta la simulación del combate entre los dos equipos.
     * @param combat El objeto Combat que contiene los datos del combate.
     */
    public void executeCombat(Combat combat) {
        int round = 1;
        while (true) {
            System.out.println("--- ROUND " + round + " ---\n");

            // Execute attacks
            for (Member attacker : combat.getTeam1().getTeamMembers()) {
                if (!attacker.isKnockedOut()) {
                    Member target = selectTarget(combat.getTeam2());
                    Character c = characterManager.getCharacter(target.getMemberId());
                    System.out.println(c.getName() + c.getWeapon().getName());
                    executeAttack(attacker, target, combat);
                }
            }

            for (Member attacker : combat.getTeam2().getTeamMembers()) {
                if (!attacker.isKnockedOut()) {
                    Member target = selectTarget(combat.getTeam1());
                    executeAttack(attacker, target, combat);
                }
            }

            // Check for knocked out members and break if a team is eliminated
            if (isTeamEliminated(combat.getTeam1()) || isTeamEliminated(combat.getTeam2())) {
                break;
            }

            round++;
        }
    }

    // Consolidated method to check if a member is knocked out
    public boolean isMemberKnockedOut(Member member) {
        Character character = characterManager.getCharacter(member.getMemberId());
        if (character == null) {
            return false; // If character is not found, return false
        }

        // Check if accumulated damage is 100% or more
        if (character.getAccumulatedDamage() >= 1.0) {
            member.setKnockedOut(true); // Mark the member as knocked out
            System.out.println(character.getName() + " has been knocked out! ");
            return true; // Member is knocked out
        } else {
            int knockoutValue = new Random().nextInt(200) + 1; // Random value between 1 and 200
            if (knockoutValue > character.getAccumulatedDamage()) {
                member.setKnockedOut(true); // Mark the member as knocked out
                System.out.println(character.getName() + " has been knocked out! ");
                return true; // Member is knocked out
            }
        }
        return false; // Member is not knocked out
    }

    // Update the method calls in the executeAttack method
    private void executeAttack(Member attacker_aux, Member target_aux, Combat combat) {
        Character attacker = characterManager.getCharacter(attacker_aux.getMemberId());
        Character target = characterManager.getCharacter(target_aux.getMemberId());
        //System.out.println(attacker.getName() + "fights " + target.getName() + "with " + attacker.getWeapon());

        // Check if the attacker and target are valid
        if (attacker == null || target == null) {
            System.out.println("Attacker or target not found! ");
            return; // Exit if either is null
        }

        // Calculate attack value
        double attackValue = calculateAttack(attacker);

        // Calculate final damage
        double finalDamage = calculateFinalDamage(attackValue, target);

        // Update accumulated damage on the target
        target.takeDamage(finalDamage); // This updates the accumulated damage as a percentage
        System.out.println(attacker.getName() + " ATTACKS " + target.getName() + " WITH " + attacker.getWeapon().getName() + " FOR " + attackValue + " DAMAGE! ");
        System.out.println("\t" + target.getName() + " RECEIVES " + finalDamage + " DAMAGE. ");

        // Update weapon and armor durability
        updateDurability(attacker.getWeapon());
        updateDurability(target.getArmor());

        // Check for knockouts after all attacks
        isMemberKnockedOut(target_aux); // Call the consolidated method
    }

    private double calculateAttack(Character attacker) {
        double weightAttacker = attacker.getWeight(); // Assuming you have a method to get weight
        double damageAttacker = attacker.getAccumulatedDamage(); // Assuming you have a method to get accumulated damage

        Item weapon = attacker.getWeapon();
        double weaponAttack = weapon.getPower();

        return (weightAttacker * (1 - damageAttacker) / 10) + (weaponAttack / 20) + 18;
    }

    private double calculateFinalDamage(double attack, Character defender) {
        double damageDefender = defender.getAccumulatedDamage(); // Assuming you have a method to get accumulated damage
        double weightDefender = defender.getWeight(); // Assuming you have a method to get weight
        double armorValue = defender.getArmor() != null ? defender.getArmor().getPower() : 0; // Assuming you have a method to get armor

        return attack - ((200 * (1 - damageDefender) / weightDefender + (armorValue / 20)) * 1.4);
    }

    private void updateDurability(Item item) {
        if (item != null) {
            item.decreaseDurability(); // Assuming you have a method to decrease durability
            if (item.getDurability() <= 0) {
                System.out.println(item.getName() + " is destroyed!");
            }
        }
    }

    private void checkForKnockout(Character target) {
        int knockoutValue = new Random().nextInt(200) + 1; // Random value between 1 and 200
        if (target.getAccumulatedDamage() >= 1.0) { // Check if accumulated damage is 100% or more
            System.out.println(target.getName() + " has been knocked out!");
        }

        if (knockoutValue > target.getAccumulatedDamage()) {
            System.out.println(target.getName() + " has been knocked out!");

            // Additional logic to mark the character as knocked out
        }
    }

    /**
     * Determina si un equipo ha sido eliminado del combate.
     * @param team El equipo a verificar.
     * @return true si el equipo ha sido eliminado, false en caso contrario.
     */
    private boolean isTeamEliminated(Team team) {
        for (Member member : team.getTeamMembers()) {
            Character character = characterManager.getCharacter(member.getMemberId());
            if (!character.isEliminated()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Declara el equipo ganador al finalizar el combate.
     * @param team1 El primer equipo.
     * @param team2 El segundo equipo.
     */
    private void declareWinner(Team team1, Team team2) {
        try {
            // Mostrar resumen de ambos equipos
            System.out.println("Team 1 Summary:");
            List<String> team1Summary = teamManager.getTeamSummary(team1);
            team1Summary.forEach(System.out::println);

            System.out.println("\nTeam 2 Summary:");
            List<String> team2Summary = teamManager.getTeamSummary(team2);
            team2Summary.forEach(System.out::println);

            // Determinar ganador y mostrar resultado
            if (isTeamEliminated(team1) && isTeamEliminated(team2)) {
                System.out.println("\nBoth teams are eliminated. It's a draw!");
            } else if (isTeamEliminated(team1)) {
                System.out.println("\nTeam " + team2.getTeamName() + " wins!");
                statsManager.incrementGamesWon(statsManager.getStatsByTeamName(team2.getTeamName()));
            } else if (isTeamEliminated(team2)) {
                System.out.println("\nTeam " + team1.getTeamName() + " wins!");
                statsManager.incrementGamesWon(statsManager.getStatsByTeamName(team1.getTeamName()));
            } else {
                System.out.println("\nIt's a draw!");
            }

            // Actualizar estadísticas
            statsManager.incrementGamesPlayed(statsManager.getStatsByTeamName(team1.getTeamName()));
            statsManager.incrementGamesPlayed(statsManager.getStatsByTeamName(team2.getTeamName()));

            // Acumular K.O.s
            statsManager.accumulateKOs(statsManager.getStatsByTeamName(team1.getTeamName()), calculateKOsDone(team1), calculateKOsReceived(team1));
            statsManager.accumulateKOs(statsManager.getStatsByTeamName(team2.getTeamName()), calculateKOsDone(team2), calculateKOsReceived(team2));

            // Falta añadir las nuevas estadísticas al equipo

        } catch (IOException e) {
            System.err.println("Error while updating stats: " + e.getMessage());
        }
        Controller controller = new Controller();

        controller.pressEnterKeyToContinue();
    }

    /**
     * Actualiza el daño recibido por un miembro del equipo.
     * @param member El miembro defensor.
     * @param damage El daño recibido por el defensor.
     */
    private void updateMemberDamage(Member member, double damage) {
        teamManager.updateMemberDamage(member, damage);
    }


    /**
     * Calcula el número de K.O.s realizados por un equipo.
     * @param team El equipo a verificar.
     * @return El número de K.O.s realizados.
     */
    private int calculateKOsDone(Team team) {
        int kos = 0;
        for (Member member : team.getTeamMembers()) {
            if (isMemberKnockedOut(member)) {
                kos++;
            }
        }
        return kos;
    }

    /**
     * Calcula el número de K.O.s recibidos por un equipo.
     * @param team El equipo a verificar.
     * @return El número de K.O.s recibidos.
     */
    private int calculateKOsReceived(Team team) {
        int kos = 0;
        for (Member member : team.getTeamMembers()) {
            if (isMemberKnockedOut(member)) {
                kos++;
            }
        }
        return kos;
    }

    private Member selectTarget(Team team) {
        List<Member> members = team.getTeamMembers();
        Random random = new Random();
        // Select a random member from the opposing team
        return members.get(random.nextInt(members.size()));
    }
}
