package es.torneo.gaelico.util;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import es.torneo.gaelico.dao.EquipoDAO;
import es.torneo.gaelico.dao.JugadorDAO;
import es.torneo.gaelico.dao.PartidoDAO;
import es.torneo.gaelico.model.*;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Utilidad para operaciones de E/S en CSV, JSON y flujos binarios serializados.
 */
public class FileManager {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, typeOfSrc, context) -> new JsonPrimitive(src.toString()))
            .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, typeOfT, context) -> LocalDate.parse(json.getAsString()))
            .setPrettyPrinting()
            .create();

    /**
     * Importa equipos y jugadores desde un CSV validando línea por línea.
     * Formato esperado:
     * EQUIPO;Nombre;Ciudad;YYYY-MM-DD
     * JUGADOR;Nombre;Dorsal;Posicion;Goles;Puntos;NombreEquipo
     */
    public static void importarCSV(String rutaArchivo, EquipoDAO equipoDAO, JugadorDAO jugadorDAO) throws IOException {
        List<String> lineas = Files.readAllLines(Paths.get(rutaArchivo), StandardCharsets.UTF_8);
        int numLinea = 0;
        int importados = 0;

        for (String linea : lineas) {
            numLinea++;
            if (linea.trim().isEmpty() || linea.startsWith("#")) continue;

            String[] campos = linea.split(";");
            try {
                if (campos[0].equalsIgnoreCase("EQUIPO")) {
                    if (campos.length != 4) throw new IllegalArgumentException("Número de columnas inválido para EQUIPO");
                    String nombre = campos[1].trim();
                    String ciudad = campos[2].trim();
                    LocalDate fecha = LocalDate.parse(campos[3].trim());

                    if (equipoDAO.buscarPorNombre(nombre) == null) {
                        equipoDAO.insertar(new Equipo(nombre, ciudad, fecha));
                        importados++;
                    } else {
                        System.out.println("[AVISO Línea " + numLinea + "] El equipo '" + nombre + "' ya existe. Omitido.");
                    }
                } else if (campos[0].equalsIgnoreCase("JUGADOR")) {
                    if (campos.length != 7) throw new IllegalArgumentException("Número de columnas inválido para JUGADOR");
                    String nombre = campos[1].trim();
                    int dorsal = Integer.parseInt(campos[2].trim());
                    String posicion = campos[3].trim();
                    int goles = Integer.parseInt(campos[4].trim());
                    int puntos = Integer.parseInt(campos[5].trim());
                    String nombreEquipo = campos[6].trim();

                    Equipo eq = equipoDAO.buscarPorNombre(nombreEquipo);
                    if (eq == null) {
                        throw new IllegalArgumentException("Equipo no encontrado: " + nombreEquipo);
                    }
                    jugadorDAO.insertar(new Jugador(0, nombre, dorsal, posicion, goles, puntos, eq.getId()));
                    importados++;
                } else {
                    System.err.println("[ERROR Línea " + numLinea + "] Tipo de registro desconocido: " + campos[0]);
                }
            } catch (DateTimeParseException e) {
                System.err.println("[ERROR Línea " + numLinea + "] Formato de fecha inválido (use YYYY-MM-DD): " + linea);
            } catch (NumberFormatException e) {
                System.err.println("[ERROR Línea " + numLinea + "] Error numérico en dorsal/goles/puntos: " + linea);
            } catch (Exception e) {
                System.err.println("[ERROR Línea " + numLinea + "] Registro corrupto (" + e.getMessage() + "): " + linea);
            }
        }
        System.out.println("Importación CSV finalizada. Registros válidos procesados: " + importados);
    }

    /**
     * Exporta la tabla de clasificación actual a formato JSON.
     */
    public static void exportarClasificacionJSON(String rutaArchivo, List<ClasificacionDTO> clasificacion) throws IOException {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(rutaArchivo), StandardCharsets.UTF_8)) {
            gson.toJson(clasificacion, writer);
        }
    }

    /**
     * Importa un calendario de partidos desde un fichero JSON.
     */
    public static void importarCalendarioJSON(String rutaArchivo, PartidoDAO partidoDAO) throws Exception {
        try (Reader reader = new InputStreamReader(new FileInputStream(rutaArchivo), StandardCharsets.UTF_8)) {
            Type listType = new TypeToken<List<Partido>>() {}.getType();
            List<Partido> partidos = gson.fromJson(reader, listType);
            if (partidos != null) {
                for (Partido p : partidos) {
                    partidoDAO.insertar(p);
                }
                System.out.println("Se han importado " + partidos.size() + " encuentros desde JSON.");
            }
        }
    }

    /**
     * Serializa la lista completa de partidos en un archivo binario (.dat).
     */
    public static void exportarBackupBinario(String rutaArchivo, List<Partido> partidos) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(rutaArchivo))) {
            oos.writeObject(partidos);
        }
    }

    /**
     * Deserializa la lista de partidos desde un archivo binario (.dat).
     */
    @SuppressWarnings("unchecked")
    public static List<Partido> leerBackupBinario(String rutaArchivo) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(rutaArchivo))) {
            return (List<Partido>) ois.readObject();
        }
    }
}