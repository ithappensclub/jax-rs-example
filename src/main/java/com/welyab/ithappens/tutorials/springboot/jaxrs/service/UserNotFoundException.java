package com.welyab.ithappens.tutorials.springboot.jaxrs.service;

public class UserNotFoundException extends ServiceException {

    private final Long userId;

    public UserNotFoundException(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }
}
