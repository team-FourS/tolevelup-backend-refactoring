package com.fours.tolevelup.controller.api;


import com.fours.tolevelup.model.ThemeExpDTO;
import com.fours.tolevelup.service.themeexp.ThemeExpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class ThemeExpController {

    private final ThemeExpService themeExpService;
    @Autowired
    public ThemeExpController(ThemeExpService themeExpService){
        this.themeExpService = themeExpService;
    }
/*
    @GetMapping("/themeexp/{id}")
    public ResponseEntity<List<ThemeExpDTO.ThemeExp>> userExp(@PathVariable String id){
        return ResponseEntity.ok(themeExpService.findUserThemeExps(id));
    }
*/
}
