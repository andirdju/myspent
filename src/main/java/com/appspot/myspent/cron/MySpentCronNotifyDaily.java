/**
 * 
 */
package com.appspot.myspent.cron;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author andi
 * 
 */
public class MySpentCronNotifyDaily extends HttpServlet {
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(final HttpServletRequest req,
			final HttpServletResponse resp) throws ServletException,
			IOException {
		this.doPost(req, resp);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(final HttpServletRequest req,
			final HttpServletResponse resp) throws ServletException,
			IOException {
		final String gaeReqHeader = req.getHeader("X-AppEngine-Cron");
		if (Boolean.parseBoolean(gaeReqHeader)) {
			final Properties props = new Properties();
			final Session session = Session.getDefaultInstance(props, null);
			final String msgBody = "Use MySpent Today, http://myspent.andird.org";
			try {
				final Message msg = new MimeMessage(session);
				msg.setFrom(new InternetAddress(
						"no-reply@myspent-hrd.appspotmail.com", "MySpent"));
				msg.setReplyTo(new Address[] { new InternetAddress(
						"no-reply@myspent-hrd.appspotmail.com", "MySpent") });
				msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
						"no-reply@myspent-hrd.appspotmail.com", "MySpent"));
				msg.addRecipient(Message.RecipientType.BCC,
						new InternetAddress("andird@gmail.com",
								"Andi R Djunaedi"));
				msg.addRecipient(Message.RecipientType.BCC,
						new InternetAddress("endah.w@gmail.com",
								"Endah Wintarsih"));
				msg.setSubject("Use MySpent Today, http://myspent.andird.org");
				msg.setText(msgBody);
				Transport.send(msg);
			} catch (final AddressException e) {
				throw new ServletException(e);
			} catch (final MessagingException e) {
				throw new ServletException(e);
			}
		} else {
			throw new ServletException("Invalid Http Request Header");
		}
	}
}
