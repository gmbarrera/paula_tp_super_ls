package Persistance;

import Business.Item.Armor;
import Business.Item.Item;
import Business.Item.Weapon;

import java.util.List;

public interface ItemDAO {
    public List<Item> getItems();
    public List<Weapon> getWeapons();
    public List<Armor> getArmors();
}
