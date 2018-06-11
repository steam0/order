package io.stene.order;

import io.stene.order.client.Customer;
import io.stene.order.client.CustomerClient;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Disabled
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class CustomerIntegrationTest {

	@Autowired
	private CustomerClient customerClient;

	@Test
	public void createCustomer() {
		Customer customer = Customer.builder().name("Harald Hårfagre").ssn("71039012345").build();
		Customer response = customerClient.createCustomer(customer);

		assertEquals(response.getName(), customer.getName());
		assertEquals(response.getSsn(), customer.getSsn());
		assertNotNull(response.getId());
	}

}
