package com.user.Utils;

import java.util.Random;

public class Utils
{
    public static String generateUsername(String name){
        String username = name.replaceAll("\\s", "").toLowerCase();
        Random random= new Random();
        int value= 100000 + random.nextInt(900000);
        username = username + '-' + value;
        return username;
    }
}

/*
 *     String username = name.replaceAll("\\s", "").toLowerCase();
 *          ----> name.replaceAll("\\s", ""): This removes all whitespace (spaces, tabs, newlines) from the input name.
 *          ----> \\s is a regex pattern that matches any whitespace
 *          ----> .toLowerCase(): Converts the string to lowercase.
 *           Example: If the input is "Raj Kumar", this becomes "rajkumar".
 *
 *
 *    int value = 100000 + random.nextInt(900000);
 *          ---> random.nextInt(900000) generates a number between 0 and 899999.
 *          ---> Adding 100000 ensures the number is at least 100000.
 *          ---> So the final value will always be a 6-digit number between 100000 and 999999.
 *
 *    username = username + '-' + value;
 *         ----> Appends a hyphen - and the 6-digit random number to the username.
 *         ---->  Combines them into a new username.
 *         ----> Example: "rajkumar-458932"
 *
 */