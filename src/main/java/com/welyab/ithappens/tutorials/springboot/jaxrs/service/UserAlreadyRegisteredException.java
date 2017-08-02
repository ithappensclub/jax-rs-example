package com.welyab.ithappens.tutorials.springboot.jaxrs.service;

public class UserAlreadyRegisteredException extends ServiceException {

    public UserAlreadyRegisteredException(String msg) {
        super(msg);
    }
}
