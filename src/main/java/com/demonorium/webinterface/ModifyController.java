package com.demonorium.webinterface;

import com.demonorium.database.BookStorage;
import com.demonorium.utils.SessionController;
import com.demonorium.utils.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

@Controller
public class ModifyController {
    @Autowired
    protected SessionController sessionController;

    @Autowired
    protected BookStorage books;

    @GetMapping("/books/edit")
    String exit(@RequestParam boolean isNew, HttpServletRequest request, Model model) throws InterruptedException {
        UserSession session = sessionController.getSession(request);
        if (session == null)
            return "redirect:/";
        if (isNew) {

        }

        return "";
    }

    @GetMapping("/remove/{id}")
    String exit(@PathVariable Long id, HttpServletRequest request, Model model) throws InterruptedException {
        UserSession session = sessionController.getSession(request);
        if ((session == null) || (id == null))
            return "redirect:/";

        books.remove(id);
        return "redirect:/";
    }
}
