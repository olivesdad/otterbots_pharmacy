package com.csumb.cst363;

import javax.sound.midi.Soundbank;
import java.util.Scanner;
import java.util.regex.Pattern;

public class TheSanitizer {

    /**
     * This ccepepts a string and checks if it is a valid date from 1900 to 2021
     */
    public static boolean isDOB(String s){
        String regex = "((19[0-9]{2})|(20(([0-1][0-9])|(2[0-1]))))-(02-(0[1-9]|[12][0-9])|(0[469]|11)-(0[1-9]|[12][0-9]|30)|(0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))";
        if (Pattern.matches(regex, s) )return true;
        else return false;
    }
    /**
     * This accepts a string and returns true if it's loosely and address.
     * we allow any combinations of alphas and numerics with the addition of dashes (-) periods (.) and commas (,)
     */
    public static boolean isAddress(String s) {
        String regex = "[0-9a-zA-Z ,\\-\\.]+";
        if (Pattern.matches(regex, s)) return true;
        else return false;
    }

    /**
     * This accepts a string and returns true if its loosely a name
     * we accept any combination of alphas with the addition of dashes (-) and appostrophies (')
     * the appostrophies are questionable for sql statements
     */
    public static boolean isName(String s) {
        String regex = "[a-z A-Z\\-']+";
       if (Pattern.matches(regex,s))return true;
       else return false;
    }

    /**
     * This accepts a string and returns true if its a valid ssn in the form ###-##-####
     */
    public static boolean isSSN(String s){
        String regex = "^(?!000|666)[0-8][0-9]{2}-(?!00)[0-9]{2}-(?!0000)[0-9]{4}$";
        if(Pattern.matches(regex,s )) return true;
        else return false;
    }

    /**
     * Accepts a string and returns true if it is a valid zipcode which consists of 5 numerics excluding 00000
     */
    public static boolean isZip(String s){
        String regex ="(?!00000)[0-9]{5}";
        if (Pattern.matches(regex, s)) return true;
        else return false;
    }

    public static boolean isFullAddress(String s){
        String regex = "[A-Za-z0-9' -]+,[A-Za-z0-9' -]+,[A-Za-z0-9' -]+,(?!00000)[0-9]{5}";
        if (Pattern.matches(regex, s)) return true;
        else return false;
    }

    public static void main(String[] args) {
        System.out.println(TheSanitizer.isFullAddress("123 something street,LA,CA"));
        System.out.println(TheSanitizer.isFullAddress("123 something street,LA,CA,900"));
        System.out.println(TheSanitizer.isFullAddress("123 something street,LA,CA,90210"));
        System.out.println(TheSanitizer.isFullAddress("123 something st, Los angeles, CA 90210"));
    }
}
