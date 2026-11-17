package es.torneo.gaelico.service;

import es.torneo.gaelico.dao.*;
import es.torneo.gaelico.model.*;
import es.torneo.gaelico.util.FileManager;

import java.util.List;

/**
 * Capa de servicio que encapsula la lógica de negocio y coordina DAOs y ficheros.
 */
public class TorneoService {
    private final EquipoDAO equipoDAO = new EquipoDAOImpl();
    private final JugadorDAO jugadorDAO = new JugadorDAOImpl();
    private final PartidoDAO partidoDAO = new PartidoDAOImpl();

    public EquipoDAO getEquipoDAO() { return equipoDAO; }
    public JugadorDAO getJugadorDAO() { return jugadorDAO; }
    public PartidoDAO getPartidoDAO() { return partidoDAO; }

    public void importarCSV(String ruta) throws Exception {
        FileManager.importarCSV(ruta, equipoDAO, jugadorDAO);
    }

    public void exportarClasificacionJSON(String ruta) throws Exception {
        List<ClasificacionDTO> tabla = partidoDAO.obtenerClasificacion();
        FileManager.exportarClasificacionJSON(ruta, tabla);
    }

    public void importarCalendarioJSON(String ruta) throws Exception {
        FileManager.importarCalendarioJSON(ruta, partidoDAO);
    }

    public void generarBackupBinario(String ruta) throws Exception {
        List<Partido> historial = partidoDAO.listarTodos();
        FileManager.exportarBackupBinario(ruta, historial);
    }

    public void restaurarBackupBinario(String ruta) throws Exception {
        List<Partido> historial = FileManager.leerBackupBinario(ruta);
        partidoDAO.restaurarDesdeBackup(historial);
    }
}