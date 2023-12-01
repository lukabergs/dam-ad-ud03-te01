package eus.birt.dam;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Ejercicio3 {
    public static void main(String[] args) throws Exception {
        System.out.println("Introduce el DNI del asistente:");
        Scanner sc = new Scanner(System.in);
        String dni = sc.nextLine();
        Pattern patronDni = Pattern.compile("\\d{8}\\w");
        Matcher comparaFormato = patronDni.matcher(dni);
        if (!comparaFormato.matches()) {
            System.out.println("El DNI proporcionado tiene un formato inválido.");
        } else {
            try {
                Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost/dbeventos",
                    "root",
                    "root"
                );
                PreparedStatement pstmt = con.prepareStatement(
                    "SELECT nombre FROM asistentes WHERE dni = ?"
                );
                
    
                pstmt.setString(1, dni);
                ResultSet res = pstmt.executeQuery();
                String nombre;
    
                if (!res.next()) {
                    System.out.println(
                        "No se encontró ningún asistente con el DNI proporcionado.\n"
                        + "Introduce el nombre del asistente:"
                    );
                    nombre = sc.nextLine();
                    pstmt = con.prepareStatement("INSERT INTO asistentes VALUES (?, ?)");
                    pstmt.setString(1, dni);
                    pstmt.setString(2, nombre);
                    pstmt.executeUpdate();
                } else {
                    nombre = res.getString(1);
                }
                System.out.printf("Estás realizando la reserva para: %s%n", nombre);
                System.out.println("Lista de eventos:");
                Statement stmt = con.createStatement();
                String sql = "SELECT id_evento, nombre_evento, capacidad - COUNT(asistentes_eventos.dni) "
                            + "FROM eventos JOIN ubicaciones USING (id_ubicacion) JOIN asistentes_eventos USING (id_evento) "
                            + "GROUP BY id_evento ORDER BY id_evento";
                res = stmt.executeQuery(sql);
                int i = 0;
                ArrayList<Integer> plazasLibres = new ArrayList<Integer>();
                while (res.next()) {
                    plazasLibres.add(res.getInt(3));
                    System.out.printf(
                        "%d. %s - Espacios disponibles: %d%n",
                        res.getInt(1),
                        res.getString(2),
                        plazasLibres.get(i++)
                    );
                }
                System.out.println("Elige el número del evento al que quiere asistir:");
                int idEvento = sc.nextInt();
    
                pstmt = con.prepareStatement("SELECT * FROM asistentes_eventos WHERE dni LIKE ? AND id_evento = ?");
                pstmt.setString(1, dni);
                pstmt.setInt(2, idEvento);
                res = pstmt.executeQuery();
    
                if (!res.next()) {
                    if (plazasLibres.get(idEvento - 1) > 0) {
                        pstmt = con.prepareStatement("INSERT INTO asistentes_eventos VALUES (?, ?)");
                        pstmt.setString(1, dni);
                        pstmt.setInt(2, idEvento);
                        pstmt.executeUpdate();
                        System.out.printf("%s ha sido registrado/a para el evento seleccionado.%n", nombre);
                    } else {
                        System.out.println("El evento seleccionado no tiene plazas disponibles.");
                    }
                } else {
                    System.out.printf("%s ya estaba registrado/a para el evento seleccionado. Registro cancelado.%n", nombre);
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
}
