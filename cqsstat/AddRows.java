import java.util.*;
/* This allows us to create instances of classes from the package java.util in 
   this program. This program will create instances of the Scanner class, the 
   StringTockenizerclass, and the Vector class, all of which are included in the 
   java.util package. */
import java.io.*;
/* This allows us to create instances of the classes from the package java.io in
   this program. This program will be using classes from the io package such as
   File, FileNotFoundException, FileWriter, PrintWriter, and IOException. */
import java.text.*;
/* This allows us to format the date. */
import java.nio.file.*;
import java.nio.file.attribute.*;

public class AddRows {
	public static void main(String[] args) {
		String[] newRows = {"",""};
		/* Initiates the list of new rows to add. */
		SimpleDateFormat formatter = new SimpleDateFormat("M/d/yyyy");
		/* Object for formatting the date to the form of a string and 
		parses the formatted string into date (if possible). */

		/* The try block below locates the folder with all the bug data, and
		generates the list of new rows that will be added and stores
		them in the list. */		
		try {
			File folder = new File("All_Bugs");
			File[] listOfFiles = folder.listFiles();
			newRows = new String[listOfFiles.length];
			/* Locates the folder, gets all the files, and then
			allocates space for the list based on number of files. */

			/* For each file, a row is created and stored into the list. */
			for (int i = 0; i < listOfFiles.length; i++) {
				
				Date date = new Date(Files.readAttributes(listOfFiles[i].toPath(), 
					BasicFileAttributes.class).creationTime().toMillis());
				String formattedDate = formatter.format(date);
				/* Gets the date of when file was added and puts it
				in the form of a string (this is for the row). */

				ArrayList<String[]> lines = new ArrayList<String[]>();
				/* Stores all the rows from the raw bug data and stores
				certain information of each attribute of the row in 
				different indexes. */
				int openP1 = 0, openP2 = 0, openP3 = 0, openP4P5 = 0,
					allOpen = 0, readytotest = 0, closed = 0, deferred = 0;
				/* Creates count variables to store the number of instances
				of a certain quality in the bug data. */
				Scanner file = new Scanner(listOfFiles[i]);
				/* Allows the program to read in the text in the current file. */

				if (!file.hasNext()) {
					System.out.println("The input file is empty.");
					System.exit(1);
				}
				/* The program terminates with error if the folder is empty. */

				while (file.hasNextLine()) {
					String[] line = new String[3];
					String lineText = file.nextLine();
					lineText = lineText.replaceAll("\"", "");
					for (int j = 0; j < line.length && lineText.length() > 0; j++) {
						int commaIndex = lineText.indexOf(",");
						if (commaIndex >= 0) {
							line[j] = lineText.substring(0, commaIndex);
							lineText = lineText.substring(commaIndex + 1);
						}
						/* If there are commas, the String needs to continue
						breaking down and the part taken out is added to the
						array. */
						else {
							line[j] = lineText;
						}
						/* After the Strings have been separated, the part
						that wasn't taken out can become the last String. */
					}
					lines.add(line);
					/* This row array is added to the data storage. */
				}
				for (int j = 0; j < lines.size(); j++) {
					String line[] = lines.get(j);
					String status = line[2].toLowerCase();
					String priority = line[1].toLowerCase();
					switch (status) {
						case "open": case "\"open\"":
							allOpen++;
							switch (priority) {
								case "p1": case "\"p1\"":
									openP1++;
									break;
								case "p2": case "\"p2\"":
									openP2++;
									break;
								case "p3": case "\"p3\"":
									openP3++;
									break;
								case "p4": case "p5": 
								case "\"p4\"": case "\"p5\"":
									openP4P5++;
									break;
								default: 
									allOpen--;
									break;
							}
							break;
						case "resolved": case "readytotest":
						case "\"resolved\"": case "\"readytotest\"":
							readytotest++;
							break;
						case "closed": case "\"closed\"":
							closed++;
							break;
						case "deferred": case "\"deferred\"":
							deferred++;
							break;
						default:
							break;
					}
				}
				/* This loop loops through each element of the file and increments
				certain variables based on the data found. */
				String newRow = formattedDate + "," + openP1 + "," + openP2 + ","
				+ openP3 + "," + openP4P5 + "," + allOpen + "," + readytotest +
				"," + closed + "," + deferred;
				newRows[i] = newRow;

				/* Converts the amount of each counter variable found into a new
				row, adding it to the global list of rows which will be added. */
				file.close();
			}
		}
		catch (FileNotFoundException e) {
			System.out.println("Folder was not found.");
			System.exit(1);
		}
		catch (IOException e) {
			System.out.println(e);
			System.exit(-1);
		}
		/* If the folder was not found, there should be an error. */

		ArrayList<String> lines2 = new ArrayList<String>();

		try {
			File filetoEdit = new File("BugStats.csv");
			Scanner editFile = new Scanner(filetoEdit);

			if (!editFile.hasNext()) {
				lines2.add("Date,Open P1,Open P2,Open P3,Open P4/P5,All " + 
					"Open,ReadyToTest,Closed,Deferred");
			}
			else {
				while (editFile.hasNextLine()) {
					String line = editFile.nextLine();
					lines2.add(line);
				}
			}
			editFile.close();
		}
		/* If the file has been created, we must copy the part created and
		copy it to a new arraylist which will hold the lines that we want
		to write. */
		catch (FileNotFoundException e) {
			lines2.add("Date,Open P1,Open P2,Open P3,Open P4/P5,All " + 
					"Open,ReadyToTest,Closed,Deferred");
		}
		/* If the file was not created or empty, we want to write
		the first row as the columns in the csv file. */
		
		for (int i = 0; i < newRows.length; i++) {
			boolean inserted = false;
			for (int j = 1; j < lines2.size(); j++) {
				Date rowDate = new Date(), thisDate = new Date();
				/* Initiates the date of the rows being compared. */
				try {
					rowDate = formatter.parse(newRows[i].substring(0, 
						newRows[i].indexOf(",")));
					thisDate = formatter.parse(lines2.get(j).substring(0, 
						lines2.get(j).indexOf(",")));
				}
				/* Makes the dates the first text before the comma. */
				catch (ParseException e) {
					System.out.println(e);
					System.exit(1);
				}

				/* adds the new row before or replaces
				the new row with the current row if the new
				row comes before or on the same day. */
				if (!inserted && thisDate.after(rowDate)) {
					lines2.add(j, newRows[i]);
					inserted = true;
				}
				else if (!inserted && thisDate.equals(rowDate)) {
					lines2.remove(j);
					lines2.add(j, newRows[i]);
					inserted = true;
				}
			}
			if (!inserted) {
				lines2.add(newRows[i]);
			}
			/* If the new row hasn't been added, then it is at the end. */
		}
		
		try {
			PrintWriter fileEditor = new PrintWriter(new FileWriter("BugStats.csv"));
			for (int i = 0; i < lines2.size(); i++) {
				fileEditor.write(lines2.get(i));
				fileEditor.println();
			}
		
			fileEditor.flush();
			fileEditor.close();
		}
		/* Writes the arraylist to a file. */
		catch (IOException e) {
			System.out.println(e);
			System.exit(1);
		}
	}
}