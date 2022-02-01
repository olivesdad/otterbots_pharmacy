package com.csumb.cst363;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        for (int i = 0 ; i < 500 ; i++) {
            System.out.println(getFullName() + " SSN:" + randomSSN() +
                    " BDAY:" + randomBday() + " ZIP:" +  randomZip());
        }
    }

    //Call getName twice on firstname and lastname text files to get a name
    public static String getFullName(){
        StringBuilder name = new StringBuilder();
        name.append(getName("fnames.txt", 4940));
        name.append(" ").append(getName("lnames.txt", 21985));
        return  name.toString();
    }

    //takes filename of random names file and the linecount returns a random slection
    public static String getName(String fileLocation, int numLines){
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
         s.append(String.format("%02d", new Random().nextInt(30) +1 ));

        return s.toString();
    }

    //generate random zip
    public static int randomZip(){
        return new Random().nextInt(89999) + 10000;
    }

    //generate random ssn
    public static String randomSSN(){
        StringBuilder s = new StringBuilder();
        s.append((new Random().nextInt(799) + 100)+"-"); //first 3 digits
        s.append(String.format("%02d", new Random().nextInt(98)+1)+"-"); //middle
        s.append(String.format("%04d", new Random().nextInt(9998)+1));
        return s.toString();
    }
}
