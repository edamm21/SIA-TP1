public class WrongBoardException extends Exception {

    private String message;

    public WrongBoardException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
