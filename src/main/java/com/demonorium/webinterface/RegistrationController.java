package com.demonorium.webinterface;


import com.demonorium.database.entity.User;
import com.demonorium.database.repository.UserRepository;
import com.demonorium.security.AppUserDetailsService;
import com.demonorium.webinterface.view.RegisterView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class RegistrationController {
    @Autowired
    public AppUserDetailsService users;

    @GetMapping("/registration")
    String registerRequest(Model model) {
        model.addAttribute("form", new RegisterView());
        return "registration";
    }

    @PostMapping("/registration")
    String registerRequest(@ModelAttribute("form") RegisterView form,  Model model) {
        final String returnTarget = model.containsAttribute("isHomeFlag")? "home-promo" : "registration";

        if (!form.getPassword().equals(form.getPasswordConf())) {
            model.addAttribute("eConf", true);
            return returnTarget;
        }
        if (!users.newUser(form.getUsername(), form.getEmail(), form.getPassword())){
            model.addAttribute("eName", true);
            return returnTarget;
        }

        return "redirect:/login";
    }
}
