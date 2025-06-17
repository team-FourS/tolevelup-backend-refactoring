package com.fours.tolevelup.service.theme;

import com.fours.tolevelup.model.entity.Theme;
import com.fours.tolevelup.repository.theme.ThemeCustomRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ThemeServiceImpl implements ThemeService {

    private final ThemeCustomRepository themeRepository;

    @Autowired
    public ThemeServiceImpl(ThemeCustomRepository themeRepository){
        this.themeRepository = themeRepository;
    }

    @Override
    public List<ThemeDTO> findThemes() {
        List<Theme> themeList = themeRepository.findAll();
        List<ThemeDTO> themeDTOList = new ArrayList<>();
        BeanUtils.copyProperties(themeList,themeDTOList);
        for(Theme theme:themeList){
            ThemeDTO themeDTO = new ThemeDTO();
            BeanUtils.copyProperties(theme,themeDTO);
            themeDTOList.add(themeDTO);
        }
        return themeDTOList;
    }
}
