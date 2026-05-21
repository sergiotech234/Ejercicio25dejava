package org.example;

// Importación de JavaFX
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// Importación JDBC
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

// Clase principal JavaFX
public class Main extends Application {

    // TableView que mostrará los empleados
    private TableView<Empleado> tableView;

    // Lista observable (se actualiza automáticamente en la interfaz)
    private ObservableList<Empleado> empleados =
            FXCollections.observableArrayList();

    // Método que inicia la aplicación
    @Override
    public void start(Stage primaryStage) {

        // Título de la ventana
        primaryStage.setTitle(
                "Ejemplo JDBC con filtro"
        );

        // Crear tabla
        tableView = new TableView<>();

        // Columna nombre
        TableColumn<Empleado, String> nombreCol =
                new TableColumn<>("Nombre");

        // Columna salario
        TableColumn<Empleado, Integer> salarioCol =
                new TableColumn<>("Salario");

        // Relacionar columnas con getters del modelo
        nombreCol.setCellValueFactory(
                new PropertyValueFactory<>("nombre")
        );

        salarioCol.setCellValueFactory(
                new PropertyValueFactory<>("salario")
        );

        // Añadir columnas a la tabla
        tableView.getColumns().addAll(
                nombreCol,
                salarioCol
        );

        // Campo de búsqueda
        TextField searchField = new TextField();

        searchField.setPromptText(
                "Buscar empleado por nombre..."
        );

        // Lista filtrada (envuelve la lista original)
        FilteredList<Empleado> filteredData =
                new FilteredList<>(empleados, p -> true);

        // Listener: detecta cambios en el texto
        searchField.textProperty().addListener(
                (obs, oldValue, newValue) -> {

            // Definir filtro
            filteredData.setPredicate(empleado -> {

                // Si no hay texto, mostrar todo
                if (newValue == null ||
                        newValue.isEmpty()) {
                    return true;
                }

                // Convertir a minúsculas para búsqueda
                String filter =
                        newValue.toLowerCase();

                // Filtrar por nombre
                return empleado.getNombre()
                        .toLowerCase()
                        .contains(filter);
            });
        });

        // Conectar tabla con datos filtrados
        tableView.setItems(filteredData);

        // Layout principal
        VBox vbox =
                new VBox(
                        10,
                        searchField,
                        tableView
                );

        // Crear escena
        Scene scene =
                new Scene(vbox, 400, 300);

        // Mostrar ventana
        primaryStage.setScene(scene);
        primaryStage.show();

        // Cargar datos desde la base de datos
        cargarDatos();
    }

    // Método que carga datos desde Oracle
    private void cargarDatos() {

        String url =
                "jdbc:oracle:thin:@localhost:1521:xe";

        String user = "RIBERA";
        String password = "ribera";

        try (

            // Conexión a BD
            Connection conn =
                    DriverManager.getConnection(
                            url,
                            user,
                            password
                    );

            // Crear sentencia SQL
            Statement stmt =
                    conn.createStatement();

            // Ejecutar consulta
            ResultSet rs =
                    stmt.executeQuery(
                    "SELECT nombre, salario FROM empleado2"
                    )

        ) {

            // Recorrer resultados
            while (rs.next()) {

                empleados.add(
                        new Empleado(
                                rs.getString("nombre"),
                                rs.getInt("salario")
                        )
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método main
    public static void main(String[] args) {
        launch(args);
    }

    // Clase modelo Empleado
    public static class Empleado {

        private final String nombre;
        private final int salario;

        public Empleado(String nombre, int salario) {
            this.nombre = nombre;
            this.salario = salario;
        }

        public String getNombre() {
            return nombre;
        }

        public int getSalario() {
            return salario;
        }
    }
}
