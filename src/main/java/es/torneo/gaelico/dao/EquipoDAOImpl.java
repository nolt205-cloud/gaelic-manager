package es.torneo.gaelico.dao;

import es.torneo.gaelico.db.DatabaseConnection;
import es.torneo.gaelico.model.Equipo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipoDAOImpl implements EquipoDAO {

    @Override
    public void insertar(Equipo equipo) throws SQLException {
        String sql = "INSERT INTO equipos (nombre, ciudad, fecha_fundacion) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, equipo.getNombre());
            ps.setString(2, equipo.getCiudad());
            ps.setDate(3, Date.valueOf(equipo.getFechaFundacion()));
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    equipo.setId(rs.getInt(1));
                }
            }
        }
    }

    @Override
    public List<Equipo> listarTodos() throws SQLException {
        List<Equipo> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, ciudad, fecha_fundacion FROM equipos ORDER BY nombre";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Equipo(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("ciudad"),
                        rs.getDate("fecha_fundacion").toLocalDate()
                ));
            }
        }
        return lista;
    }

    @Override
    public Equipo buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, nombre, ciudad, fecha_fundacion FROM equipos WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Equipo(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("ciudad"),
                            rs.getDate("fecha_fundacion").toLocalDate()
                    );
                }
            }
        }
        return null;
    }

    @Override
    public Equipo buscarPorNombre(String nombre) throws SQLException {
        String sql = "SELECT id, nombre, ciudad, fecha_fundacion FROM equipos WHERE nombre = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Equipo(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("ciudad"),
                            rs.getDate("fecha_fundacion").toLocalDate()
                    );
                }
            }
        }
        return null;
    }

    @Override
    public void actualizar(Equipo equipo) throws SQLException {
        String sql = "UPDATE equipos SET nombre = ?, ciudad = ?, fecha_fundacion = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, equipo.getNombre());
            ps.setString(2, equipo.getCiudad());
            ps.setDate(3, Date.valueOf(equipo.getFechaFundacion()));
            ps.setInt(4, equipo.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public boolean tieneDependencias(int idEquipo) throws SQLException {
        String sqlJug = "SELECT COUNT(*) FROM jugadores WHERE id_equipo = ?";
        String sqlPar = "SELECT COUNT(*) FROM partidos WHERE id_equipo_local = ? OR id_equipo_visitante = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            try (PreparedStatement ps1 = conn.prepareStatement(sqlJug)) {
                ps1.setInt(1, idEquipo);
                try (ResultSet rs = ps1.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) return true;
                }
            }
            try (PreparedStatement ps2 = conn.prepareStatement(sqlPar)) {
                ps2.setInt(1, idEquipo);
                ps2.setInt(2, idEquipo);
                try (ResultSet rs = ps2.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) return true;
                }
            }
        }
        return false;
    }

    @Override
    public void eliminar(int id, boolean cascada) throws SQLException {
        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            conn.setAutoCommit(false); // Transacción segura
            try {
                if (cascada) {
                    try (PreparedStatement psJug = conn.prepareStatement("DELETE FROM jugadores WHERE id_equipo = ?")) {
                        psJug.setInt(1, id);
                        psJug.executeUpdate();
                    }
                    try (PreparedStatement psPar = conn.prepareStatement("DELETE FROM partidos WHERE id_equipo_local = ? OR id_equipo_visitante = ?")) {
                        psPar.setInt(1, id);
                        psPar.setInt(2, id);
                        psPar.executeUpdate();
                    }
                }
                try (PreparedStatement psEq = conn.prepareStatement("DELETE FROM equipos WHERE id = ?")) {
                    psEq.setInt(1, id);
                    psEq.executeUpdate();
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