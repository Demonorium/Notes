package com.demonorium.webinterface;


import com.demonorium.database.BookStorage;
import com.demonorium.webinterface.forms.Root;
import com.demonorium.utils.SessionController;
import com.demonorium.utils.UserSession;
import com.demonorium.webinterface.forms.RegisterForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {
    @Autowired
    protected SessionController sessionController;

    @Autowired
    protected BookStorage books;

    @GetMapping("/")
    String redirect(HttpServletRequest request, Model model) {
        return "redirect:/books";
    }

    @GetMapping("/books")
    String getRequest(HttpServletRequest request, Model model) throws InterruptedException {
        UserSession session = sessionController.getSession(request);
        if (session != null) {
            model.addAttribute("root", new Root(books.getAllByUser(session.getUser()), session.getUser()));
            return "listPage";
        } else {
            model.addAttribute("registerForm", new RegisterForm());
            return "registration";
        }
    }
}
