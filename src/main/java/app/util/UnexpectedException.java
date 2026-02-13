package app.util;

public class UnexpectedException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public UnexpectedException(Throwable e) {
		super(e);
	}
}
