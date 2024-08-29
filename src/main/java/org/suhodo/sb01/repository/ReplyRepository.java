package org.suhodo.sb01.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.suhodo.sb01.domain.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    // 특정 bno의 게시글의 댓글들은 페이지 처리르 할 수 있도록 하는 메서드
    @Query("select r from Reply r where r.board.bno = :bno")
    Page<Reply> listOfBoard(Long bno, Pageable pageable);
}