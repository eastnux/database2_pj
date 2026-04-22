package mobile.banking.account;

import java.math.BigDecimal;

public record AccountUpdateDto(
        BigDecimal balance
) {
}
