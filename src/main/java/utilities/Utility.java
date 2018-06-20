package utilities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.csv.CSVRecord;

import dtos.Record;
import exceptions.*;
import constants.Constants;

public class Utility {
	
	// Custom method to parse the input date of the CSV file.
	public static Date parseDate(String input) throws Exception {
		// Separating the string to get date fields.
		String [] dateFields = input.split(" ");
		String dayString = dateFields[0];
		String monthString = dateFields[1];
		String yearString = dateFields[2];
		String formattedDate = "";
		
		if(yearString.isEmpty() || Integer.valueOf(yearString) == null) {
			throw new ExerciseDateException("ERROR. One of the date columns are not set properly!\n"
					+ "Please follow this format: 'dd (Month spelled out in 3 characters) yyyy'");
		} else {
			formattedDate = yearString.concat("-");
		}
		
		if(monthString.isEmpty() || Constants.MONTHS.indexOf(monthString.toLowerCase()) < 0) {
			throw new ExerciseDateException("ERROR. One of the date columns are not set properly!\n"
					+ "Please follow this format: 'dd (Month spelled out in 3 characters) yyyy'");
		} else {
			monthString = Integer.toString(Constants.MONTHS.indexOf(monthString.toLowerCase()) + 1);
			if(monthString.length() == 1) {
				String temp = "0";
				temp = temp.concat(monthString);
				monthString = temp;
			}
			formattedDate = formattedDate.concat(monthString).concat("-");
		}
		
		if(dayString.isEmpty() || Integer.valueOf(dayString) == null) {
			throw new ExerciseDateException("ERROR. One of the date columns are not set properly!\n"
					+ "Please follow this format: 'dd (Month spelled out in 3 characters) yyyy'");
		} else {
			formattedDate = formattedDate.concat(dayString);
		}
		
        return new SimpleDateFormat("yyyy-MM-dd").parse(formattedDate);
	}
	
	// Did not want to import a library such as Jackson because of all of the dependencies.
	// Made method to give the appropriately mapped object instead.
	public static Record createRecordObject(CSVRecord csvRecord) throws Exception {
		String entity = null; 
		String currency = null;
		double agreedFx = 0; 
		double pricePerUnit = 0;
		char buyOrSellFlag = '\0';
		int units = 0;
		Date instructionDate = null;
		Date settlementDate = null;
		
		String temp = null;
		
		// Some of the functions below make use of parsing and valueOf methods.
		// From Java API documentation, parsers for Integer, Double, etc will return an instantiated object
		// whereas valueOf simply does just that--returns the value.
		
		entity = csvRecord.get("Entity").trim();
		if(entity.isEmpty()) {
			throw new EntityException("ERROR. The Entity column is not set properly!");
		}
		
		currency = csvRecord.get("Currency").trim();
		if(currency.isEmpty() || currency == null || Constants.CURRENCIES.indexOf(currency.toUpperCase()) < 0) {
			throw new CurrencyException("ERROR. The Currency column is not set properly! Currency may either be non-extistent or there is nothing in this column.");
		}
		
		temp = csvRecord.get("AgreedFx").trim();
		if(temp.isEmpty() || Double.valueOf(temp) == null || Double.valueOf(temp) < 0) {
			throw new AgreedFxException("ERROR. The AgreedFx column is not set properly!");
		} else {
			agreedFx = Double.parseDouble(temp);
		}
		
		temp = csvRecord.get("Price per unit").trim();
		if(temp.isEmpty() || Double.valueOf(temp) == null || Double.valueOf(temp) < 0) {
			throw new PricePerUnitException("ERROR. The Price per unit column is not set properly!");
		} else {
			pricePerUnit = Double.parseDouble(csvRecord.get("Price per unit"));
		}
		
		temp = csvRecord.get("Buy/Sell").trim();
		if(temp.length() > 1 || temp.isEmpty() || (!temp.equalsIgnoreCase("B") && !temp.equalsIgnoreCase("S"))) {
			throw new BuyOrSellFlagException("ERROR. The Buy/Sell flag is not set properly!");
		} else {
			buyOrSellFlag = temp.toUpperCase().charAt(0);
		}
		
		temp = csvRecord.get("Units").trim();
		if(temp.isEmpty() || Integer.valueOf(temp) == null || Integer.valueOf(temp) < 0) {
			throw new UnitsException("ERROR. The Units column is not set properly!");
		} else {
			units = Integer.parseInt(csvRecord.get("Units"));
		}
		
		instructionDate = parseDate(csvRecord.get("InstructionDate").trim());
		settlementDate = parseDate(csvRecord.get("SettlementDate").trim());
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(settlementDate);
		
		int settlementDateDayOfWeekNum =cal.get(Calendar.DAY_OF_WEEK);
		
		String settlementDayOfWeek = Constants.DAYS_OF_WEEK.get(settlementDateDayOfWeekNum - 1);
		
		if(entity.equalsIgnoreCase("aed") || entity.equalsIgnoreCase("sar")) {
			if(settlementDayOfWeek.equalsIgnoreCase("fri") || settlementDayOfWeek.equalsIgnoreCase("sat")) {
				throw new ExerciseDateException("ERROR. The settlement date is not set properly!\n"
					+ "It must be set on a workday!"); 
			} 
		} else {
			if(settlementDayOfWeek.equalsIgnoreCase("sun") || settlementDayOfWeek.equalsIgnoreCase("sat")) {
				throw new ExerciseDateException("ERROR. The settlement date is not set properly for "+ settlementDate +"!\n"
						+ "It must be set on a workday!"); 
			}
		}
		
		if(settlementDate.before(instructionDate)) {
			throw new ExerciseDateException("ERROR. The dates are not properly set! "
					+ "You cannot have an instruction date after a settlement date for the same record!");
		}
		
		return new Record()
				.withAgreedFx(agreedFx)
				.withPricePerUnit(pricePerUnit)
				.withEntity(entity)
				.withCurrency(currency)
				.withBuyOrSellFlag(buyOrSellFlag)
				.withUnits(units)
				.withInstructionDate(instructionDate)
				.withSettlementDate(settlementDate);
	}
	
	public static double calculateAmountOfTrade(int units, double pricePerUnit, double agreedFx) {
		return units*pricePerUnit*agreedFx;
	}
}
