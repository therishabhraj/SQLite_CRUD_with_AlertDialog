package com.example.hrishabh.alertdialogue_tests_udemy;

public class Details {


    private String Name,Email;
    private  int id;

    public Details(String name, String email) {
        this.Name = name;
        this.Email = email;
    }


    public void setName(String name) {
        Name = name;
    }

    public void setEmail(String email) {
        Email = email;
    }



    public String getName() {
        return Name;
    }

    public String getEmail() {
        return Email;
    }


}
