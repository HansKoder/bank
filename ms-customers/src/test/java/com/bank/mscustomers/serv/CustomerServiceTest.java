package com.bank.mscustomers.serv;

import com.bank.mscustomers.ent.Customer;
import com.bank.mscustomers.repo.CustomerRepository;
import com.bank.mscustomers.serv.impl.CustomerServiceImpl;
import com.bank.mscustomers.util.ReadResourcesJson;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = { CustomerServiceImpl.class })
@ActiveProfiles("test")
public class CustomerServiceTest {

  @MockBean
  private CustomerRepository customerRepository;

  @Autowired
  private CustomerService customerService;

  @Test
  void shouldGetAListOfCustomers () throws IOException {
    List<Customer> mock = Arrays.asList(
      ReadResourcesJson.CUSTOMER_JHON_DOE.getReadResourceOfJson(Customer.class),
      ReadResourcesJson.CUSTOMER_LINA_DOE.getReadResourceOfJson(Customer.class)
    );

    Mockito.when(customerRepository.findByEnabled(anyBoolean()))
      .thenReturn(mock);

    List<Customer> customers = customerService.getCustomers();

    assertEquals(2, customers.size());
    assertInstanceOf(Customer.class, customers.get(0));
    assertInstanceOf(Customer.class, customers.get(1));
  }

  @Test
  void shouldGetAnyCustomer () throws IOException {
    Mockito.when(customerRepository.findById(anyLong()))
      .thenReturn(Optional.of(ReadResourcesJson.CUSTOMER_JHON_DOE.getReadResourceOfJson(Customer.class)));

    Optional<Customer> optionalCustomer = customerService.getCustomer(1L);

    assertTrue(optionalCustomer.isPresent());

    Customer customer = optionalCustomer.get();

    assertEquals(1, customer.getCustomerId());
    assertEquals("Jhon Doe", customer.getName());
    assertEquals("Av 3", customer.getAddress());
    assertEquals("882", customer.getPhone());
    assertEquals("abc12345", customer.getPassword());

    assertTrue(customer.isEnabled());
  }

  @Test
  void shouldGetEmptyCustomer () throws IOException {
    Mockito.when(customerRepository.findById(anyLong()))
      .thenReturn(Optional.empty());

    Optional<Customer> optionalCustomer = customerService.getCustomer(1L);

    assertTrue(optionalCustomer.isEmpty());

    Mockito.verify(customerRepository, Mockito.times(1)).findById(anyLong());
  }

  @Test
  void shouldSaveCustomer () throws IOException {
    Mockito.when(customerRepository.save(any(Customer.class)))
      .thenReturn(ReadResourcesJson.CUSTOMER_JHON_DOE.getReadResourceOfJson(Customer.class));

    Customer customer = customerService.saveCustomer(Mockito.mock(Customer.class));

    assertNotNull(customer);

    assertEquals(1, customer.getCustomerId());
    assertEquals("Jhon Doe", customer.getName());
    assertEquals("Av 3", customer.getAddress());
    assertEquals("882", customer.getPhone());
    assertEquals("abc12345", customer.getPassword());

    assertTrue(customer.isEnabled());
  }

}
