package org.dreamw4lker.gwn.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class HelpController {
    @GetMapping(path = {"", "/", "/help"})
    public String help() {
        return "help";
    }
}
