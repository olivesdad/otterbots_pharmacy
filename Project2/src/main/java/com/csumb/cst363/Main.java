package com.csumb.cst363;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // String s = getName("fnames.txt", 4945);
        System.out.println(getFullName());
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
}
