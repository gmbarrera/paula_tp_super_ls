package Persistance.JSON;

import Business.Item.Armor;
import Business.Item.Item;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import Business.Item.Weapon;
import Persistance.ItemDAO;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Clase para manejar la lectura, el análisis y el guardado de ítems desde y hacia archivos JSON.
 */
public class ItemJSONDAO implements ItemDAO {
    private String filepath;

    public ItemJSONDAO() {
        this.filepath = "Data/items.json";
    }

    /**
     * Lee y analiza un archivo JSON para crear una lista de objetos Item.
     *
     * @return Una lista de objetos Item que representan los ítems leídos desde el archivo.
     */
    public List<Item> getItems() {
        List<Item> items = new ArrayList<>();

        try {
            // Leemos todo el contenido del archivo
            String allFileContent = new String(Files.readAllBytes(Paths.get(this.filepath)));
            // Convertimos el contenido en un JSONArray
            JSONArray jsonArray = new JSONArray(allFileContent);

            // Iteramos sobre el JSONArray y creamos los objetos Item
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                // Extraemos los valores directamente, asumiendo que las claves existen
                int id = jsonObject.getInt("id");
                String name = jsonObject.getString("name");
                int power = jsonObject.getInt("power");
                int durability = jsonObject.getInt("durability");
                String itemClass = jsonObject.getString("class");

                // Agregamos el ítem a la lista
                if (itemClass.equals("Weapon")) {
                    items.add(new Weapon(id, name, power, durability));
                } else if (itemClass.equals("Armor")) {
                    items.add(new Armor(id, name, power, durability));
                } else {
                    System.err.println("Item class invalid " + itemClass);
                }
            }
        } catch (JSONException | IOException e) {
            System.err.println("Error: could not parse file! " + e.getMessage());
        }

        return items;
    }

    /**
     *
     * @return
     */
    public List<Weapon> getWeapons() {
        List<Weapon> weapons = new ArrayList<>();

        try {
            // Leemos todo el contenido del archivo
            String allFileContent = new String(Files.readAllBytes(Paths.get(this.filepath)));
            // Convertimos el contenido en un JSONArray
            JSONArray jsonArray = new JSONArray(allFileContent);

            // Iteramos sobre el JSONArray y creamos los objetos Item
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                // Extraemos los valores directamente, asumiendo que las claves existen
                int id = jsonObject.getInt("id");
                String name = jsonObject.getString("name");
                int power = jsonObject.getInt("power");
                int durability = jsonObject.getInt("durability");
                String itemClass = jsonObject.getString("class");

                // Agregamos el ítem a la lista
                if (itemClass.equals("Weapon")) {
                    weapons.add(new Weapon(id, name, power, durability));
                }
            }
        } catch (JSONException | IOException e) {
            System.err.println("Error: could not parse file! " + e.getMessage());
        }

        return weapons;
    }

    /**
     *
     * @return
     */
    public List<Armor> getArmors() {
        List<Armor> armors = new ArrayList<>();

        try {
            // Leemos todo el contenido del archivo
            String allFileContent = new String(Files.readAllBytes(Paths.get(this.filepath)));
            // Convertimos el contenido en un JSONArray
            JSONArray jsonArray = new JSONArray(allFileContent);

            // Iteramos sobre el JSONArray y creamos los objetos Item
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                // Extraemos los valores directamente, asumiendo que las claves existen
                int id = jsonObject.getInt("id");
                String name = jsonObject.getString("name");
                int power = jsonObject.getInt("power");
                int durability = jsonObject.getInt("durability");
                String itemClass = jsonObject.getString("class");

                // Agregamos el ítem a la lista
                if (itemClass.equals("Armor")) {
                    armors.add(new Armor(id, name, power, durability));
                }
            }
        } catch (JSONException | IOException e) {
            System.err.println("Error: could not parse file! " + e.getMessage());
        }

        return armors;
    }


//    /**
//     * Guarda la lista de ítems en un archivo JSON.
//     *
//     * @param items Lista de objetos Item que se van a guardar.
//     * @param filePath Ruta del archivo donde se guardarán los ítems.
//     * @throws IOException Si ocurre un error al escribir el archivo.
//     * @throws JSONException Si ocurre un error al crear el formato JSON.
//     */
//    public static void saveItems(List<Item> items, String filePath) throws IOException, JSONException {
//        JSONArray jsonArray = new JSONArray();
//
//        // Creamos un JSONArray a partir de la lista de ítems
//        for (Item item : items) {
//            JSONObject itemObject = new JSONObject();
//            itemObject.put("id", item.getId());
//            itemObject.put("name", item.getName());
//            itemObject.put("class", item.getItemClass());
//            itemObject.put("power", item.getPower());
//            itemObject.put("durability", item.getDurability());
//            jsonArray.put(itemObject);
//        }
//
//        // Guardamos el JSON en el archivo
//        try (FileWriter file = new FileWriter(filePath)) {
//            file.write(jsonArray.toString(4)); // Escribimos el JSON con formato indentado
//        } catch (JSONException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
