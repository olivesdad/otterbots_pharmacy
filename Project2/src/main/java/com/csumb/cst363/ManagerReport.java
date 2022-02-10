package com.csumb.cst363;
import java.sql.*;

public class ManagerReport {
    public static void main(String[] args) {
        // Establish connection to MySQL database
        // Closes connection when try block terminates
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pharmacy", "user", "passwd"); ) {

            //Variable to query by:
            int pharmacyId = 2;
            java.sql.Date startDate = java.sql.Date.valueOf("2022-01-01");
            java.sql.Date endDate = java.sql.Date.valueOf("2022-03-02");

            // Precompile SQL statements (query)
            PreparedStatement ps = con.prepareStatement("select trade_name, sum(quantity) " +
                    "from prescription p " +
                    "join drug d on p.drug_id = d.drug_id " +
                    "join pharmacy pharm on pharm.pharmacy_id = p.pharmacy_id " +
                    "where p.pharmacy_id = ?  and (filled_date >= ? and filled_date <= ?) " +
                    "group by trade_name;");

            ps.setInt(1, pharmacyId);
            ps.setDate(2, startDate);
            ps.setDate(3, endDate);

            // Receive a ResultSet object (the query result)
            ResultSet rs = ps.executeQuery();

            // Display query result as one line per row
            while(rs.next()) {
                String tradeName = rs.getString(1);
                int quantity = rs.getInt(2);
                System.out.printf("%s %d\n", tradeName, quantity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
