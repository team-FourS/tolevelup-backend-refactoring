package com.fours.tolevelup.controller.api;


import com.fours.tolevelup.service.theme.ThemeDTO;
import com.fours.tolevelup.service.theme.ThemeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ThemeController {

    final private ThemeServiceImpl themeService;
    @Autowired
    public ThemeController(ThemeServiceImpl themeService){
        this.themeService = themeService;
    }

    @GetMapping("/themes")
    public ResponseEntity<List<ThemeDTO>> themeList(){
        return ResponseEntity.ok(themeService.findThemes());
    }



}
