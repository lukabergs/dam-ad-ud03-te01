package eus.birt.dam;

import java.sql.*;

public class Ejercicio1 {
    public static void main(String[] args) throws Exception {
        try (
            // Nos conectamos a la BD y creamos un Statement
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost/dbeventos",
                "root",
                "root"
            );
            Statement stmt = con.createStatement()
        ) {
            String sql = "SELECT nombre_evento AS Evento,"
                       + "COUNT(asistentes_eventos.dni) as Asistentes,"
                       + "nombre AS Ubicacion,"
                       + "direccion as Direccion "
                       + "FROM eventos JOIN ubicaciones USING (id_ubicacion) "
                       + "JOIN asistentes_eventos USING (id_evento) "
                       + "GROUP BY id_evento ORDER BY Evento DESC";

            // Ejecutamos la sentencia SQL y obtenemos los metadatos del resultado para la cabecera de nuestra tabla
            ResultSet res = stmt.executeQuery(sql);
            ResultSetMetaData md = res.getMetaData();
            System.out.printf(
                "%-32s| %-11s| %-35s| %-12s%n",
                md.getColumnLabel(1),
                md.getColumnLabel(2),
                md.getColumnLabel(3),
                md.getColumnLabel(4)
            );
            for (int i = 0; i < 115; i++) {
                System.out.print("-");
            }

            // Imprimimos en pantalla los resultados
            System.out.println();
            while (res.next()) {
                System.out.printf(
                    "%-32s| %-11s| %-35s| %-12s%n",
                    res.getString(1),
                    res.getString(2),
                    res.getString(3),
                    res.getString(4)
                );
            }
            res.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
