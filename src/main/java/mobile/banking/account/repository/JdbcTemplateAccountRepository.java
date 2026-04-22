package mobile.banking.account.repository;

import mobile.banking.account.Account;
import mobile.banking.account.AccountDto;
import mobile.banking.account.AccountUpdateDto;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;

public class JdbcTemplateAccountRepository implements AccountRepository {
    private static final Logger log = LoggerFactory.getLogger(JdbcTemplateAccountRepository.class);

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateAccountRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Account save(final AccountDto accountDto) {
        String sql = "INSERT INTO account (account_number, balance, customer_id) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, accountDto.accountNumber());
            ps.setBigDecimal(2, accountDto.balance());
            ps.setLong(3, accountDto.customerId());
            return ps;
        }, keyHolder);

        long key = keyHolder.getKey().longValue();

        return new Account(key, accountDto.accountNumber(), accountDto.balance(), accountDto.customerId());
    }

    @Override
    public Optional<Account> findById(final Long id) {
        String sql = "SELECT * FROM account WHERE id = ?";
        try {
            Account account = jdbcTemplate.queryForObject(sql, rowMapper(), id);
            return Optional.of(account);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Account> findAll() {
        String sql = "SELECT * FROM account";
        return jdbcTemplate.query(sql, rowMapper());
    }

    @Override
    public int update(Long id, final AccountUpdateDto accountUpdateDto) {
        String sql = "UPDATE account SET balance = ? WHERE id = ?";
        return jdbcTemplate.update(sql, accountUpdateDto.balance(), id);
    }

    @Override
    public int delete(final Long id) {
        String sql = "DELETE FROM account WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    // 공통 RowMapper
    private RowMapper<Account> rowMapper() {
        return (rs, rowNum) -> new Account(
                rs.getLong("id"),
                rs.getString("account_number"),
                rs.getBigDecimal("balance"),
                rs.getLong("customer_id")
        );
    }

}

