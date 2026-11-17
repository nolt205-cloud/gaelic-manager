package es.torneo.gaelico.dao;

import es.torneo.gaelico.model.Equipo;
import java.sql.SQLException;
import java.util.List;

public interface EquipoDAO {
    void insertar(Equipo equipo) throws SQLException;
    List<Equipo> listarTodos() throws SQLException;
    Equipo buscarPorId(int id) throws SQLException;
    Equipo buscarPorNombre(String nombre) throws SQLException;
    void actualizar(Equipo equipo) throws SQLException;
    boolean tieneDependencias(int idEquipo) throws SQLException;
    void eliminar(int id, boolean cascada) throws SQLException;
}