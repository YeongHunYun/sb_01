package org.suhodo.sb01.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.suhodo.sb01.dto.BoardDTO;
import org.suhodo.sb01.dto.BoardListAllDTO;
import org.suhodo.sb01.dto.PageRequestDTO;
import org.suhodo.sb01.dto.PageResponseDTO;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@Log4j2
public class BoardServiceTests {

    @Autowired
    private BoardService boardService;

    @Test
    public void testRegister() {
        log.info(boardService.getClass().getName());

        BoardDTO boardDTO = BoardDTO.builder()
                .title("Sample Title...")

                .content("Sample Content...")
                .writer("user00")
                .build();

        Long bno = boardService.register(boardDTO);

        log.info(bno);
    }

    @Test
    public void testReadOne() {
        Long bno = 101L;

        BoardDTO boardDTO = boardService.readOne(bno);

        log.info(boardDTO);
    }

//    @Test
//    public void testModify() {
//        BoardDTO boardDTO = BoardDTO.builder()
//                .bno(101L)
//                .title("Updated... 101")
//                .content("Updated Content 101...")
//                .build();
//        boardService.modify(boardDTO);
//    }

    @Test
    public void testDelete() {
        boardService.remove(1L);
    }

    @Test
    public void testList() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .type("tcw")
                .keyword("1")
                .page(1)
                .size(10)
                .build();

        PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);

        log.info(responseDTO);
    }

    @Test
    public void testRegisterWithImages(){
        log.info(boardService.getClass().getName());

        BoardDTO boardDTO = BoardDTO.builder()
                .title("File...Sample Title")
                .content("Sample Content...")
                .writer("user00")
                .build();

        boardDTO.setFileNames(
                Arrays.asList(
                        UUID.randomUUID() + "_aaa.jpg",
                        UUID.randomUUID() + "_bbb.jpg",
                        UUID.randomUUID() + "_ccc.jpg"
                )
        );
        Long bno = boardService.register(boardDTO);
        log.info("bno : " + bno);
    }

    @Test
    public void testReadAll(){
        Long bno = 203L;
        BoardDTO boardDTO = boardService.readOne(bno);
        log.info(boardDTO);
        for (String fileName : boardDTO.getFileNames()) {
            log.info(fileName);
        }
    }

    @Test
    public void testModify(){
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(203L)
                .title("update 203")
                .content("Updated Content 203...")
                .build();

        boardDTO.setFileNames(Arrays.asList(UUID.randomUUID() + "_zzz.jpg"));
        boardService.modify(boardDTO);
    }

    @Test
    public void testListWithAll() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .build();

        PageResponseDTO<BoardListAllDTO> responseDTO = boardService.listWithAll(pageRequestDTO);

        List<BoardListAllDTO> dtoList = responseDTO.getDtoList();

        dtoList.forEach(boardListAllDTO -> {
            log.info(boardListAllDTO.getBno() + " : " + boardListAllDTO.getTitle());

            if
        })
    }


}
