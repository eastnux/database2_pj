package mobile.banking.account;

import java.math.BigDecimal;

public record AccountTransferDto(
        Long toAccountId,
        BigDecimal amount
    )
{ }
