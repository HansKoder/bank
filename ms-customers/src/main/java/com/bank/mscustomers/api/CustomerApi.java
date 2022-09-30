package com.bank.mscustomers.api;

import com.bank.mscustomers.ent.Customer;
import com.bank.mscustomers.serv.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
public class CustomerApi {

  @Autowired
  private CustomerService customerService;

  @GetMapping("/")
  public ResponseEntity<List<Customer>> getCustomers() {
    return ResponseEntity.ok(customerService.getCustomers());
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getCustomer (@PathVariable Long id) {
    Optional<Customer> customerOptional = customerService.getCustomer(id);

    if (customerOptional.isEmpty()) {
      String msg = "Not found customer with the ID " + id;

      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("msg", msg));
    }

    return ResponseEntity.ok(customerOptional.get());
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateCustomer (@RequestBody Customer customer, @PathVariable("id") Long id) {
    try {
      System.out.println("Here update customer...");
      Optional<Customer> customerOptional = customerService.getCustomer(id);

      if (customerOptional.isEmpty()) {
        String msg = "Not found customer with the ID " + id;

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("msg", msg));
      }

      Customer updated = customerOptional.get();

      if (customer.getName() != null) updated.setName(customer.getName());
      if (customer.getAddress() != null) updated.setAddress(customer.getAddress());
      if (customer.getPassword() != null) updated.setPassword(customer.getPassword());
      if (customer.getPhone() != null) updated.setPhone(customer.getPhone());

      return ResponseEntity.ok(customerService.saveCustomer(updated));
    } catch (Exception e) {
      System.out.println("error updated " + e.getMessage());
      return ResponseEntity.internalServerError().body(Collections.singletonMap("msg", e.getMessage()));
    }

  }

  @PostMapping("/")
  public ResponseEntity<Customer> saveCustomer (@RequestBody Customer customer) {
    Customer save = customerService.saveCustomer(customer);

    return ResponseEntity.status(HttpStatus.CREATED).body(save);
  }

}
