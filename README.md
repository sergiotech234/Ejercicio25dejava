📊 JavaFX TableView con JDBC y Filtro de Búsqueda

Este proyecto es una aplicación de escritorio desarrollada en JavaFX que se conecta a una base de datos Oracle mediante JDBC, mostrando una lista de empleados en un TableView con funcionalidad de búsqueda en tiempo real.

🚀 Funcionalidades
📋 Mostrar empleados desde base de datos Oracle
🔎 Filtro de búsqueda dinámico por nombre
📊 Visualización en TableView
🔌 Conexión JDBC a Oracle
⚡ Actualización en tiempo real del filtro

🗄️ Base de datos
Tabla utilizada: empleado2
CREATE TABLE empleado2 (
    nombre VARCHAR2(100),
    salario NUMBER
);

El campo de búsqueda permite filtrar empleados por nombre:

Escribe en el TextField
La tabla se actualiza automáticamente
Búsqueda no sensible a mayúsculas/minúsculas
🧠 Ejemplo de uso
Nombre	Salario
Juan	1200
Ana	1500

