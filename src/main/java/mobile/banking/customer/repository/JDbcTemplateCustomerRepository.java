package mobile.banking.customer.repository;

import mobile.banking.connection.ConnectionUtil;
import mobile.banking.customer.Customer;
import mobile.banking.customer.CustomerCreateDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class JDbcTemplateCustomerRepository implements CustomerRepository {
    private static final Logger log = LoggerFactory.getLogger(JDbcTemplateCustomerRepository.class);

    private final JdbcTemplate jdbcTemplate;

    public JDbcTemplateCustomerRepository(DataSource dataSource) {

        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Customer save(CustomerCreateDto customerDto) {
        String sql = "INSERT INTO customer (name, email, cellphone, address) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, customerDto.name());
            ps.setString(2, customerDto.email());
            ps.setString(3, customerDto.cellphone());
            ps.setString(4, customerDto.address());
            return ps;
        }, keyHolder);


        long key = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return new Customer(key, customerDto.name(), customerDto.email(), customerDto.cellphone(), customerDto.address());
    }


    @Override
    public Optional<Customer> findById(Long id) {
        String sql = "SELECT * FROM customer WHERE id = ?";
        try{
            Customer customer = jdbcTemplate.queryForObject(sql, rowMapper(), id);
            return Optional.of(customer);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

    }

    @Override
    public List<Customer> findAll() {
        String sql = "SELECT * FROM customer";
        return jdbcTemplate.query(sql, rowMapper());
    }

    @Override
    public void update(Long id, CustomerCreateDto customerDto) {
        String sql = "UPDATE customer SET name = ?, email = ?, cellphone = ? , address = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                customerDto.name(), customerDto.email(), customerDto.cellphone(), customerDto.address(), id);


    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM customer WHERE id = ?";
        jdbcTemplate.update(sql, id);

    }
    private RowMapper<Customer> rowMapper(){
        return (rs, _) -> new Customer(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("cellphone"),
                rs.getString("address")
        );
    }
}
