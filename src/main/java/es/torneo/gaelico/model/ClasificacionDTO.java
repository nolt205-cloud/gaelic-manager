package es.torneo.gaelico.model;

import java.io.Serializable;

/**
 * DTO para transportar y serializar a JSON los datos de la tabla de clasificación.
 */
public class ClasificacionDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String equipo;
    private int pj;
    private int pg;
    private int pe;
    private int pp;
    private int golesFavor;
    private int puntosFavor;
    private int tanteoFavor;
    private int tanteoContra;
    private int puntosLiga;

    public ClasificacionDTO(String equipo, int pj, int pg, int pe, int pp, int golesFavor, int puntosFavor,
                            int tanteoFavor, int tanteoContra, int puntosLiga) {
        this.equipo = equipo;
        this.pj = pj;
        this.pg = pg;
        this.pe = pe;
        this.pp = pp;
        this.golesFavor = golesFavor;
        this.puntosFavor = puntosFavor;
        this.tanteoFavor = tanteoFavor;
        this.tanteoContra = tanteoContra;
        this.puntosLiga = puntosLiga;
    }

    public String getEquipo() { return equipo; }
    public int getPj() { return pj; }
    public int getPg() { return pg; }
    public int getPe() { return pe; }
    public int getPp() { return pp; }
    public int getGolesFavor() { return golesFavor; }
    public int getPuntosFavor() { return puntosFavor; }
    public int getTanteoFavor() { return tanteoFavor; }
    public int getTanteoContra() { return tanteoContra; }
    public int getPuntosLiga() { return puntosLiga; }
}