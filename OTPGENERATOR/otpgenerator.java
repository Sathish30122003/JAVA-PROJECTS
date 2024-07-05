import java.util.Random;

public class otpgenerator {
	static char[] OTP(int len) {
		System.out.println("Generating OTP using random():");
		System.out.print("Your OTP is : ");
		String numbers="0123456789";
		Random rn=new Random();
		char[] otp=new char[len];
		for(int i=0;i<len;i++) {
			otp[i]=numbers.charAt(rn.nextInt(numbers.length()));
		}
		return otp;
	}
	public static void main(String[] args) {
		System.out.println(OTP(4));
	}
}
