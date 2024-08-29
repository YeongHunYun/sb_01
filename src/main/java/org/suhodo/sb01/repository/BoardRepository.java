package org.suhodo.sb01.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.suhodo.sb01.domain.Board;
import org.suhodo.sb01.repository.search.BoardSearch;

/* JpaRepository<Board, Long>를 상속받은 interface를 선언하면
자동으로 jpa에 의해 Bean으로 생성되어 Spring의 관리를 받게 된다.
Board : 어떤 Entity를 crud할래
Long : @Id, pk의 자료형
* */
public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch {

    // 1) 기본 CRUD는 제공된다.

    // 2) 쿼리메서드
    // https://spring.io/projects/spring-data-jpa
    // 명명 규칙에 따라 메서드를 선언만 해도 자동으로 SQL이 생성된다.
    Page<Board> findByTitleContainingOrderByBnoDesc(String keyword, Pageable pageable);

    // 3) NativeQuery(특정 DBMS의 sql)
    //  특정 DBMS 전용이 되므로 권장하지 않는다.
    // MariaDB/MySQL 현재 시간
    @Query(value = "select now()", nativeQuery = true)
    String getTime();

    // 4) JPQL
    //    JPA에서 정한 표준 SQL
    //    JPA -> Hibernate -> NativeQuery로 변환 -> 특정 DBMS
    @Query("select b from Board b where b.title like concat('%', :keyword, '%')")
    Page<Board> findKeyword(String keyword, Pageable pageable);

    // 5) QueryDsl
    //   조인등 복잡한 연산을 할 때 메서드 호출방식으로 구성하게 하는 것
    //   프로그래밍(QueryDsl) -> JPQL -> NativeQuery -> DBMS에 전달
}