package com.demonorium.webinterface;


import com.demonorium.utils.SessionController;
import com.demonorium.utils.UserSession;
import com.demonorium.webinterface.forms.RegisterForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.TreeMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;

@Controller
public class IndexController {
    @Autowired
    public SessionController sessionController;

    @GetMapping("/")
    String getRequest(HttpServletRequest request, Model model) throws InterruptedException{
        System.out.println("request");
        UserSession session = sessionController.getSession(request);
        if (session != null) {
            model.addAttribute("");
            return "noteList";
        } else {
            model.addAttribute("registerForm", new RegisterForm());
            return "registration";
        }
    }


    @PostMapping("/")
    String postRequest(Model model) {
        return "hel";
    }
}
