package com.csumb.cst363;

import java.sql.*;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller    
public class ControllerPrescription {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/*
	 * Doctor requests form to create new prescription.
	 */
	@GetMapping("/prescription/new")
	public String newPrescripton(Model model) {
		model.addAttribute("prescription", new Prescription());
		return "prescription_create";
	}
	
	/* 
	 * Patient requests form to search for prescription.
	 */
	@GetMapping("/prescription/fill")
	public String getfillForm(Model model) {
		model.addAttribute("prescription", new Prescription());
		return "prescription_fill";
	}

	/*
	 * Doctor creates new patient prescription (done by Juli)
	 */
	@PostMapping("/prescription")
	public String newPrescription( Prescription p, Model model) {

		// connect to mysql database
		try (Connection con = getConnection();) {

			// validate form input.  If it fails validation, display message.
			if(isValidSNN(p.getDoctor_ssn()) && isValidString(p.getDoctorName()) &&
					isValidSNN(p.getPatient_ssn()) && isValidString(p.getPatientName()) &&
					isValidString(p.getDrugName())) {
				PreparedStatement validateDoctor = con.prepareStatement("select doctor_id from doctor " +
						"where ssn = ? and name = ?;");
				validateDoctor.setString(1, p.getDoctor_ssn());
				validateDoctor.setString(2, p.getDoctorName());
				ResultSet resultDoctor = validateDoctor.executeQuery();

				// validate Doctor SSN and match Doctor Name
				if(resultDoctor.next()) {
					int doctorId = resultDoctor.getInt(1);

					PreparedStatement validatePatient = con.prepareStatement("select patient_id from patient " +
							"where ssn = ? and name = ?;");
					validatePatient.setString(1, p.getPatient_ssn());
					validatePatient.setString(2, p.getPatientName());
					ResultSet resultPatient = validatePatient.executeQuery();

					// validate Patient SSN and match to Patient Name
					if(resultPatient.next()) {
						int patientId = resultPatient.getInt(1);

						PreparedStatement validateDrug = con.prepareStatement("select drug_id from drug " +
								"where trade_name = ?;");
						validateDrug.setString(1, p.getDrugName());
						ResultSet resultDrug = validateDrug.executeQuery();

						// validate Drug Name and quantity
						// A prescription taken 3x a day for 30-days is 90.
						// The assumption that a pharmacy can only dispense 30-day supply at a time.
						if(p.getQuantity() <= 0 || p.getQuantity() > 90)
						{
							p.setRxid("INVALID PRESCRIPTION");
							model.addAttribute("message", "Prescription quantity must be 1 to 90.");
						} else if (resultDrug.next()) {
							p.setRxid(Integer.toString(resultDrug.getInt(1)));

							// validation complete.  insert new prescription.
							PreparedStatement ps = con.prepareStatement(
									"insert into prescription(doctor_id, patient_id, drug_id, quantity) " +
											"values(?, ?, ?, ?);",
									Statement.RETURN_GENERATED_KEYS);
							ps.setInt(1, doctorId);
							ps.setInt(2, patientId);
							ps.setInt(3, Integer.parseInt(p.getRxid()));
							ps.setInt(4, p.getQuantity());
							ps.executeUpdate();

							// get rx_id
							ResultSet rs = ps.getGeneratedKeys();
							if(rs.next()) {
								p.setRxid(Integer.toString(rs.getInt(1)));
							}

							// display message
							model.addAttribute("message", "Prescription created.");
							model.addAttribute("prescription", p);
						} else {
							model.addAttribute("message", "Drug not found.");
						} // end validate drug and quantity
					} else {
						model.addAttribute("message", "Patient not found.");
					} // end validate patient
				} else {
					model.addAttribute("message", "Doctor not found.");
				} // end validate doctor
			} else {
				model.addAttribute("message", "Invalid entry found.\n" +
						"Do not enter special characters.\n" +
						"Make sure SSN is entered as xxx-xx-xxxx.");
			} // end validate form input
		} catch (Exception e) {
			model.addAttribute("message", "SQL ERROR. " + e.getMessage());
		} // end try-catch block

		return "prescription_show";
	}

	@PostMapping("/prescription/fill")
	public String processFillForm(Prescription p,  Model model) {

		// connect to mysql database
		try (Connection con = getConnection();) {

			// validate form input.  If it fails validation, display message.
			if(isNumeric(p.getRxid()) && isValidString(p.getPatientName()) &&
					isValidString(p.getPharmacyName()) && isValidString(p.getPharmacyAddress())) {
				PreparedStatement validatePrescription = con.prepareStatement(
						"select d.name, d.ssn, patient.ssn, drug.trade_name, p.quantity, filled_date, pharmacy_id " +
								"from prescription p " +
								"    join patient on p.patient_id = patient.patient_id " +
								"    join doctor d on p.doctor_id = d.doctor_id " +
								"    join drug on p.drug_id = drug.drug_id " +
								"where rx_number = ? and patient.name = ?;");
				validatePrescription.setInt(1, Integer.parseInt(p.getRxid()));
				validatePrescription.setString(2, p.getPatientName());
				ResultSet resultPrescription = validatePrescription.executeQuery();

				// validate prescription using rx and patient name
				if(resultPrescription.next()) {
					// get information needed for form display
					p.setDoctorName(resultPrescription.getString(1));
					p.setDoctor_ssn(resultPrescription.getString(2));
					p.setPatient_ssn(resultPrescription.getString(3));
					p.setDrugName(resultPrescription.getString(4));
					p.setQuantity(resultPrescription.getInt(5));
					p.setDateFilled(resultPrescription.getString(6));
					p.setPharmacyID(Integer.toString(resultPrescription.getInt(7)));

					// validate if prescription has already been filled
					if((p.getDateFilled() == "") && (p.getPharmacyID() == "")) {
						model.addAttribute("message", "Prescription has already been filled.");
					} else {
						PreparedStatement validatePharmacy = con.prepareStatement("select pharmacy_id, phone " +
								"from pharmacy " +
								"where name = ? and address = ?;");
						validatePharmacy.setString(1, p.getPharmacyName());
						validatePharmacy.setString(2, p.getPharmacyAddress());
						ResultSet resultPharmacy = validatePharmacy.executeQuery();

						// validate pharmacy name and pharmacy address
						if (resultPharmacy.next()) {
							// get information needed for form display
							p.setPharmacyID(Integer.toString(resultPharmacy.getInt(1)));
							p.setPharmacyPhone(resultPharmacy.getString(2));

							java.sql.Date filledDate = java.sql.Date.valueOf(LocalDate.now());
							p.setDateFilled(filledDate.toString());

							// update prescription
							PreparedStatement ps = con.prepareStatement("update prescription set filled_date = ?, pharmacy_id = ? where rx_number = ?;");
							ps.setDate(1, filledDate);
							ps.setInt(2, Integer.parseInt(p.getPharmacyID()));
							ps.setInt(3, Integer.parseInt(p.getRxid()));

							int rc = ps.executeUpdate();
							if(rc == 1) {
								model.addAttribute("message", "Prescription has been filled.");
								model.addAttribute("prescription", p);
							} else {
								model.addAttribute("message", "ERROR: Prescription was not successfully filled.");
								model.addAttribute("prescription", p);
							}
						} else {
							model.addAttribute("message", "Pharmacy not found.");
						} // end validate pharmacy
					}
				} else {
					model.addAttribute("message", "Prescription not found.");
				} // end validate prescription
			} else {
				model.addAttribute("message", "Invalid entry found.\n" +
						"Do not enter special characters.\nMake sure rx is a number.");
			} // end validate form input
		} catch (Exception e) {
		model.addAttribute("message", "SQL ERROR. " + e.getMessage());
		} // end try-catch block

		return "prescription_show";
	}
	
	/*
	 * return JDBC Connection using jdbcTemplate in Spring Server
	 */
	private Connection getConnection() throws SQLException {
		Connection conn = jdbcTemplate.getDataSource().getConnection();
		return conn;
	}

	/*
	 * validate strings to ensure they are sanitized.
	 */
	private boolean isValidString(String str) {
		String regex = "[<>&\"\'()#&;+-]";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return !(matcher.find());
	}

	/*
	 * validate strings that will be converted to integer values
	 */
	private boolean isNumeric(String str) {
		try {
			Integer.parseInt(str);
		} catch (NumberFormatException e) {
		return false;
		}
		return true;
	}

	/*
	 * validate ssn in format xxx-xx-xxxx, including special rules on how ssn values are
	 */
	private boolean isValidSNN(String str) {
		String regex = "^(?!000|666)[0-8][0-9]{2}-(?!00)[0-9]{2}-(?!0000)[0-9]{4}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
}
