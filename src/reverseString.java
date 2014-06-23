// reverseString.java
// (C) 2014 splashinn
// Reverses the input string and prints it out.

import java.util.Scanner;

public class reverseString {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);

		System.out.println("Insert the String you would like to reverse:");
		  String str = scan.nextLine();
		    scan.close();

		String reversedStr = new StringBuilder(str).reverse().toString();

		System.out.println("Initial String: " + str);
		System.out.println("Reversed String: " + reversedStr);
	}
}
