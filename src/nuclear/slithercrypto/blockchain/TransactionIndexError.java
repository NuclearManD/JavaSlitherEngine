package nuclear.slithercrypto.blockchain;

@SuppressWarnings("serial")
public class TransactionIndexError extends Exception {

	public TransactionIndexError() {
	}

	public TransactionIndexError(String arg0) {
		super(arg0);
	}

	public TransactionIndexError(Throwable arg0) {
		super(arg0);
	}

	public TransactionIndexError(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public TransactionIndexError(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
