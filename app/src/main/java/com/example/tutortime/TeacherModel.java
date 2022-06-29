package com.example.tutortime;

public class TeacherModel {
    private String Address;
    private String Age;
    private String CNIC;
    private String ClassYouCanTeach;
    private String ConfirmPassword;
    private String ContactNumber;
    private String Description;
    private String Email;
    private String Experience;
    private String Gender;
    private String HowDidYouFindUs;
    private String Password;
    private String ProfileImageURLTeacher;
    private String Qualification;
    private String Role;
    private String SecondContactNumber;
    private String SubjectsYouCan;
    private String UserName;

    public TeacherModel() {
    }

    public TeacherModel(String address, String age, String CNIC,
                        String classYouCanTeach, String confirmPassword, String contactNumber,
                        String description, String email, String experience, String gender, String howDidYouFindUs,
                        String password, String profileImageURLTeacher, String qualification, String role,
                        String secondContactNumber, String subjectsYouCan, String userName) {
        Address = address;
        Age = age;
        this.CNIC = CNIC;
        ClassYouCanTeach = classYouCanTeach;
        ConfirmPassword = confirmPassword;
        ContactNumber = contactNumber;
        Description = description;
        Email = email;
        Experience = experience;
        Gender = gender;
        HowDidYouFindUs = howDidYouFindUs;
        Password = password;
        ProfileImageURLTeacher = profileImageURLTeacher;
        Qualification = qualification;
        Role = role;
        SecondContactNumber = secondContactNumber;
        SubjectsYouCan = subjectsYouCan;
        UserName = userName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getCNIC() {
        return CNIC;
    }

    public void setCNIC(String CNIC) {
        this.CNIC = CNIC;
    }

    public String getClassYouCanTeach() {
        return ClassYouCanTeach;
    }

    public void setClassYouCanTeach(String classYouCanTeach) {
        ClassYouCanTeach = classYouCanTeach;
    }

    public String getConfirmPassword() {
        return ConfirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        ConfirmPassword = confirmPassword;
    }

    public String getContactNumber() {
        return ContactNumber;
    }

    public void setContactNumber(String contactNumber) {
        ContactNumber = contactNumber;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getExperience() {
        return Experience;
    }

    public void setExperience(String experience) {
        Experience = experience;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getHowDidYouFindUs() {
        return HowDidYouFindUs;
    }

    public void setHowDidYouFindUs(String howDidYouFindUs) {
        HowDidYouFindUs = howDidYouFindUs;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getProfileImageURLTeacher() {
        return ProfileImageURLTeacher;
    }

    public void setProfileImageURLTeacher(String profileImageURLTeacher) {
        ProfileImageURLTeacher = profileImageURLTeacher;
    }

    public String getQualification() {
        return Qualification;
    }

    public void setQualification(String qualification) {
        Qualification = qualification;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }

    public String getSecondContactNumber() {
        return SecondContactNumber;
    }

    public void setSecondContactNumber(String secondContactNumber) {
        SecondContactNumber = secondContactNumber;
    }

    public String getSubjectsYouCan() {
        return SubjectsYouCan;
    }

    public void setSubjectsYouCan(String subjectsYouCan) {
        SubjectsYouCan = subjectsYouCan;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }
}
