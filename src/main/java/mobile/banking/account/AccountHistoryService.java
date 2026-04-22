package mobile.banking.account;

import mobile.banking.account.repository.AccountHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

public class AccountHistoryService {

    private static final Logger log = LoggerFactory.getLogger(AccountHistoryService.class);

    private final AccountHistoryRepository accountHistoryRepository;

    public AccountHistoryService(final AccountHistoryRepository accountHistoryRepository) {
        this.accountHistoryRepository = accountHistoryRepository;
    }

//    public List<AccountHistory> findHistories(final AccountHistorySearchCondition condition) {
//        // return accountHistoryRepository.findAll();
//        return accountHistoryRepository.findByCondition(condition);
//    }
    public List<AccountHistory> findHistories() {
        return accountHistoryRepository.findAll();
    }
    public List<AccountHistory> findHistoriesByAccountNumber(final String accountNumber) {
        return accountHistoryRepository.findByAccountNumber(accountNumber);
    }

//    public BigDecimal getAccountBalance(final AccountHistorySearchCondition condition) {
//        return accountHistoryRepository.getAccountBalance(condition);
//    }
}