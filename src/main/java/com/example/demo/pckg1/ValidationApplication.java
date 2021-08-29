package com.example.demo.pckg1;

import java.time.LocalDate;
import java.util.Date;
import java.util.regex.Pattern;

public class ValidationApplication {

	String passwordRegex = "^(?=.[a-z])(?=.[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$";
	String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
	String nameRegex = "(?=^.{6,40}$)^[a-zA-Z-]+\\s[a-zA-Z-]+$";
	String phoneRegex = "^\\d{10}$";
	Pattern password_pattern = Pattern.compile(passwordRegex);
	Pattern email_pattern = Pattern.compile(emailRegex);
	Pattern phone_pattern = Pattern.compile(phoneRegex);
	Pattern name_pattern = Pattern.compile(nameRegex);

    public boolean check_password(String password,String retyped_password) {
	    if(password!=null && retyped_password!=null) {
		    if (! password.equals(retyped_password)){
		    	return false;
		    }
		
		    if (! password_pattern.matcher(password).matches()) {
		        return false;
		    }
		   
		}
        return true;
    }
   
    public boolean check_email(String email){
	    if(email!=null) {
		    if (! email_pattern.matcher(email).matches()) {
		    	return false;
		    }
	    }
        return true;
    }
   
    public boolean check_name(String fullName){
	    if(fullName!=null) {
		    if (!name_pattern.matcher(fullName).matches()) {
		    	return false;
		    }
		}
        return true;
    }
   
    public boolean check_phone(String phoneNumber){
	    if(phoneNumber!=null) {
		    if (!phone_pattern.matcher(phoneNumber).matches()) {
		    	return false;
		    }
	    }
        return true;
    }
    
    public boolean check_age(Date dateOfBirth) {
    	Date date_now = new Date();
    	long different_in_time = date_now.getTime() - dateOfBirth.getTime();
    		
    	
    
    	return true;  	
    }


}
