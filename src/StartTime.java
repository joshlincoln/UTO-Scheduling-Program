import java.util.Comparator;

public class StartTime implements Comparator<Student> {

	public int compare(Student s1, Student s2) {

		if (s1.longestShift < s2.longestShift) {
			return 1;
		}
		else if(s1.longestShift == s2.longestShift) {
			return 0;
		}
		return -1;
	}
}
