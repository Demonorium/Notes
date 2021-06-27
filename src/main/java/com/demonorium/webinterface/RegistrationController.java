package com.demonorium.webinterface;


import com.demonorium.database.User;
import com.demonorium.database.UserRepository;
import com.demonorium.utils.SessionController;
import com.demonorium.webinterface.forms.RegisterForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class RegistrationController {

    @Autowired
    public SessionController sessionController;

    @Autowired
    public UserRepository repository;

    @GetMapping("/exit")
    String exit(HttpServletRequest request, Model model) throws InterruptedException {
        sessionController.closeSession(request);
        return "redirect:/";
    }
    @PostMapping("/registration")
    String registerRequest(@ModelAttribute("registerForm") RegisterForm registerForm, HttpServletRequest request, Model model) throws InterruptedException {
        User user = repository.getByEmail(registerForm.getEmail());

        if (user == null) {
            user = new User(registerForm.getEmail(), registerForm.getPassword());
            repository.save(user);
        } else if (!user.getPassword().equals(registerForm.getPassword())){
            user = null;
        }

        if (user != null) {
            sessionController.createSession(request, user);
            return "redirect:/";
        }

        registerForm.setEmail("");
        registerForm.resetPassword();
        return "registration";
    }
}
