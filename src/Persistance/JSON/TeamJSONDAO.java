package Persistance.JSON;

import Business.Team.Member;
import Business.Team.Team;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import Persistance.TeamDAO;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Clase para manejar la lectura, creación y eliminación de equipos desde y hacia archivos JSON.
 */
public class TeamJSONDAO implements TeamDAO {

    private String filePath = "Data/teams.json";
    /**
     * Lee y analiza un archivo JSON para crear una lista de objetos Team.
     *
     * @return Una lista de objetos Team que representan los equipos leídos desde el archivo.
     */
    public List<Team> getTeams() {
        List<Team> teams = new ArrayList<>();

        try {
            // Leemos todo el contenido del archivo
            String allFileContent = new String(Files.readAllBytes(Paths.get(filePath)));
            // Convertimos el contenido en un JSONArray
            JSONArray jsonArray = new JSONArray(allFileContent);

            // Iteramos sobre el JSONArray y creamos los objetos Team
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String name = jsonObject.getString("name");
                JSONArray membersArray = jsonObject.getJSONArray("members");

                // Crear la lista de miembros
                List<Member> members = new ArrayList<>();
                for (int j = 0; j < membersArray.length(); j++) {
                    JSONObject memberObject = membersArray.getJSONObject(j);
                    int id = memberObject.getInt("id");
                    String strategy = memberObject.getString("strategy");
                    members.add(new Member(id, strategy));
                }

                teams.add(new Team(name, members));
            }
        } catch (JSONException | IOException e) {
            System.err.println("Error: could not parse file! " + e.getMessage());
        }

        return teams;
    }

    /**
     * Crea un nuevo equipo y lo guarda en el archivo JSON.
     *
     * @param team El equipo que se va a crear y guardar.
     */
    public void createTeam(Team team) {
        JSONArray jsonArray = new JSONArray();

        try {
            List<Team> existingTeams = this.getTeams(); // Leer los equipos existentes
            existingTeams.add(team);

            for (Team existingTeam : existingTeams) {
                JSONObject teamObject = new JSONObject();
                teamObject.put("name: ", existingTeam.getTeamName());

                JSONArray membersArray = new JSONArray();
                for (Member member : existingTeam.getTeamMembers()) {
                    JSONObject memberObject = new JSONObject();
                    memberObject.put("id: ", member.getMemberId());
                    memberObject.put("strategy: ", member.getStrategy());
                    membersArray.put(memberObject);
                }

                teamObject.put("members: ", membersArray);
                jsonArray.put(teamObject);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        // Guardar el archivo JSON con formato indentado
        FileWriter file = null;
        try {
            file = new FileWriter(this.filePath);
            file.write(jsonArray.toString(4));
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                file.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Team getTeam(String name) {
        return null;
    }

    /**
     * Elimina un equipo del archivo JSON según su nombre.
     *
     * @param team El nombre del equipo que se desea eliminar.
     * @return true si el equipo fue eliminado correctamente, false si no se encontró.
     * @throws IOException Si ocurre un error al leer o escribir en el archivo.
     */
    public boolean deleteTeam(Team team) {
        try {
            // Leer el contenido del archivo JSON
            String allFileContent = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONArray jsonArray = new JSONArray(allFileContent);

            // Buscar y eliminar el equipo del JSON
            boolean teamFound = false;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject teamObject = jsonArray.getJSONObject(i);
                if (teamObject.getString("name").equalsIgnoreCase(team.getTeamName())) {
                    jsonArray.remove(i);
                    teamFound = true;
                }
            }

            if (!teamFound) {
                return false; // El equipo no se encontró
            }

            // Guardar el JSON actualizado en el archivo
            try (FileWriter file = new FileWriter(filePath)) {
                file.write(jsonArray.toString(4));
            }

            return true;

        } catch (Exception e) {
            System.err.println("Error parsing JSON: " + e.getMessage());
            return false;
        }
    }
}
