package com.spring.boot.smartcontact.controller.contact;

import com.spring.boot.smartcontact.model.Contact;
import com.spring.boot.smartcontact.model.User;
import com.spring.boot.smartcontact.service.ContactService;
import com.spring.boot.smartcontact.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class CreateController {

    private final UserService userService;

    public CreateController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute
    public User addCommonAttribute(Principal principal) {
        return userService.getUserByUserName(principal.getName());
    }

    @GetMapping("/add-contact")
    public String addContact(Model model) {
        User user = (User) model.getAttribute("user");
        if(!user.isEnabled())
            return "redirect:/logout";

        model.addAttribute("contact", new Contact());
        model.addAttribute("title", "Add Contact");
        model.addAttribute("showSidebar", true);
        return "normal/add_contact_form";
    }

    @PostMapping("/process-contact")
    public String processContact(@ModelAttribute("contact") Contact contact,
                                 @RequestParam("profileImage") MultipartFile file,
                                 Principal principal, Model model) {
        User user = (User) model.getAttribute("user");
        if(!user.isEnabled())
            return "redirect:/logout";

        model.addAttribute("message",  "Successfully added!!");
        model.addAttribute("type", "success");
        model.addAttribute("showSidebar", true);
        userService.loadImageAndSave(contact, file, principal.getName());

        return "normal/add_contact_form";
    }
}
