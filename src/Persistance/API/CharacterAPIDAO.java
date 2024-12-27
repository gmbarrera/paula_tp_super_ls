package Persistance.API;

import Business.Character.Character;
import Persistance.CharacterDAO;

import java.util.List;

public class CharacterAPIDAO implements CharacterDAO {

    public List<Character> getCharacters() {
        return List.of(new Character(1,"uno", 3));
    }
}
