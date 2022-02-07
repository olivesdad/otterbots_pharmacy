package com.csumb.cst363;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/*
 * Controller class for patient interactions.
 *   register as a new patient.
 *   update patient profile.
 */
@Controller
public class ControllerPatient {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/*
	 * Request patient_register form.
	 */
	@GetMapping("/patient/new")
	public String newPatient(Model model) {
		// return blank form for new patient registration
		model.addAttribute("patient", new Patient());
		return "patient_register";
	}
	
	/*
	 * Request form to search for patient.
	 */
	@GetMapping("/patient/edit")
	public String getPatientForm(Model model) {
		// prompt for patient id and name
		return "patient_get";
	}
	
	/*
	 * Process a form to create new patient.
	 */
	@PostMapping("/patient/new")
	public String newPatient(Patient p, Model model) {

		// TODO

		/*
		 * Complete database logic to verify and process new patient
		 */
		// fake data for generated patient id.
		p.setPatientId("300198");
		model.addAttribute("message", "Registration successful.");
		model.addAttribute("patient", p);
		return "patient_show";

	}
	
	/*
	 * Search for patient by patient id and name.
	 */
	@PostMapping("/patient/show")
	public String getPatientForm(Patient patient, @RequestParam("patientId") String patientId, @RequestParam("name") String name,
			Model model) {

		//setting up connection 
		try(Connection con =getConnection();){
			
			System.out.println("start getPatient" + patient);
			PreparedStatement ps = con.prepareStatement("select p.patient_id, p.name, p.dob, p.address, d.doctor_id , d.name, d.specialty, d.practice_since "
					+ "from patient p, doctor d where p.patient_id=? and p.name=?");
			
			ps.setString(1, patient.getPatientId());
			ps.setString(2, patient.getName());
		
			
			
			ResultSet rs = ps.executeQuery();
		if(rs.next()) {
		patient.setPatientId(rs.getString(1));
		patient.setName(rs.getString(2));
		patient.setBirthdate(rs.getString(3));
		patient.setStreet(rs.getString(4));
		patient.setPrimaryID(rs.getInt(5));
	    patient.setPrimaryName(rs.getString(6));
	    patient.setSpecialty(rs.getString(7));
	    patient.setYears(rs.getString(8));
		

		model.addAttribute("patient", patient);
		return "patient_show";
		
	} else {
		model.addAttribute("message", "Patient not found.");
		return "patient_get";
	}
		}catch (SQLException e) {
		System.out.println("SQL error in getDoctor "+e.getMessage());
		model.addAttribute("message", "SQL Error."+e.getMessage());
		model.addAttribute("patient", patient);
		return "patient_get";
	}
		
	}
	/*
	 * Search for patient by patient id.
	 */
	
	@GetMapping("/patient/edit/{patientId}")
	public String updatePatient(@PathVariable String patientId, Model model) {
			
			Patient patient= new Patient();
			patient.setPatientId(patientId);
			try(Connection con = getConnection();){
				
				PreparedStatement ps = con.prepareStatement("select p.patient_id, p.name, p.dob, p.address, d.doctor_id , d.name, d.specialty, d.practice_since "
						+ "from patient p, doctor d where p.patient_id=?");
				ps.setString(1,patientId);
				
				ResultSet rs = ps.executeQuery();
				if(rs.next()) {
				patient.setPatientId(rs.getString(1));
				patient.setName(rs.getString(2));
				patient.setBirthdate(rs.getString(3));
				patient.setStreet(rs.getString(4));
				patient.setPrimaryID(rs.getInt(5));
			    patient.setPrimaryName(rs.getString(6));
			    patient.setSpecialty(rs.getString(7));
			    patient.setYears(rs.getString(8));
			    return"patient_edit";
				
				} else {
					model.addAttribute("message", "Patient not found.");
					model.addAttribute("patient", patient);
					return "patient_get";
				}
				
		
	} catch (SQLException e) {
		model.addAttribute("message", "SQL Error."+e.getMessage());
		model.addAttribute("patient", patient);
		return "patient_get";
		
		}
	}
	
	/*
	 * Process changes to patient address and primary doctor
	 */
	@PostMapping("/patient/edit")
	public String updatePatient(Patient p, Model model) {
		
		
		/*
		 * validate primary doctor name and other data update database
		 */

		model.addAttribute("patient", p);
		return "patient_show";
		}
	

	/*
	 * return JDBC Connection using jdbcTemplate in Spring Server
	 */

	private Connection getConnection() throws SQLException {
		Connection conn = jdbcTemplate.getDataSource().getConnection();
		return conn;
	}

}
