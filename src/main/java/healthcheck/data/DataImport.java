package healthcheck.data;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class DataImport {

    public static void importOfficeData(String filePath) {

        Database db = Database.getInstance();
        //HashMap<String, Office> officeMap = db.getOfficeMap();
        ArrayList<Office> officeList = db.getOfficeList();

        try (
                Scanner importFile = new Scanner(Paths.get(filePath));
        ) {
            //dont forget excluded set!
            // 0 Office code | 1 Office Name | 2
            //0 Office CD | 1 Description~ | 2 Owner Name | 3 Owner Email | 4 Owner Phone | 7 Agreement Exec. Date| 11 Dec 24 | 12 Nov 24 | 13 Oct 24 | 14 Sep 24 | 15 Aug 24 | 16 Jul 24 | 17 Jun 24


            String line = importFile.nextLine();

            // loops through each line in the CSV
            while (importFile.hasNext()) {
                line = importFile.nextLine();
                String[] splitLine = line.split("~");

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
                updateOfficeData(db, splitLine, formatter);

            }

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    /**
     * Updates data about an office during the data import. If the office doesn't exist it will create then set that
     * office's data.
     *
     * @param db the database instance
     * @param splitLine String array containing all the office data from the CSV.
     */
    private static void updateOfficeData(Database db, String[] splitLine, DateTimeFormatter formatter) {
        String officeCode = splitLine[0].replace('-', ' ').trim();

        //check if it's an excluded office
        if (officeCode.contains("CFC") || db.getExcludedOffices().contains(officeCode)) {
            //TODO add to excluded offices part of the report
            return;
        }

        Office office;
        HashMap<String, Office> officeMap = db.getOfficeMap();
        if (officeMap.containsKey(officeCode)) {
            office = officeMap.get(officeCode);
        } else {
            office = new Office(officeCode);
            officeMap.put(officeCode, office);
            db.getOfficeList().add(office);
        }

        //TODO log changes to office data
        office.setOfficeName(splitLine[1].trim());
        office.setOfficeOwner(splitLine[2].trim());
        office.setOfficeOwnerEmail(splitLine[3].trim());
        office.setOfficePrimaryContactPerson(splitLine[2].trim());
        office.setOfficePrimaryContactEmail(splitLine[3].trim());
        try {
            office.setExecAgreementDate(LocalDate.parse(splitLine[7], formatter));
        } catch (DateTimeParseException e) {

        }

        // add billable hour history
        office.addBillableHourHistory(YearMonth.now(), Double.parseDouble(splitLine[11]));
        office.addBillableHourHistory(YearMonth.now().minusMonths(1), Double.parseDouble(splitLine[12]));
        office.addBillableHourHistory(YearMonth.now().minusMonths(2), Double.parseDouble(splitLine[13]));
        office.addBillableHourHistory(YearMonth.now().minusMonths(3), Double.parseDouble(splitLine[14]));
        office.addBillableHourHistory(YearMonth.now().minusMonths(4), Double.parseDouble(splitLine[15]));
        office.addBillableHourHistory(YearMonth.now().minusMonths(5), Double.parseDouble(splitLine[16]));
        office.addBillableHourHistory(YearMonth.now().minusMonths(6), Double.parseDouble(splitLine[17]));

    }


    //create import report. Show offices added and offices excluded
    //each import should create a unique file
}
