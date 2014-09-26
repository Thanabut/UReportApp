package sit.kmutt.com.ureportapp.sessioninterface;

import sit.kmutt.com.ureportapp.vo.SessionManager;
import sit.kmutt.com.ureportapp.vo.UserSession;

/**
 * Created by Thanabut on 25/9/2557.
 */
public interface SessionListener{
    public SessionManager getIpSession();
    public UserSession getUserSession();
}
