package com.neallee.namesorter.domain;

/** Thrown when text cannot be turned into a valid {@link Name}. */
public class InvalidNameException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidNameException(String message) {
        super(message);
    }
}
