package com.demonorium.webinterface;

import com.demonorium.database.Book;
import com.demonorium.database.BookStorage;
import com.demonorium.utils.SessionController;
import com.demonorium.utils.UserSession;
import com.demonorium.webinterface.forms.EditForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ModifyController {
    @Autowired
    protected SessionController sessionController;

    @Autowired
    protected BookStorage books;

    @GetMapping("/books/edit/{id}")
    String edit(@PathVariable Long id, HttpServletRequest request, Model model) throws InterruptedException {
        UserSession session = sessionController.getSession(request);
        if ((session == null) || (id == null))
            return "redirect:/";

        EditForm form = null;
        if (id < 0) {
            form = new EditForm();
        } else {
            Book book = books.getBookById(id);
            if (book == null) {
                form = new EditForm();
            } else
                form = new EditForm(book);
        }
        model.addAttribute("editForm", form);
        return "edit";
    }

    @PostMapping("/books/edit")
    String edit(@ModelAttribute("editForm") EditForm editForm, HttpServletRequest request, Model model) throws InterruptedException {
        UserSession session = sessionController.getSession(request);
        if ((session == null) || (editForm == null))
            return "redirect:/";

        if (editForm.getId() == null)
            books.save(new Book(editForm.getName(), editForm.getAuthor(), editForm.getYear(), session.getUser()));
        else {
            Book book = books.getBookById(editForm.getId());
            if (book != null) {
                book.setAuthor(editForm.getAuthor());
                book.setName(editForm.getName());
                book.setYear(editForm.getYear());
            }
        }
        return "redirect:/";
    }

    @GetMapping("/remove/{id}")
    String exit(@PathVariable Long id, HttpServletRequest request, Model model) throws InterruptedException {
        UserSession session = sessionController.getSession(request);
        if ((session == null) || (id == null))
            return "redirect:/";

        books.delete(books.getBookById(id));
        return "redirect:/";
    }
}
