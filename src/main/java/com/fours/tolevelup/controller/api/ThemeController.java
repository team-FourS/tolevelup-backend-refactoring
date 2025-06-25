package com.fours.tolevelup.controller.api;


import com.fours.tolevelup.service.dto.ThemeDTO;
import com.fours.tolevelup.service.ThemeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ThemeController {

    final private ThemeService themeService;

    @GetMapping("/themes")
    public ResponseEntity<List<ThemeDTO>> themeList(){
        return ResponseEntity.ok(themeService.findThemes());
    }

}
