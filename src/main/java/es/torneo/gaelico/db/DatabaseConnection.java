package es.torneo.gaelico.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestor Singleton de conexión JDBC a MySQL.
 */
public class DatabaseConnection {
	private static final String URL = "jdbc:mysql://bmvy742e2e77tut0btgw-mysql.services.clever-cloud.com:21623/bmvy742e2e77tut0btgw?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
	private static final String USER = "uu05yii37rd0netq";
	private static final String PASSWORD = "lUPXuDNO5VQyPZjDGuq";
    private static DatabaseConnection instance;

    private DatabaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Error cargando el driver JDBC de MySQL", e);
        }
    }

    /**
     * Obtiene la instancia única del gestor de base de datos.
     * @return Instancia Singleton de DatabaseConnection.
     */
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    /**
     * Abre una nueva conexión hacia MySQL para ser utilizada con try-with-resources.
     * @return Objeto Connection activo.
     * @throws SQLException Si ocurre un error de acceso o red con MySQL.
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}