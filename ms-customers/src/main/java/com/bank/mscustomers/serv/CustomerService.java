package com.bank.mscustomers.serv;

import com.bank.mscustomers.ent.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
  List<Customer> getCustomers();

  Optional<Customer> getCustomer(Long id);

  Customer saveCustomer(Customer entity);
}
