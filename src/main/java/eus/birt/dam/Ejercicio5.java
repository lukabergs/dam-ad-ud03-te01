package eus.birt.dam;

import org.vibur.dbcp.ViburDBCPDataSource;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

public class Ejercicio5 {
    public static void main(String[] args) throws SQLException {
        // Creamos un DataSource preconfigurado según nuestro método createDataSource()
        DataSource ds = createDataSource();
        // Testeamos la conexión e imprimimos en pantalla si ésta es válida o no
        try (Connection con = ds.getConnection()) {
            System.out.println("con.isValid(0) = " + con.isValid(0));
        }
    }

    private static DataSource createDataSource() {
        // Creamos un nuevo DataSource y configuramos la URL, usuario y contraseña para la conexión
        ViburDBCPDataSource ds = new ViburDBCPDataSource();
        ds.setJdbcUrl("jdbc:hsqldb:file:C:/Data/DAMDAW/AD/UD03/ad03te01/src/main/resources/db/test");
        ds.setUsername("root");
        ds.setPassword("root");
        // Iniciamos el DataSource
        ds.start();
        return ds;
   }
}