package org.suhodo.sb01.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude ="board")
public class BoardImage implements Comparable<BoardImage>{

    @Id
    private String uuid;                //uuid 값

    private String fileName;        //파일명

    private int ord;                         //순번

    @ManyToOne
    private Board board;            //부모테이블


    //순번에 맞게 정렬하기 위해
    @Override
    public int compareTo(BoardImage other) {
        return this.ord - other.ord;
    }

    // Board객체를 나중에 지정할 수 있게 하는데
    // Board엔티티 삭제 시에 BoardImage객체 참조도 변경하기 위해 사용
    public void changeBoard(Board board) {
        this.board = board;
        
    }
}
