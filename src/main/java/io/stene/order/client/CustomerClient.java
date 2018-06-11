package io.stene.order.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class CustomerClient {
    private CustomerConfig customerConfig;

    @Autowired
    public CustomerClient(CustomerConfig customerConfig) {
        this.customerConfig = customerConfig;
    }

    public Customer getCustomer(String ssn) {
        RestTemplate restTemplate = new RestTemplate();
        String url = customerConfig.getUrl()+"/customer/"+ssn;
        Customer response = restTemplate.getForObject(url, Customer.class);
        log.info("Got Customer(id={}, name={}, ssn={})", response.getId(), response.getName(), response.getSsn());
        return response;
    }

    public Customer createCustomer(Customer customer) {
        RestTemplate restTemplate = new RestTemplate();
        String url = customerConfig.getUrl()+"/customer";
        Customer response = restTemplate.postForObject(url, customer, Customer.class);
        log.info("Got Customer(id={}, name={}, ssn={})", response.getId(), response.getName(), response.getSsn());
        return response;
    }
}
