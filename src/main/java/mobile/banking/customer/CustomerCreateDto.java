package mobile.banking.customer;

public record CustomerCreateDto(
        String name,
        String email,
        String cellphone,
        String address
) {
}
