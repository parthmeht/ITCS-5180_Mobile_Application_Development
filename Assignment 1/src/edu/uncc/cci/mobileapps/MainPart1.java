/*
 * Created By : Parth Mehta
 * Student Id : 801057625
 */

package edu.uncc.cci.mobileapps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainPart1 {
	/*
	 * Question 1: - In this question you will use the Data.users array that
	 * includes a list of users. Formatted as :
	 * firstname,lastname,age,email,gender,city,state - Create a User class that
	 * should parse all the parameters for each user. - Insert each of the users in
	 * a list. - Print out the TOP 10 oldest users.
	 */

	public static void main(String[] args) {
		List<User> al = new ArrayList<User>();
		for (String str : Data.users) {
			String[] temp = str.split(",");
			User user = new User(temp[0], temp[1], Integer.parseInt(temp[2]), temp[3], temp[4], temp[5], temp[6]);
			al.add(user);
		}
		CompareMainPart1 ct = new CompareMainPart1();
		Collections.sort(al, ct);
		for (int i = 0; i < 10; i++) {
			System.out.println(al.get(i));
		}
	}
}

class CompareMainPart1 implements Comparator<User> {

	@Override
	public int compare(User o1, User o2) {
		return o2.getAge() - o1.getAge();
	}

}