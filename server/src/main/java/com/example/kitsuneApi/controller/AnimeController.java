package com.example.kitsuneApi.controller;

import lombok.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${properties.api.anime}")
public class AnimeController {

    @GetMapping("/status")
    public String getStatus() {
        return "Kitsune Anime API is online";
    }
}
