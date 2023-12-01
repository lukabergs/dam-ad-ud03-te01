package eus.birt.dam;

import java.sql.*;
import java.util.Scanner;

public class Ejercicio4 {
    public static void main(String[] args) throws Exception {
        try {
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost/dbeventos",
                "root",
                "root"
            );
            
            System.out.println("Lista de eventos:");
            Statement stmt = con.createStatement();
            String sql = "SELECT nombre_evento FROM eventos";
            ResultSet res = stmt.executeQuery(sql);
            int i = 1;
            while (res.next()) {
                System.out.printf("%d. %s%n", i++, res.getString(1));
            }
            System.out.println();

            System.out.println("Introduce el ID del evento para consultar la cantidad de asistentes:");
            Scanner sc = new Scanner(System.in);
            int id = sc.nextInt();

            CallableStatement cstmt = con.prepareCall("{ ? = call obtener_numero_asistentes(?) }");
            cstmt.registerOutParameter(1, Types.INTEGER);
            cstmt.setInt(2, id);
            cstmt.execute();
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
