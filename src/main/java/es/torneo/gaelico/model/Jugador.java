package es.torneo.gaelico.model;

import java.io.Serializable;

/**
 * Representa un jugador inscrito en un equipo del torneo.
 */
public class Jugador implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String nombre;
    private int dorsal;
    private String posicion;
    private int golesAnotados;
    private int puntosAnotados;
    private int idEquipo;

    public Jugador() {}

    public Jugador(int id, String nombre, int dorsal, String posicion, int golesAnotados, int puntosAnotados, int idEquipo) {
        this.id = id;
        this.nombre = nombre;
        this.dorsal = dorsal;
        this.posicion = posicion;
        this.golesAnotados = golesAnotados;
        this.puntosAnotados = puntosAnotados;
        this.idEquipo = idEquipo;
    }

    public int getTotalPuntosIndividual() {
        return (golesAnotados * 3) + puntosAnotados;
    }

    public String getMarcadorFormateado() {
        return String.format("%d-%d (%d)", golesAnotados, puntosAnotados, getTotalPuntosIndividual());
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public int getDorsal() { return dorsal; }
    public void setDorsal(int dorsal) { this.dorsal = dorsal; }
    public String getPosicion() { return posicion; }
    public void setPosicion(String posicion) { this.posicion = posicion; }
    public int getGolesAnotados() { return golesAnotados; }
    public void setGolesAnotados(int golesAnotados) { this.golesAnotados = golesAnotados; }
    public int getPuntosAnotados() { return puntosAnotados; }
    public void setPuntosAnotados(int puntosAnotados) { this.puntosAnotados = puntosAnotados; }
    public int getIdEquipo() { return idEquipo; }
    public void setIdEquipo(int idEquipo) { this.idEquipo = idEquipo; }

    @Override
    public String toString() {
        return String.format("Dorsal #%d | %s (%s) - Aporte: %s", dorsal, nombre, posicion, getMarcadorFormateado());
    }
}