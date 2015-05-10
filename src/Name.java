import java.util.Comparator;




public class Name implements Comparator<Student> {

	public int compare(Student s1, Student s2) {

		if (s1.name.compareTo(s2.name)< 1) {
			return -1;
		}
		else if(s1.name.compareTo(s2.name) == 0) {
			return 0;
		}
		return 1;
	}
}