package mobile.banking.config;

import mobile.banking.account.AccountHistoryService;
import mobile.banking.account.AccountService;
import mobile.banking.account.repository.AccountHistoryRepository;
import mobile.banking.account.repository.AccountRepository;
import mobile.banking.account.repository.JdbcTemplateAccountHistoryRepository;
import mobile.banking.account.repository.JdbcTemplateAccountRepository;
import mobile.banking.customer.CustomerService;
import mobile.banking.customer.repository.CustomerRepository;
import mobile.banking.customer.repository.JDbcTemplateCustomerRepository;
import mobile.banking.customer.repository.JdbcCustomerRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
@Configuration
public class jdbcTemplateConfig {
    private final DataSource dataSource;

    public jdbcTemplateConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public CustomerService customerService(CustomerRepository customerRepository){
        return new CustomerService(customerRepository);
    }

    @Bean
    public CustomerRepository customerRepository() {
        return new JDbcTemplateCustomerRepository(dataSource);
    }

    @Bean
    public AccountService accountService(){
        return new AccountService(accountRepository(), accountHistoryRepository());
    }

    @Bean
    public AccountRepository accountRepository() {
        return new JdbcTemplateAccountRepository(dataSource);
    }

    @Bean
    public AccountHistoryService accountHistoryService() {
        return new AccountHistoryService(accountHistoryRepository());
    }

    @Bean
    public AccountHistoryRepository accountHistoryRepository() {
        return new JdbcTemplateAccountHistoryRepository(dataSource);
    }
}
