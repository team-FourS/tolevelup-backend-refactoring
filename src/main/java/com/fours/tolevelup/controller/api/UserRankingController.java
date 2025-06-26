package com.fours.tolevelup.controller.api;


import com.fours.tolevelup.controller.response.common.ListResponse;
import com.fours.tolevelup.controller.response.common.Response;
import com.fours.tolevelup.controller.response.common.SliceResponse;
import com.fours.tolevelup.service.ranking.RankingService;
import com.fours.tolevelup.service.ranking.dto.UserRankingDto;
import com.fours.tolevelup.service.ranking.dto.UserThemeRankingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/rankings")
@RequiredArgsConstructor
public class UserRankingController {

    private final RankingService rankingService;

    @GetMapping("/current")
    public Response<ListResponse<UserRankingDto>> getCurrentRanking(
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "10") int range
    ) {
        return Response.success(ListResponse.of(
                rankingService.getCurrentRankingList(start, range)
        ));
    }

    @GetMapping("/current/themes")
    public Response<ListResponse<UserThemeRankingDto>> getThemeCurrentRanking(
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "10") int range,
            @RequestParam int themeId
    ) {
        return Response.success(ListResponse.of(
                rankingService.getThemeCurrentRankingList(start, range, themeId)
        ));
    }

    @GetMapping("/history")
    public Response<SliceResponse<UserRankingDto>> getRankingList(
            @RequestParam int year,
            @RequestParam int month,
            Pageable pageable
    ) {
        return Response.success(SliceResponse.of(
                rankingService.getRankingList(year, month, pageable)
        ));
    }

    @GetMapping("/history/themes")
    public Response<SliceResponse<UserThemeRankingDto>> getThemeRankingList(
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam int themeId,
            Pageable pageable
    ) {
        return Response.success(SliceResponse.of(
                rankingService.getThemeRankingList(year, month, themeId, pageable)
        ));
    }

}


