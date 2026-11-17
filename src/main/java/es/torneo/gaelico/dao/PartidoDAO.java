package es.torneo.gaelico.dao;

import es.torneo.gaelico.model.ClasificacionDTO;
import es.torneo.gaelico.model.Partido;
import java.sql.SQLException;
import java.util.List;

public interface PartidoDAO {
    void insertar(Partido partido) throws SQLException;
    List<Partido> listarTodos() throws SQLException;
    void actualizarResultado(int id, int golesLoc, int ptsLoc, int golesVis, int ptsVis) throws SQLException;
    void eliminar(int id) throws SQLException;
    List<ClasificacionDTO> obtenerClasificacion() throws SQLException;
    void restaurarDesdeBackup(List<Partido> partidos) throws SQLException;
}