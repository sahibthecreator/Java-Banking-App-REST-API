package com.bank.app.restapi.http;

import lombok.*;

/*
 HttpBody is a custom body created to return a default format
 Parameters:
    - boolean (success)
    - Any type of object (body)

example:
{
	"success": true,
	"body": {
		"id": "3b12f2ae-a345-4cb1-a15b-afb7a7e7123b",
		"first_name": "John",
		"last_name": "Doe",
		"dob": "2023-04-20T00:04:38.669+00:00",
		"bsn": "000000001",
		"email": "john@gmail.com",
		"passwd": "passwd"
	}
}
 */

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HttpBody<T> {
    private boolean success;
    private T body;
}