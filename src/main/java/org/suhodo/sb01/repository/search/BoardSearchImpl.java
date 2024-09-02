package org.suhodo.sb01.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.suhodo.sb01.domain.Board;
import org.suhodo.sb01.domain.QBoard;
import org.suhodo.sb01.domain.QReply;
import org.suhodo.sb01.dto.BoardImageDTO;
import org.suhodo.sb01.dto.BoardListAllDTO;
import org.suhodo.sb01.dto.BoardListReplyCountDTO;

import java.util.List;
import java.util.stream.Collectors;

/* BoardSearch인터페이스를 구현하는 클래스로
반드시 '인터페이스명' + Impl이어야 한다.

QuerydslRepositorySupport는 JpaRepository를 상속받은
repository와 연결해주는 역할을 한다.

[QueryDsl을 사용하기 위해 해야하는 일]
0)전단계
 Gradle에서 clean -> compileJava
 build폴더에 QBoard가 생성되었는지 확인
1) 인터페이스 선언
2) 인터페이스 자식 클래스 선언 (반드시 '인터페이스명' + Impl)
3) 인터페이스 자식 클래스는 extends QuerydslRepositorySupport 해야 한다.
4) 인터페이스 자식 클래스의 생성자를 통해 어떤 Entity클래스를 사용할 것인지 적용
5) QueryDsl 구현 메서드가 호출될 Repository에  인터페이스를 상속시켜야 한다.
* */

@Log4j2
public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch {

    public BoardSearchImpl() {
        super(Board.class);
    }

    @Override
    public Page<Board> search1(Pageable pageable) {
        QBoard board = QBoard.board;

        JPQLQuery<Board> query = from(board);       // select.. from board

        query.where(board.title.contains("1"));     // where title like ...

        this.getQuerydsl().applyPagination(pageable, query); // ORDER BY bno DESC limit 1, 10;

        List<Board> list = query.fetch();

        long count = query.fetchCount();

        log.info(list);
        log.info("count=" + count);

        return null;
    }

    @Override
    public Page<Board> searchAll(String[] types, String keyword, Pageable pageable) {
        QBoard board = QBoard.board;
        JPQLQuery<Board> query = from(board);       // select... from board

        if ((types != null) && (types.length > 0) && keyword != null) {
            // and나 or로 여러 개 조건을 만들 때 사용
            BooleanBuilder booleanBuilder = new BooleanBuilder();

            /*
            title LIKE '%1%'
            OR content LIKE '%1%'
            OR writer LIKE '%1%'
            * */

            for (String type : types) {
                switch (type) {
                    case "t":
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;
                }
            }
            /*
            WHERE (
                    title LIKE '%1%'
                    OR content LIKE '%1%'
                    OR writer LIKE '%1%'
            )
            * */
            query.where(booleanBuilder);            // WHERE (...)
        }
        query.where(board.bno.gt(0L));          // AND bno > 0L

        this.getQuerydsl().applyPagination(pageable, query);    // ORDER BY bno DESC limit 1, 10;

        /*
        SELECT *
         FROM board
         WHERE (
                title LIKE '%1%'
                OR content LIKE '%1%'
                OR writer LIKE '%1%'
         )
         AND bno > 0L
         ORDER BY bno DESC limit 1, 10;
        * */
        List<Board> list = query.fetch();


        /*
        SELECT COUNT(bno)
         FROM board
         WHERE (
                title LIKE '%1%'
                OR content LIKE '%1%'
                OR writer LIKE '%1%'
         )
         AND bno > 0L
         ORDER BY bno DESC limit 1, 10;
        * */
        long count = query.fetchCount();

        /*
         * JPA는 클라이언트에 Pagination기능을 제공하기 위해 Page<E>를 상속받은 PageImpl<E>를 만들어놨다.
         * PageImpl(실제 목록 데이터, 페이지 관련 정보 객체, 전체 개수);
         * */
        return new PageImpl<Board>(list, pageable, count);
    }

    @Override
    public Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types, String keyword, Pageable pageable) {
        // QueryDsl -> JPQL로 변환할 때 사용
        QBoard board = QBoard.board;
        QReply reply = QReply.reply;

        // 댓글이 없는 게시글(board)를 모두 가져와야 한다.
        // 부모가 없는 댓글은 가져올 필요가 없다.
        // 그러므로 board LEFT JOIN reply는
        // 기준을 board에 맞춰서
        // board는 자식 reply가 존재하지 않아도 모두 가져온다는 뜻
        JPQLQuery<Board> query = from(board);               // select... from board
        query.leftJoin(reply).on(reply.board.eq(board));    // LEFT JOIN reply ON reply.board_bno = board.bno

        query.groupBy(board);                               // GROUP BY board

        /*
            WHERE (
            board.title LIKE '%:keyword%'
            OR
            board.content LIKE '%:keyword%'
            OR
            board.writer LIKE '%:keyword%'
            )
        * */
        if ((types != null) && (types.length > 0) && keyword != null) {
            BooleanBuilder booleanBuilder = new BooleanBuilder();       // (
            for (String type : types) {
                switch (type) {
                    case "t":
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;
                }
            }
            query.where(booleanBuilder);        // )
        }

        query.where(board.bno.gt(0L));      // AND board.bno > 0

        // SELECT bno, title, writer, regDate, COUNT(reply) replyCount
        JPQLQuery<BoardListReplyCountDTO> dtoQuery = query.select(
                Projections.bean(BoardListReplyCountDTO.class,
                        board.bno,
                        board.title,
                        board.writer,
                        board.regDate,
                        reply.count().as("replyCount"))
        );

        // paging 처리를 dtoQuery에 적용해줌(페이징 처리 sql문을 추가해줌)
        this.getQuerydsl().applyPagination(pageable, dtoQuery);

        List<BoardListReplyCountDTO> dtoList = dtoQuery.fetch();        // 쿼리 실행

        long count = dtoQuery.fetchCount();                             // 현재 결과 전체 갯수

        return new PageImpl<>(dtoList, pageable, count);
    }

    @Override
    public Page<BoardListAllDTO> searchWithAll(String[] types, String keyword, Pageable pageable) {
        QBoard board = QBoard.board;
        QReply reply = QReply.reply;

        /* FROM board
           LEFT JOIN reply ON reply.bno=board.bno
           board(게시글)는 종속된 reply가 없어도 모두 가져온다.
        * */

        JPQLQuery<Board> boardJPQLQuery = from(board);
        boardJPQLQuery.leftJoin(reply).on(reply.board.eq(board));

        // title/writer/content에 keyword가 포함되어 있는지 검색 기능 추가
        if ((types != null && types.length > 0) && keyword != null) {

            // or/and 연산을 조합하기 위해 사용
            BooleanBuilder booleanBuilder = new BooleanBuilder();

            for (String type : types) {
                switch (type) {
                    case "t":
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;

                }
            }
            boardJPQLQuery.where(booleanBuilder);
        }
        /*
        WHERE (
            board.title LIKE '%:keyword%'
            OR
            board.content LIKE '%:keyword%'
            OR
            board.writer LIKE '%:keyword%'
        )
        * */

        // GROUP BY board.bno
        boardJPQLQuery.groupBy(board);

        // ORDER BY limit :skipRow, :getRows
        getQuerydsl().applyPagination(pageable, boardJPQLQuery);

        // SELECT board.bno, ..., COUNT(reply)
        JPQLQuery<Tuple> tupleJPQLQuery = boardJPQLQuery.select(board, reply.countDistinct());

        /*
        SELECT board.bno, ..., COUNT(reply)
         FROM board
         LEFT JOIN reply ON board.bno=reply.bno
         WHERE (
            board.title LIKE '%:keyword%'
            OR
            board.content LIKE '%:keyword%'
            OR
            board.writer LIKE '%:keyword%'
         )
         GROUP BY board.bno, ...
         ORDER BY limit :skipRow, :getRows;
        * */

        // sql문 실행
        List<Tuple> tupleList = tupleJPQLQuery.fetch();

        List<BoardListAllDTO> dtoList = tupleList.stream().map(tuple -> {

            Board board1 = (Board) tuple.get(board);             // Tuple의 인덱스 0요소(1번째 요소, 생략됨) 꺼내기
            long replyCount = tuple.get(1, Long.class);       // Tuple의 인덱스 1요소(2번째 요소) 꺼내기

            BoardListAllDTO dto = BoardListAllDTO.builder()
                    .bno(board1.getBno())
                    .title(board1.getTitle())
                    .writer(board1.getWriter())
                    .regDate(board1.getRegDate())
                    .replyCount(replyCount)
                    .build();

            // BoardImage를 조회해서 dto에 추가
            List<BoardImageDTO> imageDTOS = board1.getImageSet().stream().sorted()
                    .map(boardImage -> BoardImageDTO.builder()
                            .uuid(boardImage.getUuid())
                            .fileName(boardImage.getFileName())
                            .ord(boardImage.getOrd())
                            .build()
                    ).collect(Collectors.toList());

            dto.setBoardImages(imageDTOS);

            return dto;

        }).collect(Collectors.toList());

        long totalCount = boardJPQLQuery.fetchCount();

        return new PageImpl<>(dtoList, pageable, totalCount);
    }


//    @Override
//    public Page<BoardListReplyCountDTO> searchWithAll(String[] types, String keyword, Pageable pageable) {
//
//        QBoard board = QBoard.board;
//        QReply reply = QReply.reply;
//
//        JPQLQuery<Board> boardJPQLQuery = from(board);
//        boardJPQLQuery.leftJoin(reply).on(reply.board.eq(board));
//
//        getQuerydsl().applyPagination(pageable, boardJPQLQuery);
//
//        List<Board> boardList = boardJPQLQuery.fetch();
//
//        /* Board 게시물마다 연결된 자식 목록을 가져오므로
//        데이터베이스에 많은 요청이 들어가게 된다.
//        이것을 'N+1'문제라고 한다.
//        성능 저하 문제
//
//        이것을 해결하는 문제는 @BatchSize를 정해주는 것이다.
//        * */
//        boardList.forEach(board1 -> {
//            System.out.println(board1.getBno());
//            System.out.println(board1.getImageSet());
//            System.out.println("------------------------------");
//        });
//
//        return null;
//    }
}













