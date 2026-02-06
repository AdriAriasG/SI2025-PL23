1. Estructura del Proyecto (Modelo-Vista-Controlador)

  El código está organizado siguiendo el patrón MVC, que separa la lógica de datos, la  interfaz y la coordinación entre ambas:


   * Modelo (`src/main/java/giis/demo/tkrun/`):
       * CarrerasModel.java: Contiene las consultas SQL y la lógica para interactuar  
         con la base de datos.
       * CarreraEntity.java y CarreraDisplayDTO.java: Son clases que representan los  
         datos de las carreras (objetos que "transportan" la información).
   * Vista (`src/main/java/giis/demo/tkrun/CarrerasView.java`): Es la interfaz gráfica (ventanas, botones, tablas) creada con Swing.
   * Controlador (`src/main/java/giis/demo/tkrun/CarrerasController.java`): Es el     
     "pegamento". Escucha los clics en la vista y le pide al modelo los datos
     necesarios para actualizar la pantalla.

  2. La Base de Datos (SQLite)


   * Archivo: Se llama DemoDB.db y aparecerá en la carpeta raíz del proyecto una vez  
     que ejecutes el programa por primera vez.
   * Configuración: El archivo src/main/resources/application.properties indica que se usa SQLite.
   * Inicialización: Los archivos schema.sql (crea las tablas) y data.sql (inserta    
     datos de ejemplo) definen la estructura inicial.

  ---

  3. Cómo ejecutar el proyecto

  Para ver el programa en funcionamiento, sigue estos pasos:


  Paso 1: Compilar y Ejecutar
  Ejecuta la siguiente clase, el main (menú princiapl): "giis.demo.util.SwingMain"


  Paso 2: Usar la interfaz
  Se abrirá una pequeña ventana con tres botones:
   1. "Cargar Datos Iniciales para Pruebas": Clic aquí primero. Esto creará el  
      archivo DemoDB.db y le meterá datos de ejemplo.
   2. "Ejecutar giis.demo.tkrun": Abre la aplicación de gestión de carreras para que
      veas los datos en la tabla.

  ---

  4. Prueba de vista con el lector de SQLite

  Una vez hayas hecho clic en "Cargar Datos Iniciales", verás un archivo llamado    
  DemoDB.db en tu carpeta. Para probar la visualizacion de la BD:

   1. Abre `DemoDB.db` con el SQLite.
   2. Mira la tabla carreras. Deberías ver 5 filas (las que vienen de data.sql).

   (Opcional) Debería poder tambien editarse la BD desde SQLite:

   1. Haz un cambio manual desde tu lector (por ejemplo, cambia el nombre de una      
      carrera o añade una fila).
   2. En la aplicación Java, vuelve a abrir la ventana de Carreras (o pulsa el botón  
      de buscar si lo hay) y verás que los cambios se reflejan automáticamente, porque 
      el programa lee directamente de ese archivo.