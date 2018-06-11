package io.stene.order;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.model.RequestResponsePact;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.stene.order.client.Customer;
import io.stene.order.client.CustomerClient;
import io.stene.order.client.CustomerConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(PactConsumerTestExt.class)
public class CustomerPactTest {

    @Pact(provider = "customer", consumer = "order")
    public RequestResponsePact getCustomerPactTest(PactDslWithProvider builder) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);

        String ssn = "71039012345";
        return
                builder
                        .given(String.format("Customer with ssn(%s) exists.", ssn))
                        .uponReceiving("Get customer request")
                        .path(String.format("/customer/%s", ssn))
                        .method(HttpMethod.GET.name())
                .willRespondWith()
                        .status(HttpStatus.OK.value())
                        .headers(headers)
                        .body(new PactDslJsonBody()
                                .stringValue("name", "Roger Antonsen") // Strict value
                                .stringValue("ssn", ssn) // Strict value
                                .stringType("address", "Wrong Blvd")
                                .integerType("id", 0) // Value not important, but strict type
                        )
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "getCustomerPactTest")
    void testGetCustomer(MockServer mockServer) {
        Customer customer = new CustomerClient(new CustomerConfig(mockServer.getUrl())).getCustomer("71039012345");
        assertEquals(customer.getSsn(), "71039012345");
        assertEquals(customer.getName(), "Roger Antonsen");
        assertNotNull(customer.getAddress());
        assertNotNull(customer.getId());
    }

    @Pact(provider = "customer", consumer = "order")
    public RequestResponsePact createCustomer(PactDslWithProvider builder) throws JsonProcessingException {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);

        Customer customer = Customer.builder().name("Henrik Stene").ssn("31129012345").address("6850 Correct street").build();

        return
                builder
                        .uponReceiving("Create customer request")
                        .path("/customer")
                        .method(HttpMethod.POST.name())
                        .headers(headers)
                        .body(new ObjectMapper().writeValueAsString(customer))
                .willRespondWith()
                        .status(HttpStatus.CREATED.value())
                        .headers(headers)
                        .body(new PactDslJsonBody()
                                .stringValue("name", customer.getName())
                                .stringValue("ssn", customer.getSsn())
                                .stringType("address", customer.getAddress())
                                .integerType("id", 0)
                        )
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "createCustomer")
    void testCreateCustomer(MockServer mockServer) {
        Customer customer = Customer.builder().name("Henrik Stene").ssn("31129012345").address("6850 Correct street").build();
        Customer response = new CustomerClient(new CustomerConfig(mockServer.getUrl())).createCustomer(customer);
        assertEquals(response.getName(), customer.getName());
        assertEquals(response.getSsn(), customer.getSsn());
        assertNotNull(response.getId());
    }
}
