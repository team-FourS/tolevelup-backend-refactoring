package com.fours.tolevelup.controller.api;


import com.fours.tolevelup.controller.response.RankResponse;
import com.fours.tolevelup.controller.response.RankResponseBefore;
import com.fours.tolevelup.controller.response.RankResponseBefore.RankList;
import com.fours.tolevelup.controller.response.Response;
import com.fours.tolevelup.controller.response.ThemeRankResponse;
import com.fours.tolevelup.service.ranking.RankService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserRankingController {

    private final RankService rankService;

    @GetMapping("/api/v1/ranking")
    public Response<RankResponse> getRankingList(
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "10") int size
    ) {
        return Response.success(RankResponse.of(
                rankService.getRankingList(year, month, start, size)
        ));
    }

    @GetMapping("/api/v1/ranking/theme")
    public Response<ThemeRankResponse> getThemeRankingList(
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam int themeId,
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "10") int size
    ) {
        return Response.success(ThemeRankResponse.of(
                rankService.getThemeRankingList(year, month, themeId, start, size)
        ));
    }
    
    @GetMapping("/rank")
    public Response<RankList> monthExpTotal(
            Authentication authentication,
            @RequestParam("year") String year,
            @RequestParam("month") String month,
            Pageable pageable
    ) {
        return Response.success(new RankResponseBefore.RankList(
                rankService.getRankTotalList(authentication.getName(), String.format("%s-%s", year, month), pageable)));
    }

    @GetMapping("/rank/{theme}")
    public Response<RankResponseBefore.ThemeRankList> themeExpTotal(
            Authentication authentication,
            @PathVariable("theme") int themeId,
            @RequestParam("year") String year,
            @RequestParam("month") String month,
            Pageable pageable
    ) {
        return Response.success(new RankResponseBefore.ThemeRankList(
                rankService.getThemeRankList(authentication.getName(), themeId, String.format("%s-%s", year, month),
                        pageable)));
    }

}







