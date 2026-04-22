package mobile.banking.account.repository;

import mobile.banking.account.Account;
import mobile.banking.account.AccountDto;
import mobile.banking.account.AccountUpdateDto;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {

    Account save(final AccountDto accountDto);

    Optional<Account> findById(final Long id);

    List<Account> findAll();

    int update(Long id, final AccountUpdateDto accountUpdateDto);

    int delete(final Long id);
}

