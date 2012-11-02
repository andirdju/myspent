/**
 * 
 */
package org.andird.myspent.server;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import org.andird.myspent.client.MySpentException;
import org.andird.myspent.client.MySpentService;
import org.andird.myspent.shared.FieldVerifier;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author andi
 * 
 */
public class MySpentImpl extends RemoteServiceServlet implements MySpentService {
    private static final PersistenceManagerFactory pmf = JDOHelper
            .getPersistenceManagerFactory("transactions-optional");
    private static final UserService userService = UserServiceFactory.getUserService();
    private static final String comma = ",";
    private static final String openBrace = "[";
    private static final String closeBrace = "]";

    @Override
    public void addSpending(final String purp, final double amt) {
        if ((purp != null) && (purp.length() > 0)) {
            final String userHash = this.getUserHash();
            final Date currDate = new Date();
            final SpendingData spend = new SpendingData(purp, amt, userHash, currDate, true,
                    currDate);
            final PersistenceManager pm = MySpentImpl.pmf.getPersistenceManager();
            try {
                pm.makePersistent(spend);
            } finally {
                pm.close();
            }
        }
    }

    @Override
    public String getAllPrices(final int startPast, final int length) throws MySpentException {
        String res = null;
        final PersistenceManager pm = MySpentImpl.pmf.getPersistenceManager();
        List<SpendingData> qRes = null;
        try {
            final Query q = pm.newQuery(SpendingData.class,
                    "(active == true) && (createDate <= firstDate) && (createDate > secondDate)");
            q.declareParameters("java.util.Date firstDate, java.util.Date secondDate");
            q.setOrdering("id descending");
            q.setOrdering("createDate descending");

            final Date currDate = new Date();
            final GregorianCalendar cl = new GregorianCalendar();
            cl.setLenient(true);
            cl.setTime(currDate);
            cl.add(Calendar.DAY_OF_MONTH, startPast * -1);
            final Date first = cl.getTime();
            cl.setTime(currDate);
            cl.add(Calendar.DAY_OF_MONTH, (startPast + length) * -1);
            final Date second = cl.getTime();

            qRes = (List<SpendingData>) q.execute(first, second);
            if (qRes != null) {
                final Gson gson = new GsonBuilder().create();
                SpendingData spDt = null;
                res = MySpentImpl.openBrace;
                for (final Iterator<SpendingData> iterator = qRes.iterator(); iterator.hasNext();) {
                    spDt = iterator.next();
                    res += spDt.toJson(gson) + MySpentImpl.comma;
                }
                res += MySpentImpl.closeBrace;
            }
        } finally {
            pm.close();
        }
        return res;
    }

    @Override
    public String getLogoutLink() throws MySpentException {
        final User user = this.getUserObj();
        String result = null;
        if (user != null) {
            result = MySpentImpl.userService.createLogoutURL("/");
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andird.myspent.client.MySpentService#getPrices()
     */
    @Override
    public String getPrices(final int startPast, final int length) throws MySpentException {
        List<SpendingData> qRes = null;
        String res = null;
        final PersistenceManager pm = MySpentImpl.pmf.getPersistenceManager();
        try {
            final Query q = pm
                    .newQuery(SpendingData.class,
                            "(user == u) && (active == true) && (createDate <= firstDate) && (createDate > secondDate)");
            q.declareParameters("java.lang.String u, java.util.Date firstDate, java.util.Date secondDate");
            q.setOrdering("id descending");
            q.setOrdering("createDate descending");

            final Date currDate = new Date();
            final GregorianCalendar cl = new GregorianCalendar();
            cl.setLenient(true);
            cl.setTime(currDate);
            cl.add(Calendar.DAY_OF_MONTH, startPast * -1);
            final Date first = cl.getTime();
            cl.setTime(currDate);
            cl.add(Calendar.DAY_OF_MONTH, (startPast + length) * -1);
            final Date second = cl.getTime();

            qRes = (List<SpendingData>) q.execute(this.getUserHash(), first, second);
            if (qRes != null) {
                final Gson gson = new GsonBuilder().create();
                SpendingData spDt = null;
                res = MySpentImpl.openBrace;
                for (final Iterator<SpendingData> iterator = qRes.iterator(); iterator.hasNext();) {
                    spDt = iterator.next();
                    res += spDt.toJson(gson) + MySpentImpl.comma;
                }
                res += MySpentImpl.closeBrace;
            }
        } finally {
            pm.close();
        }
        return res;
    }

    /**
     * @return
     */
    private String getUserHash() {
        final User user = this.getUserObj();
        return FieldVerifier.getHash(user.getNickname().getBytes());
    }

    private User getUserObj() {
        return MySpentImpl.userService.getCurrentUser();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.andird.myspent.client.MySpentService#removeSymbol(java.lang.Long)
     */
    @Override
    public void inactivateSpending(final int id) {
        final PersistenceManager pm = MySpentImpl.pmf.getPersistenceManager();
        try {
            final SpendingData sp = pm.getObjectById(SpendingData.class, Long.valueOf(id));
            sp.setActive(Boolean.FALSE);
            sp.setChangeDate(new Date());
        } finally {
            pm.close();
        }
    }
}
