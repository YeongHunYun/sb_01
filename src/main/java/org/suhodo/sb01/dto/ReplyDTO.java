package org.suhodo.sb01.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyDTO {
    private Long rno;                           // 댓글 번호(자식)

    @NotNull
    private Long bno;                           // 게시판 글 번호(부모)

    @NotEmpty
    private String replyText;                   // 댓글

    @NotEmpty
    private String replyer;                     // 댓글 작성자

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime regDate;  // 처음 등록 시간

    // json 전송시 제외
    @JsonIgnore
    private LocalDateTime modDate;   //   수정 시간
}