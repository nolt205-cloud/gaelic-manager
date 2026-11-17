package es.torneo.gaelico.dao;

import es.torneo.gaelico.db.DatabaseConnection;
import es.torneo.gaelico.model.Jugador;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JugadorDAOImpl implements JugadorDAO {

    @Override
    public void insertar(Jugador jugador) throws SQLException {
        String sql = "INSERT INTO jugadores (nombre, dorsal, posicion, goles_anotados, puntos_anotados, id_equipo) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, jugador.getNombre());
            ps.setInt(2, jugador.getDorsal());
            ps.setString(3, jugador.getPosicion());
            ps.setInt(4, jugador.getGolesAnotados());
            ps.setInt(5, jugador.getPuntosAnotados());
            ps.setInt(6, jugador.getIdEquipo());
            ps.executeUpdate();
        }
    }

    @Override
    public List<Jugador> listarPorEquipo(int idEquipo) throws SQLException {
        List<Jugador> plantilla = new ArrayList<>();
        String sql = "SELECT * FROM jugadores WHERE id_equipo = ? ORDER BY dorsal";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEquipo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    plantilla.add(mapearJugador(rs));
                }
            }
        }
        return plantilla;
    }

    @Override
    public List<Jugador> obtenerMaximosAnotadores() throws SQLException {
        List<Jugador> top = new ArrayList<>();
        String sql = "SELECT * FROM jugadores ORDER BY (goles_anotados * 3 + puntos_anotados) DESC LIMIT 10";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                top.add(mapearJugador(rs));
            }
        }
        return top;
    }

    private Jugador mapearJugador(ResultSet rs) throws SQLException {
        return new Jugador(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getInt("dorsal"),
                rs.getString("posicion"),
                rs.getInt("goles_anotados"),
                rs.getInt("puntos_anotados"),
                rs.getInt("id_equipo")
        );
    }
}