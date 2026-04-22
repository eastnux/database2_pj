package mobile.banking.account;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AccountHistory(
        Long accountId,
        LocalDateTime transactionTime,
        TransactionType transactionType,
        BigDecimal amount,
        BigDecimal balance
) {
}
