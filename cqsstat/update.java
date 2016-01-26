import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait; 
import org.openqa.selenium.TimeoutException;
import java.util.*;
import java.io.*;
import java.text.*;
import java.nio.file.*;
import java.nio.file.attribute.*;

public class update {
    static WebDriver driver;
    static WebElement element;
    
    public static void main(String[] args){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter = new SimpleDateFormat("M/d/yyyy");
        /* These commands let us change Date and string back and forth.*/

        String[] bugSubmissionTitles = {"Date","Submitted","Total bugs","P1","P2","P3","P4","P5"};
        
        ArrayList<String[]> submissionChart = makeChartFromCSV(new File("BugSubmissions.csv"), 
            bugSubmissionTitles);

        submissionChart.remove(0);
        submissionChart.add(0, bugSubmissionTitles);
        /*This let's us store the content of one the CSV files that we will overwrite so we don't 
        delete everything every time we edit the CSV file. We need to obtain this now because
        we want to see which date was the previous date we last recorded submissions so we can update it.*/
        Date todayDate = new Date();
        Date yesterdayDate = new Date(todayDate.getTime() - 86400000);
        /* The initialization allows us to use the variables without compile-time errors.*/

        try {
            String[] endRow = submissionChart.get(submissionChart.size() - 1);
            if (endRow[0].equals(formatter.format(new Date()))) {
                endRow = submissionChart.get(submissionChart.size() - 2);
            }
            yesterdayDate = formatter.parse(endRow[0]);
        }
        catch (ParseException e) {
            System.out.println(e);
        }
        /* The last row will represent the date of previous bug submission record unless this program
        was run today (in that case, the second last row will be the date of the previous bug submission.*/

        for (int i = submissionChart.size() - 1; i > 1; i--) {
            String[] submissionChartRow = submissionChart.get(i);
            String[] previousSubmissionChartRow = submissionChart.get(i - 1);
            for (int j = 2; j < submissionChartRow.length; j++) {
                submissionChartRow[j] = "" + (Integer.parseInt(submissionChartRow[j]) -
                    Integer.parseInt(previousSubmissionChartRow[j]));
            }
        }
        /*Changes the total bugs and the priorities to the number of submissions each day (will be 
        fixed, but this makes inserting easier).*/

        String today = simpleDateFormat.format(todayDate);
        String tomorrow = simpleDateFormat.format(new Date(todayDate.getTime() + 86400000));
        String yesterday = simpleDateFormat.format(yesterdayDate);
        /*These strings will be used as the time range for the submission date search on bugzilla. We 
        want to search the bug submissions from the previous day and the bug submissions from the current
        day.*/

        File yesterdayFolder = new File("Yesterday_Bug_Submissions");
        File[] yesterdayFiles = yesterdayFolder.listFiles();
        /*We need to store the bug submission between the last recording date and today in a unique
        folder in order to identify it as data from the previous day. The first line gets the folder
        in the directory of this program with the current name and the second line gets all the files
        in the folder in the form of an array.*/

        for (int i = 0; i < yesterdayFiles.length; i++) {
            try {
                Files.delete(yesterdayFiles[i].toPath());
            }
            catch (IOException e) {
                System.out.println(e);
                System.out.println("Something went wrong when cleaning");
                System.exit(-1);
            }
        }

        File todayFolder = new File("Today_Bug_Submissions");
        File[] todayFiles = todayFolder.listFiles();
        /* This will let us store a separate file for today
        so it won't be necessary to loop through every file
        to update.*/

        for (int i = 0; i < todayFiles.length; i++) {
            try {
                Files.delete(todayFiles[i].toPath());
            }
            catch (IOException e) {
                System.out.println(e);
                System.out.println("Something went wrong when cleaning");
                System.exit(-1);
            }
        }

        setDownloaderAndLogOnBugzilla(new File("Bugs"), 
            /* Username */ "ali@teampraxis.com", 
            /* Password */ "P@$$w0rd!", 
            /* Saved search */ "CQS All Bugs",
            /* Start range (leave empty if not set) */ "",
            /* End range (leave empty if it is meant for a future
                or present timing)*/ "", 
            /* Expected titles */ new String[]{"\"Bug ID\"","\"Status\"",
            "\"Priority\""});

        setDownloaderAndLogOnBugzilla(new File("Bug_Submissions"),
            /* Username */ "ali@teampraxis.com", 
            /* Password */ "P@$$w0rd!", 
            /* Saved search */ "CQS Bug Submissions", 
            /* Start range (leave empty if not set) */ today,
            /* End range (leave empty if it is meant for a future
                or present timing)*/ tomorrow,
            /* Expected titles */ new String[]{"\"Bug ID\"","\"Priority\"",
            "\"Opened\""});

        setDownloaderAndLogOnBugzilla(new File("Today_Bug_Submissions"),
            /* Username */ "ali@teampraxis.com", 
            /* Password */ "P@$$w0rd!", 
            /* Saved search */ "CQS Bug Submissions", 
            /* Start range (leave empty if not set) */ today,
            /* End range (leave empty if it is meant for a future
                or present timing)*/ tomorrow,
            /* Expected titles */ new String[]{"\"Bug ID\"","\"Priority\"",
            "\"Opened\""});

        setDownloaderAndLogOnBugzilla(new File("Yesterday_Bug_Submissions"),
            /* Username */ "ali@teampraxis.com", 
            /* Password */ "P@$$w0rd!", 
            /* Saved search */ "CQS Bug Submissions", 
            /* Start range (leave empty if not set) */ yesterday,
            /* End range (leave empty if it is meant for a future
                or present timing)*/ today,
            /* Expected titles */ new String[]{"\"Bug ID\"","\"Priority\"",
            "\"Opened\""});


        /* Stores a list of rows to add to the file. */
        String[][] newRows = {{""}};

        removeDuplicates(new File("Bugs"));
        removeDuplicates(new File("Bug_Submissions"));
        removeDuplicates(new File("Today_Bug_Submissions"));
        removeDuplicates(new File("Yesterday_Bug_Submissions"));
        /* The removeDuplicate method will make sure all files in each of the folders
        will have only one with the same name. It will also make sure that
        the lines of the csv are broken down.*/

        yesterdayFolder = new File("Yesterday_Bug_Submissions");
        yesterdayFiles = yesterdayFolder.listFiles();
        File yesterdayFile = new File("");
        if (yesterdayFiles.length > 0) {
            yesterdayFile = yesterdayFiles[0];
        }
        /*The code above will store the file representing the bugs submitted
        yesterday.*/

        todayFolder = new File("Today_Bug_Submissions");
        todayFiles = todayFolder.listFiles();
        File todayFile = new File("");
        if (todayFiles.length > 0) {
            todayFile = todayFiles[0];
        }
        /*The code above will store the file representing the bugs submitted
        today.*/

        ArrayList<String[]> chartForYesterday = makeChartFromCSV(yesterdayFile, 
            new String[]{"Bug ID","Priority","Opened"});

        /* This creates an ArrayList storing the contents of the yesterday's bug
        submission file.*/

        File folder2 = new File("Bug_Submissions");
        File[] listOfFiles2 = folder2.listFiles();
        
        for (int i = 0; i < listOfFiles2.length; i++) {
        /* Loops through all the files in the Bug_Submissions folder. */
            Date date = new Date();
            /* This initializes a Date object.*/

            try {
                String dateString = listOfFiles2[i].getName();
                dateString = dateString.substring(dateString.indexOf("20"), 
                    dateString.indexOf(".csv"));
                date = simpleDateFormat.parse(dateString);
                /* This parses the date that is naturally embedded in the
                name of the file, and stores is in the Date variable.*/
            }
            catch (ParseException e) {
                System.out.println(e);
                System.out.println("something went wrong before replacing.");
                System.exit(-1);
            }
            String formatCreationDate = simpleDateFormat.format(date);
            /* Changes the date back into a string so it can be
            compared with another string.*/
            if (formatCreationDate.equals(yesterday)) {
                writeChartToFile(listOfFiles2[i], chartForYesterday);
                /* If the current file in the loop is from yesterday, 
                it will be overwritten by the updated version of 
                yesterday.*/
            }
        }
        /* Once this is done, the file in the yesterday folder is no longer needed.*/


        
        File folder = new File("Bugs"); //--------------Folder containing the bug data.--------------------
        File[] listOfFiles = folder.listFiles();
        newRows = new String[listOfFiles.length][];

        /* The statements above access the files of the folder and 
        creates an array storing the rows, each of which represents
        the data.*/

        for (int i = 0; i < listOfFiles.length; i++) {
            String dateString = listOfFiles[i].getName();
            dateString = dateString.substring(dateString.indexOf("20"), 
                dateString.indexOf(".csv"));
            
            newRows[i] = convertBugStatsToRow(listOfFiles[i], dateString);
            /* Stores the value of each variable representing 
            the number of instances of the name in the array 
            representing a new row, then stores the new row
            as an element in the list of new rows.*/

        }
        String[] allBugsTitles = {"Date", "Open P1", "Open P2", "Open P3", "Open P4/P5",
            "All Open", "Resolved/ReadyToTest", "Closed", "Deferred"};

        ArrayList<String[]> lines2 = makeChartFromCSV(new File("BugStats.csv"), allBugsTitles);

        lines2.remove(0);
        lines2.add(0, allBugsTitles);
        /* Creates an ArrayList to store what we already have in the file
        since it will be overwritten and we want to keep what's there */
        
        for (int i = 0; i < newRows.length; i++) {
            /* Loops through the list of rows created
            from the files in the folder. */
            insertRowIntoChart(newRows[i], lines2);
        }

        String[] yesterdayRow = convertSubmissionsToRow(yesterdayFile, yesterday);
        String[] todayRow = convertSubmissionsToRow(todayFile, today);
        
        insertRowIntoChart(yesterdayRow, submissionChart);
        insertRowIntoChart(todayRow, submissionChart);
        /* Inserts or adds the rows created to the data.*/

        try {
            Files.delete(yesterdayFile.toPath());
            Files.delete(todayFile.toPath());
        }
        catch (IOException e){}
        /*Deletes the files from the folders representing yesterday and
        the today.*/

        for (int i = 2; i < submissionChart.size(); i++) {
            String[] temp = submissionChart.get(i);
            String[] temp2 = submissionChart.get(i - 1);
            for (int j = 2; j < temp.length; j++) {
                temp[j] = "" + (Integer.parseInt(temp[j]) +
                    Integer.parseInt(temp2[j]));
            }
        }
        /*Changes total bugs and each priority from the number submitted that
        day to the total number overall*/

        writeChartToFile("BugStats.csv", lines2);
        writeChartToFile("BugSubmissions.csv", submissionChart);
        /* Finally, we got all the rows, so we can write them to 
        the files.*/
    }

    public static void removeDuplicates(File folder) {
        try {
            File[] listOfFiles = folder.listFiles();
            for (int i = 0; i < listOfFiles.length; i++) {
                String name1 = listOfFiles[i].getName();
                if (name1.charAt(name1.length() - 7) == '('
                    && name1.charAt(name1.length() - 5) == ')') {
                    name1 = name1.substring(0, name1.length() - 7);
                }
                if (name1.charAt(name1.length() - 8) == '('
                    && name1.charAt(name1.length() - 5) == ')') {
                    name1 = name1.substring(0, name1.length() - 8);
                }
                name1 = name1.trim();
                for (int j = 0; j < listOfFiles.length; j++) {
                    String name2 = listOfFiles[j].getName();
                    name2 = name2.substring(0, name2.length() - 4);
                    name2 = name2.trim();
                    if (name1.equals(name2)) {
                        PrintWriter update = new PrintWriter(listOfFiles[j]);
                        Scanner informationForUpdate = new Scanner(listOfFiles[i]);
                        while (informationForUpdate.hasNextLine()) {
                            String lineOfUpdate = informationForUpdate.nextLine();
                            update.println(lineOfUpdate);
                        }
                        informationForUpdate.close();
                        Files.delete(listOfFiles[i].toPath());
                        update.flush();
                        update.close();
                    }
                }
            }
        }
        catch (FileNotFoundException e) {
            System.out.println(e);
            System.exit(1);
        }
        catch (IOException e){
            System.out.println(e);
            System.exit(-1);
        }
    }
    /* If two files have the same name but the difference is that one has a "(#)"
    (where "#" is any number between 1 and 99) is at the end of the file, then
    the contents of the duplicate are written into the original and the duplicate
    file is deleted. */

    public static void setDownloaderAndLogOnBugzilla(File dire, String username, 
        String password, String savedSearch,
        String beginTime, String endTime, String[] firstRowOfFile) {
        WebElement element2;
        boolean notSubmitted = false;
        
        FirefoxProfile fxProfile = new FirefoxProfile();

        fxProfile.setPreference("browser.download" + 
        ".folderList",2);
        fxProfile.setPreference("browser.helperApps.alwaysAsk.force", false);

        fxProfile.setPreference("browser.download.manager" + 
        ".showWhenStarting",false);
        fxProfile.setPreference("browser.download.dir",
        dire.getAbsolutePath());
        fxProfile.setPreference("browser.helperApps" + 
        ".neverAsk.saveToDisk","text/csv");
        //Sets up the firefox profile and makes it easier to save to right location.

        WebDriver driver2 = new FirefoxDriver(fxProfile);
        driver2.get("https://bugzilla2.teampraxis.com");
        try {
            element2 = (new WebDriverWait(driver2, 40)).until(new ExpectedCondition<WebElement>() {
                public WebElement apply(WebDriver input) {
                    return input.findElement(By.id("query"));
                }
            });
        }
        catch (TimeoutException e) {
            System.out.println("Page could not be loaded.");
            driver2.close();
            update.main(new String[0]);
            System.exit(1);
        }
        try {
            element2 = (new WebDriverWait(driver2, 2)).until(new ExpectedCondition<WebElement>() {
                public WebElement apply(WebDriver input) {
                    return input.findElement(By.partialLinkText("Log out"));
                }
            });
            element2.click();
        }
        catch (TimeoutException e) {}
        catch (ElementNotVisibleException e){}
        try {
            element2 = (new WebDriverWait(driver2, 20)).until(new ExpectedCondition<WebElement>() {
                public WebElement apply(WebDriver input) {
                    return input.findElement(By.id("login_link_top"));
                }
            });
            element2.click();
            element2 = (new WebDriverWait(driver2, 10)).until(new ExpectedCondition<WebElement>() {
                public WebElement apply(WebDriver input) {
                    return input.findElement(By.id("Bugzilla_login_top"));
                }
            });
            element2.clear();
            element2.sendKeys(new String[]{("" + username)});
            element2 = (new WebDriverWait(driver2, 5)).until(new ExpectedCondition<WebElement>() {
                public WebElement apply(WebDriver input) {
                    return input.findElement(By.id("Bugzilla_password_top"));
                }
            });
            element2.clear();
            element2.sendKeys(new String[]{("" + password)});
            element2 = (new WebDriverWait(driver2, 5)).until(new ExpectedCondition<WebElement>() {
                public WebElement apply(WebDriver input) {
                    return input.findElement(By.id("log_in_top"));
                }
            });
            element2.click();
            savedSearch = savedSearch.trim();
            savedSearch = savedSearch.replaceAll(" ", "%20");
            final String finalSavedSearch = savedSearch;
            element2 = (new WebDriverWait(driver2, 20)).until(new ExpectedCondition<WebElement>() {
                public WebElement apply(WebDriver input) {
                    return input.findElement(By.cssSelector("a[href=\"buglist" 
                        + ".cgi?cmdtype=runnamed&namedcmd=" + 
                        finalSavedSearch + "\"]"));
                }
            });
            element2.click();
            element2 = (new WebDriverWait(driver2, 20)).until(new ExpectedCondition<WebElement>() {
                public WebElement apply(WebDriver input) {
                    return input.findElement(By.partialLinkText("Edit Search"));
                }
            });
            if (beginTime.length() > 0 || endTime.length() > 0) {
                element2.click();
                element2 = (new WebDriverWait(driver2, 20)).until(new ExpectedCondition<WebElement>() {
                    public WebElement apply(WebDriver input) {
                        return input.findElement(By.id("add_button"));
                    }
                });
                if (!element2.isDisplayed()) {
                    WebElement element3 = (new WebDriverWait(driver2, 20)).until(new ExpectedCondition<WebElement>() {
                        public WebElement apply(WebDriver input) {
                            return input.findElement(By.id("chart"));
                        }
                    });
                    element3.click();
                }

                List<WebElement> elements2 = driver2.findElements(By.cssSelector("input[name=\"v1\"]"));
                if (elements2.size() == 0) {
                    element2.click();
                }
                else {
                    element2 = elements2.get(0);
                    element2.clear();
                    element2.sendKeys(new String[]{"" + beginTime + " 00:00:00"});
                }
                element2 = (new WebDriverWait(driver2, 20)).until(new ExpectedCondition<WebElement>() {
                    public WebElement apply(WebDriver input) {
                        return input.findElement(By.id("add_button"));
                    }
                });
                elements2 = driver2.findElements(By.cssSelector("input[name=\"v2\"]"));
                if (elements2.size() == 0) {
                    element2.click();
                }
                else {
                    element2 = elements2.get(0);
                    element2.clear();
                    element2.sendKeys(new String[]{"" + endTime + " 00:00:00"});
                }
                element2 = (new WebDriverWait(driver2, 20)).until(new ExpectedCondition<WebElement>() {
                    public WebElement apply(WebDriver input) {
                        return input.findElement(By.id("Search"));
                    }
                });
                element2.click();
                element2 = (new WebDriverWait(driver2, 20)).until(new ExpectedCondition<WebElement>() {
                    public WebElement apply(WebDriver input) {
                        return input.findElement(By.partialLinkText("Edit Search"));
                    }
                });
            }
            List<WebElement> elements2 = driver2.findElements(By.partialLinkText("CSV"));
            if (elements2.size() == 0) {
                System.out.println("Zero bugs submitted.");
                try {
                    FileWriter file = new FileWriter(dire.getName() + "\\bugs-" + 
                        (new SimpleDateFormat("yyyy-MM-dd")).format(new Date()) + 
                        ".csv");
                    BufferedWriter output = new BufferedWriter(file);
                    String firstRow = Arrays.toString(firstRowOfFile);
                    firstRow = firstRow.substring(1, firstRow.length() - 1);
                    output.write(firstRow);
                    output.flush();
                    output.close();
                }
                catch(Exception e) {
                    System.out.println("Error");
                }
            }
            else {
                element2 = elements2.get(0);
                element2.click();
                element2.click();
            }
        }
        catch (TimeoutException e) {
            System.out.println("Page took too long to load.");
            driver2.close();
            setDownloaderAndLogOnBugzilla(dire, username, password, 
            savedSearch, beginTime, endTime, firstRowOfFile);
        }
        driver2.close();
    }
    /* Sets a firefox profiles that downloads files to a specified folder, then logs
    onto a bugzilla account with a specified username and password and clicks on a
    saved search link. */

    public static ArrayList<String[]> makeChartFromCSV(File file, String[] titles) {
        ArrayList<String[]> lines = new ArrayList<String[]>();
        try {
            Scanner editFile = new Scanner(file);

            if (!editFile.hasNext()) {
                lines.add(titles);
            }
            while (editFile.hasNextLine()) {
                ArrayList<String> lineHolder = new ArrayList<String>();
                String lineText2 = editFile.nextLine();
                while (lineText2.indexOf(",") >= 0) {
                    String val = lineText2.substring(0, lineText2.indexOf(","));
                    lineHolder.add(val);
                    val = val.trim();
                    lineText2 = lineText2.substring(lineText2.indexOf(",") + 1);
                }
                lineHolder.add(lineText2);
                String[] line = new String[lineHolder.size()];
                for (int i = 0; i < line.length; i++) {
                    line[i] = lineHolder.get(i);
                }
                if (line[line.length - 1] != null && 
                    !line[line.length - 1].equals("")) {
                    lines.add(line);
                }
            }
            editFile.close();
        }
        catch (FileNotFoundException e) {
            lines.add(titles);
        }
        return lines;
    }
    /* Scans a file and converts the contents of the CSV file into an ArrayList
    of String arrays. */

    public static void writeChartToFile(String fileName, ArrayList<String[]> chart) {
        try {
            PrintWriter fileEditor = new PrintWriter(new FileWriter(fileName));
            for (int i = 0; i < chart.size(); i++) {
                String[] temp = chart.get(i);
                for (int j = 0; j < temp.length - 1; j++) {
                    if (temp[j] != null) {
                        fileEditor.write(temp[j]);
                    }
                    else {
                        fileEditor.write(0);
                    }
                    fileEditor.write(",");
                }
                fileEditor.write(temp[temp.length - 1]);
                fileEditor.println();
            }
            fileEditor.flush();
            fileEditor.close();
        }
        catch (IOException e) {
            System.out.println(e);
            System.out.println("Something went wrong file name.");
            System.exit(1);
        }
    }
    /* Formats an ArrayList of String arrays into a CSV data file, and writes
    a new file with the given name to store the data. */

    public static void writeChartToFile(File file, ArrayList<String[]> chart) {
        try {
            PrintWriter fileEditor = new PrintWriter(file);
            for (int i = 0; i < chart.size(); i++) {
                String[] temp = chart.get(i);
                for (int j = 0; j < temp.length - 1; j++) {
                    fileEditor.write(temp[j]);
                    fileEditor.write(",");
                }
                fileEditor.write(temp[temp.length - 1]);
                fileEditor.println();
            }
            fileEditor.flush();
            fileEditor.close();
        }
        catch (IOException e) {
            System.out.println(e);
            System.out.println("Something went wrong file.");
            System.exit(1);
        }
    }
    /* Formats an ArrayList of String arrays into a CSV data file, and writes
    a the data into a given File. */

    public static String[] convertSubmissionsToRow (File csvFile, String dateOf) {
        ArrayList<String[]> lines = makeChartFromCSV(csvFile,
                new String[]{"Bug ID","Priority","Opened"});
        Date date = new Date();
        try {
            date = (new SimpleDateFormat("yyyy-MM-dd")).parse(dateOf);
        }
        catch (ParseException e) {
            System.out.println("Invalid String.");
        }
        dateOf = (new SimpleDateFormat("M/d/yyyy")).format(date);
        
        int priorityIndex = 0;
        /* Initializes the integers that will store the location of that status and 
        priority for the first column */
        String firstline[] = lines.get(0);
        for (int j = 0; j < firstline.length; j++) {
            if (firstline[j].equalsIgnoreCase("priority") ||
                firstline[j].equalsIgnoreCase("\"priority\"")) {
                priorityIndex = j;
            }
        }
        int counts[] = {0, 0, 0, 0, 0, 0, 0};
        for (int j = 1; j < lines.size(); j++) {
            counts[0]++;
            counts[1]++;
            /*Increments the number of total bugs submitted that day
            as we loop down each row. */
            String[] thisLine = lines.get(j);
            String priorityType = thisLine[priorityIndex].toLowerCase();
            /* We go down each row in the column that has the priorities.*/
            switch (priorityType) {
                case "p1": case "\"p1\"":
                    counts[2]++;
                    break;
                case "p2": case "\"p2\"":
                    counts[3]++;
                    break;
                case "p3": case "\"p3\"":
                    counts[4]++;
                    break;
                case "p4": case "\"p4\"":
                    counts[5]++;
                    break;
                case "p5": case "\"p5\"":
                    counts[6]++;
                    break;
            }
            /*Increments the priority types. */
        }
        String[] submissionRow = new String[counts.length + 1];
        submissionRow[0] = dateOf;
        for (int j = 1; j < submissionRow.length; j++) {
            submissionRow[j] = "" + counts[j - 1];
        }
        return submissionRow;
    }

    public static String[] convertBugStatsToRow (File csvFile, String dateOf) {
        ArrayList<String[]> lines = makeChartFromCSV(csvFile, 
                new String[]{"Bug ID","Opened","Priority"});
        /* Gathers the contents of the file and stores it in an ArrayList.*/

        Date date = new Date();
        try {
            date = (new SimpleDateFormat("yyyy-MM-dd")).parse(dateOf);
        }
        catch (ParseException e) {
            System.out.println("Invalid String.");
        }
        dateOf = (new SimpleDateFormat("M/d/yyyy")).format(date);

        int statusIndex = 0, priorityIndex = 0;
        /* Initializes the integers that will store the location of that status and 
        priority for the first column */
        String firstline[] = lines.get(0);
        for (int j = 0; j < firstline.length; j++) {
            if (firstline[j].equalsIgnoreCase("status") ||
                firstline[j].equalsIgnoreCase("\"status\"")) {
                statusIndex = j;
            }
            if (firstline[j].equalsIgnoreCase("priority") ||
                firstline[j].equalsIgnoreCase("\"priority\"")) {
                priorityIndex = j;
            }
        }
        /* Looks for the column representing the status and the column
        representing the priority by looping through the first row 
        and stores the location.*/
            
        int columntitles[] = {0, 0, 0, 0, 0, 0, 0, 0};
        /* Creates an array of integers that will increment
        every time we find an element matching a specific name. */
            
        for (int j = 0; j < lines.size(); j++) {
            String line[] = lines.get(j);
            String status = line[statusIndex].toLowerCase();
            String priority = line[priorityIndex].toLowerCase();
            switch (status) {
                case "open": case "\"open\"":
                    columntitles[4]++;
                    switch (priority) {
                        case "p1": case "\"p1\"":
                            columntitles[0]++;
                            break;
                        case "p2": case "\"p2\"":
                            columntitles[1]++;
                            break;
                        case "p3": case "\"p3\"":
                            columntitles[2]++;
                            break;
                        case "p4": case "p5": 
                        case "\"p4\"": case "\"p5\"":
                            columntitles[3]++;
                            break;
                        default: 
                            columntitles[4]--;
                            break;
                    }
                    break;
                case "resolved": case "readytotest":
                case "\"resolved\"": case "\"readytotest\"":
                case "\"ready to test\"": case "ready to test":
                    columntitles[5]++;
                    break;
                case "closed": case "\"closed\"":
                    columntitles[6]++;
                    break;
                case "deferred": case "\"deferred\"":
                    columntitles[7]++;
                    break;
                default:
                    break;
            }
        }
        /* The code above scans each line for the matching names,
        and increments the variables representing the number of 
        instances of each name or combination of names.*/

        String newRow[] = new String[columntitles.length + 1];
        newRow[0] = dateOf;
        for (int j = 1; j < newRow.length; j++) {
            newRow[j] = "" + columntitles[j - 1];
        }
        return newRow;
    }

    public static void insertRowIntoChart (String[] row, ArrayList<String[]> chart) {
        boolean inserted = false;
        for (int j = 1; j < chart.size(); j++) {
            Date rowDate = new Date(), thisDate = new Date();
            /* Initializes the dates for comparison */
            try {
                SimpleDateFormat formatter = new
                        SimpleDateFormat("M/d/yyyy");
                rowDate = formatter.parse(row[0]);
                String[] temp = chart.get(j);
                thisDate = formatter.parse(temp[0]);
            }
            /*Stores the value of the dates for comparison. */
            catch (ParseException e) {
                System.out.println(e);
                System.out.println(Arrays.toString(row));
                System.out.println(Arrays.toString(chart.get(j)));
                System.exit(1);
            }
            if (!inserted && thisDate.after(rowDate)) {
                chart.add(j, row);
                inserted = true;
            }
            else if (!inserted && thisDate.equals(rowDate)) {
                chart.remove(j);
                chart.add(j, row);
                inserted = true;
            }
        }
        if (!inserted) {
            chart.add(row);
        }
    }
}