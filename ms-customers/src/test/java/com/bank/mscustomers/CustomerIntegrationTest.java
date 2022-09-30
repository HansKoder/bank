package com.bank.mscustomers;

import com.bank.mscustomers.ent.Customer;
import com.bank.mscustomers.repo.CustomerRepository;
import com.bank.mscustomers.util.ReadResourcesJson;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
public class CustomerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private CustomerRepository customerRepository;

  @AfterEach
  void afterEach () {
    customerRepository.deleteAll();
  }

  @Test
  @Sql("/queries/customers.sql")
  void shouldGetCustomers () throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/")
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2))
      .andExpect(MockMvcResultMatchers.jsonPath("$.[0].customerId").value(1))
      .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value("M. Doe"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.[1].customerId").value(2))
      .andExpect(MockMvcResultMatchers.jsonPath("$.[1].name").value("Juan"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2))
    ;
  }

  @Test
  @Sql("/queries/customers.sql")
  void shouldGetCustomer () throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/{id}", 1L)
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.customerId").value(1L))
      .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("M. Doe"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.address").value("av 3"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.phone").value("882"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("abc123"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.enabled").value(true))
    ;
  }

  @Test
  void shouldGetNoneCustomerBecauseNotFoundInDB () throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/{id}", 1L)
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(MockMvcResultMatchers.status().isNotFound())
      .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("Not found customer with the ID 1"))
    ;
  }

  @Test
  void shouldCannotUpdateBecauseNotFoundCustomerInDB () throws Exception {
    String content = objectMapper
      .writeValueAsString(ReadResourcesJson.CUSTOMER_JHON_DOE_UPDATED.getReadResourceOfJson(Customer.class));

    mockMvc.perform(MockMvcRequestBuilders.put("/{id}", 1L)
      .contentType(MediaType.APPLICATION_JSON).content(content))
      .andExpect(MockMvcResultMatchers.status().isNotFound())
      .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("Not found customer with the ID 1"));
  }

  @Test
  @Sql("/queries/customers.sql")
  void shouldUpdateWithSuccessfully () throws Exception {
    String content = objectMapper
      .writeValueAsString(ReadResourcesJson.CUSTOMER_JHON_DOE_UPDATED.getReadResourceOfJson(Customer.class));

    mockMvc.perform(MockMvcRequestBuilders.put("/{id}", 1L)
        .contentType(MediaType.APPLICATION_JSON).content(content))
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.customerId").value(1L))
      .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Jhon D."))
      .andExpect(MockMvcResultMatchers.jsonPath("$.address").value("123 Main Street, New York, NY 10030"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.phone").value("395230404"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("haha202020"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.enabled").value(true));

    Optional<Customer> optionalCustomer = customerRepository.findById(1L);

    assertTrue(optionalCustomer.isPresent());
    assertEquals("Jhon D.", optionalCustomer.get().getName());
    assertEquals("123 Main Street, New York, NY 10030", optionalCustomer.get().getAddress());
    assertEquals("395230404", optionalCustomer.get().getPhone());
    assertEquals("haha202020", optionalCustomer.get().getPassword());
  }

  @Test
  void shouldCreateNewCustomerWithSuccessfully () throws Exception {
    Customer customer = ReadResourcesJson.CUSTOMER_LINA_DOE.getReadResourceOfJson(Customer.class);

    mockMvc.perform(MockMvcRequestBuilders.post("/")
        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(customer)))
      .andExpect(MockMvcResultMatchers.status().isCreated())
      .andExpect(MockMvcResultMatchers.jsonPath("$.customerId").value(1L))
      .andExpect(MockMvcResultMatchers.jsonPath("$.enabled").value(true));
  }
}
