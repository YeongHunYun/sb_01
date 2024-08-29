package org.suhodo.sb01.dto;

/*
Controller에서 View로 Pagination하기 위해서 전송하는 클래스
 */

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class PageResponseDTO<E> {
    private int page;   // 몇 페이지
    private int size;       // 한개 페이지의 크기
    private int total;          // 전체 row 개수

    private int start;          //현재 페이지에서 page 시작 숫자
    private int end;            //현재 페이지에서 page 시작 숫자

    private boolean prev;       //앞에 이동할 부분이 있는지
    private boolean next;      //뒤에 이동할 부분이 있는지

    private List<E> dtoList;    //이 페이지에 보여줄 row 리스트

    @Builder(builderMethodName = "withAll")
    public PageResponseDTO(PageRequestDTO pageRequestDTO, List<E> dtoList, int total) {
        if (total <= 0) return;

        this.page = pageRequestDTO.getPage();
        this.size = pageRequestDTO.getSize();

        this.total = total;
        this.dtoList = dtoList;

        this.end = (int) (Math.ceil(this.page / 10.0)) * 10;
        this.start = this.end - 9;

        int last = (int) (Math.ceil(total / (double) size));
        this.end = end > last ? last : end;

        this.prev = this.start > 1;

        this.next = total > this.end * this.size;
    }
}
