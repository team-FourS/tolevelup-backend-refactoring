package com.fours.tolevelup.controller.response.common;


import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Slice;

@Getter
@AllArgsConstructor
public class SliceResponse<T> {
    private boolean hasNext;
    private int sliceNumber;
    private int numberOfElements;
    private List<T> content;

    public static <T> SliceResponse<T> of(Slice<T> slice){
        return new SliceResponse<>(
                slice.hasNext(),
                slice.getNumber(),
                slice.getNumberOfElements(),
                slice.getContent()
        );
    }
}
