package nuclear.slitherio;

import java.math.BigInteger;
import java.util.Arrays;

@SuppressWarnings("serial")
public class uint256_t extends BigInteger{

	public static final BigInteger MAX_VALUE = new BigInteger("115792089237316195423570985008687907853269984665640564039457584007913129639935");
	synchronized public static uint256_t make(byte[] val) {
		uint256_t num=new uint256_t(SlitherS.reverse(val));
		while(num.signum()==-1){
			num=fromBigInt(num.add(MAX_VALUE));
		}
		num=fromBigInt(num.and(MAX_VALUE));
		return num;
	}
	public uint256_t(String string) {
		super(string);
	}
	public uint256_t(byte[] x) {
		super(x);
	}
	public byte[] littleEndian() {
		return Arrays.copyOf(SlitherS.reverse(toByteArray()),32);
	}
	synchronized public static uint256_t fromBigInt(BigInteger bi) {
		return new uint256_t(bi.toString());
	}
}
