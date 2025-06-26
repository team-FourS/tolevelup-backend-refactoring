package com.fours.tolevelup.controller.api;



import com.fours.tolevelup.controller.response.common.Response;
import com.fours.tolevelup.controller.response.UserThemeMissionResponse;
import com.fours.tolevelup.service.UserMissionService;
import com.fours.tolevelup.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/missions")
public class UserMissionController {

    private final UserMissionService userMissionService;
    private final UserService userService;


    @GetMapping()
    public Response<List<UserThemeMissionResponse>> getTodayMissionsByTheme(
            Authentication authentication,
            @RequestParam(required = true) int themeId
    ) {
        return Response.success(
                userMissionService.getTodayMissionWithTheme(authentication.getName(), themeId)
        );
    }

    @PatchMapping("/{logId}")
    public Response<String> changeMissionStatus(
            Authentication authentication,
            @PathVariable Long logId
    ) {
        userMissionService.changeStatus(authentication.getName(), logId);
        userService.userLevelUp(authentication.getName());
        return Response.success("미션 상태 변경 성공");
    }


}
