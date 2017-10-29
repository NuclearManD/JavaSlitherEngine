package nuclear.slitherio;

import java.math.BigInteger;
import java.util.Arrays;

@SuppressWarnings("serial")
public class uint256_t extends BigInteger{

	public uint256_t(byte[] val) {
		super(SlitherS.reverse(val));
	}
	public uint256_t(String string) {
		super(string);
	}
	public byte[] littleEndian() {
		return Arrays.copyOf(SlitherS.reverse(toByteArray()),32);
	}
}
