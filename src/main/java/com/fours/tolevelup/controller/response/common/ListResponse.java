package com.fours.tolevelup.controller.response.common;


import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ListResponse<T> {
    private int size;
    private List<T> content;

    public static <T> ListResponse<T> of(List<T> content) {
        return new ListResponse<>(
                content.size(),
                content
        );
    }
}
