package org.suhodo.sb01.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/* @MappedSuperclass : Entity클래스의 부모 클래스로 상속하기 위한 클래스이다.
                        여러 Entity클래스가 공통으로 가져야 할 기능을 만들어놓는다.
   @EntityListeners(value = {AuditingEntityListener.class})
      : 엔티티가 DB에 추가되거나 변경될 때 이벤트 수신한다는 의미
        main함수 소속 클래스에 @EnableJpaAuditing을 설정해야 수신할 수 있다.
   @Getter : 각 필드들의 getter 메서드를 자동으로 만들어줘라.
* */
@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
@Getter
abstract class BaseEntity {
    // 처음 행의 데이터가 생성될 때 시간을 저장한다.
    // 이후에는 변경되지 않는다.
    @CreatedDate
    @Column(name = "regdate", updatable = false)
    private LocalDateTime regDate;

    // 데이터가 변경될 때 마다 시간을 기록한다.
    @LastModifiedDate
    @Column(name = "moddate")
    private LocalDateTime modDate;
}