import java.util.Comparator;




public class CustomComparator implements Comparator<Student> {

	public int compare(Student s1, Student s2) {

		if (s1.hoursPerWeek < s2.hoursPerWeek) {
			return -1;
		}
		else if(s1.hoursPerWeek == s2.hoursPerWeek) {
			return 0;
		}
		return 1;
	}
}