package mobile.banking.customer;


import mobile.banking.customer.repository.CustomerRepository;
import mobile.banking.customer.repository.JdbcCustomerRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
@Transactional //test 끝나면 그 값은 알아서 지워줌
public class CustomerRepositoryTest {
    private static final Logger log = LoggerFactory.getLogger(CustomerRepositoryTest.class);
//  private static final CustomerRepository customerRepository = new JdbcCustomerRepository();

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void crud(){
        // save
        var createDto = new CustomerCreateDto("홍길동","gildong@gmail.com","010-1234-5678","대전광역시");
        var customer = customerRepository.save(createDto);
        log.info("customer= id : {}", customer.id());

        // findbyId
        var customer2 =  customerRepository.findById(customer.id()).get();
        log.info("customer2= id : {}", customer2);

        // update
        var updateDto = new CustomerCreateDto("홍길동","gildong@gmail.com","010-1234-5678","제주도");
        customerRepository.update(customer.id(), updateDto);
        var customer3 = customerRepository.findById(customer.id()).get();
        assertThat(customer3.address()).isEqualTo("제주도");

        //delete
        customerRepository.delete(customer.id());
        assertThat(customerRepository.findById(customer.id())).isEmpty();
    }

}
