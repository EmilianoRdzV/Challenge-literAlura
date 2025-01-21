# Proyecto Literalura

## Descripción
¡Bienvenido/a a LiterAlura! Una aplicación en consola que muestra información sobre libros y autores utilizando la API de Gutendex. Ofrece múltiples opciones para gestionar un catálogo de libros, como la búsqueda por título, el registro automático en la base de datos y la consulta de estadísticas de descargas.

## Estado del proyecto
✅ Proyecto completado

## Características
- **Búsqueda de libros por título:** Consulta y registra información del libro y su autor.
- **Listar libros registrados:** Muestra todos los libros almacenados en la base de datos.
- **Listar autores registrados:** Despliega información de autores con sus respectivos libros.
- **Filtrar autores vivos en un determinado año:** Permite consultar autores vivos en un año específico.
- **Listar libros por idioma:** Filtra libros disponibles por idioma.
- **Generar estadísticas de descargas:** Muestra datos promedio, máximo y mínimo de descargas.
- **Top 10 libros más descargados:** Presenta un ranking de los libros más populares.
- **Búsqueda de autores por nombre:** Encuentra información de autores registrados.

## Requisitos
- Java 17+
- Maven 3+
- PostgreSQL (Base de datos configurada)

## Instalación
1. Clonar el repositorio:
   ```bash
   git clone https://github.com/usuario/Literalura.git
   ```
2. Navegar al directorio del proyecto:
   ```bash
   cd Literalura
   ```
3. Configurar la base de datos en `application.properties`:
   ```properties
   spring.datasource.url=${DB_URL}
   spring.datasource.username=${DB_USER}
   spring.datasource.password=${DB_PASSWORD}
   spring.jpa.hibernate.ddl-auto=update
   ```

4. Construir el proyecto con Maven:
   ```bash
   mvn clean install
   ```

5. Ejecutar la aplicación:
   ```bash
   mvn spring-boot:run
   ```

## Uso
Al ejecutar la aplicación, aparecerá un menú con las siguientes opciones:
```
--------------
**Catálogo de libros en Literalura**
1.- Buscar libro por título
2.- Listar libros registrados
3.- Listar autores registrados
4.- Listar autores vivos en un determinado año
5.- Listar libros por idioma
6.- Estadísticas de libros por número de descargas
7.- Top 10 libros más descargados
8.- Buscar autor por nombre
0.- Salir
--------------
Elija la opción a través de su número:
```

