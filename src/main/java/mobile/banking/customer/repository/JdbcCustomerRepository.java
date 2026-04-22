package mobile.banking.customer.repository;

import mobile.banking.connection.ConnectionUtil;
import mobile.banking.customer.Customer;
import mobile.banking.customer.CustomerCreateDto;
//import mobile.banking.customer.CustomerSearchCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcCustomerRepository implements CustomerRepository {

    private static final Logger log = LoggerFactory.getLogger(JdbcCustomerRepository.class);

    @Override
    public Customer save(final CustomerCreateDto customerDto) {
        String sql = "INSERT INTO customer (name, email, cellphone, address) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement psmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            psmt.setString(1, customerDto.name());
            psmt.setString(2, customerDto.email());
            psmt.setString(3, customerDto.cellphone());
            psmt.setString(4, customerDto.address());
            psmt.executeUpdate();

            Long id = null;
            try (ResultSet rs = psmt.getGeneratedKeys()) {
                if (rs.next()) {
                    id = rs.getLong("id");
                }
            }

            return new Customer(id, customerDto.name(), customerDto.email(), customerDto.cellphone(), customerDto.address());
        } catch (SQLException e) {
            log.error("db error", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Customer> findById(final Long id) {
        String sql = "SELECT * FROM customer WHERE id = ?";
        ResultSet rs = null;
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement psmt = conn.prepareStatement(sql)
        ) {
            psmt.setLong(1, id);
            rs = psmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapper(rs));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            log.error("db error", e);
            throw new RuntimeException(e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException _) {
                }
            }
        }
    }

    @Override
    public List<Customer> findAll() {
        String sql = "SELECT * FROM customer";

        List<Customer> customerList = new ArrayList<>();
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement psmt = conn.prepareStatement(sql);
             ResultSet rs = psmt.executeQuery()) {
            while (rs.next()) {
                customerList.add(mapper(rs));
            }
            return customerList;
        } catch (SQLException e) {
            log.error("db error", e);
            throw new RuntimeException(e);
        }
    }

//    @Override
//    public Optional<Customer> findByEmail(String email) {
//        return Optional.empty();
//    }

//    @Override
//    public List<Customer> findByCondition(CustomerSearchCondition cond) {
//        return List.of();
//    }

    @Override
    public void update(Long id, final CustomerCreateDto customerDto) {
        String sql = "UPDATE customer SET name = ?, email = ?, cellphone = ? , address = ? WHERE id = ?";
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement psmt = conn.prepareStatement(sql)
        ) {
            psmt.setString(1, customerDto.name());
            psmt.setString(2, customerDto.email());
            psmt.setString(3, customerDto.cellphone());
            psmt.setString(4, customerDto.address());
            psmt.setLong(5, id);
            int result = psmt.executeUpdate();
            log.info("update customer id " + id + ", result " + result);
        } catch (SQLException e) {
            log.error("db error", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(final Long id) {
        String sql = "DELETE FROM customer WHERE id = ?";
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement psmt = conn.prepareStatement(sql)
        ) {
            psmt.setLong(1, id);
            int result = psmt.executeUpdate();
            log.info("delete customer id " + id + ", result " + result);
        } catch (SQLException e) {
            log.error("db error", e);
            throw new RuntimeException(e);
        }
    }

    private Customer mapper(ResultSet rs) throws SQLException {
        return new Customer(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("cellphone"),
                rs.getString("address")
        );
    }

    // CREATE, READ, UPDATE, DELETE = CRUD

}