package com.bank.mscustomers.api;

import com.bank.mscustomers.ent.Customer;
import com.bank.mscustomers.serv.CustomerService;
import com.bank.mscustomers.util.ReadResourcesJson;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@WebMvcTest(CustomerApi.class)
public class CustomerApiTest {

  @MockBean
  private CustomerService customerService;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void shouldGetCustomers () throws Exception {
    List<Customer> list = Arrays.asList(
      ReadResourcesJson.CUSTOMER_JHON_DOE.getReadResourceOfJson(Customer.class),
      ReadResourcesJson.CUSTOMER_LINA_DOE.getReadResourceOfJson(Customer.class)
    );

    Mockito.when(customerService.getCustomers())
      .thenReturn(list);

    mockMvc.perform(MockMvcRequestBuilders.get("/")
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2))
      .andExpect(MockMvcResultMatchers.jsonPath("$.[0].customerId").value(1L))
      .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value("Jhon Doe"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.[0].enabled").value(true))
      .andExpect(MockMvcResultMatchers.jsonPath("$.[1].customerId").value(2L))
      .andExpect(MockMvcResultMatchers.jsonPath("$.[1].name").value("Lina Doe"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.[1].enabled").value(true))
    ;
  }

  @Test
  void shouldGetAnyCustomer () throws Exception {
    Mockito.when(customerService.getCustomer(anyLong()))
      .thenReturn(Optional.of(ReadResourcesJson.CUSTOMER_JHON_DOE.getReadResourceOfJson(Customer.class)));

    mockMvc.perform(MockMvcRequestBuilders.get("/{id}", 1L)
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.customerId").value(1L))
      .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Jhon Doe"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.enabled").value(true))
    ;
  }

  @Test
  void shouldGetNoneCustomerBecauseNotExistsInDB () throws Exception {
    Mockito.when(customerService.getCustomer(anyLong()))
      .thenReturn(Optional.empty());

    mockMvc.perform(MockMvcRequestBuilders.get("/{id}", 1L)
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(MockMvcResultMatchers.status().isNotFound())
      .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("Not found customer with the ID 1"))
    ;

    Mockito.verify(customerService, Mockito.times(1)).getCustomer(anyLong());
  }

  @Test
  void shouldCannotUpdateBecauseCustomerNotExitsInDB () throws Exception {
    Mockito.when(customerService.getCustomer(anyLong()))
      .thenReturn(Optional.empty());

    String content = objectMapper
      .writeValueAsString(ReadResourcesJson.CUSTOMER_JHON_DOE_UPDATED.getReadResourceOfJson(Customer.class));

    mockMvc.perform(MockMvcRequestBuilders.put("/{id}", 1L)
        .contentType(MediaType.APPLICATION_JSON)
        .content(content))
      .andExpect(MockMvcResultMatchers.status().isNotFound())
      .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("Not found customer with the ID 1"));

    Mockito.verify(customerService, Mockito.times(1)).getCustomer(anyLong());
    Mockito.verify(customerService, Mockito.never()).saveCustomer(any(Customer.class));
  }

  @Test
  void shouldUpdateCustomerWithSuccessfully () throws Exception {
    Mockito.when(customerService.getCustomer(anyLong()))
      .thenReturn(Optional.of(ReadResourcesJson.CUSTOMER_JHON_DOE.getReadResourceOfJson(Customer.class)));

    Mockito.when(customerService.saveCustomer(any(Customer.class)))
      .thenReturn(ReadResourcesJson.CUSTOMER_JHON_DOE_UPDATED.getReadResourceOfJson(Customer.class));

    String content = objectMapper
      .writeValueAsString(ReadResourcesJson.CUSTOMER_JHON_DOE_UPDATED.getReadResourceOfJson(Customer.class));

    mockMvc.perform(MockMvcRequestBuilders.put("/{id}", 1L)
        .contentType(MediaType.APPLICATION_JSON)
        .content(content))
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.customerId").value(1))
      .andExpect(MockMvcResultMatchers.jsonPath("$.enabled").value(true))
      .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Jhon D."))
      .andExpect(MockMvcResultMatchers.jsonPath("$.address").value("123 Main Street, New York, NY 10030"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.phone").value("395230404"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("haha202020"))
    ;

  }

  @Test
  void shouldCreateNewCustomerWithSuccessfully () throws Exception {
    Mockito.when(customerService.saveCustomer(any(Customer.class)))
      .thenReturn(ReadResourcesJson.CUSTOMER_JHON_DOE.getReadResourceOfJson(Customer.class));

    String content = objectMapper
      .writeValueAsString(ReadResourcesJson.CUSTOMER_JHON_DOE.getReadResourceOfJson(Customer.class));

    mockMvc.perform(MockMvcRequestBuilders.post("/")
      .contentType(MediaType.APPLICATION_JSON)
      .content(content))
      .andExpect(MockMvcResultMatchers.status().isCreated())
      .andExpect(MockMvcResultMatchers.jsonPath("$.customerId").value(1))
      .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Jhon Doe"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.address").value("Av 3"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.phone").value("882"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("abc12345"))
    ;
  }
}
