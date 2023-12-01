package eus.birt.dam;

import java.sql.*;
import java.util.Scanner;

public class Ejercicio4 {
    public static void main(String[] args) throws Exception {
        try {
            // Nos conectamos a la BD, creamos un Statement, lo ejecutamos y mostramos la lista de eventos
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost/dbeventos",
                "root",
                "root"
            );
            
            System.out.println("Lista de eventos:");
            Statement stmt = con.createStatement();
            String sql = "SELECT id_evento, nombre_evento FROM eventos ORDER BY id_evento";
            ResultSet res = stmt.executeQuery(sql);
            while (res.next()) {
                System.out.printf("%d. %s%n", res.getInt(1), res.getString(2));
            }
            System.out.println();

            // Se pide al usuario el número del evento cuya cantidad de asistentes queremos consultar
            System.out.println("Introduce el ID del evento para consultar la cantidad de asistentes:");
            Scanner sc = new Scanner(System.in);
            int id = sc.nextInt();

            // Ejecutamos la función obtener_numero_asistentes almacenada en la BD
            CallableStatement cstmt = con.prepareCall("{ ? = call obtener_numero_asistentes(?) }");
            cstmt.registerOutParameter(1, Types.INTEGER);
            cstmt.setInt(2, id);
            cstmt.execute();

            // Imprimimos en pantalla el resultado
            System.out.printf(
                "El número de asistentes para el evento seleccionado es: %d%n",
                cstmt.getInt(1)
            );

            sc.close();
            res.close();
            stmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
