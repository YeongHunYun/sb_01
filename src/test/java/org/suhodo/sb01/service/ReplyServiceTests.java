package org.suhodo.sb01.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.suhodo.sb01.dto.ReplyDTO;

@SpringBootTest
@Log4j2

public class ReplyServiceTests {

    @Autowired
    private ReplyService replyService;
    @Test
    public void testRegister(){
        ReplyDTO replyDTO = ReplyDTO.builder()
                .replyText("ReplyDTO Text")
                .replyer("replyer")
                .bno(99L)
                .build();

        log.info(replyService.register(replyDTO));
    }
}
