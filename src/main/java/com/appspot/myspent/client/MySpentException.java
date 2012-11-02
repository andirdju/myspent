/**
 * 
 */
package com.appspot.myspent.client;

import java.io.Serializable;

/**
 * @author andi
 * 
 */
public class MySpentException extends Exception implements Serializable {
	private String symbol;

	/**
	 * 
	 */
	public MySpentException() {
		// do nothing
	}

	/**
	 * @param symb
	 */
	public MySpentException(final String symb) {
		this.symbol = symb;
	}

	/**
	 * @return the symbol
	 */
	public String getSymbol() {
		return this.symbol;
	}

	/**
	 * @param symbol
	 *            the symbol to set
	 */
	public void setSymbol(final String symbol) {
		this.symbol = symbol;
	}

}
