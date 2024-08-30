package org.suhodo.sb01.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.suhodo.sb01.domain.Board;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class BoardRepositoryTests {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Test
    public void testInsert() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Board board = Board.builder()
                    .title("title..." + i)
                    .content("content..." + i)
                    .writer("user" + (i % 10))
                    .build();

            Board result = boardRepository.save(board);
            log.info("BNO: " + result.getBno());
        });
    }

    @Test
    public void testSelect() {
        Long bno = 100L;

        Optional<Board> result = boardRepository.findById(bno);

        // 데이터 객체가 존재하면 정상 반환, 아니면 예외처리
        Board board = result.orElseThrow();

        log.info(board);
    }

    @Test
    public void testUpdate() {
        Long bno = 100L;

        Optional<Board> result = boardRepository.findById(bno);

        Board board = result.orElseThrow();

        board.change("update..title 100", "update content 100");

        /* board 객체의 값이 새로운 값이면 insert 문 호출
           board 객체의 값이 갱신이면 update 문 호출
        * */
        boardRepository.save(board);
    }

    @Test
    public void testDelete() {
        Long bno = 1L;

        boardRepository.deleteById(bno);
    }

    @Test
    public void testPaging() {
        // 0페이지, 10개씩, bno번호 큰 순서대로를 담고 있다.
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        Page<Board> result = boardRepository.findAll(pageable);

        log.info("total count: " + result.getTotalElements());
        log.info("total pages: " + result.getTotalPages());
        log.info("page number: " + result.getNumber());
        log.info("page size: " + result.getSize());

        List<Board> todoList = result.getContent();

        todoList.forEach(board -> log.info(board));
    }

    @Test
    public void testQueryMethod() {
        Pageable pageable = PageRequest.of(3, 5, Sort.by("bno").descending());

        Page<Board> result = boardRepository.findByTitleContainingOrderByBnoDesc("title", pageable);

        log.info("total count: " + result.getTotalElements());
        log.info("total pages: " + result.getTotalPages());
        log.info("page number: " + result.getNumber());
        log.info("page size: " + result.getSize());

        List<Board> todoList = result.getContent();

        todoList.forEach(board -> log.info(board));
    }

    @Test
    public void testTime() {
        String time = boardRepository.getTime();

        log.info(time);
    }

    @Test
    public void testSearch1() {
        Pageable pageable = PageRequest.of(1, 10, Sort.by("bno").descending());

        boardRepository.search1(pageable);
    }

    @Test
    public void testSearchAll2() {
        String[] types = {"t", "c", "w"};
        String keyword = "1";
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);

        log.info(result.getTotalPages());
        log.info(result.getSize());
        log.info(result.getNumber());
        log.info(result.hasPrevious() + " : " + result.hasNext());

        result.getContent().forEach(board -> log.info(board));
    }

    @Test
    public  void testInsertWithImages() {
        Board board = Board.builder()
                .title("Image Test")
                .content("첨부파일 테스트")
                .writer("tester")
                .build();

        for(int i=0; i<3; i++){
            board.addImage(UUID.randomUUID().toString(), "file" + i  + ".jpg");

            boardRepository.save(board);
        }
    }


    @Test
    @Transactional //이걸 해줘야 여러 작업들을 하나의 작업처럼 사용
    @Commit //커밋
    public void testReadWithImages() {
        Optional<Board> result = boardRepository.findById(317L);

        Board board = result.orElseThrow();

        log.info(board);
        log.info("--------------");
        log.info(board.getImageSet());
    }


    @Test
    public void testModifyImages() {
        Optional<Board> result = boardRepository.findByIdWithImages(317L);

        Board board = result.orElseThrow();

        board.clearImages();

        //
        for(int i=0; i<2; i++){
            board.addImage(UUID.randomUUID().toString(), "updatefile" + i  + ".jpg");

            boardRepository.save(board);
        }

    }

    @Test
    @Transactional
    @Commit
    public void testRemoveAll() {
        Long bno = 315L;

                replyRepository.deleteByBoard_Bno(bno);
                boardRepository.deleteById(bno);

    }

    @Test
    public void testInsertAll(){
        for(int i=0;i<=100;i++){
            Board board = Board.builder()
                    .title("Title.."+i)
                    .content("Content..."+i)
                    .writer("writer..."+i)
                    .build();

            for(int j=0;j<3;j++){
                if(i%5 ==0)
                    continue;

                board.addImage(UUID.randomUUID().toString(), i+ "file" + j  + ".jpg");
            }
            boardRepository.save(board);
        }
    }

    @Transactional
    @Test
    public void testSearchImageReplyCount() {
        Pageable pageable = PageRequest.of(1, 10, Sort.by("bno").descending());
        boardRepository.serachWithAll(null, null, pageable);
    }

}