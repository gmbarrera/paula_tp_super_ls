package Business.Character;

import Persistance.CharacterDAO;
import Persistance.JSON.CharacterJSONDAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase encargada de gestionar las operaciones relacionadas con los personajes.
 */
public class CharacterManager {
    private final CharacterDAO characterDAO;

    /**
     * Crea una instancia de CharacterManager e inicializa los DAOs necesarios.
     */
    public CharacterManager() {
        // Decido usar el JSON DAO
        this.characterDAO = new CharacterJSONDAO();
    }

    /**
     * Carga una lista de personajes desde un archivo JSON.
     *
     * @return una lista de objetos {@link Character}.
     * @throws IOException si ocurre un error al leer o parsear el archivo JSON.
     */
    public List<Character> loadCharacters() throws IOException {
        return characterDAO.getCharacters();
    }

    /**
     * Obtiene los nombres de todos los personajes desde un archivo JSON.
     *
     * @return una lista de nombres de personajes.
     * @throws IOException si ocurre un error al leer o parsear el archivo JSON.
     */
    public List<String> getCharacterNames() throws IOException {
        List<Character> characters = characterDAO.getCharacters();
        List<String> characterNames = new ArrayList<>();
        for (Character character : characters) {
            characterNames.add(character.getName());
        }
        return characterNames;
    }

    /**
     * Obtiene un personaje por su índice dentro de la lista de personajes en el archivo JSON.
     *
     * @param index    el índice del personaje en la lista.
     * @return el objeto {@link Character} correspondiente al índice.
     * @throws IOException               si ocurre un error al leer o parsear el archivo JSON.
     * @throws IndexOutOfBoundsException si el índice es inválido.
     */
    public Character getCharacterByIndex(int index) throws IOException {
        List<Character> characters = characterDAO.getCharacters();
        if (index >= 0 && index < characters.size()) {
            return characters.get(index);
        } else {
            throw new IndexOutOfBoundsException("Invalid character index.");
        }
    }

    /**
     * Obtiene un personaje por su nombre desde un archivo JSON.
     *
     * @param name     el nombre del personaje a buscar.
     * @return el objeto {@link Character} que coincide con el nombre, o {@code null} si no se encuentra.
     */
    public Character getCharacter(String name) {
        List<Character> characters = characterDAO.getCharacters();
        for (Character character : characters) {
            if (character.getName().equalsIgnoreCase(name)) {
                return character;
            }
        }
        return null;
    }

    /**
     * Obtiene un personaje por su ID desde un archivo JSON predefinido.
     *
     * @param id el ID del personaje a buscar.
     * @return el objeto {@link Character} que coincide con el ID, o {@code null} si no se encuentra.
     */
    public Character getCharacter(int id) {
        List<Character> characters = characterDAO.getCharacters();
        for (Character character : characters) {
            if (character.getId() == id) {
                return character;
            }
        }
        return null;
    }
}
