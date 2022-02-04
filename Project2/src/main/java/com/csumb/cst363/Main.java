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

public class Main {

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
    public static final String drugFile = "drug.txt";
    public static final int drugLines = 99;

    //Constants for number of patients, doctors and scripts
    public static final int doctorCount = 10;
    public static final int patientCount = 500;
    public static final int scriptCount = 1000;

    //Main driver
    public static void main(String[] args) {
        List <Patient>patients  = new ArrayList<>();
        List <Doctor> doctors = new ArrayList<>();
        List <Prescription> prescriptions = new ArrayList<>();

        //create 10 random doctors in doctors list
        for (int i = 0; i < doctorCount; i++){
            doctors.add(getDoctor(i+1));
        }
        //create 500 random patients, use doctors list to populate primary id and primary name
        for (int i = 0 ; i < patientCount ; i++) {
            //populate patients list with 500 random generated patients
            patients.add(getPatiant(i+1, doctors.get(new Random().nextInt(doctors.size()-1))));
        }
        //create random prescriptions
        for (int i =0 ; i < scriptCount; i++){
            int p =new Random().nextInt(patientCount -1) +1 ;
            int d = new Random().nextInt(doctorCount - 1) +1;
            prescriptions.add(getPrescription(doctors.get(d), patients.get(p)));
        }


        for (Doctor d : doctors){ //print doctors
            System.out.println(insertDoctorStatement(d));
            insert(insertDoctorStatement(d));
        }
        for (Patient p : patients){ //diag to print patients
           System.out.println(insertPatientStatement(p));
           insert(insertPatientStatement(p));
        }
//        for (Prescription p : prescriptions){
//            System.out.println(p.toString());
//        }

    }//END MAIN

    public static Prescription getPrescription(Doctor doctor, Patient patient){
        Prescription prescription = new Prescription();

        prescription.setDrugName(getRandomLine(drugFile, drugLines));
        prescription.setQuantity(new Random().nextInt(100));
        prescription.setPatientName(patient.getName());
        prescription.setPatient_ssn(patient.getSsn());
        prescription.setDoctorName(doctor.getName());
        prescription.setDoctor_ssn(doctor.getSsn());
        return prescription;
    }

    //generate a doctor
    public static Doctor getDoctor(int id){
        Doctor doc = new Doctor();
        doc.setId(id);
        doc.setName(randomFullName());
        doc.setSsn(randomSSN());
        doc.setSpecialty(randomSpecialty());
        doc.setPractice_since_year((new Random().nextInt(122)+1900)+"");
        return doc;
    }
    //generate a patient
    public static Patient getPatiant(int id, Doctor primary){
        Patient p = new Patient();
        //populate all fields
        p.setPatientId(id +"");
        p.setName(randomFullName());
        p.setBirthdate(randomBday());
        p.setStreet(randomAddress());
        p.setState(getRandomLine(stateFIle, stateLines));
        p.setSsn(randomSSN());
        p.setZipcode(randomZip());
        p.setCity(getRandomLine(citiesFile, cityLines));
        p.setPrimaryID(primary.getId());
        p.setPrimaryName(primary.getName());

        return p;
    }
    //Call getName twice on firstname and lastname text files to get a name
    public static String randomFullName(){
        StringBuilder name = new StringBuilder();
        name.append(getRandomLine(fNameFile, fNameLines));
        name.append(" ").append(getRandomLine(lastNameFile, lnameLines));
        return  name.toString();
    }

    //return a random gnerated street address
    public static String randomAddress(){
        StringBuilder s = new StringBuilder();
        s.append((new Random().nextInt(9998)+1)+" ").append(getRandomLine(streetFile, streetLines));
        return s.toString();
    }

    //takes filename of random names file and the linecount returns a random slection
    public static String getRandomLine(String fileLocation, int numLines){
        File file = new File(fileLocation);
        Scanner scanner = null;
        String s = "";

        // generate random number
        int rand = new Random().nextInt(numLines-1) +1;

        try {
            scanner = new Scanner(file);
            for (int i = 0; i < rand ; i++) scanner.nextLine();
                s=scanner.nextLine();
        }
        catch (FileNotFoundException e){
            System.out.printf(e.toString());
        }
        return s;
    }

    //generate random birthdate format is YYYY-MM-DD
    public static String randomBday(){
        StringBuilder s = new StringBuilder();
        //year first
         s.append((new Random().nextInt(122) +1900) + "-") ;
         s.append(String.format("%02d", new Random().nextInt(11) + 1) +"-");
         s.append(String.format("%02d", new Random().nextInt(27) +1 ));

        return s.toString();
    }

    //generate random zip
    public static String randomZip(){
        return (new Random().nextInt(89999) + 10000)+"";
    }

    //generate random ssn
    public static String randomSSN(){
        StringBuilder s = new StringBuilder();
        s.append((new Random().nextInt(799) + 100)+"-"); //first 3 digits
        s.append(String.format("%02d", new Random().nextInt(98)+1)+"-"); //middle
        s.append(String.format("%04d", new Random().nextInt(9998)+1));
        return s.toString();
    }

    //generate random speciality
    public static String randomSpecialty(){

        String[] specialties = { "Internal Medicine",
                "Family Medicine", "Pediatrics", "Orthopedics",
                "Dermatology",  "Cardiology", "Gynecology",
                "Gastroenterology", "Psychiatry", "Oncology" };
        return specialties[new Random().nextInt(specialties.length)];

    }

    //##################################
    // use this to run sql statements ##
    //##################################
    public static void insert(String s){
        try (Connection con = DriverManager.getConnection("jdbc:mysql://192.168.1.18:3306/pharmacy", "andy", "olive"); ) {
            PreparedStatement ps = con.prepareStatement(s);
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String insertDoctorStatement(Doctor d){ //generate an insert satement for doctors
        return "insert into doctor values ("+ d.getId()+",'"
                                            + d.getSsn()+"','"
                                            + d.getName() + "','"
                                            + d.getSpecialty()+"','"
                                            + d.getPractice_since_year()+"');";
    }

    public static String insertPatientStatement(Patient p ){
        //insert into patient values (1, '222-22-2222', 'name', '1995-12-12', '123 street', 1);
        return "insert into patient values ("+
                p.getPatientId()+",'"+
                p.getSsn()+"','"+
                p.getName()+"','"+
                p.getBirthdate()+"','"+
                p.getStreet() + " " +p.getCity()+", "+p.getState()+ " "+p.getZipcode()+"',"+
                p.getPrimaryID()+");";

    }

    public static int getDrugId(String drug){

        try (Connection con = DriverManager.getConnection("jdbc:mysql://192.168.1.18:3306/pharmacy", "andy", "olive"); ) {
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
    }catch (Exception e){
            e.printStackTrace();
        }
}
