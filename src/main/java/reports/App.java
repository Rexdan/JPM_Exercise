package reports;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import constants.Constants;
import dtos.Record;
import utilities.Utility;

// I would normally add a logger here, but the exercise specifically asked for
// using as few dependencies as possible.
// Going to have to use System.out.println() statements. Sorry!

public class App 
{
	public static void getFileNames(String rootDir, ArrayList<File> fileList) {
		File root = new File(rootDir);
	    File [] list = root.listFiles();
	    
	    for ( File f : list ) {
	        if ( f.isDirectory() ) {
	        	getFileNames(f.getAbsolutePath(), fileList);
	        } else {
	        	fileList.add(f);
	        }
	    }
	}
	
    public static void main( String[] args )
    {
    	String rootDirForTestFiles = "src/main/resources";
    	
    	// Getting test files.
    	ArrayList<File> fileList = new ArrayList<File>();
    	getFileNames(rootDirForTestFiles, fileList);
    	
    	// Easier to just take the keys, sort them, and then pull out each value from the maps.
    	List<Record> outgoingEntities = new ArrayList<Record>();
		List<Record> incomingEntities = new ArrayList<Record>();
    	
    	for(File file : fileList) {
    		if(file.exists()) {
        		System.out.println("File exists. Attempting to parse: " + file.getName());
        		try {
        			FileReader reader = new FileReader(file);
        			boolean hasFileHeader = true;
        			// Tried with xlsx format, but apache did not parse it correctly even with the
        			// EXCEL qualifier.
        			CSVParser parser = new CSVParser(reader, 
        									CSVFormat.DEFAULT
        									.withHeader(Constants.FILE_HEADER)
        									.withSkipHeaderRecord(hasFileHeader)
        									.withIgnoreEmptyLines(true));
        			
        			// Exercise said "All data to be in memory." Read in file contents into stack.
        			// Would have just parsed it record by record otherwise.
        			List<CSVRecord> list = parser.getRecords();
        			
        			// Creating DTO's (Data Transferable Objects) for ease of data manipulation throughout code.
        	    	for(int i = 0; i < list.size(); i++) {
        	    		Record record = null;
        	    		try {
        	    			record = Utility.createRecordObject(list.get(i));
        	    			
        	    			// Using Calendar because Date has a lot of deprecated methods.
            	    		Calendar cal = Calendar.getInstance();
            	    		
            	    		cal.setTime(record.getInstructionDate());
            	    		int instructionDateDayOfWeekNum = cal.get(Calendar.DAY_OF_WEEK);
            	    		
            	    		cal.setTime(record.getSettlementDate());
            	    		int settlementDateDayOfWeekNum = cal.get(Calendar.DAY_OF_WEEK);
            	    		
            	    		String instructionDayOfWeek = Constants.DAYS_OF_WEEK.get(instructionDateDayOfWeekNum - 1);
            	    		String settlementDayOfWeek = Constants.DAYS_OF_WEEK.get(settlementDateDayOfWeekNum - 1);
            	    		
            	    		String currency = record.getCurrency();
            	    		
            	    		// Case for when the work week is Sun-Thu
            	    		if(currency.equalsIgnoreCase("aed") || 
            	    			currency.equalsIgnoreCase("sar")) {
            	    			if(instructionDayOfWeek.equalsIgnoreCase("fri") || 
            	    				instructionDayOfWeek.equalsIgnoreCase("sat")) {
            	    				// Settlement Day should be changed to next working day.
            	    				// Don't have to set cal again because it was previously set above.
            	    				if(settlementDayOfWeek.equalsIgnoreCase("thu")) {
            	    					cal.add(Calendar.DAY_OF_MONTH, 3);
            	    				} else {
            	    					cal.add(Calendar.DAY_OF_MONTH, 1);
            	    				}
            	    			}
            	    		} else {
            	    			if(instructionDayOfWeek.equalsIgnoreCase("sat") || 
            	    				instructionDayOfWeek.equalsIgnoreCase("sun")) {
            	    				// Settlement Day should be changed to next working day.
            	    				// Don't have to set cal again because it was previously set above.
            	    				if(settlementDayOfWeek.equalsIgnoreCase("fri")) {
            	    					cal.add(Calendar.DAY_OF_MONTH, 3);
            	    				} else {
            	    					cal.add(Calendar.DAY_OF_MONTH, 1);
            	    				}
        						}
            	    		}
            	    		
            	    		// Resetting it here in case it got changed above.
            	    		record.setSettlementDate(cal.getTime());
            	    		
            	    		int units = record.getUnits();
            	    		double pricePerUnit = record.getPricePerUnit();
            	    		double agreedFx = record.getAgreedFx();    	    		
            	    		double amountTraded = Utility.calculateAmountOfTrade(units, pricePerUnit, agreedFx);
            	    		record.setAmountTraded(amountTraded);
            	    		
            	    		// Ranking
            	    		if(record.getBuyOrSell() == 'B') {
            	    			outgoingEntities.add(record);
            	    		} else if(record.getBuyOrSell() == 'S') {
            	    			incomingEntities.add(record);
            	    		}    	    		
        	    			
        	    		} catch(Exception e) {
        	    			e.printStackTrace();
        	    		}
        	    	}
        	    	
        	    	System.out.println("File parsed. Showing records in ranked order based on the amount traded.");
        	    	
    				parser.close();
    			} catch(FileNotFoundException e) {
    				System.out.println("ERROR. Could not find file specified. Given absolute path: " + file.getAbsolutePath());
    				e.printStackTrace();
    			} catch (IOException e) {
    				System.out.println("ERROR. There's something wrong with the file.");
    				e.printStackTrace();
    			}
        	} else {
        		System.out.println("Specified file path does not exist.");
        	}
    	}
    	// Easier to simply sort these after ingesting them for such a small amount of records.
    	Collections.sort(incomingEntities);
    	Collections.sort(outgoingEntities);
    	
    	System.out.println("###########################################################Incoming Entities###########################################################");
    	for(Record record : incomingEntities) {
    		record.setRanking(incomingEntities.indexOf(record) + 1);
    		System.out.println(record);
    		if(incomingEntities.indexOf(record) != incomingEntities.size() - 1)
    			System.out.println("*******************************************************************");
    	}
    	
    	System.out.println("###########################################################Outgoing Entities###########################################################");
    	for(Record record : outgoingEntities) {
    		record.setRanking(outgoingEntities.indexOf(record) + 1);
    		System.out.println(record);
    		if(outgoingEntities.indexOf(record) != outgoingEntities.size() - 1)
    			System.out.println("*******************************************************************");
    	}
    }
}
