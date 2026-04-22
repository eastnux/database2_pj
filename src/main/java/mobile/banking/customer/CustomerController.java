package mobile.banking.customer;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/customers") //공통으로 호출되는 URL을 위로
public class CustomerController {
    private final CustomerService customerService;
    public CustomerController(
            final CustomerService customerService
    ){
        this.customerService = customerService;

    }
//    http://localhost:8080/hello
    @GetMapping("/hello")
    public ResponseEntity<String> hello(){
        return ResponseEntity.ok("Hello, World!");
    }

    @PostMapping("")
    public ResponseEntity<Customer> createCustomer(
            @RequestBody final CustomerCreateDto customerCreateDto
    ){
        Customer customer = customerService.createCustomer(customerCreateDto);
//        return ResponseEntity.ok(customer); //ResponseEntity.c(customer)가 규약에 맞긴 함
        return ResponseEntity.status(HttpStatus.CREATED).body(customer);

    }

    // http://localhost:8080/api/customers
    @GetMapping("")
    public ResponseEntity<List<Customer>> getCustomers(){
        List<Customer> customerList = customerService.findCustomer();

        return customerList.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(customerList);
    }

    // http://localhost:8080/api/customers/1
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(
            @PathVariable("id") final Long id
    ){
        try {
            Customer customer = customerService.findCustomerById(id).orElseThrow(
                    () -> new IllegalArgumentException("Customer not found with id: " + id)
            );
            return ResponseEntity.ok(customer);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateCustomer(
            @PathVariable final Long id,
            @RequestBody final CustomerCreateDto customerDto
    ){
        try {
            customerService.findCustomerById(id).orElseThrow(
                    () -> new IllegalArgumentException("Customer not found with id: " + id)
            );
            customerService.updateCustomer(id, customerDto);
            Customer customer = customerService.findCustomerById(id).orElseThrow(
                    () -> new IllegalArgumentException("Customer not found with id: " + id)
            );
            return ResponseEntity.ok(customer);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeCustomer(
            @PathVariable final Long id
    ){
        try {
            customerService.findCustomerById(id).orElseThrow(
                    () -> new IllegalArgumentException("Customer not found with id: " + id)
            );
            customerService.removeCustomer(id);
            return ResponseEntity.ok("Customer with id " + id + " has been removed.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
