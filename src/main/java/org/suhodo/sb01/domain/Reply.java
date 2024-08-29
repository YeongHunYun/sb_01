package org.suhodo.sb01.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "board")
public class Reply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

    /* Reply엔티티는 Board와 관계에서 자식 엔티티이다.
    그러므로 Reply관점에서 보면 'n : 1'관계이다.
    그래서 @ManyToOne이라고 설정한다.

    EAGER : 즉시 테이블로부터 정보를 동기화한다.
            편리하지만 성능이 떨어진다.
    LAZY : 나중에 필요할 때 요청할 테니 일반적인 상황에서는 동기화하지 마라.
            성능때문에 이걸 선택
    * */
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    private String replyText;

    private String replyer;

    public void changeText(String text) {
        this.replyText = text;
    }
}