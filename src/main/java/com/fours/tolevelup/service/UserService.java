package com.fours.tolevelup.service;

import com.fours.tolevelup.controller.request.UserUpdateRequest;
import com.fours.tolevelup.controller.response.UserResponse;
import com.fours.tolevelup.exception.ErrorCode;
import com.fours.tolevelup.exception.TluApplicationException;
import com.fours.tolevelup.model.AlarmDTO;
import com.fours.tolevelup.model.ThemeExpDTO;
import com.fours.tolevelup.model.UserDTO;
import com.fours.tolevelup.model.entity.Alarm;
import com.fours.tolevelup.model.entity.Character;
import com.fours.tolevelup.model.entity.Theme;
import com.fours.tolevelup.model.entity.ThemeExp;
import com.fours.tolevelup.model.entity.User;
import com.fours.tolevelup.model.entity.UserCharacter;
import com.fours.tolevelup.repository.AlarmRepository;
import com.fours.tolevelup.repository.CommentRepository;
import com.fours.tolevelup.repository.FollowRepository;
import com.fours.tolevelup.repository.LikeRepository;
import com.fours.tolevelup.repository.character.CharacterRepository;
import com.fours.tolevelup.repository.character.UserCharacterRepository;
import com.fours.tolevelup.repository.MissionLogRepository;
import com.fours.tolevelup.repository.theme.ThemeRepository;
import com.fours.tolevelup.repository.ThemeExpRepository;
import com.fours.tolevelup.repository.UserRepository;
import com.fours.tolevelup.util.JwtTokenUtils;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final AlarmRepository alarmRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final ThemeExpRepository themeExpRepository;
    private final ThemeRepository themeRepository;
    private final MissionLogService missionLogService;
    private final MissionLogRepository missionLogRepository;
    private final UserCharacterRepository userCharacterRepository;
    private final CharacterRepository characterRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.secret-key}")
    private String secretKey;
    @Value("${jwt.token.expired-time-ms}")
    private long expiredTimeMs;


    public UserDTO loadUserVoByUserId(String id) {
        return userRepository.findById(id).map(UserDTO::fromEntity).orElseThrow(() ->
                new TluApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", id)));
    }


    @Transactional
    public void userJoin(String id, String password, String name, String email) {
        if (id == null || password == null || name == null || email == null) {
            throw new TluApplicationException(ErrorCode.DATA_NOT_ENTERED);
        }
        userRepository.findById(id).ifPresent(it -> {
            throw new TluApplicationException(ErrorCode.DUPLICATED_USER_ID, String.format("%s is duplicated", id));
        });
        userRepository.findByEmail(email).ifPresent(it -> {
            throw new TluApplicationException(ErrorCode.DUPLICATED_USER_EMAIL,
                    String.format("%s is duplicated", email));
        });
        userRepository.save(
                User.builder().id(id).password(encoder.encode(password)).name(name).email(email).build());
        List<Theme> themeList = themeRepository.findAll();
        User user = getUserOrException(id);
        for (Theme theme : themeList) {
            themeExpRepository.save(
                    ThemeExp.builder().id(user.getId() + theme.getName()).user(user).theme(theme).build());
        }
        List<Character> characterList = characterRepository.findByLevel();
        for (Character character : characterList) {
            userCharacterRepository.save(
                    UserCharacter.builder()
                            .id(user.getId() + character.getId())
                            .user(user)
                            .character(character)
                            .build()
            );
        }
        //missionLogService.createMissionLog(user);
    }

    @Transactional
    public void changeUserInformation(
            String userId, UserUpdateRequest request
    ) {
        User user = getUserOrException(userId);
        user.update(
                (request.getPassword() == null) ? null : encoder.encode(request.getPassword()),
                request.getName(),
                (request.getEmail() == null) ? null : getVerifiedEmail(request.getEmail()),
                request.getIntro()
        );
    }

    public String login(String userId, String password) {
        UserDTO user = loadUserVoByUserId(userId);
        passwordMatchCheck(user.getPassword(), password);
        return JwtTokenUtils.generateToken(userId, secretKey, expiredTimeMs);
    }

    @Transactional
    public void delete(String userId) {
        // TODO: case 설정해서 자동으로 삭제되도록 할까요
        User user = getUserOrException(userId);
        themeExpRepository.deleteAllByUser(user);
        missionLogRepository.deleteAllByUser(user);
        userCharacterRepository.deleteAllByUser(user);
        commentRepository.deleteAllByUser(user);
        followRepository.deleteAllByUser(user);
        likeRepository.deleteAllByUser(user);
        alarmRepository.deleteAllByUser(user);
        userRepository.delete(user);
    }

    public boolean passwordCheck(String userId, String password) {
        UserDTO user = loadUserVoByUserId(userId);
        passwordMatchCheck(user.getPassword(), password);
        return true;
    }

    public UserResponse.UserData findUserPrivateData(String userId) {
        UserDTO user = loadUserVoByUserId(userId);
        //passwordMatchCheck(user.getPassword(),password);
        return UserResponse.UserData.fromUserDTO(user);
    }

    private String getVerifiedEmail(String email) {
        userRepository.findByEmail(email).ifPresent(it -> {
            throw new TluApplicationException(ErrorCode.DUPLICATED_USER_EMAIL,
                    "같은 이메일이 이미 존재합니다.");
        });
        return email;
    }


    public UserResponse.UserData findUserData(String userId) {
        UserDTO user = loadUserVoByUserId(userId);
        return UserResponse.UserData.fromUserDTO(user);
    }


    public UserResponse.UserAllData findUserAllData(String userId) {
        return UserResponse.UserAllData.builder().
                userData(findUserData(userId)).expData(userAllThemeExp(userId)).build();
    }


    public List<UserResponse.UserExpData> userAllThemeExp(String userId) {
        List<Theme> themeList = themeRepository.findAll();
        List<UserResponse.UserExpData> userExpList = new ArrayList<>();
        for (Theme t : themeList) {
            userExpList.add(UserResponse.UserExpData.fromExpDTO(userThemeExp(userId, t.getId())));
        }
        return userExpList;
    }


    public ThemeExpDTO userThemeExp(String userId, int themeId) {
        ThemeExp themeExp = themeExpRepository.getThemeExpByUserAndTheme(userId, themeId).orElseThrow(() ->
                new TluApplicationException(ErrorCode.THEME_EXP_NOT_FOUND));
        return ThemeExpDTO.fromEntity(themeExp);
    }

    public long totalReceivedLikes(String userId) {
        User user = getUserOrException(userId);
        return likeRepository.countAllByToUser(user);
    }

    public Slice<AlarmDTO> findUserAlarmList(String id, Pageable pageable) {
        return alarmRepository.findAllByToUser(id, pageable).map(AlarmDTO::fromEntity);
    }

    @Transactional
    public void deleteAllAlarm(String userId) {
        User user = getUserOrException(userId);
        alarmRepository.deleteByUser(user);
    }

    @Transactional
    public void deleteAlarm(String id, Long alarmId) {
        Alarm alarm = alarmRepository.findById(alarmId).orElseThrow(() ->
                new TluApplicationException(ErrorCode.ALARM_NOT_FOUND));
        if (!alarm.getToUser().getId().equals(id)) {
            throw new TluApplicationException(ErrorCode.INVALID_PERMISSION);
        }
        alarmRepository.delete(alarm);
    }

    private void passwordMatchCheck(String userPassword, String checkPassword) {
        if (!encoder.matches(checkPassword, userPassword)) {
            throw new TluApplicationException(ErrorCode.INVALID_PASSWORD);
        }
    }

    private User getUserOrException(String id) {
        return userRepository.findById(id).orElseThrow(() ->
                new TluApplicationException(ErrorCode.USER_NOT_FOUND,
                        String.format("%s is duplicated and c check", id)));
    }


    public void userLevelUp(String id) {
        int total = themeExpRepository.expTotal(id);
        if (total >= 40) {
            userRepository.levelUp(id);
        } else if (total >= 30) {
            userRepository.levelUp(id);
        } else if (total >= 20) {
            userRepository.levelUp(id);
        } else if (total >= 10) {
            userRepository.levelUp(id);
        }
    }


}