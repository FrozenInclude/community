package com.bcsd.community.controller.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class DocumentViewController {

    @GetMapping("/document")
    public RedirectView redirectToDocument() {
        return new RedirectView("/document/document");
    }
    @GetMapping("/document/")
    public RedirectView redirectToDocument2() {
        return new RedirectView("document");
    }

    @GetMapping
    public RedirectView redirectToDocument3() {
        return new RedirectView("/login");
    }
    

}