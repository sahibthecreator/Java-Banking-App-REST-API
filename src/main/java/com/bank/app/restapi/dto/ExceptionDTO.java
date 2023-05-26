package com.bank.app.restapi.dto;


public record ExceptionDTO(int status, String exception, String message) {
}
