import java.io.Serializable;
import java.util.ArrayList;


public class Office implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Fields
	String name;
	int [][] shiftRequirement;
	ArrayList<ArrayList<Integer>> officeReq = new ArrayList<ArrayList<Integer>>();
	String [][] schedule = new String[37][7];
	String [] timeSlot = {"6:00am", "6:30am", "7:00am", "7:30am", "8:00am", "8:30am", "9:00am", "9:30am", "10:00am", "10:30am", "11:00am", "11:30am", "12:00pm", "12:30pm", "1:00pm", "1:30pm", "2:00pm", "2:30pm", "3:00pm", "3:30pm", "4:00pm", "4:30pm", "5:00pm", "5:30pm", "6:00pm", "6:30pm", "7:00pm", "7:30pm", "8:00pm", "8:30pm", "9:00pm", "9:30pm", "10:00pm", "10:30pm", "11:00pm", "11:30pm"};
	ArrayList<String> offices = new ArrayList<String>();
	int startShift;
	int endShift;
	int longestShift;
	int type = 0;
	
	
	// constructor
	public Office() {
		
	}
	public Office(ArrayList<ArrayList<Integer>> officeReq , String name) {
		this.officeReq = officeReq;
		this.name = name;
	}
	
	
	// setters
	public void setOfficeReq(ArrayList<ArrayList<Integer>> shiftRequirement) {
		this.officeReq = shiftRequirement;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	// getters
	public ArrayList<ArrayList<Integer>> getOfficeReq(){ 
		return officeReq;
	}
	
	
	
	// methods
	
	public float getScheduledHours() {
		float scheduledHours = 0;
		for(int i = 0; i < officeReq.size(); i++) {
			for(int j = 0; j <officeReq.get(0).size(); j++) {
				if(officeReq.get(i).get(j) >= 0)
				scheduledHours += officeReq.get(i).get(j);
			}
		}
		return scheduledHours/2;
	}
	public void printOffice() {
		System.out.println("Office name: " + name);
		for(int i = 0; i < officeReq.size()-2; i++) {
			System.out.print(timeSlot[i]+ "\t | ");
			for(int j = 0; j < officeReq.get(0).size(); j++) {		
				System.out.print(officeReq.get(i).get(j) + " | ");
			}
			System.out.println();
		}
	}
	
	public ArrayList<Integer> getDayReq(Day day){
		ArrayList<Integer> alDayReq = new ArrayList<Integer>();
		for(int i = 0; i < 37; i++) {
			alDayReq.add(officeReq.get(i).get(day.ordinal()));
		}
	return alDayReq;
	}
	
	public void printOfficeSchedule() {	
		System.out.println("Office name: " + name + " SCHEDULE");

		for(int i = 0; i < 37; i++) {
			for(int j = 0; j < 7; j++) {
				if(schedule[i][j] != null) {
					String value = schedule[i][j];
					String time = timeSlot[i];
					value = value + " " + time;
					schedule[i][j] = value;
				}
				if(schedule[i][j] != null) {
				System.out.println(schedule[i][j] + "  | \t");	
				}
				else
					System.out.print(schedule[i][j] + "\t\t | \t");
			}
			System.out.println();
		}
	}
	
	public void printDayReq() {
		
		ArrayList<Integer> allDayReq = new ArrayList<Integer>();
		for(Day day : Day.values()) {
			allDayReq = getDayReq(day);
			for(int i = 0; i < allDayReq.size(); i++) {
				System.out.println(day);
				System.out.println(allDayReq.get(i));
			}
		}
	}
	

	
	public int determineWeeklyRequirement(Office office) {
		int hourlyRequirement = 0;
		for(int i = 0; i < 37; i++) {
			for(int j = 0; j < 7; j++) {
				hourlyRequirement += office.shiftRequirement[i][j];
			}
		}
		return hourlyRequirement;
	}
	
}
