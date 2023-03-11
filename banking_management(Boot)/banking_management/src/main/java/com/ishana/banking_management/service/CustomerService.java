package com.ishana.banking_management.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ishana.banking_management.dto.BankAccount;
import com.ishana.banking_management.dto.BankTransaction;
import com.ishana.banking_management.dto.Customer;
import com.ishana.banking_management.dto.Login;
import com.ishana.banking_management.exception.MyException;
import com.ishana.banking_management.helper.MailVerification;
import com.ishana.banking_management.helper.ResponseStructure;
import com.ishana.banking_management.repository.BankRepository;
import com.ishana.banking_management.repository.CustomerRepository;

@Service
public class CustomerService {
   @Autowired
	CustomerRepository customerRepository;
  @Autowired
  MailVerification mailVerification;
@Autowired
BankAccount bankAccount;
@Autowired
BankRepository bankRepository;
@Autowired
BankTransaction bankTransaction;

	public  ResponseStructure<Customer> saveCustomer(Customer customer) throws MyException {																		
		ResponseStructure<Customer> responseStructure = new ResponseStructure<>();
		
		int age=Period.between(customer.getDob().toLocalDate(), LocalDate.now()).getYears();
		customer.setAge(age);
		if (age<18) {
			throw new MyException("Check Id and Try again");
		} else {
			
			Random random=new Random();
			int otp=random.nextInt(100000,999999);
			 customer.setOtp(otp);
			mailVerification.sendMail(customer);
			
			responseStructure.setMessage("OTP has send to mail to verification");
			responseStructure.setCode(HttpStatus.PROCESSING.value());
			responseStructure.setData(customerRepository.save(customer));
		}
		
		return responseStructure;
	}


	public ResponseStructure<Customer> otpVerify(int cust_id, int otp) throws MyException {
		ResponseStructure<Customer>responseStructure=new ResponseStructure<>();
		Optional<Customer>optional=customerRepository.findById(cust_id);
		if (optional.isEmpty()) {
			throw new MyException("Check ID again & try again");
		} else {
			Customer customer=optional.get();
			if (customer.getOtp()==otp) {
				responseStructure.setMessage("Account created Succesfully");
				responseStructure.setCode(HttpStatus.CREATED.value());
				customer.setStatus(true);
				responseStructure.setData(customerRepository.save(customer));
			} else {
                  throw new MyException("OTP MissMacth");
			}
		}
		return responseStructure;
	}


	public ResponseStructure<Customer> login(Login login) throws MyException {
		ResponseStructure<Customer>responseStructure=new ResponseStructure<>();
		Optional<Customer>optional=customerRepository.findById(login.getId());
		
		if (optional.isEmpty()) {
			throw new MyException("Invalid content");
		} else {
Customer customer=optional.get();
if (customer.getPassword().equals(login.getPassword())) {
	responseStructure.setCode(HttpStatus.ACCEPTED.value());
	responseStructure.setMessage("Customer Login Succesfull");
	responseStructure.setData(customer);
	
} else {
         throw new  MyException("Invalid Password");
}
		}
		return responseStructure;
	}


	public ResponseStructure<Customer> createAccount(int cust_id, String type) throws MyException {
		ResponseStructure<Customer>responseStructure=new ResponseStructure<>();
    Optional<Customer>optional=customerRepository.findById(cust_id);
		
		if (optional.isEmpty()) {
			throw new MyException("Invalid content");
		} else {
Customer customer=optional.get();  
List<BankAccount> list=customer.getBankAccounts();
boolean flag=true;
for (BankAccount bankAccount : list) {
	if (bankAccount.getType().equals(type)) {
		flag=false;
		break;
	}
}
if (!flag) {
	throw new MyException(type+"Account already exists");
}else {
bankAccount.setType(type);
if (type.equals("savings")) {
	bankAccount.setBanklimit(5000);
} else {

	bankAccount.setBanklimit(10000);
}

list.add(bankAccount);
customer.setBankAccounts(list);
}	
responseStructure.setCode(HttpStatus.ACCEPTED.value());
responseStructure.setData(customerRepository.save(customer));
responseStructure.setMessage("Account created wait for management Approve");
		}
		return responseStructure;
	}


	public ResponseStructure<List<BankAccount>> feacthAccountsTrue(int cust_id) throws MyException {
		ResponseStructure<List<BankAccount>>responseStructure=new ResponseStructure<>();
	    Optional<Customer>optional=customerRepository.findById(cust_id);
	   Customer customer= optional.get();
	  List<BankAccount> list= customer.getBankAccounts();
	  List<BankAccount> res=new ArrayList<BankAccount>();
	  for (BankAccount bankAccount : list) {
		if (bankAccount.isStatus()) {
			res.add(bankAccount);
		}
	}
	      if (res.isEmpty()) {
			throw  new MyException("No Active Accounts found");
		}     else {
			responseStructure.setCode(HttpStatus.FOUND.value());
			responseStructure.setMessage("Accounts found(Accounts which status are true)");
			responseStructure.setData(res);
		}
		return responseStructure;
	}


	public ResponseStructure<Double> checkBalance(long acno) {
		ResponseStructure<Double>responseStructure=new ResponseStructure<>();
		 Optional<BankAccount>optional=bankRepository.findById(acno);
		BankAccount bankAccount=optional.get();
		
		responseStructure.setCode(HttpStatus.FOUND.value());
		responseStructure.setMessage("Data Found");
		responseStructure.setData(bankAccount.getAmount());
		return responseStructure;
	}


	public ResponseStructure<BankAccount> deposit(long acno,double ammount) {
		ResponseStructure<BankAccount>responseStructure=new ResponseStructure<>();
		BankAccount bankAccount=bankRepository.findById(acno).get();
		bankAccount.setAmount(bankAccount.getAmount()+ammount);
		bankTransaction.setDateTime(LocalDateTime.now());
		bankTransaction.setDeposit(ammount);
		bankTransaction.setBalance(bankAccount.getAmount());
		
		List<BankTransaction> transactions=bankAccount.getBankTransaction();
		transactions.add(bankTransaction);
		
		bankAccount.setBankTransaction(transactions);
		responseStructure.setCode(HttpStatus.ACCEPTED.value());
		responseStructure.setMessage("Amount Added Sucessfully");
		responseStructure.setData(bankRepository.save(bankAccount));
		return responseStructure;
	}


	public ResponseStructure<BankAccount> withdraw(long acno, double ammount) throws MyException {
		ResponseStructure<BankAccount>responseStructure=new ResponseStructure<>();
		BankAccount bankAccount=bankRepository.findById(acno).get();
		if (ammount>bankAccount.getBanklimit()) {
			throw new MyException("Transaction out of Limit");
		} else {
if (ammount>bankAccount.getAmount()) {
	throw new MyException("Insufficient Funds");
}else {
	

		
		bankAccount.setAmount(bankAccount.getAmount()-ammount);
		bankTransaction.setDateTime(LocalDateTime.now());
		bankTransaction.setWithdraw(ammount);
		bankTransaction.setBalance(bankAccount.getAmount());
		
		List<BankTransaction> transactions=bankAccount.getBankTransaction();
		transactions.add(bankTransaction);
		
		bankAccount.setBankTransaction(transactions);
		responseStructure.setCode(HttpStatus.ACCEPTED.value());
		responseStructure.setMessage("Transction succesfull (Ammount Dedected Check balanace)");
		responseStructure.setData(bankRepository.save(bankAccount));
}
		}
		return responseStructure;
	}


	public ResponseStructure<List<BankTransaction>> viewTransaction(long acno) throws MyException {
		ResponseStructure<List<BankTransaction>>responseStructure=new ResponseStructure<>();
		BankAccount bankAccount=bankRepository.findById(acno).get();
		List<BankTransaction>listTransaction=bankAccount.getBankTransaction();
	if(listTransaction.isEmpty()) {
		throw new MyException("No Transaction");
	}else {
		responseStructure.setCode(HttpStatus.FOUND.value());
		responseStructure.setMessage("Data Found");
		responseStructure.setData(listTransaction);
	}
		return responseStructure;
	}
}
