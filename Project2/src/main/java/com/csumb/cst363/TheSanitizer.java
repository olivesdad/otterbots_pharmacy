package com.csumb.cst363;

import javax.sound.midi.Soundbank;
import java.util.Scanner;
import java.util.regex.Pattern;

public class TheSanitizer {

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

}
