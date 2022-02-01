package com.csumb.cst363;
import java.sql.*;

public class World {

    public static void main(String[] args) {

        try (Connection con = DriverManager.getConnection("jdbc:mysql://192.168.1.18:3306/world", "andy", "olive"); ) {
            PreparedStatement ps = con.prepareStatement("select code, name, population, lifeexpectancy from Country where continent=? and lifeexpectancy<=?");
            ps.setString(1, "Asia");
            ps.setInt(2, 76);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String code = rs.getString(1);
                String name = rs.getString(2);
                int population = rs.getInt(3);
                double life = rs.getDouble(4);
                System.out.printf("%5s %-20s %12d %8.1f\n", code, name, population, life);
            }

            int population2021 = 97340000;
            String ucode = "VNM";
            double ulife2021 = 75.4;
            PreparedStatement ups = con.prepareStatement("update Country set population=?, lifeexpectancy=? where code=?");
            ups.setInt(1, population2021);
            ups.setDouble(2,  ulife2021);
            ups.setString(3,  ucode);
            int rowcount = ups.executeUpdate();
            System.out.printf("rows updated %d\n", rowcount);


            PreparedStatement ps1 = con.prepareStatement("select code, name, population, lifeexpectancy from Country where code=?");
            ps1.setString(1,ucode);
            ResultSet rs1 = ps1.executeQuery();
            while (rs1.next()) {
                String code = rs1.getString(1);
                String name = rs1.getString(2);
                int population = rs1.getInt(3);
                double life = rs1.getDouble(4);
                System.out.printf("%5s %-20s %12d %8.1f\n", code, name, population, life);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}