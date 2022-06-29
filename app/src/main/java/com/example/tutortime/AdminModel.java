package com.example.tutortime;

public class AdminModel {


    private String ProfileImageURL;
    private String Name;
    private String CNIC;
    private String ContactNumber;
    private String Email;
    private String Password;
    private String ConfirmPassword;
    private String Role;
    private String Address;

    public AdminModel() {
    }

    public AdminModel(String profileImageURL, String name, String CNIC, String contactNumber, String email, String password, String confirmPassword, String role, String address) {
        ProfileImageURL = profileImageURL;
        Name = name;
        this.CNIC = CNIC;
        ContactNumber = contactNumber;
        Email = email;
        Password = password;
        ConfirmPassword = confirmPassword;
        Role = role;
        Address = address;
    }


    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getProfileImageURL() {
        return ProfileImageURL;
    }

    public void setProfileImageURL(String profileImageURL) {
        ProfileImageURL = profileImageURL;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCNIC() {
        return CNIC;
    }

    public void setCNIC(String CNIC) {
        this.CNIC = CNIC;
    }

    public String getContactNumber() {
        return ContactNumber;
    }

    public void setContactNumber(String contactNumber) {
        ContactNumber = contactNumber;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getConfirmPassword() {
        return ConfirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        ConfirmPassword = confirmPassword;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }
}
