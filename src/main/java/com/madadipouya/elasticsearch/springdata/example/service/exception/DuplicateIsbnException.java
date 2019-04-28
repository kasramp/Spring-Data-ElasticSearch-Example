package com.madadipouya.elasticsearch.springdata.example.service.exception;

public class DuplicateIsbnException extends Exception {

    public DuplicateIsbnException(String message) {
        super(message);
    }
}
