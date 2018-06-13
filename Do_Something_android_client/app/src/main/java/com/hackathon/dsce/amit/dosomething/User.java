package com.hackathon.dsce.amit.dosomething;

/**
 * Created by Amit on 23-08-2017.
 */

public class User {

    private static String userName;
    private static String password;
    private static String Email;
    private static String phoneNum;
    private static Boolean isLoggedin = false;

    public static Boolean getIsLoggedin() {
        return isLoggedin;
    }

    public static void setIsLoggedin(Boolean isLoggedin) {
        User.isLoggedin = isLoggedin;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        User.userName = userName;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        User.password = password;
    }


    public static String getEmail() {
        return Email;
    }

    public static void setEmail(String email) {
        Email = email;
    }

    public static String getPhoneNum() {
        return phoneNum;
    }

    public static void setPhoneNum(String phoneNum) {
        User.phoneNum = phoneNum;
    }

    public static void removeAllCredentials(){

        Email = "";
        userName = "";
        phoneNum = "";
        password = "";
    }
}
