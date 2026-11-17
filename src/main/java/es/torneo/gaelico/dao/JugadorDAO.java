package es.torneo.gaelico.dao;

import es.torneo.gaelico.model.Jugador;
import java.sql.SQLException;
import java.util.List;

public interface JugadorDAO {
    void insertar(Jugador jugador) throws SQLException;
    List<Jugador> listarPorEquipo(int idEquipo) throws SQLException;
    List<Jugador> obtenerMaximosAnotadores() throws SQLException;
}