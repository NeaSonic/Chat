package com.kekw.chatapi.security;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/auth")
@AllArgsConstructor
public class AuthController {

    @GetMapping
    public String getAuth(){
        return "auth successful";
    }
}
