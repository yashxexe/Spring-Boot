package com.ishana.banking_management.helper;

import lombok.Data;

@Data
public class ResponseStructure<T> {
int code;
String message;
T data;
}