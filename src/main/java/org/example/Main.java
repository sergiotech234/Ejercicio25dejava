package org.example;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main extends Application {

    private TableView<Empleado> tableView;
    private ObservableList<Empleado> empleados = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Ejemplo JDBC con filtro");

        tableView = new TableView<>();

        // Columnas
        TableColumn<Empleado, String> nombreCol = new TableColumn<>("Nombre");
        TableColumn<Empleado, Integer> salarioCol = new TableColumn<>("Salario");

        nombreCol.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        salarioCol.setCellValueFactory(new PropertyValueFactory<>("salario"));

        tableView.getColumns().addAll(nombreCol, salarioCol);

        // 🔎 Campo de búsqueda
        TextField searchField = new TextField();
        searchField.setPromptText("Buscar empleado por nombre...");

        // Lista filtrada
        FilteredList<Empleado> filteredData = new FilteredList<>(empleados, p -> true);

        // Listener de búsqueda
        searchField.textProperty().addListener((obs, oldValue, newValue) -> {
            filteredData.setPredicate(empleado -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true; // mostrar todo
                }

                String lowerCaseFilter = newValue.toLowerCase();

                return empleado.getNombre().toLowerCase().contains(lowerCaseFilter);
            });
        });

        tableView.setItems(filteredData);

        VBox vbox = new VBox(10, searchField, tableView);
        Scene scene = new Scene(vbox, 400, 300);

        primaryStage.setScene(scene);
        primaryStage.show();

        cargarDatos();
    }

    private void cargarDatos() {
        String url = "jdbc:oracle:thin:@localhost:1521:xe";
        String user = "RIBERA";
        String password = "ribera";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT nombre, salario FROM empleado2")) {

            while (rs.next()) {
                empleados.add(new Empleado(
                        rs.getString("nombre"),
                        rs.getInt("salario")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

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
