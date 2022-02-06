package com.csumb.cst363;

import javax.sound.midi.Soundbank;
import java.util.Scanner;
import java.util.regex.Pattern;

public class TheSanitizer {

    public static boolean isAddress(String s) {
        String regex = "[0-9a-zA-Z ,\\-\\.]+";
        if (Pattern.matches(regex, s)) return true;
        else return false;
    }

    public static boolean isName(String s) {
        String regex = "[a-z A-Z\\-']+";
       if (Pattern.matches(regex,s))return true;
       else return false;
    }

    public static boolean isSSN(String s){
        String regex = "^(?!000|666)[0-8][0-9]{2}-(?!00)[0-9]{2}-(?!0000)[0-9]{4}$";
        if(Pattern.matches(regex,s )) return true;
        else return false;
    }

    public static boolean isZip(String s){
        String regex ="(?!00000)[0-9]{5}";
        if (Pattern.matches(regex, s)) return true;
        else return false;
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("enter: ");
        String s = scanner.nextLine();
        System.out.println(isAddress(s));
        System.out.println(isSSN(s));
        System.out.println(isName(s));
        System.out.println(isZip(s));

    }



}
