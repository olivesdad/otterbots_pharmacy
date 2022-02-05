package com.csumb.cst363;
import java.time.LocalDate;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.temporal.ChronoUnit;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
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

		//make cat address into single string
		String addy = p.getStreet() + ", "+
					  p.getCity()+ ", " +p.getState()+ " " + p.getZipcode();

		try (Connection con = getConnection();) {

			//$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
			//$$ Query Doctor Name and return doctor_id $$
			//$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
			PreparedStatement ds = con.prepareStatement("select doctor_id, specialty from doctor where name =?;");
			ds.setString(1,p.getPrimaryName());
			ResultSet rs = ds.executeQuery();
			int doctorid = 0;
			String special = "";

			//logic to check if the patient is a minor
			LocalDate dob = LocalDate.parse(p.getBirthdate()).plus(12, ChronoUnit.YEARS);
			LocalDate today = LocalDate.now();
			boolean adult = true;
			if( dob.isAfter(today)){
				adult = false;
			}

			//get info about the doctor
			if (rs.next()){
				doctorid = rs.getInt(1);
				special = rs.getString(2);
			}
			//throw exception if doctor is not found
			if (doctorid == 0) throw new IOException("Doctor not found");

			//Check doctor is internal med or family  and an adult
			else if (adult && !special.equals("Family Medicine")  && !special.equals("Internal Medicine")){
				throw new IOException("Primary Doctor must specialize in Internal or Familiy medicine");
			}
			else if (!adult && !special.equals("Pediatrics")){
				throw new IOException("Minors should see a Pediatric specialist");
			}

			//%%%%%%%%%%%%%%%%%%%%%%%%%%%
			//%% The insert statement %%%
			//%%%%%%%%%%%%%%%%%%%%%%%%%%%
			PreparedStatement ps = con.prepareStatement("insert into patient (ssn, name, dob, address, doctorid ) values(?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, p.getSsn());
			ps.setString(2, p.getName());
			ps.setString(3, p.getBirthdate());
			ps.setString(4, addy);
			ps.setInt(5,doctorid);
			ps.execute();

			// display message and patient information
			model.addAttribute("message", "Registration successful.");
			model.addAttribute("patient", p);
			return "patient_show";

		} catch (SQLException e) {
			model.addAttribute("message", "SQL Error."+e.getMessage());
			model.addAttribute("patient", p);
			return "patient_show";
		} catch (IOException e){
			model.addAttribute("message", "Invalid input: "+e.getMessage());
			model.addAttribute("patient", p);
			return "patient_show";
		}


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
