// Java File IO API
import java.io.Serializable;
import java.util.*;

import org.apache.commons.collections4.comparators.ComparatorChain;

public class Schedule implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Fields
	ArrayList<Student> TCs = new ArrayList<Student>();
	ArrayList<Office> offices = new ArrayList<Office>();
	ArrayList<ArrayList<Integer>> dayAvail = new ArrayList<ArrayList<Integer>>();
	ArrayList<Integer> dayReq = new ArrayList<Integer>();
	ArrayList<Student> potentialTCs = new ArrayList<Student>();
	int highThresh;
	int startSchedule;
	int endSchedule;
	float shortestShift;

	// constructors
	public Schedule(ArrayList<Student> TCs, ArrayList<Office> offices) {
		this.TCs = TCs;
		this.offices = offices;
	}

	public Schedule() {

	}

	// setters / getters

	public int getHighThresh() {
		return highThresh;
	}

	public void setHighThresh(int highThresh) {
		this.highThresh = highThresh;
	}

	public int getStartSchedule() {
		return startSchedule;
	}

	public void setStartSchedule(int startSchedule) {
		this.startSchedule = startSchedule;
	}

	public int getEndSchedule() {
		return endSchedule;
	}

	public void setEndSchedule(int endSchedule) {
		this.endSchedule = endSchedule;
	}

	public float getShortestShift() {
		return shortestShift;
	}

	public void setShortestShift(float shortestShift) {
		this.shortestShift = shortestShift;
	}

	public void printStudentNames() {
		System.out.println("Total number of TCs = " + TCs.size());
		for (int i = 0; i < TCs.size(); i++) {
			System.out.println(TCs.get(i).name);
		}

	}

	public void setDayAvail(List<Student> TCs, Day day) {

		for (Student student : TCs) {
			dayAvail.add(student.getDayAvail(day));
		}
	}

	public void setDayOfficeReq(List<Office> offices, Day day) {
		for (Office office : offices) {
			dayAvail.add(office.getDayReq(day));
		}
		System.out.println();
	}

	/*
	 * Algorithm : Iterative greedy longest shift
	 */

	public void scheduling(ArrayList<Student> TCs, Office office) {

		ComparatorChain<Student> chain = new ComparatorChain<Student>();
		chain.addComparator(new CustomComparator());
		chain.addComparator(new LongestShift());
		//chain.addComparator(new StartTime());
		Collections.sort(TCs, chain);
		for (Day day : Day.values()) {
			for (Student s1 : TCs) {
				s1.hoursPerDay = 0;
			}
			getLongestShift(TCs, office, day, highThresh);
			/*
			 * int count = 0; for (Student student1 : TCs) {
			 * System.out.println("Name: " + student1.name + " Longest shift: "
			 * + student1.longestShift + " Start time: " + student1.startTime +
			 * " End time: " + student1.endTime); count++; }
			 * System.out.println(count); System.out.println(
			 * "-------------------------------------------------------------------------"
			 * ); break;
			 */
			fillDayOfficeReq(TCs, office, day);
		}
	}

	public void getLongestShift(ArrayList<Student> students, Office office,
			Day day, int thresh) {
		for (Student student : TCs) {
			getLongestShift(student, office, day, highThresh);
		}
	}

	public void getLongestShift(Student TC, Office office, Day day, int thresh) {

		TC.endTime = 0;
		TC.startTime = 0;
		TC.longestShift = 0;
		int dayWeight = 0;
		int count = 0;
		int add = 0;

		for (int i = 0; i < 37; i++) {
			if (TC.shiftAvail.get(i).get(day.ordinal()) == 1
					&& office.officeReq.get(i).get(day.ordinal()) >= 1) {
				dayWeight++;
			}
		}
		for (int i = 0; i < 37; i++) {
			if (TC.shiftAvail.get(i).get(day.ordinal()) == 0) {
				count = 0;
			} else if (TC.shiftAvail.get(i).get(day.ordinal()) == 1
					&& office.officeReq.get(i).get(day.ordinal()) >= 1) {
				count++;
				add++;
				if (count > thresh) {
					break;
				}
				if (count > TC.longestShift) {
					TC.endTime = i;
					TC.longestShift = count;
					TC.startTime = TC.endTime - count;
				}
			}
		}
		if (add == 0) {
			TC.endTime = 0;
			TC.longestShift = 0;
			TC.startTime = 0;
		}
		TC.dayWeight = dayWeight;
	}

	public void getLongestOfficeShift(Office office, Day day, int thresh) {
		ArrayList<Integer> officeReq = office.getDayReq(day);
		office.endShift = 0;
		office.longestShift = 0;
		office.startShift = 0;
		int count = 0;
		int add = 0;
		for (int i = 0; i < 37; i++) {
			if (officeReq.get(i) == 0) {
				count = 0;
			} else if (officeReq.get(i) >= 1) {
				count++;
				add++;
				if (count > thresh) {
					break;
				}
				if (count > office.longestShift) {
					office.endShift = i + 1;
					office.longestShift = count;
					office.startShift = office.endShift - count;
				}
			}
		}
		if (add == 0) {
			office.endShift = 0;
			office.longestShift = 0;
			office.startShift = 0;
		}
	}

	public void scheduleTC(Student TC, Office office, Day day) {

		if (TC == null) {
			return;
		}

		// determine shift interval
		int start = office.startShift;
		int end = office.endShift;
		if (TC.startTime >= office.startShift) {
			start = TC.startTime;
		}
		if (TC.endTime <= office.endShift) {
			end = TC.endTime;
		}

		TC.schedule[start][day.ordinal()] = office.name;
		TC.schedule[end][day.ordinal()] = office.name;
		office.schedule[start][day.ordinal()] += TC.name;
		office.schedule[end][day.ordinal()] += TC.name;
		
//		if(TC.schedule[end][day.ordinal()].equals(TC.schedule[end-1][day.ordinal()]) || (TC.schedule[start][day.ordinal()].equals(TC.schedule[start-1][day.ordinal()]))) {
//			TC.schedule[end][day.ordinal()] = null;
//			TC.schedule[end+1][day.ordinal()] = null;
//			TC.schedule[start][day.ordinal()] = null;
//			TC.schedule[start][start-1] = null;
//			
//		}
		TC.scheduledHours += (end - start);
		TC.hoursPerDay += (end - start);
		TC.hoursPerWeek += (end - start);

		// check for breaks
		if (((end - start) >= 12) && ((end - start) < 18)) {
			TC.schedule[(start + 7)][day.ordinal()] = "30 break";
			TC.scheduledHours -= 1;
			TC.hoursPerDay -= 1;
			TC.hoursPerWeek -= 1;

		} else if ((end - start) >= 18) {
			TC.schedule[(start + 9)][day.ordinal()] = "60 break";
			TC.scheduledHours -= 2;
			TC.hoursPerDay -= 2;
			TC.hoursPerWeek -= 2;
		}

		// set opening / closing conditions
		if (start <= startSchedule) {
			TC.opened[day.ordinal()] = true;
		} else if (end >= endSchedule) {
			TC.closed[day.ordinal()] = true;
		}
		for (int i = start + 1; i <= end; i++) {
			TC.shiftAvail.get(i).set(day.ordinal(), 0);
			int officeReq = office.officeReq.get(i).get(day.ordinal());
			office.officeReq.get(i).set(day.ordinal(), officeReq - 1);
		}
		if((TC.scheduledHours/TC.desiredHours) >= .80) {
			TC.scheduled = true;
		}
	}

	public Student chooseTC(ArrayList<Student> Students, Office office, Day day) {

		ArrayList<Student> uniqueTCs = new ArrayList<Student>();
		Student chosenTC = null;
		ComparatorChain<Student> chain = new ComparatorChain<Student>();
		chain.addComparator(new CustomComparator());
		chain.addComparator(new LongestShift());
		//chain.addComparator(new StartTime());
		Collections.sort(TCs, chain);
		
		// schedule special office
		for (int i = 0; i < GUI.officeNames.size(); i++) {
			if (office.name.equals(GUI.officeNames.get(i))) {
				for (Student TC : Students) {
					if (TC.getOffice().equals(office.name)) {
						uniqueTCs.add(TC);
					}
				}
				chosenTC = meetsRequirement(uniqueTCs, office, day);
				return chosenTC;

			}
		}
		
		// schedule generic office
		chosenTC = meetsRequirement(Students, office, day);
		return chosenTC;
	}

	public Student meetsRequirement(ArrayList<Student> TCs, Office office, Day day) {

		int start;
		int end;
		int workableShift;
		boolean opening = false;
		boolean closing = false;
		ArrayList<Student> elgibleTCs = new ArrayList<Student>();
		Student chosenTC = null;
		ComparatorChain<Student> chain = new ComparatorChain<Student>();
		chain.addComparator(new CustomComparator());
		chain.addComparator(new LongestShift());
		//chain.addComparator(new StartTime());
		Collections.sort(TCs, chain);
		for (Student TC : TCs) {
			if (TC.longestShift >= shortestShift
					&& TC.hoursPerDay <= highThresh - TC.longestShift
					&& TC.hoursPerWeek <= TC.desiredHours - TC.longestShift
					&& !(TC.endTime <= office.startShift || TC.startTime >= office.endShift)) {
			}
			elgibleTCs.add(TC);
		}
		for (Student elgibleTC : elgibleTCs) {
			start = office.startShift;
			end = office.endShift;
			if (elgibleTC.startTime >= office.startShift) {
				start = elgibleTC.startTime;
			}
			if (elgibleTC.endTime <= office.endShift) {
				end = elgibleTC.endTime;
			}
			workableShift = end - start;
			// opening / closing conditions of shift
			if (start <= startSchedule) {
				opening = true;
			}
			if (end >= endSchedule) {
				closing = true;
			}
			if ((closing == true && elgibleTC.opened[day.ordinal()] == true)
					|| (opening == true && elgibleTC.closed[day.ordinal()] == true)) {
				continue;

			} else {
				if (workableShift >= shortestShift
						&& elgibleTC.hoursPerWeek <= elgibleTC.desiredHours
								- workableShift
						&& elgibleTC.hoursPerDay <= highThresh - workableShift) {
					chosenTC = elgibleTC;
					return chosenTC;
				}

			}
		}
		return chosenTC;

	}

	public void fillDayOfficeReq(ArrayList<Student> student, Office office,
			Day day) {

		int thresh = highThresh;
		getLongestOfficeShift(office, day, thresh);
		Student chosenTC = null;
		// System.out.println("Name: " + office.name + " Longest shift: " +
		// office.longestShift + " Start time: " + office.startShift +
		// " End time: " + office.endShift);
		while (office.longestShift >= shortestShift) {
			// get starting time for office
			while (thresh >= shortestShift) {
				chosenTC = chooseTC(student, office, day);
				if (chosenTC != null) {
					break;
				}
				// System.out.println(office.name + " Longest shift: "
				// + office.longestShift + " Starting time: "
				// + office.startShift + " End time: " + office.endShift
				// + day);
				thresh--;
				getLongestShift(student, office, day, thresh);
				getLongestOfficeShift(office, day, thresh);
			}
			if (chosenTC == null) {
				break;
			}
			thresh = highThresh;
			scheduleTC(chosenTC, office, day);
			ComparatorChain<Student> chain = new ComparatorChain<Student>();
			chain.addComparator(new CustomComparator());
			chain.addComparator(new LongestShift());
			//chain.addComparator(new StartTime());
			Collections.sort(TCs, chain);
			// System.out.println(chosenTC.shiftAvail.get(0).get(day.ordinal()));
			getLongestShift(chosenTC, office, day, thresh);
			getLongestOfficeShift(office, day, thresh);
			// System.out.println(office.officeReq.get(0).get(day.ordinal()));
		}
	}
}