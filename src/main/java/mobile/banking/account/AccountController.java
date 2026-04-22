package mobile.banking.account;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping()
    public ResponseEntity<List<Account>> getAccounts(){
        List<Account> accounts = accountService.findAccounts();
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(
            @PathVariable("id") final Long id
    ){
        try {
            Account account = accountService.findAccountById(id).orElseThrow(
                    () -> new IllegalArgumentException("Account not found with id: " + id)
            );
            return ResponseEntity.ok(account);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping()
    public ResponseEntity<Account> createAccount(
            @RequestBody final AccountDto accountDto
    ){
        Account account = accountService.createAccount(accountDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateAccount(
            @PathVariable final long id,
            @RequestBody final AccountUpdateDto accountUpdateDto
    ){
        try {
            accountService.findAccountById(id).orElseThrow(
                    () -> new IllegalArgumentException("Account not found with id: " + id)
            );
            int updateCount = accountService.updateAccount(id, accountUpdateDto);
            if (updateCount > 0) {
                return ResponseEntity.ok("Account with id " + id + " has been updated.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update account with id: " + id);
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccount(
            @PathVariable final long id
    ){
        try {
            accountService.findAccountById(id).orElseThrow(
                    () -> new IllegalArgumentException("Account not found with id: " + id)
            );
            accountService.removeAccount(id);
            return ResponseEntity.ok("Account with id " + id + " has been deleted.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<?> deposit(
            @PathVariable final long id,
            @RequestBody final AccountTransactionDto accountTransactionDto
    ){
        try {
            accountService.deposit(id, accountTransactionDto.amount());
            Account account = accountService.findAccountById(id).orElseThrow();
            return ResponseEntity.ok(account);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/withdraw")
    public ResponseEntity<?> withdraw(
            @PathVariable final long id,
            @RequestBody final AccountTransactionDto accountTransactionDto
    ){
        try {
            accountService.withdraw(id, accountTransactionDto.amount());
            Account account = accountService.findAccountById(id).orElseThrow();
            return ResponseEntity.ok(account);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

     @PostMapping("/{id}/transfer")
    public ResponseEntity<?> transfer(
            @PathVariable final Long id,
            @RequestBody final AccountTransferDto accountTransferDto
     ){
        try{
            accountService.transfer(id, accountTransferDto.toAccountId(), accountTransferDto.amount());
            Account account = accountService.findAccountById(id).orElseThrow();
            return ResponseEntity.ok(account);
        }
        catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
     }
}
