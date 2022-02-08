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
import java.util.regex.Pattern;

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
/**
 * This is the add new patient method. It accepts a patient object and performs the following actions:
 * 1. connect to pharmacy database
 * 2. checks if the enteres SSN is already in the patient table. Throws IOException if the ssn is in use
 * 3. checks the doctors name in the database and returns the first entries ID and specialty to use later. Throws IOException if Dr not found
 * 4. checks the patients age to determine if they are younger than 18
 * 5. Checks if the entered doctor is a valid selection. (Minors should match with Pediatrics DR and others should match with Family or Internal med) Throws IOException
 * 6. inserts patient into patient table with patient info and doctors id (Throws IOException for any SQL errors)
 */
        //make cat address into single string
        String addy = p.getStreet() + "," +
                p.getCity() + "," + p.getState() + "," + p.getZipcode();

        try (Connection con = getConnection();) {

            //#######
            //REGEX##
            //#######
            if (!TheSanitizer.isSSN(p.getSsn())) throw new IOException(p.getSsn() + " is not a valid ssn");
            if (!TheSanitizer.isAddress(addy)) throw new IOException(addy + " is not a valid address");
            if (!TheSanitizer.isZip(p.getZipcode())) throw new IOException(p.getZipcode() + " is not a valid zipcode");
            if (!TheSanitizer.isName(p.getName())) throw new IOException(p.getName() + " is not a valid name");
            if (!TheSanitizer.isDOB(p.getBirthdate()))
                throw new IOException(p.getBirthdate() + " is not a valid birthdate");

            PreparedStatement ss = con.prepareStatement("select * from patient where ssn =?");
            ss.setString(1, p.getSsn());
            ResultSet ssn = ss.executeQuery();
            //String pattern = "[1-8][0-9][0-9]-([0-9][1-9]|[1-9][0-9])-([0-9][0-9][0-9][1-9]|[0-9][0-9][1-9]0|[0-9][1-9]00|[1-9]000)";

            if (ssn.next()) throw new IOException("SSN is already in use!");

            //$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
            //$$ Query Doctor Name and return doctor_id $$
            //$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
            PreparedStatement ds = con.prepareStatement("select doctor_id, specialty from doctor where name =?;");
            ds.setString(1, p.getPrimaryName());
            ResultSet rs = ds.executeQuery();
            int doctorid = 0;
            String special = "";
            //get info about the doctor
            if (rs.next()) {
                doctorid = rs.getInt(1);
                special = rs.getString(2);
            }
            if (doctorid == 0) throw new IOException("Doctor not found");

            //Check if patient is a minor
            LocalDate dob = LocalDate.parse(p.getBirthdate()).plus(12, ChronoUnit.YEARS);
            LocalDate today = LocalDate.now();
            boolean adult = true;
            if (dob.isAfter(today)) {
                adult = false;
            }

            //Check doctor is internal med or family  and an adult
            if (adult) {
                //throw exception if doctor is not found
                if (!special.equals("Family Medicine") && !special.equals("Internal Medicine")) {
                    throw new IOException("Primary Doctor must specialize in Internal or Famliy medicine");
                }
            } else {
                //check if the patient is a minor and doctor is pediatics
                if (!special.equals("Pediatrics")) {
                    throw new IOException("Minors should see a Pediatric specialist");
                }
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
            ps.setInt(5, doctorid);
            ps.execute();

            // display message and patient information
            model.addAttribute("message", "Registration successful.");
            model.addAttribute("patient", p);
            return "patient_show";

        } catch (SQLException e) {
            model.addAttribute("message", "SQL Error." + e.getMessage());
            model.addAttribute("patient", p);
            return "patient_show";
        } catch (IOException e) {//catch block for invalid data messages
            model.addAttribute("message", "Invalid input: " + e.getMessage());
            model.addAttribute("patient", p);
            return "patient_show";
        }

    }
	
    /*
     * Search for patient by patient id and name.
     */
    @PostMapping("/patient/show")
    public String getPatientForm(Patient patient, @RequestParam("patientId") String patientId, @RequestParam("name") String name,
                                 Model model) {

        //setting up connection
        try(Connection con =getConnection();){
            if (!TheSanitizer.isName(patient.getName())) throw new IOException(patient.getName() + " is not a valid name");

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
                //patient.setStreet(rs.getString(4));
                String[] addy = rs.getString(4).split(",");
                patient.setStreet(addy[0]);
                patient.setCity(addy[1]);
                patient.setState(addy[2]);
                patient.setZipcode(addy[3]);
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
        } catch(IOException e) {
            model.addAttribute("message", "Invalid input:"+e.getMessage());
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
		                //patient.setStreet(rs.getString(4));
		                String[] addy = rs.getString(4).split(",");
		                patient.setStreet(addy[0]);
		                patient.setCity(addy[1]);
		                patient.setState(addy[2]);
		                patient.setZipcode(addy[3]);
		                patient.setPrimaryID(rs.getInt(5));
		                patient.setPrimaryName(rs.getString(6));
		                patient.setSpecialty(rs.getString(7));
		                patient.setYears(rs.getString(8));
		                model.addAttribute("patient",patient);
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
	public String updatePatient(Patient patient, Model model) {
	
		 String addy = patient.getStreet() + "," +
	                patient.getCity() + "," + patient.getState() + "," + patient.getZipcode();
		 
		try (Connection con = getConnection();) {
			
			 if (!TheSanitizer.isAddress(addy)) throw new IOException(addy + " is not a valid address");
	            if (!TheSanitizer.isZip(patient.getZipcode())) throw new IOException(patient.getZipcode() + " is not a valid zipcode");
	            if (!TheSanitizer.isName(patient.getName())) throw new IOException(patient.getName() + " is not a valid name");
	            
	            PreparedStatement ps = con.prepareStatement("UPDATE patient set address=?, name=? where patient_id=?");
	            ps.setString(1, addy);
				ps.setString(2,  patient.getPrimaryName());
				ps.setString(3,  patient.getPatientId());

				int rc = ps.executeUpdate();
				if (rc==1) {
					model.addAttribute("message", "Update successful");
					model.addAttribute("patient", patient);
					return "patient_show";
					
				}else {
					model.addAttribute("message", "Error. Update was not successful");
					model.addAttribute("patient", patient);
					return "patient_edit";
				}
					
			} catch (SQLException e) {
				model.addAttribute("message", "SQL Error."+e.getMessage());
				model.addAttribute("patient", patient);
				return "patient_edit";
			
		}  catch(IOException e) {
            model.addAttribute("message", "Invalid input:"+e.getMessage());
            model.addAttribute("patient", patient);
            return "patient_edit";
        }

	}

	/*
	 * return JDBC Connection using jdbcTemplate in Spring Server
	 */

	private Connection getConnection() throws SQLException {
		Connection conn = jdbcTemplate.getDataSource().getConnection();
		return conn;
	}

}
