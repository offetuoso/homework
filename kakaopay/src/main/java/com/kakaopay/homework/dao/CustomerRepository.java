package com.kakaopay.homework.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.kakaopay.homework.entity.Customer;
import com.kakaopay.homework.entity.Spread;


public interface CustomerRepository extends CrudRepository<Customer, Integer> {

	List<Customer> findByName(String name);
	List<Customer> findByNameLike(String name);
	
	@Query("from Customer where name = ?1 or name = ?2 ")
	List<Customer> findVipList(String valye1, String valye2);
	
	
	@Query(value="select *from Customer where name = ?1 or name = ?2 ", nativeQuery=true)
	List<Customer> findVipList2(String valye1, String valye2);
	
	

	
}
