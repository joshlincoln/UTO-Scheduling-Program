//// Java File IO API
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.FileReader;
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.lang.Object;
//import java.util.*;
//
//// Apache POI API
//import org.apache.poi.*;
//import org.apache.poi.hssf.util.CellReference;
//import org.apache.poi.xssf.usermodel.XSSFCell;
//import org.apache.poi.xssf.usermodel.XSSFCellStyle;
//import org.apache.poi.xssf.usermodel.XSSFColor;
//import org.apache.poi.xssf.usermodel.XSSFRow;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor;
//
//public class main {
//	
//	public static float totalWorkableHours;
//	public static float totalRemainingWorkableHours;
//	public static float totalScheduledHours;
//	public static float hoursNotScheduled;
//	
//
//	public static void main(String[] args) throws Exception {
//
//		/*
//		 *  Get Data
//		 */
//		System.out.println("Reading...\n\n");
//		ArrayList<Student> TCs = createStudents();
//		ArrayList<Office> offices = createOffices();
//		configTCs(TCs);
//		
//		/*
//		 *  Calculate management needs 
//		 */
//		determineManagementNeeds(TCs, offices);
//		
//		Schedule schedule = new Schedule(TCs, offices);
//		createSchedule(schedule, TCs, offices);
//		
//		/*
//		 * Evaluate Schedule
//		 */
//		evaluateSchedule(TCs, offices);
//		testing(TCs, offices);
//		System.out.println("Done");
//
//	}
//
//	public static void testing(ArrayList<Student> TCs, ArrayList<Office> offices) {
//
//		// print offices
//		// System.out.println("Printing Offices");
//		// for(Office office : offices) {
//		// office.printOffice();
//		// }
//
//		// print TCs
//		System.out.println("Printing TCs...\n");
//		for (Student student : TCs) {
//			// System.out.println(student.name);
//			student.printStudent();
//			// student.printStudentAvailability();
//		}
//	}
//
//	public static String getVal(XSSFCell cell) {
//		if (cell == null)
//			return "";
//		if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC)
//			return Integer.toString((int) cell.getNumericCellValue());
//		if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING)
//			return cell.getStringCellValue();
//		return null;
//	}
//	
////	@SuppressWarnings({ "resource", "null" })
////	public static ArrayList<Student> CheckForExistingStudents() throws FileNotFoundException, IOException{
////		ArrayList<Student> existingStudents = null;
////		
////		try{
////			ObjectInputStream studentRead = new ObjectInputStream(new FileInputStream("students.txt"));
////			existingStudents.add((Student) studentRead.readObject());
////		}
////		catch(Exception e) {
////			e.printStackTrace();
////		}
////		return existingStudents;
////	}
////	
////	public static void WriteStudents(ArrayList<Student> TCs) throws FileNotFoundException, IOException {
////		ObjectOutputStream studentWrite = new ObjectOutputStream(new FileOutputStream("students.txt"));
////		for(Student TC : TCs) {
////			studentWrite.writeObject(TC);
////		}
////		studentWrite.flush();
////		studentWrite.close();
////	}
//	
//	public static ArrayList<Student> createStudents() throws Exception {
//		
//		
//		List<File> fileList = listFiles("C:/Users/Josh/Dropbox/Projects/Scheduling Program/ExcelFiles/Availabilities");
//
//		ArrayList<Student> TCs = new ArrayList<Student>();
//
//		for (int i = 0; i <= fileList.size() - 1; i++) {
//			TCs.add(AvailParse(fileList.get(i)));
//
//		}
//		return TCs;
//	}
//
//	public static Student AvailParse(File filename) throws Exception {
//
//		// file xlsx read
//		FileInputStream fis = new FileInputStream(filename);
//		XSSFWorkbook wb = new XSSFWorkbook(fis);
//		XSSFSheet ws = wb.getSheetAt(0);
//
//		int rowNum = 37;
//		int colNum = 7;
//		int availability = 0;
//		String name;
//		int hours;
//		Integer[][] shiftAvailability = new Integer[rowNum][colNum];
//		ArrayList<ArrayList<Integer>> shiftAvail = new ArrayList<ArrayList<Integer>>();
//
//		// add days to the availability grid
//
//		// add name and desired hours
//		XSSFCell nameC = ws.getRow(13).getCell(0);
//		String string = getVal(nameC);
//		name = string.substring(string.lastIndexOf(':') + 1).trim();
//		XSSFCell hoursC = ws.getRow(15).getCell(0);
//		String string1 = getVal(hoursC);
//		hours = Integer.parseInt(string1
//				.substring(string1.lastIndexOf(':') + 1).trim());
//
//		// read availability into availability grid
//		for (int i = 19; i < 56; i++) {
//			shiftAvail.add(new ArrayList<Integer>());
//			XSSFRow row1 = ws.getRow(i);
//			for (int j = 1; j < colNum + 1; j++) {
//				XSSFCell cell = row1.getCell(j);
//				// System.out.println(cell + "at" + i + " "+ j);
//				XSSFColor bgColor = cell.getCellStyle()
//						.getFillForegroundXSSFColor();
//				// System.out.println(bgColor + "at" + i + " "+ j);
//				String color;
//				if (bgColor != null) {
//					color = bgColor.getARGBHex();
//				} else {
//					// System.out.println(filename);
//					color = "nope";
//				}
//				if (color.equals("FFFFFFFF")) {
//					availability = 1;
//				} else {
//					availability = 0;
//				}
//				shiftAvail.get(i - 19).add(j - 1, availability);
//				shiftAvailability[i - 19][j - 1] = availability;
//			}
//			wb.close();
//
//		}
//
//		// shiftAvail = toArrayList(shiftAvailability);
//		// print the availability grid
//		/*
//		 * for(int i = 0; i < rowNum; i++) { for(int j = 0; j < colNum-1; j++) {
//		 * System.out.print(shiftAvailability[i][j]); } System.out.println(); }
//		 */
//		return new Student(shiftAvail, name, hours);
//	}
//
//	public static Office OfficeParse(File filename) throws Exception {
//
//		int rowNum = 37;
//		int colNum = 7;
//		Integer[][] officeRequirement = new Integer[rowNum][colNum];
//		ArrayList<ArrayList<Integer>> officeReq = new ArrayList<ArrayList<Integer>>();
//
//		// add days to the availability grid
//
//		// initialize array to all zeros
//		/*
//		 * for(int i = 0; i < rowNum; i++) { for(int j = 0; j < colNum; j++) {
//		 * shiftAvailability[i][j] = ""; } }
//		 */
//
//		FileInputStream fis = new FileInputStream(filename);
//		XSSFWorkbook wb = new XSSFWorkbook(fis);
//		XSSFSheet ws = wb.getSheetAt(0);
//		XSSFCell nameC = ws.getRow(0).getCell(0);
//		String officeName = getVal(nameC);
//		for (int i = 2; i < rowNum + 2; i++) {
//			officeReq.add(new ArrayList<Integer>());
//			XSSFRow row = ws.getRow(i);
//			for (int j = 1; j < colNum + 1; j++) {
//				XSSFCell cell = row.getCell(j);
//				int hours = Integer.parseInt(getVal(cell));
//				officeReq.get(i - 2).add(j - 1, hours);
//				officeRequirement[i - 2][j - 1] = hours;
//			}
//		}
//
//		// officeReq = toArrayList(officeRequirement);
//
//		// print the office requirements
//		/*
//		 * for(int i = 0; i < rowNum; i++) { for(int j = 0; j <colNum; j++) {
//		 * System.out.print(officeRequirement[i][j]); } System.out.println(); }
//		 */
//		wb.close();
//		return new Office(officeReq, officeName);
//	}
//
//	public static void configTCs(ArrayList<Student> TCs) throws Exception {
//
//		FileInputStream fis = new FileInputStream(
//				"C:/Users/Josh/Dropbox/Projects/Scheduling Program/ExcelFiles/TCconfig.xlsx");
//		XSSFWorkbook wb = new XSSFWorkbook(fis);
//		XSSFSheet ws = wb.getSheetAt(0);
//
//		int rowNum = 73;
//		int colNum = 2;
//		String[][] TCconfig = new String[rowNum][colNum];
//
//		for (int i = 0; i < rowNum; i++) {
//			XSSFRow row = ws.getRow(i);
//			for (int j = 0; j < colNum; j++) {
//				XSSFCell cell = row.getCell(j);
//				String data = getVal(cell);
//				TCconfig[i][j] = data.trim();
//			}
//		}
//
//		for (Student TC : TCs) {
//			for (int i = 0; i < rowNum; i++) {
//				if (TC.name.equals(TCconfig[i][0])) {
//					if (TCconfig[i][1].equals("byac290")) {
//						TC.byac290 = true;
//					} else if (TCconfig[i][1].equals("adminSupport")) {
//						TC.adminSupport = true;
//					} else if (TCconfig[i][1].equals("cpcom140")) {
//						TC.cpcom140 = true;
//					} else if (TCconfig[i][1].equals("SSM")) {
//						TC.SSM = true;
//					} else if (TCconfig[i][1].equals("LS")) {
//						TC.LS = true;
//					}
//				}
//
//			}
//		}
//
////		File fileName = new File(
////				"C:/Users/Josh/Dropbox/Projects/ExcelParse/ExcelFiles/TCconfig.txt");
////		FileInputStream in = new FileInputStream(fileName);
////		Properties TCproperties = new Properties();
////		TCproperties
////				.load(new FileInputStream(
////						"C:/Users/Josh/Dropbox/Projects/ExcelParse/bin/TCconfig.properties"));
////
////		for (Student tc : TCs) {
////			tc.byac290 = Boolean
////					.parseBoolean(TCproperties.getProperty(tc.name));
////			tc.cpcom140 = Boolean.parseBoolean(TCproperties
////					.getProperty(tc.name));
////			tc.adminSupport = Boolean.parseBoolean(TCproperties
////					.getProperty(tc.name));
////			tc.LS = Boolean.parseBoolean(TCproperties.getProperty(tc.name));
////			tc.SSM = Boolean.parseBoolean(TCproperties.getProperty(tc.name));
////		}
//	}
//
//	public static ArrayList<File> listFiles(String dirName) {
//		File directory = new File(dirName);
//		ArrayList<File> fList = new ArrayList<File>(Arrays.asList(directory
//				.listFiles()));
//		return fList;
//	}
//
//
//
//	public static ArrayList<Office> createOffices() throws Exception {
//
//		String directory = "C:/Users/Josh/Dropbox/Projects/Scheduling Program/ExcelFiles/Offices";
//		List<File> fileList = listFiles(directory);
//
//		ArrayList<Office> offices = new ArrayList<Office>();
//
//		for (int i = 0; i <= fileList.size() - 1; i++) {
//			offices.add(OfficeParse(fileList.get(i)));
//
//		}
//		return offices;
//	}
//
//	public static void printTCSchedule(ArrayList<Student> TCs) throws Exception {
//
//
//		@SuppressWarnings("resource")
//		XSSFWorkbook schedule = new XSSFWorkbook();
//		XSSFSheet students = schedule.createSheet("TC Schedule");
//
//
//		int rowIndex = 0;
//		for (Student TC : TCs) {
//			XSSFCellStyle style = schedule.createCellStyle();
//			style.setBorderBottom(XSSFCellStyle.BORDER_MEDIUM);
//			style.setBorderLeft(XSSFCellStyle.BORDER_MEDIUM);
//			style.setBorderRight(XSSFCellStyle.BORDER_MEDIUM);
//			style.setBorderTop(XSSFCellStyle.BORDER_MEDIUM);
//			students.setHorizontallyCenter(true);
//			students.setDefaultColumnWidth(33);
//			XSSFRow row = students.createRow(rowIndex++);
//			XSSFCell cell = row.createCell(0);
//			cell.setCellStyle(style);
//			cell.setCellValue("Student: " + TC.name + " Schedule");
//			XSSFRow row1 = students.createRow(rowIndex++);
//			XSSFCell cell1 = row1.createCell(0);
//			cell1.setCellStyle(style);
//			cell1.setCellValue("Desired Hours: " + TC.desiredHours / 2);
//			XSSFRow row2 = students.createRow(rowIndex++);
//			XSSFCell cell2 = row2.createCell(0);
//			cell2.setCellStyle(style);
//			cell2.setCellValue("Scheduled Hours: " + TC.scheduledHours / 2);
//			int cellIndex = 0;
//			TC.makeFinalSchedule();
//			for (int i = 0; i < 37; i++) {
//				cellIndex = 0;
//				for (int j = 0; j < 7; j++) {
//					cellIndex++;
//					if(TC.finalSchedule[i][j] == null) {
//					}
//					else {
//					XSSFRow row3 = students.createRow(rowIndex++);
//					XSSFCell cell3 = row3.createCell(cellIndex);
//					cellIndex++;
//					style.setBorderBottom(XSSFCellStyle.BORDER_MEDIUM);
//					style.setBorderLeft(XSSFCellStyle.BORDER_MEDIUM);
//					style.setBorderRight(XSSFCellStyle.BORDER_MEDIUM);
//					style.setBorderTop(XSSFCellStyle.BORDER_MEDIUM);
//					cell3.setCellStyle(style);
//					cell3.setCellValue(TC.finalSchedule[i][j]);
//					}
//				}
//			}
//		}
//		FileOutputStream outFile = new FileOutputStream(new File(
//				"C:/Users/Josh/Dropbox/Projects/Scheduling Program/ExcelFiles/TCSchedule.xlsx"));
//		schedule.write(outFile);
//		outFile.close();
//
//	}
//	
//	//determine total workable weekly hours by TCs
//	public static float getWorkableHours(ArrayList<Student> TCs) {
//		
//		int totalWeeklyTCHours = 0;
//		for(Student TC : TCs) {
//			totalWeeklyTCHours += TC.getDesiredHours();
//		}
//		return totalWeeklyTCHours;
//	}
//	
//	// determine how many hours the TCs were not scheduled that they wanted to be
//	public static void getRemainingHours(ArrayList<Student> TCs) {
//		
//		totalRemainingWorkableHours = 0;
//		for(Student TC : TCs) {
//			totalRemainingWorkableHours += (TC.desiredHours - TC.scheduledHours);
//		}
//	}
//	// determine how many total hours you've scheduled in the offices
//	public static float getScheduledHours (ArrayList<Office> offices) {
//		
//		int totalScheduledHours = 0;
//		for(Office office : offices) {
//			for(int i = 1; i < 37; i++) {
//				for(int j = 0; j < 7; j++) {
//					totalScheduledHours +=  office.officeReq.get(i).get(j);
//				}
//			}
//		}
//		return totalScheduledHours;
//	}
//	// determine how many hours the scheduling algorithm failed to schedule in the offices post scheduling 
//	public static void getUnScheuledHours(ArrayList<Office> offices) {
//		
//		for(Office office : offices) {
//			for(int i = 1; i < 37; i++) {
//				for(int j = 0; j < 7; j++) {
//					hoursNotScheduled += office.officeReq.get(i).get(j);
//				}
//			}
//		}
//	}
//	
//	// determines how many TCs are needed based on the specified offices requirements 
//	public static void determineManagementNeeds(ArrayList<Student> TCs, ArrayList<Office> offices) {
//		
//		int numTC = TCs.size();
//		float averageDesiredHours = 0;
//		for(Student TC : TCs) {
//			averageDesiredHours += TC.desiredHours;
//		}
//		averageDesiredHours = (float)averageDesiredHours/numTC;
//		
//		System.out.println("Determining the required TCs according to the given schedule parameters...\n");
//		
//		totalWorkableHours = getWorkableHours(TCs);
//		totalScheduledHours = getScheduledHours(offices);
//		System.out.println("Total workable hours: " + totalWorkableHours );
//		System.out.println("Total scheduled hours: " + totalScheduledHours);
//	
//		float totalNeededTCs = (totalScheduledHours/averageDesiredHours);
//		System.out.println("The total number of TCs needed is: " + totalNeededTCs);
//		System.out.println("The total number of actual TCs is: " + numTC + "\n\n");
//	}
//	
//	
//	// creates the schedule using the TCs, offices, and schedule object
//	public static void createSchedule(Schedule schedule, ArrayList<Student> TCs, ArrayList<Office> offices) throws Exception {
//
//		for (Office office : offices) {
//			schedule.scheduling(TCs, office);
//		}
//		Collections.sort(TCs, new Name());
//		printTCSchedule(TCs);
//	}
//	
//	public static void evaluateSchedule(ArrayList<Student> TCs, ArrayList<Office> offices) {
//		
//		// totalWorkableRemainingHours = sum of desired hours - sum of scheduled hours by TC
//		// hoursNotScheduled = sum of offices hours intending to be scheduled  - sum of total actual scheduled hours 
//		int count = TCs.size();
//		
//		getRemainingHours(TCs);
//		getUnScheuledHours(offices);
//		
//		System.out.println("Evaluating Schedule Efficacy...\n");
//		System.out.println("Total unscheduled desired hours for TCs: " + totalRemainingWorkableHours);
//		System.out.println("Average unscheduled hours per TC: " + totalRemainingWorkableHours/count) ;
//		System.out.println("Total unscheduled hours in the offices: " +  hoursNotScheduled);
//		System.out.println("Percentage of schedule met: " + (100-(float)(hoursNotScheduled/totalScheduledHours)*100) + "\n\n");
//		
//		
//		
//	}
//
//}
