import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Student implements Comparator<Student>, Comparable<Student>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Fields
	String name;
	ArrayList<ArrayList<Integer>> shiftAvail = new ArrayList<ArrayList<Integer>>();
	String[][] schedule = new String[37][7];
	String[] timeSlot = { "6:00am", "6:30am", "7:00am", "7:30am", "8:00am",
			"8:30am", "9:00am", "9:30am", "10:00am", "10:30am", "11:00am",
			"11:30am", "12:00pm", "12:30pm", "1:00pm", "1:30pm", "2:00pm",
			"2:30pm", "3:00pm", "3:30pm", "4:00pm", "4:30pm", "5:00pm",
			"5:30pm", "6:00pm", "6:30pm", "7:00pm", "7:30pm", "8:00pm",
			"8:30pm", "9:00pm", "9:30pm", "10:00pm", "10:30pm", "11:00pm",
			"11:30pm" };
	ArrayList<String> officesWorked = new ArrayList<String>();
	String office;
	int[][] dayAvail = new int[14][7];
	String[][] finalSchedule = new String[37][7];
	int dayWeight;
	int longestShift;
	int experience;
	int desiredHours;
	int hoursRemaining;
	int startTime;
	int endTime;
	int maxHours;
	int scheduledHours;
	int hoursPerDay;
	int hoursPerWeek;
	boolean scheduled;
	boolean[] closed = new boolean[7];
	boolean[] opened = new boolean[7];;

	// constructor
	public Student() {

	}

	public Student(ArrayList<ArrayList<Integer>> avail, String name,
			int desiredHours) {

		this.shiftAvail = avail;
		this.name = name;
		this.desiredHours = desiredHours * 2;

		hoursRemaining = desiredHours - scheduledHours;
		hoursPerWeek = 0;
		experience = 0;
		maxHours = 50;
		scheduledHours = 0;
		hoursPerDay = 0;
		endTime = 0;
		scheduled = false;
	}

	// setter methods
	public void setShiftAvail(ArrayList<ArrayList<Integer>> shiftAvail) {
		this.shiftAvail = shiftAvail;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addOfficesWorked(String office) {
		officesWorked.add(office);
	}

	public void removeOfficesWorked(String office) {
		officesWorked.add(office);
	}

	public void setExperience(int e) {
		this.experience = e;
	}

	public void setDesieredHours(int h) {
		this.desiredHours = h;
	}

	public void setHoursRemaining(int r) {
		this.hoursRemaining = r;
	}

	public void setMaxHours(int m) {
		this.maxHours = m;
	}

	public void setOffice(String s) {
		this.office = s;
	}

	// getter methods
	public String getName() {
		return name;
	}

	public ArrayList<ArrayList<Integer>> getShiftAvail() {
		return shiftAvail;
	}

	public String[][] getSchedule() {
		return schedule;
	}
	
	public String getOffice() {
		return office;
	}

	public ArrayList<String> getOfficesWorked() {
		return officesWorked;
	}

	public int getExperience() {
		return experience;
	}

	public int getDesiredHours() {
		return desiredHours;
	}

	public int getMaxHours() {
		return maxHours;
	}

	public int getLongestShift() {
		return longestShift;
	}

	// methods
	public void printStudent() {
		// print student availability
		// printStudentAvailability();

		// print student schedule
		// printStudentSchedule();

	}

	public void printOfficesWorked() {
	}

	public void printStudentAvailability() {
		System.out.println("Student : " + name);
		for (int i = 0; i < shiftAvail.size(); i++) {
			for (int j = 0; j < shiftAvail.get(0).size(); j++) {
				System.out.print(shiftAvail.get(i).get(j) + " | ");
			}
			System.out.println();
		}

	}

	public void makeFinalSchedule() {
		for (int i = 0; i < 37; i++) {
			for (int j = 0; j < 7; j++) {
				if (schedule[i][j] != null) {
					String value = schedule[i][j];
					if (schedule[i][j].equals("30 break")
							|| schedule[i][j].equals("60 break")) {
						finalSchedule[i][j] = value;
					} else {
						String time = timeSlot[i];
						value = value + " " + time;
						finalSchedule[i][j] = value;
					}
					// }
					// if(schedule[i][j] != null) {
					// //System.out.print(schedule[i][j] + "  | \t");
					// }
					// else
					// System.out.print("\t\t | \t");
					// }
					// System.out.println();
				}
			}
		}
	}

	public void printStudentSchedule() {
		makeFinalSchedule();
		System.out.println("Student: " + name + " Schedule" + "\n");
		System.out.println("Desired Hours: " + desiredHours / 2 + "\n");
		System.out.println("Scheduled Hours: " + scheduledHours / 2 + "\n");
		for (int i = 0; i < 37; i++) {
			for (int j = 0; j < 7; j++) {
				if (finalSchedule[i][j] != null) {
					System.out.print(finalSchedule[i][j] + "  | \t");
				} else
					System.out.print("\t\t | \t");
			}
			System.out.println("\n");
		}
	}

	public ArrayList<Integer> getDayAvail(Day day) {
		ArrayList<Integer> alDayAvl = new ArrayList<Integer>();
		for (int i = 0; i < 37; i++) {
			alDayAvl.add(shiftAvail.get(i).get(day.ordinal()));
		}
		return alDayAvl;
	}

	public int compare(Student s1, Student s2) {
		if (s1.longestShift >= s1.longestShift) {
			return 1;
		}
		return 0;
	}


	public boolean equals(Object Student) {

		if (this.name.equals(((Student) Student).getName())) {
			return true;
		}
		return false;

	}

	@Override
	public int compareTo(Student o) {
		// TODO Auto-generated method stub
		return 0;
	}
}
