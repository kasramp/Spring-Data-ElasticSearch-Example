package com.madadipouya.elasticsearch.springdata.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class Redirect {

    @GetMapping(value = "/")
    public ModelAndView redirectToDocPage() {
        return new ModelAndView("redirect:/swagger-ui/index.html");
    }

    @GetMapping(value = "/apidocs")
    public ModelAndView redirectToApiPage() {
        return new ModelAndView("redirect:/swagger-ui/index.html");
    }
}
