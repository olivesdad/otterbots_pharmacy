package com.csumb.cst363;
import java.sql.*;

public class FDAReport {

	//setup driver
	public static void main(String[] args) {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pharmacy", "root", "olive007"); ) {
      
        	String tradename= "";
        	java.sql.Date startDate = java.sql.Date.valueOf("2021-01-01");     
        	java.sql.Date endDate = java.sql.Date.valueOf("2022-12-31");     
        	
        	//query statement
        	String query= "SELECT doc.name,sum(quantity)\n"
            		+ "FROM doctor doc\n"
            		+ "JOIN prescription p ON doc.doctor_id = p.doctor_id\n"
            		+ "JOIN drug d ON p.drug_id = d.drug_id\n"
            		+ "WHERE d.trade_name = ? " + " AND filled_date = ? and filled_date = ? "
            		+ "GROUP BY doc.name;\n";
        
        //open statement & receive query result
        PreparedStatement ps = con.prepareStatement(query);     	
        ps.setString(1, tradename + '%');
        ps.setDate(2, startDate);
        ps.setDate(3,endDate);
        ResultSet rs = ps.executeQuery();
        
        //display result
        while(rs.next()) {
        	String doctorName= rs.getString(1);
        	int quantity = rs.getInt(2);
        	System.out.printf("%s, %s, %s \n", doctorName, quantity);
        }
        
        
        } catch (Exception e) {
        	e.printStackTrace();
        
        }
     } 

}