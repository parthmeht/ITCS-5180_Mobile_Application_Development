/*
 * Created By : Parth Mehta
 * Student Id : 801057625
 */

package edu.uncc.cci.mobileapps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class MainPart2 {
	/*
	 * Question 2: - In this question you will use the Data.users array that
	 * includes a list of users. Formatted as :
	 * firstname,lastname,age,email,gender,city,state - Create a User class that
	 * should parse all the parameters for each user. - The goal is to count the
	 * number of users living each state. - Print out the list of State, Count order
	 * in ascending order by count.
	 */

	public static void main(String[] args) {
		HashMap<String, Integer> hm = new HashMap<String, Integer>();
		for (String str : Data.users) {
			String[] temp = str.split(",");
			User user = new User(temp[0], temp[1], Integer.parseInt(temp[2]), temp[3], temp[4], temp[5], temp[6]);
			if (hm.containsKey(user.getState())) {
				hm.put(user.getState(), hm.get(user.getState()) + 1);
			} else {
				hm.put(user.getState(), 1);
			}
		}
		ArrayList<Map.Entry<String, Integer>> list = new ArrayList(hm.entrySet());
		CompareMainPart2 ct2 = new CompareMainPart2();
		Collections.sort(list, ct2);
		System.out.println(list);
	}
}

class CompareMainPart2 implements Comparator<Map.Entry<String, Integer>> {

	@Override
	public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
		return o1.getValue() - o2.getValue();
	}

}