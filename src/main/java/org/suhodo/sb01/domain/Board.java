package org.suhodo.sb01.domain;

import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


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

    
    /*
    @OneToMany에 mappedBy설정을 하지 않으면
    BoardImage와 사이에 중간 테이블을 만든다
    아래처럼 설정해야 1:n 관계로 양방향 연결이 된다

    cascade설정
    부모가 변경/삭제될 때 자식도 변경/삭제되는 설정
    
    @BatchSize(size=20)
    20개씩 모아서 처리하겠다
     */
    @OneToMany(mappedBy = "board",
                                    cascade = {CascadeType.ALL},
                                    fetch = FetchType.LAZY,
                                    orphanRemoval= true)
    @Builder.Default
    @BatchSize(size=20)
    private Set<BoardImage> imageSet = new HashSet<>();


    public void addImage(String uuid, String fileName){
        BoardImage boardImage = BoardImage.builder()
                .uuid(uuid)
                .fileName(fileName)
                .board(this)
                .ord(imageSet.size()    )
                .build();
        imageSet.add(boardImage);
    }

    public void clearImages(){
        imageSet.forEach(boardImage -> boardImage.changeBoard(null));
        this.imageSet.clear();
    }

    public void change(String title, String content){
        this.title = title;
        this.content = content;
    }



}
