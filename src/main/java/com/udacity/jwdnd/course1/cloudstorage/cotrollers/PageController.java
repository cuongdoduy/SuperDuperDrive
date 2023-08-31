package com.udacity.jwdnd.course1.cloudstorage.cotrollers;

import com.udacity.jwdnd.course1.cloudstorage.models.File;
import com.udacity.jwdnd.course1.cloudstorage.models.ResponseObject;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.services.AuthorizationService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PageController {
    private final AuthorizationService authorizationService;
    @Autowired
    private FileService fileService;
    @Autowired
    private UserService userService;
    @Autowired
    private NoteService noteService;

    public PageController(
            AuthorizationService authorizationService
    ) {

        this.authorizationService = authorizationService;
    }
    @GetMapping("/home")
    public String getHomePage(Model model,Authentication authentication)
    {
        String userName = (String) authentication.getPrincipal();
        User user = userService.getUser(userName);
        model.addAttribute("files", fileService.getAllFiles(user.getUserId()));
        model.addAttribute("notes", noteService.getAllNotes(user.getUserId()));
        return "home";
    }
    @GetMapping("/login")
    public String getLoginPage()
    {
        return "login";
    }
    @GetMapping("/signup")
    public String getSignupPage()
    {
        return "signup";
    }
    @GetMapping("/result")
    public String getResultPage(
            Model model,
            @RequestParam(value = "isSuccess",required = false) Boolean isSuccess,
            @RequestParam(value = "message",required = false) String message
            )
    {
        model.addAttribute("isSuccess",isSuccess);
        model.addAttribute("message",message);
        return "result";
    }

    @PostMapping("/register")
    public String postSignupPage(User newUser)
    {
        return authorizationService.signUp(newUser)? "redirect:signup?success" : "redirect:signup?error";
    }
}
