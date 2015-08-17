package exceptions;

public class IncorrectPasswordException extends Exception {
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public IncorrectPasswordException() { super(); }
	  public IncorrectPasswordException(String message) { super(message); }
	  public IncorrectPasswordException(String message, Throwable cause) { super(message, cause); }
	  public IncorrectPasswordException(Throwable cause) { super(cause); }
}
