package cl.APIREST.exception;

public class EmailAlreadyRegisteredException extends RuntimeException {
    // excepcion personalizada
    public EmailAlreadyRegisteredException(String message) {
        super(message);
    }




}
