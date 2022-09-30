package com.bank.mscustomers.serv.impl;

import com.bank.mscustomers.ent.Customer;
import com.bank.mscustomers.repo.CustomerRepository;
import com.bank.mscustomers.serv.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

  @Autowired
  private CustomerRepository customerRepository;

  @Override
  public List<Customer> getCustomers() {
    return customerRepository.findByEnabled(true);
  }

  @Override
  public Optional<Customer> getCustomer(Long id) {
    return customerRepository.findById(id);
  }

  @Override
  public Customer saveCustomer(Customer entity) {
    return customerRepository.save(entity);
  }


}
