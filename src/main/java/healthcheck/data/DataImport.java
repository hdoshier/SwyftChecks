package healthcheck.data;

import healthcheck.data.firestore.Database;
import healthcheck.data.firestore.ReadData;
import healthcheck.data.firestore.WriteData;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class DataImport {

    public static void importOfficeData(String filePath) {

        try (
                Scanner importFile = new Scanner(Paths.get(filePath));
        ) {
            //TODO dont forget excluded set!


            // skips first line of csv
            String line = importFile.nextLine();
            ArrayList<Office> officeList = Database.getInstance().getOfficeList();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");

            // loops through each line in the CSV
            while (importFile.hasNext()) {
                line = importFile.nextLine();
                String[] splitLine = line.split("~");
                updateOfficeData(splitLine, officeList, formatter);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates data about an office during the data import. If the office doesn't exist it will create then set that
     * office's data.
     *
     * @param splitLine String array containing all the office data from the CSV.
     */
    private static void updateOfficeData(String[] splitLine, ArrayList<Office> officeList, DateTimeFormatter formatter) {
        String officeCode = splitLine[0].replace('-', ' ').trim();



        // TODO create an explicit excluded office list in settings - ex. ABS KS WCT
        if (isExcludedOffice(officeCode)) {
            return;
        }

        Office office;
        // if database already contains office, only add billable history
        // else create a new office
        if (officeList.contains(officeCode)) {
            office = ReadData.readIndividualOffice(new Office(officeCode));
            addBillableHourHistoryToOffice(office, splitLine);
            WriteData.saveOffice(office);
            return;
        }

        office = new Office(officeCode);
        // splitLine index content
        //0 Office Code | 1 Office Name | 2 Owner Name | 3 Owner Email | 4 Owner Phone | 7 Agreement Exec. Date
        //11 Dec 24 (current month) | 12 Nov 24 | 13 Oct 24 | 14 Sep 24 | 15 Aug 24 | 16 Jul 24 | 17 Jun 24

        office.setOfficeName(splitLine[1].trim());
        office.setOfficeOwner(splitLine[2].trim());
        office.setOfficeOwnerEmail(splitLine[3].trim());
        office.setOfficePrimaryContactPerson(splitLine[2].trim());
        office.setOfficePrimaryContactEmail(splitLine[3].trim());
        office.setOfficePrimaryContactPhone(splitLine[4].trim());
        try {
            office.setExecAgreementDate(LocalDate.parse(splitLine[7], formatter));
        } catch (DateTimeParseException e) {
            //do nothing
        }

        // add billable hour history
        addBillableHourHistoryToOffice(office, splitLine);
        officeList.add(office);

        WriteData.saveOffice(office);
    }

    private static void addBillableHourHistoryToOffice(Office office, String[] splitLine) {
        YearMonth month = YearMonth.now();
        for (int i = 11; i < 18; i++) {
            office.addBillableHourHistory(month, Double.parseDouble(splitLine[i]));
            month = month.minusMonths(1);
        }
    }

    private static boolean isExcludedOffice(String officeCode) {
        if (officeCode.contains("CFC ") || officeCode.contains("AYS ")) {
            return true;
        }
        if (officeCode.contains("DEMO")) {
            return true;
        }
        if (officeCode.equals("DEV")) {
            return true;
        }
        if (officeCode.contains("DEV BC VAN")) {
            return true;
        }
        if (officeCode.contains("DEV FL JAX")) {
            return true;
        }
        if (officeCode.contains("DEV GB LIV")) {
            return true;
        }
        if (officeCode.contains("DEV KS WCT")) {
            return true;
        }
        if (officeCode.contains("DEV OH CLB")) {
            return true;
        }
        if (officeCode.contains("SWYFTOPS")) {
            return true;
        }
        if (officeCode.contains("CORPO")) {
            return true;
        }
        if (officeCode.contains("SUB TRAINING")) {
            return true;
        }
        return false;
    }


    //TODO create import report. Show offices added
    //each import should create a unique file
}
