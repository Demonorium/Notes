package com.demonorium.utils;

import com.demonorium.database.User;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.TreeMap;
import java.util.concurrent.Semaphore;

@Component
public class SessionController {
    private Semaphore semaphore = new Semaphore(1);
    private TreeMap<String, UserSession> sessions = new TreeMap<>();

    public UserSession getSession(String httpSessionID) throws InterruptedException{
        semaphore.acquire();
        UserSession session = null;
        if (sessions.containsKey(httpSessionID)) {
            session = sessions.get(httpSessionID);
        }
        semaphore.release();

        return session;
    }

    public UserSession createSession(String httpSessionID, User user) throws InterruptedException {
        semaphore.acquire();
        UserSession session;
        if (sessions.containsKey(httpSessionID)) {
            session = sessions.get(httpSessionID);
        } else {
            session = new UserSession(user);
        }
        sessions.put(httpSessionID, session);
        semaphore.release();
        return session;
    }

    public void closeSession(String httpSessionID) throws InterruptedException {
        semaphore.acquire();
        sessions.remove(httpSessionID);
        semaphore.release();
    }

    private String getSessionID(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return session.getId();
    }

    public UserSession getSession(HttpServletRequest request) throws InterruptedException {
        return getSession(getSessionID(request));
    }

    public UserSession createSession(HttpServletRequest request, User user) throws InterruptedException {
        return createSession(getSessionID(request), user);
    }

    public void closeSession(HttpServletRequest request) throws InterruptedException {
        closeSession(getSessionID(request));
    }
}
