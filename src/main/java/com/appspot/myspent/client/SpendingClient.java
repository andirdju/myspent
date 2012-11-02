/**
 * 
 */
package com.appspot.myspent.client;

import java.io.Serializable;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

/**
 * @author andi
 * 
 */
public class SpendingClient extends JavaScriptObject implements Serializable {

	/**
	 * @param jsonStr
	 * @return
	 */
	public static final native JsArray<SpendingClient> buildArray(String jsonStr) /*-{
		return eval(jsonStr);
	}-*/;

	/**
	 * @param jsonStr
	 * @return
	 */
	public static final native SpendingClient buildObject(String jsonStr) /*-{
		return eval(jsonStr);
	}-*/;

	public static final native void drawChart(String jsonStr) /*-{
		if (@com.appspot.myspent.client.SpendingClient::isSupportsSVG()()) {
			@com.appspot.myspent.client.SpendingClient::drawChartSvg(Ljava/lang/String;)(jsonStr);
		} else {
			@com.appspot.myspent.client.SpendingClient::drawChartImg(Ljava/lang/String;)(jsonStr);
		}
	}-*/;

	/**
	 * @param jsonStr
	 *            $wnd.drawChart(jsonStr);
	 * @return
	 */
	private static final native void drawChartImg(String jsonStr) /*-{
		var chartArr = eval(jsonStr);
		var i = 0;
		var data = '';
		var label = '';
		var len = chartArr.length;
		var leni = len - 1;
		var width = 250;
		var amp = '&';
		for (i = 0; i < len; i++) {
			data += encodeURI(chartArr[i].amount);
			label += encodeURI(i + 1);
			if (i < leni) {
				data += encodeURI(',');
				label += encodeURI('|');
			}
		}
		var host = 'https://chart.googleapis.com/chart?';
		var param = 'chxt=x,y&chds=a&cht=lc&chxl=1:|New|Old|3:|Low$|High$&chxt=x,x,y,y&chxp=1,1|3,1&chg=15,15,1,5';
		var urlimg = host + param + amp + 'chs=' + width + 'x' + width + amp
				+ 'chd=t:' + data + amp + 'chl=' + label;
		$doc.getElementById('img_chart').src = urlimg;
		$doc.getElementById('img_chart').style.display = '';
	}-*/;

	private static final native void drawChartSvg(String jsonStr) /*-{
		var chartData = new $wnd.google.visualization.DataTable();
		var chart = new $wnd.google.visualization.LineChart($doc
				.getElementById('chart_div'));
		var chartArr = eval(jsonStr);
		chartData.addColumn('string', 'day');
		chartData.addColumn('number', 'spending');
		chartData.addRows(chartArr.length);
		var i = 0;
		for (i = 0; i < chartArr.length; i++) {
			var j = 0;
			for (j = 0; j < 2; j++) {
				if (j == 0) {
					chartData.setValue(i, j, chartArr[i].changeDate);
				} else {
					chartData.setValue(i, j, chartArr[i].amount);
				}
			}
		}
		chart.draw(chartData, {
			width : 250,
			height : 250,
			title : 'Spending Chart'
		});
	}-*/;

	private static final native boolean isSupportsSVG() /*-{
		return $wnd.Modernizr.svg;
	}-*/;

	/**
	 * 
	 */
	protected SpendingClient() {

	}

	/**
	 * @return the amount
	 */
	public final native double getAmount() /*-{
		return this.amount;
	}-*/;

	/**
	 * @return the change
	 */
	public final native double getChange() /*-{
		return this.change;
	}-*/;

	/**
	 * @return the changeDate
	 */
	public final native String getChangeDate() /*-{
		return this.changeDate;
	}-*/;

	/**
	 * @return
	 */
	public final double getChangePercent() {
		return (100.0 * this.getChange()) / this.getAmount();
	}

	/**
	 * @return the createDate
	 */
	public final native String getCreateDate() /*-{
		return this.createDate;
	}-*/;

	/**
	 * @return the id
	 */
	public final native int getId() /*-{
		return this.id;
	}-*/;

	/**
	 * @return the purpose
	 */
	public final native String getPurpose() /*-{
		return this.purpose;
	}-*/;

	/**
	 * @return the active
	 */
	public final native boolean isActive() /*-{
		return this.active;
	}-*/;

	/**
	 * @param act
	 *            the active to set
	 */
	public final native void setActive(boolean act) /*-{
		this.active = act;
	}-*/;

	/**
	 * @param amt
	 * @return
	 */
	public final native void setAmount(double amt) /*-{
		this.amount = amount;
	}-*/;

	/**
	 * @param chg
	 * @return
	 */
	public final native void setChange(double chg) /*-{
		this.change = chg;
	}-*/;

	/**
	 * @param changeDate
	 *            the createDate to set
	 */
	public final native void setChangeDate(String date) /*-{
		this.changeDate = date;
	}-*/;

	/**
	 * @param createDate
	 *            the createDate to set
	 */
	public final native void setCreateDate(String date) /*-{
		this.createDate = date;
	}-*/;

	/**
	 * @param idx
	 *            the id to set
	 */
	public final native void setId(int idx) /*-{
		this.id = idx;
	}-*/;

	/**
	 * @param purp
	 * @return
	 */
	public final native void setPurpose(String purp) /*-{
		this.purpose = purp;
	}-*/;
}
