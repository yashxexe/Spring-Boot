package com.ishana.banking_management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ishana.banking_management.dto.BankAccount;
import com.ishana.banking_management.dto.BankTransaction;
import com.ishana.banking_management.dto.Customer;
import com.ishana.banking_management.dto.Login;
import com.ishana.banking_management.exception.MyException;
import com.ishana.banking_management.helper.ResponseStructure;
import com.ishana.banking_management.service.CustomerService;

@RestController
@RequestMapping("customer")
public class CustomerController {
 @Autowired
 CustomerService customerService;
 @PostMapping("add")
	public ResponseStructure<Customer> saveCustomer(@RequestBody Customer customer) throws MyException {
		return customerService.saveCustomer(customer);
	}
 @PutMapping("otp/{cust_id}/{otp}")
 public ResponseStructure<Customer> otpVerify(@PathVariable int cust_id,@PathVariable int otp) throws MyException {
	return customerService.otpVerify(cust_id,otp);
}
 @PostMapping("login")
 public ResponseStructure<Customer> login(@RequestBody Login login) throws MyException {
	return customerService.login(login);
}
 @PostMapping("account/{cust_id}/{type}")
 public ResponseStructure<Customer> createAccount(@PathVariable int cust_id, @PathVariable String type) throws MyException{
	 return customerService.createAccount(cust_id,type);
 }
 @GetMapping("accounts/{cust_id}")
 public ResponseStructure<List<BankAccount>>feacthAccountsTrue(@PathVariable int cust_id) throws MyException{
	 return customerService.feacthAccountsTrue(cust_id);
 }
 @GetMapping("account/check/{acno}")
 public ResponseStructure<Double>checkBalance(@PathVariable long acno) throws MyException{
	 return customerService.checkBalance(acno);
 }
 @PutMapping("account/deposit/{acno}/{ammount}")
 public ResponseStructure<BankAccount>deposit(@PathVariable long acno,@PathVariable double ammount ) throws MyException{
	 return customerService.deposit(acno,ammount);
 }
 @PutMapping("account/withdraw/{acno}/{ammount}")
 public ResponseStructure<BankAccount>withdraw(@PathVariable long acno,@PathVariable double ammount ) throws MyException{
	 return customerService.withdraw(acno,ammount);
 }
 @GetMapping("account/viewtransaction/{acno}")
 public ResponseStructure<List<BankTransaction>>viewTransaction(@PathVariable long acno) throws MyException{
	 return customerService.viewTransaction(acno);
 }
}