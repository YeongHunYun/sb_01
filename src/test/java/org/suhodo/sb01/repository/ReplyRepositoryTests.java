package org.suhodo.sb01.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.suhodo.sb01.domain.Board;
import org.suhodo.sb01.domain.Reply;


@SpringBootTest
@Log4j2
public class ReplyRepositoryTests {
    @Autowired
    private ReplyRepository replyRepository;

    @Test
    public void testInsert() {
        Long bno = 201L;

        Board board = Board.builder()
                .bno(bno)
                .build();

        // bno가 100인 게시글의 자식으로 댓글을 추가
        Reply reply = Reply.builder()
                .board(board)
                .replyText("댓글......")
                .replyer("replyer1")
                .build();

        replyRepository.save(reply);
    }

    /* @Transactional을 넣지 않으면
    Reply정보를 요청하고 Session이 끊어진다.
    다시 Reply내부의 필드인 Board를 요청하려고 할 때
    Session이 끊어졌으므로 요청을 할 수 없다.

    그래서 @Transactional을 넣어주면 메서드 종료될 때까지
    Session이 유지된다.
    * */
    @Transactional
    @Test
    public void testBoardReplies() {
        Long bno = 101L;

        Pageable pageable = PageRequest.of(0, 10, Sort.by("rno").descending());

        Page<Reply> result = replyRepository.listOfBoard(bno, pageable);

        result.getContent().forEach(reply -> {
            log.info(reply);            // reply.ToString();
        });
    }
}














