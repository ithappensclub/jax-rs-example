package com.welyab.ithappens.tutorials.springboot.jaxrs.data.hibernate;

public class HibernateUtilException extends RuntimeException {

    public HibernateUtilException() {
    }

    public HibernateUtilException(String message) {
        super(message);
    }

    public HibernateUtilException(String message, Throwable cause) {
        super(message, cause);
    }

    public HibernateUtilException(Throwable cause) {
        super(cause);
    }
}
