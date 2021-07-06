package com.demonorium.webinterface;

import com.demonorium.database.StorageController;
import com.demonorium.database.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class SearchController {
    @Autowired
    protected StorageController storage;


    @GetMapping("/search")
    String search(@ModelAttribute("searchInput")   String searchInput,
                  @ModelAttribute("searchTarget")  Integer searchTarget,
                  Principal principal, Model model) {
        User user = storage.user.getByUsername(principal.getName());
        model.addAttribute("user", user);

        if ((searchInput != null) && (searchTarget != null)) {


            return "search";
        }


        return "search";
    }

}
