package com.ishana.banking_management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ishana.banking_management.dto.BankAccount;
import com.ishana.banking_management.dto.Management;
import com.ishana.banking_management.exception.MyException;
import com.ishana.banking_management.helper.ResponseStructure;
import com.ishana.banking_management.repository.BankRepository;
import com.ishana.banking_management.repository.ManagementRepository;

@Service
public class ManagementService {
  @Autowired
  ManagementRepository managementRepository;
  
  @Autowired
  BankRepository bankRepository;
	public ResponseStructure<Management> saveManagement(Management management) {
		ResponseStructure<Management>structure=new ResponseStructure<>();
		structure.setMessage("Management Deatails Added");
		structure.setCode(HttpStatus.CREATED.value());
		structure.setData(managementRepository.save(management));
		return structure;
	}
	public ResponseStructure<Management> login(Management management) throws MyException {
		ResponseStructure<Management>responseStructure=new ResponseStructure<>();
		Management management1=managementRepository.findByEmail(management.getEmail());
		
		if (management1==null) {
			throw new MyException("Invalid Management Details");
		} 
if (management1.getPassword().equals(management.getPassword())) {
	responseStructure.setCode(HttpStatus.ACCEPTED.value());
	responseStructure.setMessage("Management Login Succesfull");
	responseStructure.setData(management1);
	
} else {
         throw new  MyException("Invalid Password");
}
		
		return responseStructure;
	}
	public ResponseStructure<List<BankAccount>> feacthAllAccounts() throws MyException {
		ResponseStructure<List<BankAccount>>responseStructure=new ResponseStructure<>();
		List<BankAccount>list=bankRepository.findAll();
		if (list.isEmpty()) {
			throw new MyException("No Accounts Created");
		} else {
			responseStructure.setCode(HttpStatus.FOUND.value());
			responseStructure.setMessage("Data Found(Created Accounts Review & approve)");
			responseStructure.setData(list);
		}
		return responseStructure;
	}
	public ResponseStructure<BankAccount> changeStatus(long acno) {
		ResponseStructure<BankAccount>responseStructure=new ResponseStructure<>();
		      
		Optional<BankAccount> optional=bankRepository.findById(acno);
		  BankAccount bankAccount= optional.get();
		   if (bankAccount.isStatus()) {
			   bankAccount.setStatus(false);
			   
		}else {
			bankAccount.setStatus(true);
		}
		responseStructure.setCode(HttpStatus.ACCEPTED.value());
		responseStructure.setMessage("Status Changed (Success..!)");
		responseStructure.setData(bankRepository.save(bankAccount));
		return responseStructure;
	}

}