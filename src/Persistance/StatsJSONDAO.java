package Persistance;

import Business.Stats.Stats;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Clase para manejar la lectura, el análisis y el guardado de estadísticas de equipos desde y hacia archivos JSON.
 */
public class StatsJSONDAO {

    /**
     * Lee y analiza un archivo JSON para crear una lista de objetos Stats.
     *
     * @param filePath Ruta del archivo JSON donde se encuentran los datos de las estadísticas.
     * @return Una lista de objetos Stats que representan las estadísticas leídas desde el archivo.
     */
    public static List<Stats> readAndParseStats(String filePath) {
        List<Stats> statsList = new ArrayList<>();

        try {
            // Leemos todo el contenido del archivo
            String allFileContent = new String(Files.readAllBytes(Paths.get(filePath)));
            // Convertimos el contenido en un JSONArray
            JSONArray jsonArray = new JSONArray(allFileContent);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String teamName = jsonObject.getString("team_name");
                int gamesPlayed = jsonObject.getInt("games_played");
                int gamesWon = jsonObject.getInt("games_won");
                int koDone = jsonObject.getInt("ko_done");
                int koReceived = jsonObject.getInt("ko_received");

                // Agregamos la estadística a la lista
                statsList.add(new Stats(teamName, gamesPlayed, gamesWon, koDone, koReceived));
            }
        } catch (JSONException | IOException e) {
            System.err.println("Error: could not parse file! " + e.getMessage());
        }

        return statsList;
    }

    /**
     * Guarda la lista de estadísticas en un archivo JSON.
     *
     * @param statsList Lista de objetos Stats que se van a guardar.
     * @param filePath Ruta del archivo donde se guardarán las estadísticas.
     * @throws IOException Si ocurre un error al escribir el archivo.
     * @throws JSONException Si ocurre un error al crear el formato JSON.
     */
    public void saveStats(List<Stats> statsList, String filePath) throws IOException, JSONException {
        JSONArray jsonArray = new JSONArray();

        // Creamos un JSONArray a partir de la lista de estadísticas
        for (Stats stats : statsList) {
            JSONObject statsObject = new JSONObject();
            statsObject.put("team_name", stats.getTeamName());
            statsObject.put("games_played", stats.getGamesPlayed());
            statsObject.put("games_won", stats.getGamesWon());
            statsObject.put("ko_done", stats.getKODone());
            statsObject.put("ko_received", stats.getKOReceived());
            jsonArray.put(statsObject);
        }

        // Guardamos el JSON en el archivo
        try (FileWriter file = new FileWriter(filePath)) {
            file.write(jsonArray.toString(4)); // Escribimos el JSON con formato indentado
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
