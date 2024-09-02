package org.suhodo.sb01.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.suhodo.sb01.domain.Board;
import org.suhodo.sb01.dto.BoardListAllDTO;
import org.suhodo.sb01.dto.BoardListReplyCountDTO;

public interface BoardSearch {
    Page<Board> search1(Pageable pageable);

    Page<Board> searchAll(String[] types, String keyword, Pageable pageable);

    Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types, String keyword, Pageable pageable);

//    Page<BoardListReplyCountDTO> serachWithAll(String[] types, String keyword, Pageable pageable);

    Page<BoardListAllDTO> serachWithAll(String[] types, String keyword, Pageable pageable);

}

