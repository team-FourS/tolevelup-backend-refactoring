package com.fours.tolevelup.service;

import com.fours.tolevelup.model.entity.Theme;
import com.fours.tolevelup.repository.ThemeRepository;
import com.fours.tolevelup.service.dto.ThemeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ThemeService {

    private final ThemeRepository themeRepository;

    @Transactional(readOnly = true)
    public List<ThemeDTO> findThemes() {
        List<Theme> themeList = themeRepository.findAll();
        return themeList.stream().map(ThemeDTO::from).toList();
    }

}
