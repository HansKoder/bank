package com.bank.mscustomers.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public enum ReadResourcesJson {

  CUSTOMER_JHON_DOE("./src/test/resources/fixtures/CustomerJhonDoe.json"),
  CUSTOMER_JHON_DOE_UPDATED("./src/test/resources/fixtures/CustomerJhonDoeUpdated.json"),
  CUSTOMER_JHON_DOE_CREATED("./src/test/resources/fixtures/CustomerJhonDoeCreated.json"),
  CUSTOMER_LINA_DOE("./src/test/resources/fixtures/CustomerLinaDoe.json");

  private final String resourcePath;

  ObjectMapper mapper = new ObjectMapper();

  ReadResourcesJson(String resourcePath) {
    this.resourcePath = resourcePath;
  }

  public <T> T getReadResourceOfJson (Class<T> clazz) throws IOException {
      return mapper.readValue(new File(this.resourcePath), clazz);
  }

}
