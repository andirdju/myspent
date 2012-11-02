/**
 * 
 */
package org.andird.myspent.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author andi
 * 
 */
public interface MySpentServiceAsync {

	void addSpending(String purp, double amt, AsyncCallback<Void> callback); 

	void getAllPrices(int startPast, int length, AsyncCallback<String> callback);

	void getLogoutLink(AsyncCallback<String> callback);

	void getPrices(int startPast, int length, AsyncCallback<String> callback);

	void inactivateSpending(int id, AsyncCallback<Void> callback);
}
