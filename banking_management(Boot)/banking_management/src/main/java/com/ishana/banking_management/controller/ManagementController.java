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
import com.ishana.banking_management.dto.Management;
import com.ishana.banking_management.exception.MyException;
import com.ishana.banking_management.helper.ResponseStructure;
import com.ishana.banking_management.service.ManagementService;

@RestController
@RequestMapping("management")
public class ManagementController {
@Autowired
ManagementService managementService;
	@PostMapping("add")
	public ResponseStructure<Management> saveManagement(@RequestBody Management management) {
		return managementService.saveManagement(management);
	}
	@PostMapping("login")
	public ResponseStructure<Management>login(@RequestBody Management management) throws MyException{
		return managementService.login(management);
	}
	@GetMapping("accounts")
	public ResponseStructure<List<BankAccount>> feacthAllAccounts() throws MyException{
		return managementService.feacthAllAccounts();
	}
	@PutMapping("accountchange/{acno}")
	public ResponseStructure<BankAccount> changeStatus(@PathVariable long acno) throws MyException{
		return managementService.changeStatus(acno);
	}
}