package org.suhodo.sb01.controller;


import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


// json으로 데이터를 반환하는 Rest Controller
@RestController
@Log4j2
public class SampleJSONController {

    @GetMapping("/helloArr")
    public String[] helloArr() {

        log.info("helloArrrrrrrrrrr");

        return new String[]{"AAA", "BBB", "CCC"};
    }
}
