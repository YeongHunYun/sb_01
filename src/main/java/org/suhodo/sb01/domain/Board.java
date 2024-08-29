package org.suhodo.sb01.domain;

import lombok.*;

import javax.persistence.*;


//실행될 때 JPA에 의해 테이블로 성성된다.
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class Board extends BaseEntity {

    //@Id : primary key
    //GenerationType.IDENTITY : auto_increment 일련번호 자동증가
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;
    @Column(length = 500, nullable = false)
    private String title;
    @Column(length = 2000, nullable = false)
    private String content;
    @Column(length = 50, nullable = false)
    private String writer;

    public void change(String title, String content) {
        this.title = title;
        this.content = content;

    }


}


