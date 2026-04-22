package mobile.banking.account;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/account-histories")
public class AccountHistoryController {
    private final AccountHistoryService accountHistoryService;

    public AccountHistoryController(AccountHistoryService accountHistoryService) {
        this.accountHistoryService = accountHistoryService;
    }

    @GetMapping()
    public ResponseEntity<List<AccountHistory>> getAccountHistories(){
        List<AccountHistory>histories = accountHistoryService.findHistories();
        return ResponseEntity.ok(histories);
    }

    //{{host}}/account-histories/search?accountNumber=1001-0001-1234
    @GetMapping("search")
    public ResponseEntity<List<AccountHistory>> getAccountHistories(
            @RequestParam("accountNumber") final String accountNumber
    ){
        List<AccountHistory>histories = accountHistoryService.findHistoriesByAccountNumber(accountNumber);
        return ResponseEntity.ok(histories);
    }
}
