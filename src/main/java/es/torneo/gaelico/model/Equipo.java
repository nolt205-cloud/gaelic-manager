package es.torneo.gaelico.model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Representa un club participante en el torneo de Fútbol Gaélico.
 */
public class Equipo implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String nombre;
    private String ciudad;
    private LocalDate fechaFundacion;

    public Equipo() {}

    public Equipo(int id, String nombre, String ciudad, LocalDate fechaFundacion) {
        this.id = id;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.fechaFundacion = fechaFundacion;
    }

    public Equipo(String nombre, String ciudad, LocalDate fechaFundacion) {
        this(0, nombre, ciudad, fechaFundacion);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    public LocalDate getFechaFundacion() { return fechaFundacion; }
    public void setFechaFundacion(LocalDate fechaFundacion) { this.fechaFundacion = fechaFundacion; }

    @Override
    public String toString() {
        return String.format("[%d] %s (%s) - Fundado: %s", id, nombre, ciudad, fechaFundacion);
    }
}