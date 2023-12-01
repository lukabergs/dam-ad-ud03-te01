package eus.birt.dam;

import java.sql.*;
import java.util.Scanner;

public class Ejercicio2 {
    public static void main(String[] args) throws Exception {
        try {
            // Nos conectamos a la BD y creamos un PreparedStatement
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost/dbeventos",
                "root",
                "root"
            );
            PreparedStatement pstmt = con.prepareStatement(
                "SELECT nombre FROM asistentes WHERE dni = ?"
            );

            // Pedimos al usuario el DNI del asistente a buscar
            System.out.println("Introduce el DNI del asistente:");
            Scanner sc = new Scanner(System.in);
            String dni = sc.nextLine();

            // Ejecutamos la sentencia
            pstmt.setString(1, dni);
            ResultSet res = pstmt.executeQuery();

            if (!res.next()) { // Si no hay resultados, se avisa al usuario y se termina la ejecución del programa
                System.out.println("No se ha encontrado ningún asistente con ese DNI.");
            } else { // Si hay resultados, se pide al usuario un nombre nuevo para actualizar el presente en la BD
                String nombreActual = res.getString(1);
                System.out.printf("Nombre actual: %s%n", nombreActual);
                System.out.println("Introduce el nuevo nombre (deja en blanco para no modificar):");
                String nombreNuevo = sc.nextLine();
                if (nombreNuevo.length() > 0) { // Si se introduce un nombre nuevo, se ejecuta la sentencia de actualización
                    pstmt = con.prepareStatement("UPDATE asistentes SET nombre = ? WHERE dni = ?");
                    pstmt.setString(1, nombreNuevo);
                    pstmt.setString(2, dni);
                    pstmt. executeUpdate();
                    System.out.println("Nombre actualizado con éxito.");
                } else { // Si no se introduce nada en consola, se avisa al usuario y se termina la ejecución del programa sin actualizaciones
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
