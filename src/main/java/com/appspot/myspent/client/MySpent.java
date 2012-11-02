package com.appspot.myspent.client;

import java.util.Date;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class MySpent implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	private static final NumberFormat priceFormat = NumberFormat
			.getFormat("#,###.##");

	private final VerticalPanel mainPanel = new VerticalPanel();
	private final FlexTable mySpentTable = new FlexTable();
	private final VerticalPanel inputTable = new VerticalPanel();
	private final Hidden startPast = new Hidden();
	private final TextBox lengthPast = new TextBox();
	private final HorizontalPanel topPanel = new HorizontalPanel();
	private final TextArea purposeText = new TextArea();
	private final TextBox amountText = new TextBox();
	private final Button addBtn = new Button("Add");
	private final Button refreshBtn = new Button("Refresh");
	private final Label timeLabel = new Label();
	private final Label errMsg = new Label();
	private final Anchor srcLink = new Anchor();
	private final Anchor logoutLink = new Anchor();
	private MySpentServiceAsync mySpentPriveSvc = GWT
			.create(MySpentService.class);

	private void addSpending() {
		this.addSpending(this.purposeText.getValue(),
				this.amountText.getValue());
		this.purposeText.setFocus(true);
		this.purposeText.setValue(null);
		this.amountText.setValue(null);
	}

	private void addSpending(final String purpose, final String amount) {
		if ((purpose != null) && (amount != null)) {
			try {
				// add to server
				final double amountVal = Double.parseDouble(amount);

				// initialize service
				if (this.mySpentPriveSvc == null) {
					this.mySpentPriveSvc = GWT.create(MySpentService.class);
				}

				// setup the call back
				final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

					@Override
					public void onFailure(final Throwable caught) {
						MySpent.this.errMsg.setText(caught.getMessage());
						MySpent.this.errMsg.setVisible(true);
					}

					@Override
					public void onSuccess(final Void result) {
						MySpent.this.refreshSpendingList(
								MySpent.this.startPast.getValue(),
								MySpent.this.lengthPast.getValue());
					}
				};

				// call the service
				this.mySpentPriveSvc.addSpending(purpose, amountVal, callback);
			} catch (final Throwable e) {
				// do nothing
			}
		}
	}

	/**
	 * This is the entry point method.
	 */
	@Override
	public void onModuleLoad() {
		// setup top table
		this.setupTopPanel();

		// setup input table
		this.setupInputTable();

		// setup myspent table
		this.setupMySpentTable();

		// error msg
		this.errMsg.setStyleName("errorMsg");
		this.errMsg.setVisible(false);

		// setup focus on symbol input
		this.purposeText.setFocus(true);
		this.purposeText.setValue(null);

		// setup main panel
		this.mainPanel.add(this.topPanel);
		this.mainPanel.add(this.inputTable);
		this.mainPanel.add(this.timeLabel);
		this.mainPanel.add(this.errMsg);
		this.mainPanel.add(this.mySpentTable);
		this.mainPanel.setVisible(false);

		// setup main panel in html
		RootPanel.get("spendingList").add(this.mainPanel);
	}

	private void refreshSpendingList(final String pastFirst,
			final String pastSecond) {
		try {
			// initialize service
			if (this.mySpentPriveSvc == null) {
				this.mySpentPriveSvc = GWT.create(MySpentService.class);
			}

			// setup async callback
			final AsyncCallback<String> callback = new AsyncCallback<String>() {
				@Override
				public void onFailure(final Throwable caught) {
					MySpent.this.errMsg.setText(caught.getMessage());
					MySpent.this.errMsg.setVisible(true);
				}

				@Override
				public void onSuccess(final String result) {
					MySpent.this.updateTable(result);
				}
			};

			// call the service
			this.mySpentPriveSvc.getPrices(Integer.parseInt(pastFirst),
					Integer.parseInt(pastSecond), callback);
		} catch (final Throwable e) {
			// do nothing
		}
	}

	private void removeSpending(final int id) {
		// initialize service
		if (this.mySpentPriveSvc == null) {
			this.mySpentPriveSvc = GWT.create(MySpentService.class);
		}

		// setup the call back
		final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

			@Override
			public void onFailure(final Throwable caught) {
				MySpent.this.errMsg.setText(caught.getMessage());
				MySpent.this.errMsg.setVisible(true);
			}

			@Override
			public void onSuccess(final Void result) {
				MySpent.this.refreshSpendingList(
						MySpent.this.startPast.getValue(),
						MySpent.this.lengthPast.getValue());
			}
		};

		// call the service
		this.mySpentPriveSvc.inactivateSpending(id, callback);
	}

	private void setupInputTable() {
		HorizontalPanel horPanel = null;

		// purpose
		horPanel = new HorizontalPanel();
		horPanel.setSpacing(3);
		horPanel.add(new Label("Purpose"));
		horPanel.add(this.purposeText);
		this.inputTable.add(horPanel);

		// amount
		horPanel = new HorizontalPanel();
		horPanel.setSpacing(3);
		horPanel.add(new Label("Amount"));
		horPanel.add(this.amountText);
		horPanel.add(this.addBtn);
		this.inputTable.add(horPanel);

		// length and start
		horPanel = new HorizontalPanel();
		horPanel.setSpacing(3);
		horPanel.add(new Label("Duration"));
		horPanel.add(this.startPast);
		horPanel.add(this.lengthPast);
		horPanel.add(this.refreshBtn);
		this.inputTable.add(horPanel);

		// set input
		this.startPast.setValue("0");
		this.lengthPast.setTitle("How many days ago?");
		this.lengthPast.setMaxLength(5);
		this.lengthPast.setWidth("5em");
		this.lengthPast.setValue("2");

		// set input contraints
		this.amountText.setMaxLength(12);
		this.amountText.setWidth("5em");

		// set input contraints
		this.purposeText.setVisibleLines(2);
		this.amountText.setWidth("5em");

		// add add btn click handler
		this.addBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				MySpent.this.addSpending();
			}
		});

		// add enter key handler
		this.amountText.addKeyPressHandler(new KeyPressHandler() {
			@Override
			public void onKeyPress(final KeyPressEvent event) {
				if (event.getCharCode() == KeyCodes.KEY_ENTER) {
					MySpent.this.addSpending();
				}
			}
		});

		// add refresh btn click handler
		this.refreshBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				MySpent.this.refreshSpendingList(
						MySpent.this.startPast.getValue(),
						MySpent.this.lengthPast.getValue());
				MySpent.this.purposeText.setValue(null);
			}
		});
	}

	private void setupLogoutLink() {
		// initialize service
		if (this.mySpentPriveSvc == null) {
			this.mySpentPriveSvc = GWT.create(MySpentService.class);
		}

		// setup async callback
		final AsyncCallback<String> callback = new AsyncCallback<String>() {
			@Override
			public void onFailure(final Throwable caught) {
				MySpent.this.errMsg.setText(caught.getMessage());
				MySpent.this.errMsg.setVisible(true);
			}

			@Override
			public void onSuccess(final String result) {
				MySpent.this.logoutLink.setHref(result);
				MySpent.this.logoutLink.setText("logout");
				MySpent.this.mainPanel.setVisible(true);
			}
		};

		// call the service
		this.mySpentPriveSvc.getLogoutLink(callback);
	}

	private void setupMySpentTable() {
		// setup myspent table
		this.mySpentTable.setText(0, 0, "Purpose");
		this.mySpentTable.setText(0, 1, "Amount");
		this.mySpentTable.setText(0, 2, "Date");
		this.mySpentTable.setText(0, 3, "Del");

		// Add styles to elements in the myspent list table.
		this.mySpentTable.getRowFormatter().addStyleName(0,
				"spendingListHeader");
		this.mySpentTable.addStyleName("spendingListList");
	}

	private void setupTopPanel() {
		// setup logout link
		this.setupLogoutLink();

		// add file link
		this.srcLink.setHref("http://code.google.com/p/myspent-appspot-com");

		// setup anchor
		this.srcLink.setText("src");

		// add link
		this.topPanel.add(this.srcLink);
		this.topPanel.add(this.logoutLink);
		this.topPanel.setSpacing(5);
	}

	private void updateTable(final SpendingClient spend, final int row) {
		// add remove button
		final Button remBtn = new Button("x");

		// Populate the Price and Change fields with new data.
		this.mySpentTable.setText(row, 0, spend.getPurpose());
		this.mySpentTable.setText(row, 1,
				MySpent.priceFormat.format(spend.getAmount()));
		this.mySpentTable.setText(row, 2, spend.getCreateDate());
		this.mySpentTable.setWidget(row, 3, remBtn);

		// css
		this.mySpentTable.getCellFormatter().addStyleName(row, 1,
				"spendingListNumericColumn");
		this.mySpentTable.getCellFormatter().addStyleName(row, 3,
				"spendingListRemoveColumn");
		remBtn.addStyleDependentName("remove");

		remBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				try {
					// remove spending from server
					MySpent.this.removeSpending(spend.getId());
					MySpent.this.purposeText.setValue(null);
				} catch (final Throwable e) {
					// do nothing
				}
			}
		});
	}

	private void updateTable(final String prices) {
		// setup graph
		SpendingClient.drawChart(prices);

		// destroy table
		this.mySpentTable.setVisible(false);
		this.mySpentTable.removeAllRows();

		// build table
		this.setupMySpentTable();
		double sumVal = 0.0d;
		final JsArray<SpendingClient> arr = SpendingClient.buildArray(prices);
		for (int i = 0; i < arr.length(); i++) {
			final SpendingClient sp = arr.get(i);
			sumVal += sp.getAmount();
			this.updateTable(sp, i + 1);
		}

		// clear err msg
		this.errMsg.setVisible(false);

		// set last update
		this.timeLabel.setText(DateTimeFormat.getMediumDateTimeFormat().format(
				new Date())
				+ " -- Sum: " + MySpent.priceFormat.format(sumVal));
		this.mySpentTable.setVisible(true);
	}

}
