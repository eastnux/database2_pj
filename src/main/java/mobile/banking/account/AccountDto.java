package mobile.banking.account;

import java.math.BigDecimal;

public record AccountDto(
        String accountNumber,
        BigDecimal balance,
        Long customerId
) {
}
