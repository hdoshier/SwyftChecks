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

        try (
                Scanner importFile = new Scanner(Paths.get(filePath));
        ) {
            //TODO dont forget excluded set!


            // skips first line of csv
            String line = importFile.nextLine();

            // loops through each line in the CSV
            while (importFile.hasNext()) {
                line = importFile.nextLine();
                String[] splitLine = line.split("~");

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
                updateOfficeData(db, splitLine, formatter);

            }
            db.sortOfficeList();

        } catch (Exception e) {
            e.printStackTrace();
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

        // splitLine index content
        //0 Office Code | 1 Office Name | 2 Owner Name | 3 Owner Email | 4 Owner Phone | 7 Agreement Exec. Date
        //11 Dec 24 (current month) | 12 Nov 24 | 13 Oct 24 | 14 Sep 24 | 15 Aug 24 | 16 Jul 24 | 17 Jun 24

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
        YearMonth month = YearMonth.now();
        for (int i = 11; i < 18; i++) {
            office.addBillableHourHistory(month, Double.parseDouble(splitLine[i]));
            month = month.minusMonths(1);
        }

    }


    //TODO create import report. Show offices added and offices excluded
    //each import should create a unique file
}
