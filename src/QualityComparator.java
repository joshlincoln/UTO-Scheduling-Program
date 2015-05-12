import java.util.Comparator;

public class QualityComparator implements Comparator<Schedule> {

	public int compare(Schedule s1, Schedule s2) {

		if (s1.getQuality() < s2.getQuality()) {
			return -1;
		}
		else if(s1.getQuality() == s2.getQuality()) {
			return 0;
		}
		return 1;
	}
}
