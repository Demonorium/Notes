package com.demonorium.webinterface;

import com.demonorium.database.StorageController;
import com.demonorium.database.entity.User;
import com.demonorium.security.AppUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
public class SettingsController {
    @Autowired
    StorageController storage;

    @Autowired
    AppUserDetailsService userDetailsService;

    @GetMapping("/settings")
    String settings(Principal principal, Model model) {
        User user = storage.user.getByUsername(principal.getName());
        model.addAttribute("user", user);

        return "settings";
    }

    @GetMapping("/profile")
    String profile(Principal principal, Model model) {
        User user = storage.user.getByUsername(principal.getName());
        model.addAttribute("user", user);

        return "profile";
    }
    @PostMapping("/profile")
    String profileErr(Principal principal, Model model) {
        User user = storage.user.getByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("pe", true);
        return "profile";
    }


    @PostMapping("/profile/remove_user")
    ModelAndView removeProfile(@RequestParam("password") String password, Principal principal, Model model) {
        if (userDetailsService.removeUser(password, principal.getName())) {
            return new ModelAndView("redirect:/logout");
        }

        return new ModelAndView("redirect:/profile");
    }
}
