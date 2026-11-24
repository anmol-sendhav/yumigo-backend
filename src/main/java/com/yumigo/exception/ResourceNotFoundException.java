package com.yumigo.exception;

public class ResourceNotFoundException extends RuntimeException{

public ResourceNotFoundException(String message,Long id){

    super(message + " not found with id " + id);
}
}
