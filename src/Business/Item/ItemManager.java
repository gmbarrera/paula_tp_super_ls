package Business.Item;

import Persistance.JSON.ItemJSONDAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase encargada de gestionar los ítems, incluyendo la carga desde archivos JSON
 * y la búsqueda de ítems específicos.
 */
public class ItemManager {

    private final ItemJSONDAO itemDAO;

    /**
     * Constructor que inicializa el DAO para manejar operaciones de persistencia de ítems.
     */
    public ItemManager() {
        this.itemDAO = new ItemJSONDAO();
    }

    /**
     * Carga la lista completa de ítems desde un archivo JSON.
     *
     * @return Lista de ítems cargados.
     * @throws IOException Si ocurre un error al leer o parsear el archivo JSON.
     */
    public List<Item> loadItems() throws IOException {
        return itemDAO.readAndParseItems("Data/items.json");
    }

    /**
     * Obtiene los nombres de todos los ítems almacenados en un archivo JSON.
     *
     * @param filePath Ruta al archivo JSON de ítems.
     * @return Lista de nombres de ítems.
     * @throws IOException Si ocurre un error al leer o parsear el archivo JSON.
     */
    public List<String> getItemNames(String filePath) throws IOException {
        List<Item> items = itemDAO.readAndParseItems(filePath);
        List<String> itemNames = new ArrayList<>();
        for (Item item : items) {
            itemNames.add(item.getName());
        }
        return itemNames;
    }

    /**
     * Busca un ítem en la lista por su índice.
     *
     * @param index    Índice del ítem en la lista.
     * @param filePath Ruta al archivo JSON de ítems.
     * @return El ítem correspondiente al índice dado.
     * @throws IOException             Si ocurre un error al leer o parsear el archivo JSON.
     * @throws IndexOutOfBoundsException Si el índice es inválido.
     */
    public Item getItemByIndex(int index, String filePath) throws IOException {
        List<Item> items = itemDAO.readAndParseItems(filePath);
        if (index >= 0 && index < items.size()) {
            return items.get(index);
        } else {
            throw new IndexOutOfBoundsException("Invalid item index.");
        }
    }
}
