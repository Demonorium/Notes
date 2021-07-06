package com.demonorium.webinterface;


import com.demonorium.webinterface.view.RegisterView;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class IndexController {
    @GetMapping("/")
    String redirect(Principal principal, Model model) {
        if (principal == null)
            return "redirect:/home-promo";
        else
            return "redirect:/home";
    }

    @GetMapping("/home-promo")
    String getRequest(Principal principal, Model model) throws InterruptedException {
        if (principal != null)
            return "redirect:/home";
        model.addAttribute("form", new RegisterView());
        model.addAttribute("isHomeFlag");
        return "home-promo";
    }


}
