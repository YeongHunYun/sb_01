package org.suhodo.sb01.service;

import org.suhodo.sb01.domain.Board;
import org.suhodo.sb01.dto.*;

import java.util.List;
import java.util.stream.Collectors;

public interface BoardService {
    Long register(BoardDTO boardDTO);
    BoardDTO readOne(Long bno);
    void modify(BoardDTO boardDTO);
    void remove(Long bno);
    PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);
    PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO);
    PageResponseDTO<BoardListAllDTO> listWithAll(PageRequestDTO pageRequestDTO);

    /* 객체지향의 원칙은 위배하지만, 기본 인터페이스를 사용하는 구현 클래스를 일일이 고칠 필요없다는 편의성때문에
    추가된 문법

    인터페이스에도 구현 메서드를 추가할 수 있게 허용함.

    인터페이스에 새로운 기능의 메서드를 추가하고 싶을 때
    만약에 인터페이스를 기존 문법대로 추상 메서드로 구현하게 되면, 모든 자식 구현 클래스는 일일이 메서드를 구현해야 한다.

    그런데 새로 추가하는 메서드의 기능이 모든 자식 구현 클래스에 공통 메서드라면 일일이 구현할 필요가 없다.
    그래서 default 키워드를 추가해서 메서드를 구현하면 인터페이스도 구현 메서드를 가질 수 있다.

    default 메서드는 반드시 자식 구현 객체에서만 사용할 수 있다.
    * */
    /* 기존에 Board <-> BoardDTO를 변환할 때는 ModelMapper객체를 사용하였다.
    하지만 새로 추가한 List<String> fileNames와 Set<BoardImage> imageSet은 ModelMapper로
    변환이 어렵고 복잡하다.
    그래서 DTO를 Entity로 변환시켜주는 계층인 BoardService계층에서
    dotToEntity를 사용해서 변환하도록 한다.
    * */
    default Board dtoToEntity(BoardDTO boardDTO) {
        Board board = Board.builder()
                .bno(boardDTO.getBno())
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .writer(boardDTO.getWriter())
                .build();

        if(boardDTO.getFileNames() != null){
            boardDTO.getFileNames().forEach(fileName ->{
                String[] arr = fileName.split("_");
                board.addImage(arr[0], arr[1]); // UUID와 originalFileName으로 분리
            });
        }

        return board;
    }

    default BoardDTO entityToDto(Board board) {
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getWriter())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .build();

        List<String> fileNames = board.getImageSet().stream().sorted().map(
                boardImage -> {
                    return boardImage.getUuid() + "_" + boardImage.getFileName();
                }
        ).collect(Collectors.toList());

        boardDTO.setFileNames(fileNames);

        return boardDTO;
    }
}