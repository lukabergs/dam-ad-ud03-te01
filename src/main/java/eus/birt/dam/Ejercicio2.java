package eus.birt.dam;

import java.sql.*;
import java.util.Scanner;

public class Ejercicio2 {
    public static void main(String[] args) throws Exception {
        try {
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost/dbeventos",
                "root",
                "root"
            );
            PreparedStatement pstmt = con.prepareStatement(
                "SELECT nombre FROM asistentes WHERE dni = ?"
            );

            System.out.println("Introduce el DNI del asistente:");
            Scanner sc = new Scanner(System.in);
            String dni = sc.nextLine();

            pstmt.setString(1, dni);
            ResultSet res = pstmt.executeQuery();

            if (!res.next()) {
                System.out.println("No se ha encontrado ningún asistente con ese DNI.");
            } else {
                String nombreActual = res.getString(1);
                System.out.printf("Nombre actual: %s%n", nombreActual);
                System.out.println("Introduce el nuevo nombre (deja en blanco para no modificar):");
                String nombreNuevo = sc.nextLine();
                if (nombreNuevo.length() > 0) {
                    pstmt = con.prepareStatement("UPDATE asistentes SET nombre = ? WHERE dni = ?");
                    pstmt.setString(1, nombreNuevo);
                    pstmt.setString(2, dni);
                    pstmt. executeUpdate();
                    System.out.println("Nombre actualizado con éxito.");
                } else {
                    System.out.println("Nombre no modificado.");
                }
            }
            
            sc.close();
            res.close();
            pstmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
