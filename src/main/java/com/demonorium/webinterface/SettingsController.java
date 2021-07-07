package com.demonorium.webinterface;

import com.demonorium.database.StorageController;
import com.demonorium.database.entity.User;
import com.demonorium.security.AppUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class SettingsController {
    @Autowired
    StorageController storage;

    @Autowired
    AppUserDetailsService userDetailsService;

//    @GetMapping("/settings")
//    String settings(Principal principal, Model model) {
//        User user = storage.user.getByUsername(principal.getName());
//        model.addAttribute("user", user);
//        return "settings";
//    }
//
//    @GetMapping("/settings/edit")
//    String settings(@ModelAttribute("user") User userparam, Principal principal, Model model) {
//        User user = storage.user.getByUsername(principal.getName());
//        user.setAutosave(userparam.getAutosave());
//        storage.user.save(user);
//        model.addAttribute("user", user);
//        return "settings";
//    }

    @GetMapping("/profile")
    String profile(Principal principal, Model model) {
        User user = storage.user.getByUsername(principal.getName());
        model.addAttribute("user", user);

        return "profile";
    }

    @GetMapping("/profile/remove_user")
    ResponseEntity<String> removeProfile(@RequestParam("password") String password, Principal principal, HttpServletRequest req, Model model) throws ServletException {
        if (userDetailsService.removeUser(password, principal.getName())) {
            req.logout();
            return ResponseEntity.ok("correct");
        }
        return ResponseEntity.unprocessableEntity().body("correct");
    }

    @GetMapping("/profile/change_password")
    ResponseEntity<String> changePassword(@RequestParam("password") String password, @RequestParam("new_password") String newPassword, Principal principal, Model model) {
        if (userDetailsService.changePassword(password, newPassword, principal.getName())) {
            return ResponseEntity.ok("correct");
        }
        return ResponseEntity.unprocessableEntity().body("bad password");
    }
}
