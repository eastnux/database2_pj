package mobile.banking.customer;

import mobile.banking.customer.repository.CustomerRepository;

import java.util.List;
import java.util.Optional;

public class CustomerService {
    private final CustomerRepository customerRepository;
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    public  Customer createCustomer(CustomerCreateDto customerDto){
        return customerRepository.save(customerDto);
    }

    public Optional<Customer> findCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    public List<Customer> findCustomer() {
        return customerRepository.findAll();
    }

    public void updateCustomer(Long id, CustomerCreateDto CustomerDto){
        customerRepository.update(id, CustomerDto);
    }

    public void removeCustomer(Long id){
        customerRepository.delete(id);
    }
}
