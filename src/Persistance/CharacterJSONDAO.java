package Persistance;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Business.Character.Character;

/**
 * Clase para manejar la lectura y el análisis de los personajes desde un archivo JSON.
 */
public class CharacterJSONDAO implements CharacterDAO {

    private String filePath = "Data/characters.json";
    /**
     * Lee y analiza el archivo JSON de personajes para crear una lista de objetos Character.
     *
     * @return Una lista de objetos Character que representa los personajes leídos desde el archivo.
     */
    public List<Character> getCharacters() {
        List<Character> characters = new ArrayList<>();

        try {
            // Leemos todo el contenido del archivo
            String allFileContent = new String(Files.readAllBytes(Paths.get(filePath)));
            // Convertimos el contenido en un JSONArray
            JSONArray jsonArray = new JSONArray(allFileContent);

            // Iteramos sobre el JSONArray y creamos los objetos Character
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                int id = jsonObject.getInt("id");
                String name = jsonObject.getString("name");
                int weight = jsonObject.getInt("weight");

                // Agregamos el personaje a la lista
                characters.add(new Character(id, name, weight));
            }

        } catch (JSONException | IOException e) {
            System.err.println("Error: could not parse file! " + e.getMessage());
        }

        return characters;
    }
}
