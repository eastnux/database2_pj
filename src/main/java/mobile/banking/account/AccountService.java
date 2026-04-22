package mobile.banking.account;

import mobile.banking.account.repository.AccountHistoryRepository;
import mobile.banking.account.repository.AccountRepository;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class AccountService {

    private static final Logger log = LoggerFactory.getLogger(AccountService.class);

    private final AccountRepository accountRepository;
    private final AccountHistoryRepository accountHistoryRepository;

    public AccountService(final AccountRepository accountRepository,
                          final AccountHistoryRepository accountHistoryRepository) {
        this.accountRepository = accountRepository;
        this.accountHistoryRepository = accountHistoryRepository;
    }

    public Account createAccount(final AccountDto accountDto) {
        return accountRepository.save(accountDto);
    }

    public Optional<Account> findAccountById(final Long id) {
        return accountRepository.findById(id);
    }

    public List<Account> findAccounts() {
        return accountRepository.findAll();
    }

    public int updateAccount(Long id, final AccountUpdateDto accountUpdateDto) {
        return accountRepository.update(id, accountUpdateDto);
    }

    public int removeAccount(final Long id) {
        return accountRepository.delete(id);
    }

    @Transactional
    public void deposit(final Long id, final BigDecimal amount){
        Account account = accountRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Account not found with id: " + id)
        );
        BigDecimal newBalance = account.balance().add(amount);
        accountRepository.update(id, new AccountUpdateDto(newBalance)); //UPDATE

        if(true) {
            throw new RuntimeException("강제 예외 발생"); //트랜젝션 롤백 테스트용
        }
        AccountHistory accountHistory = new AccountHistory( //INSERT
                account.id(),
                LocalDateTime.now(),
                TransactionType.DEPOSIT,
                amount,
                newBalance
        );
        accountHistoryRepository.save(accountHistory);
    }

    @Transactional
    public void withdraw(final Long id, final BigDecimal amount){
        Account account = accountRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Account not found with id: " + id)
        );

        if (account.balance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds in account with id: " + id);
        }

        BigDecimal newBalance = account.balance().subtract(amount);
        accountRepository.update(id, new AccountUpdateDto(newBalance));

        AccountHistory accountHistory = new AccountHistory(
                account.id(),
                LocalDateTime.now(),
                TransactionType.WITHDRAW,
                amount,
                newBalance
        );
        accountHistoryRepository.save(accountHistory);
    }

    @Transactional
    public void transfer(final Long fromId, final Long toId, final BigDecimal amount){
        Account fromAccount = accountRepository.findById(fromId).orElseThrow(
                () -> new IllegalArgumentException("Source account not found with id: " + fromId)
        );
        BigDecimal newFromBalance = fromAccount.balance().subtract(amount);

        if (newFromBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Insufficient funds in source account with id: " + fromId);
        }

        AccountHistory fromAccountHistory = new AccountHistory(
                fromAccount.id(),
                LocalDateTime.now(),
                TransactionType.WITHDRAW,
                amount,
                newFromBalance
        );
        accountHistoryRepository.save(fromAccountHistory);

        
        Account toAccount = accountRepository.findById(toId).orElseThrow(
                () -> new IllegalArgumentException("Destination account not found with id: " + toId)
        );
        BigDecimal newToBalance = toAccount.balance().add(amount);
//        accountRepository.update(fromId, new accountUpdateDto(newFromBalance));
        accountRepository.update(toId, new AccountUpdateDto(newToBalance));

        AccountHistory toAccountHistory = new AccountHistory(
                toAccount.id(),
                LocalDateTime.now(),
                TransactionType.DEPOSIT,
                amount,
                newToBalance
        );
        accountHistoryRepository.save(toAccountHistory);

    }
}

