package com.csumb.cst363;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class DataGenerate {

    //USE THESE VARS TO CONNECT TO SERVER
    public static final String user = "andy";
    public static final String pw = "olive";
    public static final String server = "jdbc:mysql://192.168.1.18:3306/pharmacy";

    //Constants for number of patients, doctors and scripts
    public static final int doctorCount = 10;
    public static final int patientCount = 500;
    public static final int scriptCount = 5000;

    //Constants for random text files
    public static final String fNameFile = "fnames.txt";
    public static final int fNameLines = 4940;
    public static final String lastNameFile = "lnames.txt";
    public static final int lnameLines = 21985;
    public static final String stateFIle = "states.txt";
    public static final int stateLines = 51;
    public static final String streetFile = "streets.txt";
    public static final int streetLines = 500;
    public static final String citiesFile = "cities.txt";
    public static final int cityLines = 385;


    //Main driver
    public static void main(String[] args) {
        List<Patient> patients = new ArrayList<>();
        List<Doctor> doctors = new ArrayList<>();
        List<Prescription> prescriptions = new ArrayList<>();

        //create 10 random doctors in doctors list
        for (int i = 0; i < doctorCount; i++) {
            doctors.add(getDoctor(i + 1));
        }
        //create 500 random patients, use doctors list to populate primary id and primary name
        for (int i = 0; i < patientCount; i++) {
            //populate patients list with 500 random generated patients
            patients.add(getPatiant(i + 1, doctors.get(new Random().nextInt(doctors.size() - 1))));
        }
        /*
         *  _                  _     _    _         _
         * (_)_ _  ___ ___ _ _| |_  | |__| |___  __| |__
         * | | ' \(_-</ -_) '_|  _| | '_ \ / _ \/ _| / /
         * |_|_||_/__/\___|_|  \__| |_.__/_\___/\__|_\_\
         */
        //make connection
        try (Connection con = DriverManager.getConnection(server, user, pw);) {
            PreparedStatement ps = null;

            //insert doctors
            for (Doctor d : doctors) { //print doctors
                System.out.println(insertDoctorStatement(d));
                ps = con.prepareStatement(insertDoctorStatement(d));
                ps.execute();
            }
            //insert patients
            for (Patient p : patients) { //diag to print patients
                System.out.println(insertPatientStatement(p));
                ps = con.prepareStatement(insertPatientStatement(p));
                ps.execute();
            }
            //create random prescriptions and insert them by selecting random doctors and patients from lists
            for (int i = 0; i < scriptCount; i++) {
                int p = new Random().nextInt(patientCount - 1) + 1;
                int d = new Random().nextInt(doctorCount - 1) + 1;
                ps = con.prepareStatement(insertPrescription(doctors.get(d), patients.get(p)));
                ps.execute();
                //  prescriptions.add(getPrescription(doctors.get(d), patients.get(p)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//END MAIN


    //generate a doctor
    public static Doctor getDoctor(int id) {
        Doctor doc = new Doctor();
        doc.setId(id);
        String name, SSN;
        do { //use the santizer to make sure the SSNs and names are valid
            name =randomFullName();
            SSN = randomSSN();
            doc.setName(name);
            doc.setSsn(SSN);
        }while (!TheSanitizer.isName(name) || !TheSanitizer.isSSN(SSN));
        doc.setSpecialty(randomSpecialty());
        doc.setPractice_since_year((new Random().nextInt(122) + 1900) + "");
        return doc;
    }

    //generate a patient
    public static Patient getPatiant(int id, Doctor primary) {
        Patient p = new Patient();
        String name, SSN, dob, addy;
        //populate all fields
        do { //use the santizer to make sure the name ssn dob and address are valid
            name = randomFullName();
            SSN = randomSSN();
            dob = randomBday();
            addy = randomAddress();
            p.setSsn(SSN);
            p.setBirthdate(dob);
            p.setName(name);
            p.setStreet(addy);
        }while (!TheSanitizer.isSSN(SSN) || !TheSanitizer.isDOB(dob) || !TheSanitizer.isAddress(addy) || !TheSanitizer.isName(name));
        p.setPatientId(id + "");
        p.setZipcode(randomZip());
        p.setState(getRandomLine(stateFIle, stateLines));
        p.setCity(getRandomLine(citiesFile, cityLines));
        p.setPrimaryID(primary.getId());
        p.setPrimaryName(primary.getName());

        return p;
    }

    /*
                         __
                        /\ \
 _ __    __      ___    \_\ \
/\`'__\/'__`\  /' _ `\  /'_` \
\ \ \//\ \L\.\_/\ \/\ \/\ \L\ \
 \ \_\\ \__/.\_\ \_\ \_\ \___,_\
  \/_/ \/__/\/_/\/_/\/_/\/__,_ /
     */
    //Call getName twice on firstname and lastname text files to get a name
    public static String randomFullName() {
        StringBuilder name = new StringBuilder();
        name.append(getRandomLine(fNameFile, fNameLines));
        name.append(" ").append(getRandomLine(lastNameFile, lnameLines));
        return name.toString();
    }

    //return a random gnerated street address
    public static String randomAddress() {
        StringBuilder s = new StringBuilder();
        s.append((new Random().nextInt(9998) + 1) + " ").append(getRandomLine(streetFile, streetLines));
        return s.toString();
    }

    //takes filename of random names file and the linecount returns a random slection
    public static String getRandomLine(String fileLocation, int numLines) {
        File file = new File(fileLocation);
        Scanner scanner = null;
        String s = "";

        // generate random number
        int rand = new Random().nextInt(numLines - 1) + 1;

        try {
            scanner = new Scanner(file);
            for (int i = 0; i < rand; i++) scanner.nextLine();
            s = scanner.nextLine();
        } catch (FileNotFoundException e) {
            System.out.printf(e.toString());
        }
        return s;
    }

    //generate random birthdate format is YYYY-MM-DD
    public static String randomBday() {
        StringBuilder s = new StringBuilder();
        //year first
        s.append((new Random().nextInt(122) + 1900) + "-");
        s.append(String.format("%02d", new Random().nextInt(11) + 1) + "-");
        s.append(String.format("%02d", new Random().nextInt(27) + 1));
        return s.toString();
    }

    //generate random zip
    public static String randomZip() {
        return (new Random().nextInt(89999) + 10000) + "";
    }

    //generate random ssn
    public static String randomSSN() {
        StringBuilder s = new StringBuilder();
        s.append((new Random().nextInt(799) + 100) + "-"); //first 3 digits
        s.append(String.format("%02d", new Random().nextInt(98) + 1) + "-"); //middle
        s.append(String.format("%04d", new Random().nextInt(9998) + 1));
        return s.toString();
    }

    //generate random speciality
    public static String randomSpecialty() {

        String[] specialties = {"Internal Medicine",
                "Family Medicine", "Pediatrics", "Orthopedics",
                "Dermatology", "Cardiology", "Gynecology",
                "Gastroenterology", "Psychiatry", "Oncology"};
        return specialties[new Random().nextInt(specialties.length)];

    }

    //###############################
    // Generate insert statements ##
    //#############################


    public static String insertDoctorStatement(Doctor d) { //generate an insert satement for doctors
        return "insert into doctor values (" + d.getId() + ",'"
                + d.getSsn() + "','"
                + d.getName() + "','"
                + d.getSpecialty() + "','"
                + d.getPractice_since_year() + "');";
    }

    public static String insertPatientStatement(Patient p) {
        //insert into patient values (1, '222-22-2222', 'name', '1995-12-12', '123 street', 1);
        return "insert into patient values (" +
                p.getPatientId() + ",'" +
                p.getSsn() + "','" +
                p.getName() + "','" +
                p.getBirthdate() + "','" +
                p.getStreet() + "," + p.getCity() + "," + p.getState() + "," + p.getZipcode() + "'," +
                p.getPrimaryID() + ");";

    }

    public static String insertPrescription(Doctor d, Patient p) {
        // INSERT INTO prescription (doctor_id, patient_id, drug_id, quantity) values (2, 3, 1 , 59);
        String s = "insert into prescription (doctor_id, patient_id, drug_id, quantity) values (" +
                d.getId() + "," +
                p.getPatientId() + "," +
                (new Random().nextInt(98) + 1) + "," +
                (new Random().nextInt(100) + 1) + ");";
        System.out.println(s);
        return s;
    }
}
