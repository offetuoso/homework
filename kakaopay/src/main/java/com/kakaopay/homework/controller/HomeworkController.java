package com.kakaopay.homework.controller;


import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kakaopay.homework.dao.CustomerRepository;
import com.kakaopay.homework.entity.Customer;

@RestController
public class HomeworkController {
	private CustomerRepository repository;

	public HomeworkController(CustomerRepository repository) {
		super();
		this.repository = repository;
	}
	
	@PutMapping("/customer")
	public Customer putCustomer(Customer customer) {
		
		return repository.save(customer);
		
	}
	
	@PostMapping("/customer")
	public Customer postCustomer(Customer customer) {
		
		return repository.save(customer);
		
	}
	
	@DeleteMapping("/customer")
	public void deleteCustomer(int id) {
		
		repository.deleteById(id);
		
	}
	
	@GetMapping("/customer")
	public Customer getCustomer(int id) {
		
		return repository.findById(id).orElse(null);
		
	}
	
	@GetMapping("/customer/list")
	public List<Customer> allCustomer() {
		
		return (List<Customer>) repository.findAll();
		
	}
	
	@GetMapping("/customer/name")
    public List<Customer> getCustomer(String name) {
		
		return repository.findByName(name);
		
	}
	
	@GetMapping("/customer/search")
	public List<Customer> searchCustomer(String name) {
		
		return repository.findByNameLike("%"+name+"%");
		
	}
	
	@GetMapping("/customer/vip/list")
	public List<Customer> vipCustomer(String value1, String value2) {
		
		return (List<Customer>) repository.findVipList(value1, value2);
				
		
	}
	
	@GetMapping("/customer/vip2/list")
	public List<Customer> vip2Customer(String value1, String value2) {
		
		return (List<Customer>) repository.findVipList(value1, value2);
		
		
	}
	
	
	
	
	
}
