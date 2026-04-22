package mobile.banking.account.repository;

import mobile.banking.account.AccountHistory;
//import mobile.banking.account.AccountHistorySearchCondition;
import mobile.banking.account.TransactionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplateAccountHistoryRepository implements AccountHistoryRepository {

    private static final Logger log = LoggerFactory.getLogger(JdbcTemplateAccountHistoryRepository.class);

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateAccountHistoryRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void save(final AccountHistory accountHistory) {
        String sql = "INSERT INTO account_history (account_id, transaction_type, amount, balance) " +
                "VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                accountHistory.accountId(), accountHistory.transactionType().name(),
                accountHistory.amount(), accountHistory.balance());
    }

    @Override
    public List<AccountHistory> findAll() {
        String sql = "SELECT * FROM account_history ORDER BY transaction_time DESC";
        return jdbcTemplate.query(sql, accountHistoryRowMapper);
    }

    @Override
    public List<AccountHistory> findByAccountId(final Long accountId) {
        String sql = "SELECT * FROM account_history WHERE account_id = ? ORDER BY transaction_time DESC";
        return jdbcTemplate.query(sql, accountHistoryRowMapper, accountId);
    }

    @Override
    public List<AccountHistory> findByAccountNumber(final String accountNumber) {
        String sql = "SELECT AH.* " +
                "FROM account_history AH, account A " +
                "WHERE AH.account_id = A.id AND A.account_number = ? " +
                "ORDER BY AH.transaction_time DESC";
        return jdbcTemplate.query(sql, accountHistoryRowMapper, accountNumber);
    }

//    @Override
//    public List<AccountHistory> findByCondition(final AccountHistorySearchCondition condition) {
//        String sql = "SELECT AH.* " +
//                "FROM account_history AH, account A " +
//                "WHERE AH.account_id = A.id";
//
//        List<Object> params = new ArrayList<>();
//        sql += makeCondition(condition, params);
//
//        log.info(sql);
//        log.info(params.toString());
//
//        return jdbcTemplate.query(sql, accountHistoryRowMapper, params.toArray());
//    }

//    @Override
//    public BigDecimal getAccountBalance(final AccountHistorySearchCondition condition) {
//        String sql = "SELECT SUM(AH.amount) " +
//                "FROM account_history AH, account A " +
//                "WHERE AH.account_id = A.id ";
//
//        List<Object> params = new ArrayList<>();
//        sql += makeCondition(condition, params);
//
//        log.info(sql);
//        log.info(params.toString());
//
//        return jdbcTemplate.queryForObject(sql, BigDecimal.class, params.toArray());
//    }

//    private static String makeCondition(AccountHistorySearchCondition condition, List<Object> params) {
//        String condString = "";
//        if (condition.accountNumber() != null) {
//            condString += " AND A.account_number = ? ";
//            params.add(condition.accountNumber());
//
//        }
//        if (condition.duration() != null) {
//            if (condition.duration().matches("\\d+")) {
//                condString += " AND AH.transaction_time >= NOW() - INTERVAL '" + condition.duration() + "' DAY ";
//            } else {
//                throw new IllegalArgumentException("Duration must be a valid number");
//            }
//        } else {
//            LocalDate start = LocalDate.now().minusDays(7);
//            LocalDate end = LocalDate.now().plusDays(1);
//            if (condition.startDate() != null) {
//                start = LocalDate.parse(condition.startDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//            }
//            if (condition.endDate() != null) {
//                end = LocalDate.parse(condition.endDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//                end = end.plusDays(1);
//            }
//
//            condString += " AND AH.transaction_time BETWEEN ? AND ? ";
//            params.add(start);
//            params.add(end);
//        }
//        return condString;
//    }

    private final RowMapper<AccountHistory> accountHistoryRowMapper = (rs, rowNum) -> new AccountHistory(
            rs.getLong("account_id"),
            rs.getTimestamp("transaction_time").toLocalDateTime(),
            TransactionType.valueOf(rs.getString("transaction_type")),
            rs.getBigDecimal("amount"),
            rs.getBigDecimal("balance")
    );
}
