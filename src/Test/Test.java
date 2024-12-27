package Test;

import Business.Character.Character;
import Business.Character.CharacterManager;
import Persistance.CharacterDAO;
import Persistance.JSON.CharacterJSONDAO;

import java.io.IOException;
import java.util.List;

public class Test {
    public static void main(String[] args) throws IOException {
        CharacterDAO characterDAO = new CharacterJSONDAO();
        System.out.println(characterDAO.getCharacters());

        CharacterManager charManager = new CharacterManager();
        List<Character> chars = charManager.loadCharacters();
        System.out.println(chars);

        System.out.println(charManager.getCharacterByIndex(2));
        System.out.println(charManager.getCharacter("LS Panda"));
        System.out.println(charManager.getCharacter(68053));
        System.out.println(charManager.getCharacterNames());
    }
}
