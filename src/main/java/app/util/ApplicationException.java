package app.util;

public class ApplicationException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public ApplicationException(String s) {
		super(s);
	}
	public ApplicationException(Throwable e) {
		super(e);
	}
}
