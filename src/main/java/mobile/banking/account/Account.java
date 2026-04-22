package mobile.banking.account;

import java.math.BigDecimal;

public record Account (
        Long id,
        String accountNumber,
        BigDecimal balance,
        Long customerId
){

}
