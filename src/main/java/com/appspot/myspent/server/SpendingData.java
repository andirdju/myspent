/**
 * 
 */
package com.appspot.myspent.server;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.gson.Gson;

/**
 * @author andi
 * 
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class SpendingData {
	private static final String idPref = "{\"id\":";
	private static final String createDatePref = ",\"createDate\":";
	private static final String changeDatePref = ",\"changeDate\":";
	private static final String purpPref = ",\"purpose\":";
	private static final String amtPref = ",\"amount\":";
	private static final String chngPref = ",\"change\":";
	private static final String activePref = ",\"active\":";
	private static final String end = "}";
	private static final String timeZone = "GMT+07:00";

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;

	@Persistent
	private Date createDate = new Date();

	@Persistent
	private Date changeDate = new Date();

	@Persistent
	private String purpose;

	@Persistent
	private String user;

	@Persistent
	private double amount;

	@Persistent
	private double change;

	@Persistent
	private boolean active;

	/**
	 * 
	 */
	public SpendingData() {

	}

	/**
	 * @param purp
	 * @param amt
	 * @param usr
	 * @param crtDate
	 * @param act
	 * @param chngDate
	 */
	public SpendingData(final String purp, final double amt, final String usr,
			final Date crtDate, final boolean act, final Date chngDate) {
		this.purpose = purp;
		this.amount = amt;
		this.user = usr;
		this.createDate = crtDate;
		this.changeDate = chngDate;
		this.active = act;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		final SpendingData other = (SpendingData) obj;
		if (this.id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!this.id.equals(other.id)) {
			return false;
		}
		return true;
	}

	/**
	 * @return the amount
	 */
	public double getAmount() {
		return this.amount;
	}

	/**
	 * @return the change
	 */
	public double getChange() {
		return this.change;
	}

	/**
	 * @return the changeDate
	 */
	public Date getChangeDate() {
		return this.changeDate;
	}

	/**
	 * @return
	 */
	public String getChangeDateStr() {
		if (this.changeDate != null) {
			final DateFormat df = DateFormat.getDateTimeInstance(
					DateFormat.SHORT, DateFormat.SHORT, Locale.getDefault());
			df.setTimeZone(TimeZone.getTimeZone(SpendingData.timeZone));
			return df.format(this.changeDate);
		}
		return null;
	}

	/**
	 * @return
	 */
	public double getChangePercent() {
		return 100.0 * this.change / this.amount;
	}

	/**
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return this.createDate;
	}

	/**
	 * @return
	 */
	public String getCreateDateStr() {
		if (this.createDate != null) {
			final DateFormat df = DateFormat.getDateTimeInstance(
					DateFormat.SHORT, DateFormat.SHORT, Locale.getDefault());
			df.setTimeZone(TimeZone.getTimeZone(SpendingData.timeZone));
			return df.format(this.createDate);
		}
		return null;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * @return the purpose
	 */
	public String getPurpose() {
		return this.purpose;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return this.user;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		return result;
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return this.active;
	}

	/**
	 * @param active
	 *            the active to set
	 */
	public void setActive(final boolean active) {
		this.active = active;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(final double price) {
		this.amount = price;
	}

	/**
	 * @param change
	 *            the change to set
	 */
	public void setChange(final double change) {
		this.change = change;
	}

	/**
	 * @param changeDate
	 *            the changeDate to set
	 */
	public void setChangeDate(final Date changeDate) {
		this.changeDate = changeDate;
	}

	/**
	 * @param createDate
	 *            the createDate to set
	 */
	public void setCreateDate(final Date actionDate) {
		this.createDate = actionDate;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(final Long id) {
		this.id = id;
	}

	/**
	 * @param purpose
	 *            the purpose to set
	 */
	public void setPurpose(final String symbol) {
		this.purpose = symbol;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(final String user) {
		this.user = user;
	}

	/**
	 * @return
	 */
	public final String toJson(final Gson gsn) {
		return SpendingData.idPref + gsn.toJson(this.id)
				+ SpendingData.createDatePref
				+ gsn.toJson(this.getCreateDateStr())
				+ SpendingData.changeDatePref
				+ gsn.toJson(this.getChangeDateStr()) + SpendingData.purpPref
				+ gsn.toJson(this.purpose) + SpendingData.amtPref
				+ gsn.toJson(this.amount) + SpendingData.chngPref
				+ gsn.toJson(this.change) + SpendingData.activePref
				+ gsn.toJson(this.active) + SpendingData.end;
	}
}
