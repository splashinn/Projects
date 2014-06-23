// Checksum.java
// (C) 2014 splashinn
// Checks if a 16-number code is valid for a credit card

import java.util.*;

public class Checksum{
	public static void main(String[] args){
		Scanner sc = new Scanner(System.in);

		System.out.println("Enter credit card number without spaces: ");
		String codeString = sc.nextLine();
		System.out.println();

		char[] codeChar = codeString.toCharArray();
		int[] number = new int[codeChar.length];
		for (int i=0; i<codeChar.length;i++){
			number[i]=Character.digit(codeChar[i],10);
		}
		int sum=0;

		for (int i=0; i<16;i+=2){
			number[i]=number[i]*2;
			if (number[i]>=10) {
				sum+=1+number[i]%10;
			} else {
				sum+=number[i];
			}
			sum+=number[i+1];
		}
		if (sum%10==0) System.out.println("Code is valid");
		else System.out.println("Code is invalid");
	}

}
