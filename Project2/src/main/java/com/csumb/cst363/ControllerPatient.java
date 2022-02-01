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
	public String getPatientForm(@RequestParam("patientId") String patientId, @RequestParam("name") String name,
			Model model) {

		// TODO

		/*
		 * code to search for patient by id and name retrieve patient data and primary
		 * doctor data create Patient object
		 */
		
		// return fake data for now.
		Patient p = new Patient();
		p.setPatientId(patientId);
		p.setName(name);
		p.setBirthdate("2001-01-01");
		p.setStreet("123 Main");
		p.setCity("SunCity");
		p.setState("CA");
		p.setZipcode("99999");
		p.setPrimaryID(11111);
		p.setPrimaryName("Dr. Watson");
		p.setSpecialty("Family Medicine");
		p.setYears("1992");

		model.addAttribute("patient", p);
		return "patient_show";
	}

	/*
	 * Search for patient by patient id.
	 */
	@GetMapping("/patient/edit/{patientId}")
	public String updatePatient(@PathVariable String patientId, Model model) {

		// TODO Complete database logic search for patient by id.

		// return fake data for now.
		Patient p = new Patient();
		p.setPatientId(patientId);
		p.setName("Alex Patient");
		p.setBirthdate("2001-01-01");
		p.setStreet("123 Main");
		p.setCity("SunCity");
		p.setState("CA");
		p.setZipcode("99999");
		p.setPrimaryID(11111);
		p.setPrimaryName("Dr. Watson");
		p.setSpecialty("Family Medicine");
		p.setYears("1992");

		model.addAttribute("patient", p);
		return "patient_edit";
	}
	
	
	/*
	 * Process changes to patient address and primary doctor
	 */
	@PostMapping("/patient/edit")
	public String updatePatient(Patient p, Model model) {

		// TODO

		/*
		 * validate primary doctor name and other data update databaser
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
