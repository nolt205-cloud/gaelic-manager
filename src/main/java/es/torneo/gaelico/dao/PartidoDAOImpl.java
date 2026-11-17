package es.torneo.gaelico.dao;

import es.torneo.gaelico.db.DatabaseConnection;
import es.torneo.gaelico.model.ClasificacionDTO;
import es.torneo.gaelico.model.Partido;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PartidoDAOImpl implements PartidoDAO {

    @Override
    public void insertar(Partido p) throws SQLException {
        String sql = "INSERT INTO partidos (id_equipo_local, id_equipo_visitante, goles_local, puntos_local, goles_visitante, puntos_visitante, fecha) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, p.getIdEquipoLocal());
            ps.setInt(2, p.getIdEquipoVisitante());
            ps.setInt(3, p.getGolesLocal());
            ps.setInt(4, p.getPuntosLocal());
            ps.setInt(5, p.getGolesVisitante());
            ps.setInt(6, p.getPuntosVisitante());
            ps.setDate(7, Date.valueOf(p.getFecha()));
            ps.executeUpdate();
        }
    }

    @Override
    public List<Partido> listarTodos() throws SQLException {
        List<Partido> lista = new ArrayList<>();
        String sql = "SELECT p.*, el.nombre AS nom_loc, ev.nombre AS nom_vis " +
                     "FROM partidos p " +
                     "JOIN equipos el ON p.id_equipo_local = el.id " +
                     "JOIN equipos ev ON p.id_equipo_visitante = ev.id " +
                     "ORDER BY p.fecha DESC";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Partido p = new Partido(
                        rs.getInt("id"),
                        rs.getInt("id_equipo_local"),
                        rs.getInt("id_equipo_visitante"),
                        rs.getInt("goles_local"),
                        rs.getInt("puntos_local"),
                        rs.getInt("goles_visitante"),
                        rs.getInt("puntos_visitante"),
                        rs.getDate("fecha").toLocalDate()
                );
                p.setNombreLocal(rs.getString("nom_loc"));
                p.setNombreVisitante(rs.getString("nom_vis"));
                lista.add(p);
            }
        }
        return lista;
    }

    @Override
    public void actualizarResultado(int id, int golesLoc, int ptsLoc, int golesVis, int ptsVis) throws SQLException {
        String sql = "UPDATE partidos SET goles_local = ?, puntos_local = ?, goles_visitante = ?, puntos_visitante = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, golesLoc);
            ps.setInt(2, ptsLoc);
            ps.setInt(3, golesVis);
            ps.setInt(4, ptsVis);
            ps.setInt(5, id);
            ps.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM partidos WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public List<ClasificacionDTO> obtenerClasificacion() throws SQLException {
        List<ClasificacionDTO> tabla = new ArrayList<>();
        String sql = "SELECT * FROM vista_clasificacion";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                tabla.add(new ClasificacionDTO(
                        rs.getString("equipo"),
                        rs.getInt("pj"),
                        rs.getInt("pg"),
                        rs.getInt("pe"),
                        rs.getInt("pp"),
                        rs.getInt("goles_favor"),
                        rs.getInt("puntos_favor"),
                        rs.getInt("tanteo_favor"),
                        rs.getInt("tanteo_contra"),
                        rs.getInt("puntos_liga")
                ));
            }
        }
        return tabla;
    }

    @Override
    public void restaurarDesdeBackup(List<Partido> partidos) throws SQLException {
        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            conn.setAutoCommit(false);
            try (Statement st = conn.createStatement()) {
                st.executeUpdate("DELETE FROM partidos");
                String sql = "INSERT INTO partidos (id_equipo_local, id_equipo_visitante, goles_local, puntos_local, goles_visitante, puntos_visitante, fecha) VALUES (?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    for (Partido p : partidos) {
                        ps.setInt(1, p.getIdEquipoLocal());
                        ps.setInt(2, p.getIdEquipoVisitante());
                        ps.setInt(3, p.getGolesLocal());
                        ps.setInt(4, p.getPuntosLocal());
                        ps.setInt(5, p.getGolesVisitante());
                        ps.setInt(6, p.getPuntosVisitante());
                        ps.setDate(7, Date.valueOf(p.getFecha()));
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
}