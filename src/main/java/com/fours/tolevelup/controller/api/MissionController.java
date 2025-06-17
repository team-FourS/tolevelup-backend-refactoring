package com.fours.tolevelup.controller.api;


import com.fours.tolevelup.controller.response.MissionResponse;
import com.fours.tolevelup.controller.response.Response;
import com.fours.tolevelup.service.character.CharacterService;
import com.fours.tolevelup.service.mission.MissionServiceImpl;
import com.fours.tolevelup.service.user.UserServiceImpl;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/missions")
public class MissionController {

    private final MissionServiceImpl missionService;

    private final UserServiceImpl userService;

    private final CharacterService characterService;

    @Autowired
    public MissionController(MissionServiceImpl missionService, UserServiceImpl userService,
                             CharacterService characterService) {
        this.missionService = missionService;
        this.userService = userService;
        this.characterService = characterService;
    }


    @GetMapping("/themes/{themeId}")
    public Response<List<MissionResponse.ThemeMissions>> themeMissions(Authentication authentication,
                                                                       @PathVariable("themeId") int themeId) {
        return Response.success(missionService.todayThemeMissions(authentication.getName(), themeId).stream().
                map(MissionResponse.ThemeMissions::fromMissionDTO).collect(Collectors.toList()));
    }


    @PutMapping("/{missionId}")
    public Response<String> missionCheck(Authentication authentication, @PathVariable int missionId) {
        missionService.changeMissionStatus(authentication.getName(), missionId);
        userService.userLevelUp(authentication.getName());
        //characterService.levelUpUserCharacter(authentication.getName());
        return Response.success("미션 상태 변경");
    }


}
