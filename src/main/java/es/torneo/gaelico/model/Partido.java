package es.torneo.gaelico.model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Representa un encuentro de Fútbol Gaélico con su sistema de puntuación propio (Gol=3, Punto=1).
 */
public class Partido implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private int idEquipoLocal;
    private int idEquipoVisitante;
    private String nombreLocal;
    private String nombreVisitante;
    private int golesLocal;
    private int puntosLocal;
    private int golesVisitante;
    private int puntosVisitante;
    private LocalDate fecha;

    public Partido() {}

    public Partido(int id, int idEquipoLocal, int idEquipoVisitante, int golesLocal, int puntosLocal,
                   int golesVisitante, int puntosVisitante, LocalDate fecha) {
        this.id = id;
        this.idEquipoLocal = idEquipoLocal;
        this.idEquipoVisitante = idEquipoVisitante;
        this.golesLocal = golesLocal;
        this.puntosLocal = puntosLocal;
        this.golesVisitante = golesVisitante;
        this.puntosVisitante = puntosVisitante;
        this.fecha = fecha;
    }

    public int getTotalLocal() {
        return (golesLocal * 3) + puntosLocal;
    }

    public int getTotalVisitante() {
        return (golesVisitante * 3) + puntosVisitante;
    }

    public String getMarcadorLocal() {
        return String.format("%d-%d (%d)", golesLocal, puntosLocal, getTotalLocal());
    }

    public String getMarcadorVisitante() {
        return String.format("%d-%d (%d)", golesVisitante, puntosVisitante, getTotalVisitante());
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdEquipoLocal() { return idEquipoLocal; }
    public void setIdEquipoLocal(int idEquipoLocal) { this.idEquipoLocal = idEquipoLocal; }
    public int getIdEquipoVisitante() { return idEquipoVisitante; }
    public void setIdEquipoVisitante(int idEquipoVisitante) { this.idEquipoVisitante = idEquipoVisitante; }
    public String getNombreLocal() { return nombreLocal; }
    public void setNombreLocal(String nombreLocal) { this.nombreLocal = nombreLocal; }
    public String getNombreVisitante() { return nombreVisitante; }
    public void setNombreVisitante(String nombreVisitante) { this.nombreVisitante = nombreVisitante; }
    public int getGolesLocal() { return golesLocal; }
    public void setGolesLocal(int golesLocal) { this.golesLocal = golesLocal; }
    public int getPuntosLocal() { return puntosLocal; }
    public void setPuntosLocal(int puntosLocal) { this.puntosLocal = puntosLocal; }
    public int getGolesVisitante() { return golesVisitante; }
    public void setGolesVisitante(int golesVisitante) { this.golesVisitante = golesVisitante; }
    public int getPuntosVisitante() { return puntosVisitante; }
    public void setPuntosVisitante(int puntosVisitante) { this.puntosVisitante = puntosVisitante; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    @Override
    public String toString() {
        String loc = nombreLocal != null ? nombreLocal : "ID:" + idEquipoLocal;
        String vis = nombreVisitante != null ? nombreVisitante : "ID:" + idEquipoVisitante;
        return String.format("[ID:%d | %s] %s %s vs %s %s",
                id, fecha, loc, getMarcadorLocal(), getMarcadorVisitante(), vis);
    }
}