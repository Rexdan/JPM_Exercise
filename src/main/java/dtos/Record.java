package dtos;

import java.util.Date;

/**
 * The purpose of this Object is to make accessing each row value in the CSV file easier.
 * 
 * @author Andre.Pereira
 *
 */
public class Record implements Comparable<Record> {

	private String entity, currency;
	private Date instructionDate, settlementDate;
	private char buyOrSellFlag;
	private double agreedFx, pricePerUnit, amountTraded;
	private int units, ranking;

	public Record() {
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}
	
	public Record withEntity(String entity) {
		this.entity = entity;
		return this;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public Record withCurrency(String currency) {
		this.currency = currency;
		return this;
	}

	public Date getInstructionDate() {
		return instructionDate;
	}

	public void setInstructionDate(Date instructionDate) {
		this.instructionDate = instructionDate;
	}
	
	public Record withInstructionDate(Date instructionDate) {
		this.instructionDate = instructionDate;
		return this;
	}

	public Date getSettlementDate() {
		return settlementDate;
	}

	public void setSettlementDate(Date settlementDate) {
		this.settlementDate = settlementDate;
	}
	
	public Record withSettlementDate(Date settlementDate) {
		this.settlementDate = settlementDate;
		return this;
	}

	public char getBuyOrSell() {
		return buyOrSellFlag;
	}

	public void setBuyOrSellFlag(char buyOrSellFlag) {
		this.buyOrSellFlag = buyOrSellFlag;
	}
	
	public Record withBuyOrSellFlag(char buyOrSellFlag) {
		this.buyOrSellFlag = buyOrSellFlag;
		return this;
	}

	public double getAgreedFx() {
		return agreedFx;
	}

	public void setAgreedFx(double agreedFx) {
		this.agreedFx = agreedFx;
	}
	
	public Record withAgreedFx(double agreedFx) {
		this.agreedFx = agreedFx;
		return this;
	}

	public double getPricePerUnit() {
		return pricePerUnit;
	}

	public void setPricePerUnit(double pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}
	
	public Record withPricePerUnit(double pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
		return this;
	}

	public int getUnits() {
		return units;
	}

	public void setUnits(int units) {
		this.units = units;
	}
	
	public Record withUnits(int units) {
		this.units = units;
		return this;
	}
	
	public double getAmountTraded() {
		return amountTraded;
	}

	public void setAmountTraded(double amountTraded) {
		this.amountTraded = amountTraded;
	}
	
	public Record withAmountTraded(double amountTraded) {
		this.amountTraded = amountTraded;
		return this;
	}
	
	public int getRanking() {
		return ranking;
	}

	public void setRanking(int ranking) {
		this.ranking = ranking;
	}
	
	public Record withRanking(int ranking) {
		this.ranking = ranking;
		return this;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Ranking : ");
		builder.append(ranking+"\n");
		builder.append("Entity : ");
		builder.append(entity+"\n");
		builder.append("Amount Traded (USD): ");
		builder.append(amountTraded+"\n");
		builder.append("Currency : ");
		builder.append(currency+"\n");
		builder.append("Instruction Date : ");
		builder.append(instructionDate+"\n");
		builder.append("Settlement Date : ");
		builder.append(settlementDate+"\n");
		builder.append("Buy Or Sell Flag : ");
		builder.append(buyOrSellFlag+"\n");
		builder.append("AgreedFx : ");
		builder.append(agreedFx+"\n");
		builder.append("Price Per Unit : ");
		builder.append(pricePerUnit+"\n");
		builder.append("Units : ");
		builder.append(units);
		return builder.toString();
	}

	// Used for the ranking system.
	public int compareTo(Record r) {
		if(this.amountTraded > r.getAmountTraded())
			return -1;
		else if(this.amountTraded < r.getAmountTraded())
			return 1;
		else if(this.amountTraded == r.getAmountTraded())
			return 0;
		return -2;
	}
}
