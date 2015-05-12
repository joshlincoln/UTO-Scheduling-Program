import java.awt.EventQueue;

import javax.swing.JFrame;

import java.awt.Window.Type;
import java.awt.BorderLayout;

import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.ImageIcon;

import java.awt.Font;
import java.awt.event.ActionEvent;

import org.apache.commons.collections4.comparators.ComparatorChain;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import java.awt.Choice;

import javax.swing.JOptionPane;
import javax.swing.JDialog;

public class GUI {

	private JFrame frmTcScheduler;
	private JTextField textField_shiftOpen;
	private JTextField textField_shortestShift;
	private JTextField textField_shiftClose;
	private JTextField textField_longestShift;
	private static ArrayList<Student> TCs = new ArrayList<Student>();
	private static ArrayList<Office> Offices = new ArrayList<Office>();
	public static ArrayList<String> officeNames = new ArrayList<String>();
	private static String availDirectory;
	private static String reqDirectory;
	private static String configFile;
	private static Schedule Schedule;
	public static JTextArea textArea_Output = new JTextArea();
	private static String[] timeSlot = { "6:00 AM", "6:30 AM", "7:00 AM",
			"7:30 AM", "8:00 AM", "8:30 AM", "9:00 AM", "9:30 AM", "10:00 AM",
			"10:30 AM", "11:00 AM", "11:30 AM", "12:00 PM", "12:30 PM",
			"1:00 PM", "1:30 PM", "2:00 PM", "2:30 PM", "3:00 PM", "3:30 PM",
			"4:00 PM", "4:30 PM", "5:00 PM", "5:30 PM", "6:00 PM", "6:30 PM",
			"7:00 PM", "7:30 PM", "8:00 PM", "8:30 PM", "9:00 PM", "9:30 PM",
			"10:00 PM", "10:30 PM", "11:00 PM", "11:30 PM" };

	// Schedule Schedule = new Schedule();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frmTcScheduler.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmTcScheduler = new JFrame();
		frmTcScheduler.setResizable(false);
		frmTcScheduler.setType(Type.UTILITY);
		frmTcScheduler.setTitle("TC Scheduler");
		frmTcScheduler.setBounds(100, 100, 654, 544);
		frmTcScheduler.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTcScheduler.getContentPane().setLayout(new BorderLayout(0, 0));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frmTcScheduler.getContentPane().add(tabbedPane, BorderLayout.CENTER);

		JPanel panel1 = new JPanel();
		tabbedPane.addTab("Schedule", null, panel1, null);
		panel1.setLayout(null);

		textArea_Output.setEditable(false);
		textArea_Output.setLineWrap(true);
		textArea_Output.setWrapStyleWord(true);
		textArea_Output.setBounds(299, 177, 78, 128);
		panel1.add(textArea_Output);

		// int c = JOptionPane.showConfirmDialog(panel1,
		// "Would you like to import existing data?",
		// "Existing data detected", JOptionPane.YES_NO_OPTION);
		// panel1.setBounds(200, 25, 260, 90);
		// panel1.add(panel1);
		//
		// if (c == JOptionPane.YES_OPTION) {
		// textArea_Output.append("Reading existing data from file...");
		// try {
		// readData();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }

		JScrollPane scrollPane = new JScrollPane(textArea_Output);
		scrollPane.setBounds(254, 20, 380, 308);
		panel1.add(scrollPane);

		JButton AvailButton = new JButton("Get Availability");
		AvailButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent availClicked) {
				JFileChooser availChooser = new JFileChooser();
				availChooser
						.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				availChooser
						.setCurrentDirectory(new File(
								"C:/Users/Josh/Dropbox/Projects/Scheduling Program/ExcelFiles"));
				int returnValue = availChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					TCs = null;
					File path = availChooser.getSelectedFile();
					availDirectory = path.getAbsolutePath();
					textArea_Output.append("\n");
					textArea_Output.append("TC availability directory: ");
					textArea_Output.append(availDirectory);
					textArea_Output.append("\n");
					try {
						TCs = createStudents(availDirectory);
						createTCConfig(TCs);
						createAvail(TCs);
					} catch (Exception e) {
						e.printStackTrace();
						textArea_Output.append("Invalid Input");
						textArea_Output.append("\n");
					}
				}
			}
		});

		AvailButton.setBounds(42, 20, 138, 28);
		panel1.add(AvailButton);

		JButton RequirementsButton = new JButton("Get Requirements");
		RequirementsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent reqClicked) {
				JFileChooser reqChooser = new JFileChooser();
				reqChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				reqChooser
						.setCurrentDirectory(new File(
								"C:/Users/Josh/Dropbox/Projects/Scheduling Program/ExcelFiles"));
				int returnValue = reqChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					Offices = null;
					File selectedFile = reqChooser.getSelectedFile();
					reqDirectory = selectedFile.getAbsolutePath();
					textArea_Output.append("\n");
					textArea_Output.append("Office requirements directory: ");
					textArea_Output.append(reqDirectory);
					textArea_Output.append("\n");
					try {
						Offices = createOffices(reqDirectory);
					} catch (Exception e) {
						e.printStackTrace();
						textArea_Output.append("Invalid Input");
						textArea_Output.append("\n");
					}
				}
			}
		});
		RequirementsButton.setBounds(42, 60, 138, 28);
		panel1.add(RequirementsButton);

		JButton btnTcConfiguration = new JButton("TC Configuration");
		btnTcConfiguration.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent reqClicked) {
				JFileChooser configChooser = new JFileChooser();
				configChooser
						.setCurrentDirectory(new File(
								"C:/Users/Josh/Dropbox/Projects/Scheduling Program/ExcelFiles"));
				int returnValue = configChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = configChooser.getSelectedFile();
					configFile = selectedFile.getAbsolutePath();
					textArea_Output.append("\n");
					textArea_Output.append("TC configuration file: ");
					textArea_Output.append(configFile);
					textArea_Output.append("\n");
					try {
						configTCs(TCs, configFile);
					} catch (Exception e) {
						e.printStackTrace();
						textArea_Output.append("Invalid Input");
						textArea_Output.append("\n");
					}
				}

			}
		});

		// create schedule object

		btnTcConfiguration.setBounds(42, 100, 138, 28);
		panel1.add(btnTcConfiguration);

		JLabel label_scheduleParameters = new JLabel(
				"Enter the parameters for the schedule:");
		label_scheduleParameters.setFont(new Font("Tahoma", Font.PLAIN, 14));
		label_scheduleParameters.setBounds(10, 170, 270, 20);
		panel1.add(label_scheduleParameters);

		// closing shift parameter components
		JLabel lblClosingTimehhmm = new JLabel("Closing Time (hh:mm PM):");
		lblClosingTimehhmm.setBounds(10, 298, 170, 20);
		panel1.add(lblClosingTimehhmm);

		textField_shiftClose = new JTextField();
		textField_shiftClose.setColumns(10);
		textField_shiftClose.setBounds(175, 294, 65, 28);
		panel1.add(textField_shiftClose);
		textField_shiftClose.setText("10:30 PM");

		// opening shift parameter components
		JLabel lblOpeningTimehhmm = new JLabel("Opening Time (hh:mm AM):");
		lblOpeningTimehhmm.setBounds(10, 202, 170, 20);
		panel1.add(lblOpeningTimehhmm);

		textField_shiftOpen = new JTextField();
		textField_shiftOpen.setBounds(175, 202, 65, 28);
		panel1.add(textField_shiftOpen);
		textField_shiftOpen.setColumns(10);
		textField_shiftOpen.setText("6:30 AM");

		// longest shift parameter components
		JLabel lable_longestShift = new JLabel("Longest Shift (h):");
		lable_longestShift.setBounds(10, 266, 170, 20);
		panel1.add(lable_longestShift);

		textField_longestShift = new JTextField();
		textField_longestShift.setBounds(175, 262, 65, 28);
		panel1.add(textField_longestShift);
		textField_longestShift.setColumns(10);
		textField_longestShift.setText("10");

		// shortest shift parameter components
		JLabel lblShortestShifthours = new JLabel("Shortest Shift (h.m):");
		lblShortestShifthours.setBounds(10, 234, 170, 20);
		panel1.add(lblShortestShifthours);

		textField_shortestShift = new JTextField();
		textField_shortestShift.setColumns(10);
		textField_shortestShift.setBounds(175, 232, 65, 28);
		panel1.add(textField_shortestShift);
		textField_shortestShift.setText("2");

		JButton button_ScheduleParameters = new JButton("Configure");
		button_ScheduleParameters.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Schedule = null;
				try {
					Schedule = new Schedule(TCs, Offices);
					Float shortestShift = 2 * (Float
							.parseFloat(textField_shortestShift.getText()));
					Integer longestShift = 2 * (Integer
							.parseInt(textField_longestShift.getText()));
					Schedule.setShortestShift(shortestShift);
					Schedule.setHighThresh(longestShift);
					String startShift = textField_shiftOpen.getText();
					for (int i = 0; i < timeSlot.length; i++) {
						if (startShift.equals(timeSlot[i])) {
							int startSchedule = i;
							Schedule.setStartSchedule(startSchedule);
							break;
						}
					}
					String endShift = textField_shiftClose.getText();
					for (int i = 0; i < timeSlot.length; i++) {
						if (endShift.equals(timeSlot[i])) {
							int endSchedule = i;
							Schedule.setEndSchedule(endSchedule);
							break;
						}
					}
					Schedule.determineManagementNeeds(TCs, Offices);
					textArea_Output.append("\n");
					textArea_Output.append("TCs Configured");
					textArea_Output.append("\n");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		button_ScheduleParameters.setBounds(91, 330, 89, 23);
		panel1.add(button_ScheduleParameters);

		JButton button_generateSchedule = new JButton("Generate Schedule");
		button_generateSchedule.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {

				try {
					createSchedule(Schedule, TCs, Offices);
					writeData(TCs, Offices, Schedule);
				} catch (Exception e) {
					e.printStackTrace();
					textArea_Output.append("Invalid inptut");
				}
			}
		});
		button_generateSchedule.setFont(new Font("SansSerif", Font.PLAIN, 18));
		button_generateSchedule.setBounds(234, 388, 191, 48);
		panel1.add(button_generateSchedule);

		JLabel backgroundImage = new JLabel("");
		backgroundImage
				.setIcon(new ImageIcon(
						"C:\\Users\\Josh\\Dropbox\\Projects\\Scheduling Program\\GUIbackground.png"));
		backgroundImage.setBounds(0, -24, 649, 511);
		panel1.add(backgroundImage);
	}

	/*
	 * Methods from main
	 */
	public static String getVal(XSSFCell cell) {
		if (cell == null)
			return "";
		if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC)
			return Integer.toString((int) cell.getNumericCellValue());
		if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING)
			return cell.getStringCellValue();
		return null;
	}

	/*
	 * Saving and reading student objects after open/close of program
	 */

	public static Schedule testAll(Schedule schedule,
			ArrayList<Office> offices, ArrayList<Student> tcs) {
		ArrayList<Schedule> schedules = new ArrayList<Schedule>();
		int count = 0;
		for (float i = 4; i <= 8; i++) {
			for (int j = 12; j <= 20; j++) {
				count++;
				textArea_Output.append("trying new schedule : " + count);
				try {
					TCs = createStudents(availDirectory);
					Offices = createOffices(reqDirectory);
					configTCs(TCs, configFile);
					Schedule testSchedule = new Schedule(TCs, Offices);
					testSchedule.setShortestShift(i);
					testSchedule.setHighThresh(j);
					//testSchedule = createSchedule(testSchedule, TCs, Offices);
					testSchedule.setQuality(testSchedule.getPercentageMet()
							- testSchedule.getAverageUnscheduledHours());

					schedules.add(testSchedule);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
		Collections.sort(schedules, new QualityComparator());
		return schedules.get(0);

	}

	public static void readData() throws IOException {
		textArea_Output.setText("Existing data found, reading... \n");
		try {
			textArea_Output.setText("Existing data found, reading... \n");
			TCs = checkForExistingStudents();
			Offices = checkForExistingOffices();
			// Schedule = checkForExistingSchedule();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<Student> checkForExistingStudents()
			throws FileNotFoundException, IOException {

		try {
			ObjectInputStream studentRead = new ObjectInputStream(
					new FileInputStream("students.ser"));
			@SuppressWarnings("unchecked")
			ArrayList<Student> existingStudents = (ArrayList<Student>) studentRead
					.readObject();
			studentRead.close();
			TCs = existingStudents;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return TCs;
	}

	public static ArrayList<Office> checkForExistingOffices()
			throws FileNotFoundException, IOException {
		new ArrayList<Office>();

		try {
			ObjectInputStream officeRead = new ObjectInputStream(
					new FileInputStream("offices.ser"));
			@SuppressWarnings("unchecked")
			ArrayList<Office> existingOffices = (ArrayList<Office>) officeRead
					.readObject();
			officeRead.close();
			Offices = existingOffices;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Offices;
	}

	public static Schedule checkForExistingSchedule() {
		try {
			ObjectInputStream scheduleRead = new ObjectInputStream(
					new FileInputStream("schedule.ser"));
			Schedule existingSchedule = (Schedule) scheduleRead.readObject();
			scheduleRead.close();
			Schedule = existingSchedule;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Schedule;
	}

	public static void writeData(ArrayList<Student> TCs,
			ArrayList<Office> offices, Schedule schedule) throws IOException {
		try {
			writeStudents(TCs);
			writeOffices(offices);
			// writeSchedule(schedule);
			textArea_Output.append("\nData saved...");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			textArea_Output.append("\nError saving data...");
		}

	}

	public static void writeStudents(ArrayList<Student> TCs)
			throws FileNotFoundException, IOException {
		ObjectOutputStream studentWrite = new ObjectOutputStream(
				new FileOutputStream("students.ser"));
		studentWrite.writeObject(TCs);
		studentWrite.flush();
		studentWrite.close();
	}

	public static void writeOffices(ArrayList<Office> offices)
			throws IOException {
		ObjectOutputStream officeWrite = new ObjectOutputStream(
				new FileOutputStream("offices.ser"));
		officeWrite.writeObject(offices);
		officeWrite.flush();
		officeWrite.close();

	}

	public static void writeSchedule(Schedule schedule) throws IOException {
		ObjectOutputStream scheduleWrite = new ObjectOutputStream(
				new FileOutputStream("schedule.ser"));
		scheduleWrite.writeObject(schedule);
		scheduleWrite.flush();
		scheduleWrite.close();
	}

	public static Student AvailParse(File filename) throws Exception {

		// file xlsx read
		FileInputStream fis = new FileInputStream(filename);
		XSSFWorkbook wb = new XSSFWorkbook(fis);
		XSSFSheet ws = wb.getSheetAt(0);

		int rowNum = 37;
		int colNum = 7;
		int availability = 0;
		String name;
		int hours;
		Integer[][] shiftAvailability = new Integer[rowNum][colNum];
		ArrayList<ArrayList<Integer>> shiftAvail = new ArrayList<ArrayList<Integer>>();

		// add days to the availability grid

		// add name and desired hours
		XSSFCell nameC = ws.getRow(13).getCell(0);
		String string = getVal(nameC);
		name = string.substring(string.lastIndexOf(':') + 1).trim();
		XSSFCell hoursC = ws.getRow(15).getCell(0);
		String string1 = getVal(hoursC);
		hours = Integer.parseInt(string1
				.substring(string1.lastIndexOf(':') + 1).trim());

		// read availability into availability grid
		for (int i = 19; i < 56; i++) {
			shiftAvail.add(new ArrayList<Integer>());
			XSSFRow row1 = ws.getRow(i);
			for (int j = 1; j < colNum + 1; j++) {
				XSSFCell cell = row1.getCell(j);
				// System.out.println(cell + "at" + i + " "+ j);
				XSSFColor bgColor = cell.getCellStyle()
						.getFillForegroundXSSFColor();
				// System.out.println(bgColor + "at" + i + " "+ j);
				String color;
				if (bgColor != null) {
					color = bgColor.getARGBHex();
				} else {
					// System.out.println(filename);
					color = "nope";
				}
				if (color.equals("FFFFFFFF")) {
					availability = 1;
				} else {
					availability = 0;
				}
				shiftAvail.get(i - 19).add(j - 1, availability);
				shiftAvailability[i - 19][j - 1] = availability;
			}
			wb.close();

		}
		return new Student(shiftAvail, name, hours);
	}

	public static void createAvail(ArrayList<Student> TCs)
			throws FileNotFoundException {
		File availFile = new File("TCAvailability.txt");
		PrintStream availOut = new PrintStream(new FileOutputStream(availFile));
		System.setOut(availOut);

		for (Student TC : TCs) {
			TC.printStudentAvailability();
		}
	}

	public static void createTCConfig(ArrayList<Student> TCs)
			throws IOException {

		XSSFWorkbook TCNames = new XSSFWorkbook();
		XSSFSheet students = TCNames.createSheet("TC Config");

		int rowIndex = 0;
		for (Student TC : TCs) {
			XSSFRow row = students.createRow(rowIndex++);
			XSSFCell cell = row.createCell(0);
			cell.setCellValue(TC.getName());
		}

		FileOutputStream outFile = new FileOutputStream(
				new File("TCNames.xlsx"));
		TCNames.write(outFile);
		outFile.close();
		TCNames.close();
	}

	public static Office OfficeParse(File filename) throws Exception {

		int rowNum = 37;
		int colNum = 7;
		Integer[][] officeRequirement = new Integer[rowNum][colNum];
		ArrayList<ArrayList<Integer>> officeReq = new ArrayList<ArrayList<Integer>>();

		// add days to the availability grid
		FileInputStream fis = new FileInputStream(filename);
		XSSFWorkbook wb = new XSSFWorkbook(fis);
		XSSFSheet ws = wb.getSheetAt(0);
		XSSFCell nameC = ws.getRow(0).getCell(0);
		String officeName = getVal(nameC);
		for (int i = 2; i < rowNum + 2; i++) {
			officeReq.add(new ArrayList<Integer>());
			XSSFRow row = ws.getRow(i);
			for (int j = 1; j < colNum + 1; j++) {
				XSSFCell cell = row.getCell(j);
				int hours = Integer.parseInt(getVal(cell));
				officeReq.get(i - 2).add(j - 1, hours);
				officeRequirement[i - 2][j - 1] = hours;
			}
		}
		wb.close();
		return new Office(officeReq, officeName);
	}

	public static void configTCs(ArrayList<Student> TCs, String directory)
			throws Exception {

		FileInputStream fis = new FileInputStream(directory);
		XSSFWorkbook wb = new XSSFWorkbook(fis);
		XSSFSheet ws = wb.getSheetAt(0);

		int rowNum = TCs.size();
		int colNum = 2;
		String[][] TCconfig = new String[rowNum][colNum];

		for (int i = 0; i < rowNum; i++) {
			XSSFRow row = ws.getRow(i);
			XSSFCell data = row.getCell(0);
			XSSFCell data1 = row.getCell(1);
			String name = getVal(data);
			String office = getVal(data1);
			TCconfig[i][0] = name.trim();
			TCconfig[i][1] = office.trim();

			TCs.get(TCs.indexOf(new Student(null, name, 0))).setOffice(office);
			;
		}

		for (int i = 0; i < rowNum; i++) {
			if (TCconfig[i][1] != "") {
				officeNames.add(TCconfig[i][1]);
			}
		}

		Set<String> hs = new HashSet<>();
		hs.addAll(officeNames);
		officeNames.clear();
		officeNames.addAll(hs);

		wb.close();
	}

	public static ArrayList<File> listFiles(String dirName) {
		File directory = new File(dirName);
		ArrayList<File> fList = new ArrayList<File>(Arrays.asList(directory
				.listFiles()));
		return fList;
	}

	public static ArrayList<Student> createStudents(String directory)
			throws Exception {

		List<File> fileList = listFiles(directory);

		ArrayList<Student> TCs = new ArrayList<Student>();

		for (int i = 0; i <= fileList.size() - 1; i++) {
			TCs.add(AvailParse(fileList.get(i)));

		}
		return TCs;
	}

	public static ArrayList<Office> createOffices(String directory)
			throws Exception {

		List<File> fileList = listFiles(directory);

		ArrayList<Office> offices = new ArrayList<Office>();

		for (int i = 0; i <= fileList.size() - 1; i++) {
			offices.add(OfficeParse(fileList.get(i)));

		}
		return offices;
	}

	public static void printTCSchedule(ArrayList<Student> TCs,
			ArrayList<Office> offices) throws Exception {

		// XSSFWorkbook schedule = new XSSFWorkbook();
		// XSSFSheet students = schedule.createSheet("TC Schedule");
		//
		// int rowIndex = 0;
		// for (Student TC : TCs) {
		// XSSFCellStyle style = schedule.createCellStyle();
		// style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		// style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		// style.setBorderRight(XSSFCellStyle.BORDER_THIN);
		// style.setBorderTop(XSSFCellStyle.BORDER_THIN);
		// students.setHorizontallyCenter(true);
		// students.setDefaultColumnWidth(33);
		// XSSFRow row = students.createRow(rowIndex++);
		// XSSFCell cell = row.createCell(0);
		// cell.setCellStyle(style);
		// cell.setCellValue("Student: " + TC.name + " Schedule");
		// XSSFRow row1 = students.createRow(rowIndex++);
		// XSSFCell cell1 = row1.createCell(0);
		// cell1.setCellStyle(style);
		// cell1.setCellValue("Desired Hours: " + TC.desiredHours / 2);
		// XSSFRow row2 = students.createRow(rowIndex++);
		// XSSFCell cell2 = row2.createCell(0);
		// cell2.setCellStyle(style);
		// cell2.setCellValue("Scheduled Hours: " + TC.scheduledHours / 2);
		// int cellIndex = 0;
		// TC.makeFinalSchedule();
		// for (int i = 0; i < 37; i++) {
		// cellIndex = 0;
		// for (int j = 0; j < 7; j++) {
		// cellIndex++;
		// if (TC.finalSchedule[i][j] == null) {
		// } else {
		// XSSFRow row3 = students.createRow(rowIndex++);
		// XSSFCell cell3 = row3.createCell(cellIndex);
		// cellIndex++;
		// style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		// style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		// style.setBorderRight(XSSFCellStyle.BORDER_THIN);
		// style.setBorderTop(XSSFCellStyle.BORDER_THIN);
		// // cell3.setCellStyle(style);
		// cell3.setCellValue(TC.finalSchedule[i][j]);
		// }
		// }
		// }
		// }
		// FileOutputStream outFile = new FileOutputStream(
		// new File(
		// "C:/Users/Josh/Dropbox/Projects/Scheduling Program/ExcelFiles/TCSchedule.xlsx"));
		// schedule.write(outFile);
		// outFile.close();

		File scheduleFile = new File("TCSchedule.txt");
		PrintStream scheduleOut = new PrintStream(new FileOutputStream(
				scheduleFile));
		System.setOut(scheduleOut);

		for (Student student : TCs) {
			student.printStudentSchedule();
		}
		scheduleOut.flush();
		scheduleOut.close();

		File unScheduled = new File("UnscheduledTCs.txt");
		PrintStream unOut = new PrintStream(new FileOutputStream(unScheduled));
		System.setOut(unOut);
		for (Student TC : TCs) {
			if (TC.scheduled == false) {
				TC.printStudentSchedule();
			}
		}
		unOut.flush();
		unOut.close();
	}

	// determine total workable weekly hours by TCs

	// creates the schedule using the TCs, offices, and schedule object
	public static void createSchedule(Schedule schedule,
			ArrayList<Student> TCs, ArrayList<Office> offices) throws Exception {
			schedule.determineManagementNeeds(TCs, offices);
			schedule.scheduling(TCs, offices);
			schedule.evaluateSchedule(TCs, offices);
		Collections.sort(TCs, new Name());
		printTCSchedule(TCs, offices);
	}

}
