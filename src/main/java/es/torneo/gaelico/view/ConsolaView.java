package es.torneo.gaelico.view;

import es.torneo.gaelico.model.*;
import es.torneo.gaelico.service.TorneoService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * Interfaz de usuario por consola con validación rigurosa de entradas.
 */
public class ConsolaView {
    private final Scanner scanner = new Scanner(System.in);
    private final TorneoService service = new TorneoService();

    public void iniciar() {
        int opcion;
        do {
            System.out.println("\n=======================================================");
            System.out.println("     SISTEMA DE GESTIÓN: COPA DE FÚTBOL GAÉLICO");
            System.out.println("=======================================================");
            System.out.println("1. Gestión de Equipos y Jugadores (CRUD)");
            System.out.println("2. Gestión de Encuentros y Actas");
            System.out.println("3. Clasificación y Estadísticas");
            System.out.println("4. Importación y Exportación de Ficheros");
            System.out.println("5. Copias de Seguridad (Historial Binario)");
            System.out.println("0. Salir de la aplicación");
            opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1 -> menuEquiposJugadores();
                case 2 -> menuEncuentros();
                case 3 -> menuClasificacion();
                case 4 -> menuFicheros();
                case 5 -> menuBackup();
                case 0 -> System.out.println("Cerrando Gaelic Manager. ¡Slán!");
                default -> System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    private void menuEquiposJugadores() {
        int op;
        do {
            System.out.println("\n--- GESTIÓN DE EQUIPOS Y JUGADORES ---");
            System.out.println("1.1 (1). Listar todos los equipos");
            System.out.println("1.2 (2). Dar de alta un nuevo equipo");
            System.out.println("1.3 (3). Modificar datos de un equipo");
            System.out.println("1.4 (4). Eliminar un equipo");
            System.out.println("1.5 (5). Registrar jugador en un equipo");
            System.out.println("1.6 (6). Listar plantilla de un equipo");
            System.out.println("1.7 (7). Volver al menú principal");
            op = leerEntero("Seleccione subopción (1-7): ");

            try {
                switch (op) {
                    case 1 -> {
                        List<Equipo> equipos = service.getEquipoDAO().listarTodos();
                        if (equipos.isEmpty()) System.out.println("No hay equipos registrados.");
                        else equipos.forEach(System.out::println);
                    }
                    case 2 -> {
                        System.out.print("Nombre del equipo: ");
                        String nombre = scanner.nextLine().trim();
                        System.out.print("Ciudad: ");
                        String ciudad = scanner.nextLine().trim();
                        LocalDate fecha = leerFecha("Fecha de fundación (YYYY-MM-DD): ");
                        service.getEquipoDAO().insertar(new Equipo(nombre, ciudad, fecha));
                        System.out.println("Equipo registrado con éxito.");
                    }
                    case 3 -> {
                        int id = leerEntero("ID del equipo a modificar: ");
                        Equipo eq = service.getEquipoDAO().buscarPorId(id);
                        if (eq == null) {
                            System.out.println("Equipo no encontrado.");
                            break;
                        }
                        System.out.print("Nuevo nombre (" + eq.getNombre() + "): ");
                        String nom = scanner.nextLine().trim();
                        if (!nom.isEmpty()) eq.setNombre(nom);
                        System.out.print("Nueva ciudad (" + eq.getCiudad() + "): ");
                        String ciu = scanner.nextLine().trim();
                        if (!ciu.isEmpty()) eq.setCiudad(ciu);
                        service.getEquipoDAO().actualizar(eq);
                        System.out.println("Equipo actualizado.");
                    }
                    case 4 -> {
                        int id = leerEntero("ID del equipo a eliminar: ");
                        if (service.getEquipoDAO().tieneDependencias(id)) {
                            System.out.print("ATENCIÓN: El equipo tiene jugadores o partidos asociados. ¿Eliminar en cascada? (S/N): ");
                            String resp = scanner.nextLine().trim();
                            if (resp.equalsIgnoreCase("S")) {
                                service.getEquipoDAO().eliminar(id, true);
                                System.out.println("Equipo y registros asociados eliminados.");
                            } else {
                                System.out.println("Operación cancelada para preservar integridad referencial.");
                            }
                        } else {
                            service.getEquipoDAO().eliminar(id, false);
                            System.out.println("Equipo eliminado.");
                        }
                    }
                    case 5 -> {
                        int idEq = leerEntero("ID del equipo: ");
                        if (service.getEquipoDAO().buscarPorId(idEq) == null) {
                            System.out.println("El equipo indicado no existe.");
                            break;
                        }
                        System.out.print("Nombre del jugador: ");
                        String nombre = scanner.nextLine().trim();
                        int dorsal = leerEntero("Dorsal: ");
                        System.out.print("Posición (Portero/Defensa/Medio/Delantero): ");
                        String pos = scanner.nextLine().trim();
                        int goles = leerEntero("Goles anotados hasta la fecha: ");
                        int puntos = leerEntero("Puntos (over-the-bar) anotados: ");
                        service.getJugadorDAO().insertar(new Jugador(0, nombre, dorsal, pos, goles, puntos, idEq));
                        System.out.println("Jugador inscrito correctamente.");
                    }
                    case 6 -> {
                        int idEq = leerEntero("ID del equipo: ");
                        List<Jugador> plantilla = service.getJugadorDAO().listarPorEquipo(idEq);
                        if (plantilla.isEmpty()) System.out.println("Plantilla vacía o equipo inexistente.");
                        else plantilla.forEach(System.out::println);
                    }
                }
            } catch (SQLException e) {
                System.err.println("Error de base de datos: " + e.getMessage());
            }
        } while (op != 7);
    }

    private void menuEncuentros() {
        int op;
        do {
            System.out.println("\n--- GESTIÓN DE ENCUENTROS ---");
            System.out.println("2.1 (1). Registrar acta de partido (goles, puntos locales y visitantes)");
            System.out.println("2.2 (2). Listar historial de partidos disputados");
            System.out.println("2.3 (3). Modificar resultado de un partido");
            System.out.println("2.4 (4). Eliminar registro de un partido");
            System.out.println("2.5 (5). Volver al menú principal");
            op = leerEntero("Seleccione subopción (1-5): ");

            try {
                switch (op) {
                    case 1 -> {
                        int idLoc = leerEntero("ID Equipo Local: ");
                        int idVis = leerEntero("ID Equipo Visitante: ");
                        if (idLoc == idVis) {
                            System.out.println("Un equipo no puede jugar contra sí mismo.");
                            break;
                        }
                        int gLoc = leerEntero("Goles Local (valen 3 pts): ");
                        int pLoc = leerEntero("Puntos Local (valen 1 pt): ");
                        int gVis = leerEntero("Goles Visitante (valen 3 pts): ");
                        int pVis = leerEntero("Puntos Visitante (valen 1 pt): ");
                        LocalDate fecha = leerFecha("Fecha del encuentro (YYYY-MM-DD): ");
                        service.getPartidoDAO().insertar(new Partido(0, idLoc, idVis, gLoc, pLoc, gVis, pVis, fecha));
                        System.out.println("Acta de partido registrada.");
                    }
                    case 2 -> {
                        List<Partido> partidos = service.getPartidoDAO().listarTodos();
                        if (partidos.isEmpty()) System.out.println("No hay partidos registrados.");
                        else partidos.forEach(System.out::println);
                    }
                    case 3 -> {
                        int id = leerEntero("ID del partido a modificar: ");
                        int gLoc = leerEntero("Nuevos Goles Local: ");
                        int pLoc = leerEntero("Nuevos Puntos Local: ");
                        int gVis = leerEntero("Nuevos Goles Visitante: ");
                        int pVis = leerEntero("Nuevos Puntos Visitante: ");
                        service.getPartidoDAO().actualizarResultado(id, gLoc, pLoc, gVis, pVis);
                        System.out.println("Acta actualizada.");
                    }
                    case 4 -> {
                        int id = leerEntero("ID del partido a eliminar: ");
                        service.getPartidoDAO().eliminar(id);
                        System.out.println("Partido eliminado.");
                    }
                }
            } catch (SQLException e) {
                System.err.println("Error SQL en encuentros: " + e.getMessage());
            }
        } while (op != 5);
    }

    private void menuClasificacion() {
        int op;
        do {
            System.out.println("\n--- CLASIFICACIÓN Y ESTADÍSTICAS ---");
            System.out.println("3.1 (1). Mostrar tabla de clasificación en consola");
            System.out.println("3.2 (2). Ver desglose de puntuaciones (goles vs puntos over-the-bar)");
            System.out.println("3.3 (3). Máximos anotadores del torneo");
            System.out.println("3.4 (4). Volver al menú principal");
            op = leerEntero("Seleccione subopción (1-4): ");

            try {
                switch (op) {
                    case 1 -> {
                        List<ClasificacionDTO> tabla = service.getPartidoDAO().obtenerClasificacion();
                        System.out.printf("%-22s | %2s | %2s | %2s | %2s | %4s | %4s | %4s | %3s%n",
                                "EQUIPO", "PJ", "PG", "PE", "PP", "TF", "TC", "DIF", "PTS");
                        System.out.println("-----------------------------------------------------------------------");
                        for (ClasificacionDTO c : tabla) {
                            int dif = c.getTanteoFavor() - c.getTanteoContra();
                            System.out.printf("%-22s | %2d | %2d | %2d | %2d | %4d | %4d | %+4d | %3d%n",
                                    c.getEquipo(), c.getPj(), c.getPg(), c.getPe(), c.getPp(),
                                    c.getTanteoFavor(), c.getTanteoContra(), dif, c.getPuntosLiga());
                        }
                    }
                    case 2 -> {
                        List<ClasificacionDTO> tabla = service.getPartidoDAO().obtenerClasificacion();
                        System.out.printf("%-22s | %6s | %6s | %15s%n", "EQUIPO", "GOLES", "PUNTOS", "MARCADOR GLOBAL");
                        System.out.println("-----------------------------------------------------------");
                        for (ClasificacionDTO c : tabla) {
                            String formato = String.format("%d-%d (%d)", c.getGolesFavor(), c.getPuntosFavor(), c.getTanteoFavor());
                            System.out.printf("%-22s | %6d | %6d | %15s%n",
                                    c.getEquipo(), c.getGolesFavor(), c.getPuntosFavor(), formato);
                        }
                    }
                    case 3 -> {
                        List<Jugador> top = service.getJugadorDAO().obtenerMaximosAnotadores();
                        if (top.isEmpty()) System.out.println("No hay jugadores registrados.");
                        else top.forEach(System.out::println);
                    }
                }
            } catch (SQLException e) {
                System.err.println("Error consultando estadísticas: " + e.getMessage());
            }
        } while (op != 4);
    }

    private void menuFicheros() {
        int op;
        do {
            System.out.println("\n--- IMPORTACIÓN Y EXPORTACIÓN ---");
            System.out.println("4.1 (1). Importar equipos y jugadores desde archivo CSV");
            System.out.println("4.2 (2). Exportar clasificación actual a archivo JSON");
            System.out.println("4.3 (3). Importar calendario de jornadas desde archivo JSON");
            System.out.println("4.4 (4). Volver al menú principal");
            op = leerEntero("Seleccione subopción (1-4): ");

            try {
                switch (op) {
                    case 1 -> {
                        service.importarCSV("equipos_iniciales.csv");
                    }
                    case 2 -> {
                        service.exportarClasificacionJSON("clasificacion_torneo.json");
                        System.out.println("Clasificación exportada a 'clasificacion_torneo.json'.");
                    }
                    case 3 -> {
                        service.importarCalendarioJSON("calendario.json");
                    }
                }
            } catch (Exception e) {
                System.err.println("Error en operación de ficheros: " + e.getMessage());
            }
        } while (op != 4);
    }

    private void menuBackup() {
        int op;
        do {
            System.out.println("\n--- COPIAS DE SEGURIDAD (BINARIO) ---");
            System.out.println("5.1 (1). Exportar histórico completo a archivo binario (.dat)");
            System.out.println("5.2 (2). Restaurar/leer histórico desde archivo binario (.dat)");
            System.out.println("5.3 (3). Volver al menú principal");
            op = leerEntero("Seleccione subopción (1-3): ");

            try {
                switch (op) {
                    case 1 -> {
                        service.generarBackupBinario("backup_historico.dat");
                        System.out.println("Copia de seguridad guardada en 'backup_historico.dat'.");
                    }
                    case 2 -> {
                        service.restaurarBackupBinario("backup_historico.dat");
                        System.out.println("Histórico de partidos restaurado correctamente desde binario.");
                    }
                }
            } catch (Exception e) {
                System.err.println("Error en copia de seguridad: " + e.getMessage());
            }
        } while (op != 3);
    }

    private int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, introduzca un número entero.");
            }
        }
    }

    private LocalDate leerFecha(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return LocalDate.parse(scanner.nextLine().trim());
            } catch (DateTimeParseException e) {
                System.out.println("Formato de fecha incorrecto. Use YYYY-MM-DD (ej. 2026-05-15).");
            }
        }
    }
}