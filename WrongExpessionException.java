class WrongExpressionException extends Exception {
    public String message;
    WrongExpressionException(String message) {
	this.message = message;
    }
}
