/*
 * Created By : Parth Mehta
 * Student Id : 801057625
 */

package edu.uncc.cci.mobileapps;

import java.util.HashSet;
import java.util.Set;

public class MainPart3 {
	/*
	 * Question 3: - In this question you will use the Data.users and
	 * Data.otherUsers array that includes a list of users. Formatted as :
	 * firstname,lastname,age,email,gender,city,state - Create a User class that
	 * should parse all the parameters for each user. - The goal is to print out the
	 * users that are exist in both the Data.users and Data.otherUsers. Two users
	 * are equal if all their attributes are equal. - Print out the list of users
	 * which exist in both Data.users and Data.otherUsers.
	 */



	public static void main(String[] args) {
		Set<User> users = new HashSet<User>();
		for (String str : Data.users) {
			String[] temp = str.split(",");
			User user = new User(temp[0], temp[1], Integer.parseInt(temp[2]), temp[3], temp[4], temp[5], temp[6]);
			users.add(user);
		}
		for (String str : Data.otherUsers) {
			String[] temp = str.split(",");
			User user = new User(temp[0], temp[1], Integer.parseInt(temp[2]), temp[3], temp[4], temp[5], temp[6]);
			if (users.contains(user)) {
				System.out.println(user);
			}
		}

	}
}