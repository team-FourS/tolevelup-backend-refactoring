package com.fours.tolevelup.service.themeexp;

import com.fours.tolevelup.model.ThemeExpDTO;
import com.fours.tolevelup.model.entity.Mission;
import com.fours.tolevelup.model.entity.ThemeExp;
import com.fours.tolevelup.repository.theme.ThemeRepositoryImpl;
import com.fours.tolevelup.repository.themeexp.ThemeExpRepository;
import com.fours.tolevelup.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ThemeExpServiceImpl implements ThemeExpService {

    private final ThemeExpRepository themeExpRepository;
    private final ThemeRepositoryImpl themeRepository;
    private final UserRepository userRepository;
/*
    @Override
    public List<ThemeExpDTO.ThemeExp> findUserThemeExps(String id) {
        List<ThemeExpDTO.ThemeExp> userThemeExps = new ArrayList<>();
        List<ThemeExp> userExpList = themeExpRepository.findByUser_id(id); //유저아이디로 찾기
        for(ThemeExp themeExp:userExpList){
            userThemeExps.add(
                    ThemeExpDTO.ThemeExp.builder()
                    .theme(themeExp.getTheme().getName())
                    .exp_total(themeExp.getExp_total())
                    .build()
            );
        }
        return userThemeExps;
    }
*/
    /*
    @Override
    public void plusUserThemeExp(String user_id, Mission mission) {
        themeExpRepository.updateExpPlus(mission.getExp(), user_id,mission.getTheme().getId());
    }

    @Override
    public void minusUserThemeExp(String user_id, Mission mission) {
        themeExpRepository.updateExpMinus(mission.getExp(), user_id, mission.getTheme().getId());
    }
    */

    /* UserService 에 위치
    @Override
    @Transactional
    public void saveUserThemeExps(String id) {
        List<Theme> themeList = themeRepository.findAll();
        User user = userRepository.findById(id).get();
        for(Theme theme : themeList){
            themeExpRepository.save1(
                    ThemeExp.builder()
                            .id(user.getId()+theme.getId())
                            .user(user)
                            .theme(theme)
                            .build()
            );
        }
    }
    */
}