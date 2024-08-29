package org.suhodo.sb01;

import lombok.Cleanup;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootTest
@Log4j2 //Data 소스 커넥션
public class DataSourceTests {

    @Autowired
    private DataSource dataSource;

    @Test
    public void testDataSource() throws SQLException {
        // @Cleanup은 메서드가 종료될 때 자동으로
        // connection.close()를 호출해준다.
        @Cleanup
        Connection connection = dataSource.getConnection();

        log.info(connection);

        Assertions.assertNotNull(connection);


    }
}