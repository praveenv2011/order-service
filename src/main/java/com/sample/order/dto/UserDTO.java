package com.sample.order.dto;


import java.util.List;

public class UserDTO {

    private Long userId;

    private String userName;

    private String password;

    private String email;

    private String phoneNumber;

    private List<AddressDTO> addresses;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

//    public List<AddressDTO> getAddresses() {
//        return addresses;
//    }
//
//    public void setAddresses(List<AddressDTO> addresses) {
//        this.addresses = addresses;
//    }
}
