package mobile.banking.customer.repository;

import mobile.banking.customer.Customer;
import mobile.banking.customer.CustomerCreateDto;
//import mobile.banking.customer.CustomerSearchCondition;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository {
    Customer save(final CustomerCreateDto customerDto);

    Optional<Customer> findById(final Long id);

    List<Customer> findAll();

//    Optional<Customer> findByEmail(final String email);

//    List<Customer> findByCondition(final CustomerSearchCondition cond);

    void update(Long id, final CustomerCreateDto customerDto);

    void delete(final Long id);

}
