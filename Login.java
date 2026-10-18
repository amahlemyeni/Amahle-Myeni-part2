/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Student
 */
public class Login {

    private String username;
    private String password;
    private String cellPhone;
    private String firstName;
    private String lastName;
    
    public Login(String username, String password, String cellPhone,
            String firstName, String lastName){
    
  this.username = username;
  this.password = password;
  this.cellPhone = cellPhone;
  this.firstName = firstName;
  this.lastName = lastName; 
    }
        
    public boolean checkUserName(String username){
        return username.contains("_")&& username.length()<= 5;  
    }  
    public boolean checkPasswordComplexity(String password){
            return password.length() >= 8
                    && password.matches(".*[A-Z].*")
                    && password.matches(".*[0-9].*")
                    && password.matches(".*[^a-zA-Z0-9].*");      
    }
    public boolean checkCellPhoneNumber(String cellPhone){
        return cellPhone.matches("^\\+27\\d{9}$"); 
    }
        public String registerUser() {
           if (!checkUserName(username)){ 
           return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
         
           }
           
if (!checkPasswordComplexity(password)){
    return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number and a special character.";
}

if (!checkCellPhoneNumber(cellPhone)){
    return "Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.";
}

return "Username successfully captured.\n"
        +"Password successfully captured.\n"
        +"Cell number successfully captured.";
        }
        
public boolean loginUser(String enteredUsername, String enteredPassword){
    return enteredUsername.equals(username)
            &&enteredPassword.equals(password);
}

public String returnLoginStatus(String enteredUsername, String enteredPassword) {
    
    if (loginUser(enteredUsername, enteredPassword)){
        return "Welcome " + firstName + "," + lastName
                + " it is great to see you again.";
       }
    
return "Username or password incorrect, please try again.";

}
}


      
        