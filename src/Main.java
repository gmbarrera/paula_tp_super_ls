import Presentation.Controller;

/**
 * Clase principal del programa.
 * Contiene el método main, que es el punto de entrada de la aplicación.
 */
public class Main {

    /**
     * Método principal que inicia la ejecución del programa.
     * Crea una instancia de {@link Controller} y llama al método {@code startProgram()}
     * para iniciar el flujo del programa.
     *
     * @param args Argumentos de línea de comandos (no se utilizan en esta aplicación).
     */
    public static void main(String[] args) {
        Controller controller = new Controller();
        controller.startProgram();
    }
}
