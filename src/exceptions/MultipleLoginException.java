package exceptions;

public class MultipleLoginException extends Exception {
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public MultipleLoginException() { super(); }
	  public MultipleLoginException(String message) { super(message); }
	  public MultipleLoginException(String message, Throwable cause) { super(message, cause); }
	  public MultipleLoginException(Throwable cause) { super(cause); }
}
