package mobile.banking.account.repository;

import mobile.banking.account.*;

import java.math.BigDecimal;
import java.util.List;

public interface AccountHistoryRepository {
    void save(final AccountHistory accountHistory);

    List<AccountHistory> findAll();

//    List<AccountHistory> findByCondition(final AccountHistorySearchCondition condition);

    List<AccountHistory> findByAccountId(final Long accountId);

    List<AccountHistory> findByAccountNumber(final String accountNumber);

//    BigDecimal getAccountBalance(final AccountHistorySearchCondition condition);
}