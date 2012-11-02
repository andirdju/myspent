/**
 * 
 */
package com.appspot.myspent.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * @author andi
 * 
 */
@RemoteServiceRelativePath("spending")
public interface MySpentService extends RemoteService {

	public void addSpending(String purp, double amt);

	public String getAllPrices(int startPast, int length)
			throws MySpentException;

	public String getLogoutLink() throws MySpentException;

	public String getPrices(int startPast, int length) throws MySpentException;

	public void inactivateSpending(int id);
}
