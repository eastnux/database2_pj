package mobile.banking.customer;

public record Customer (
    Long id,
    String name,
    String email,
    String cellphone,
    String address
) {

}
