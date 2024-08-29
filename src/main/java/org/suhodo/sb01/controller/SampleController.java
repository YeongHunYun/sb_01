package org.suhodo.sb01.controller;


import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/*Facede 패턴
FrontController - PageController의 이중 구조
FrontController : Tomcat에서 데이터를 우선처리/공통처리하는 부분
                주소와 매핑된 pageController로 전달한다.
                스프링에서는 DipatcherServlet이 담당한다
PageController: FrontController에서 넘겨받은 데이터를 처리
                이곳에서 jsp/thymeleaf로 데이터를 전달
jsp/thymeleaf : Page Controller에서 넘겨받은 데이터를
                html코드 부분에 배치시켜서
                브라우저에 렌더링 될 화면
                내부적으로는 java로 변환된다.
 */

@Controller
@Log4j2

public class SampleController {

    @GetMapping("/hello")
    public void hello(Model model) {
        log.info("hello.........");

        // templates/hello.html로 아래 데이터 전달
        model.addAttribute("msg", "HELLO WORLDDDD");
    }

    @GetMapping("/ex/ex1")
    public void ex1(Model model) {
        List<String> list = Arrays.asList("AAA", "BBB", "CCC", "DDD");

        model.addAttribute("list", list);
    }


    class SampleDTO {
        private String p1, p2, p3;

        public String getP1() {
            return p1;
        }

        public String getP2() {
            return p2;
        }

        public String getP3() {
            return p3;
        }
    }

    @GetMapping("/ex/ex2")
    public void ex2(Model model) {
        log.info("ex/ex2.........");

        List<String> strList = IntStream.range(1, 10)
                .mapToObj(i -> "Data" + i)
                .collect(Collectors.toList());
        model.addAttribute("list", strList);

        Map<String, String> map = new HashMap<>();
        map.put("A", "AAAA");
        map.put("B", "BBB");
        model.addAttribute("map", map);

        SampleDTO dto = new SampleDTO();
        dto.p1 = "Value -- p1";
        dto.p2 = "Value -- p2";
        dto.p3 = "Value -- p3";
        model.addAttribute("dto", dto);
    }

    @GetMapping("/ex/ex3")
    public void ex3(Model model) {
        model.addAttribute("arr", new String[]{"AAA", "BBB", "CCC"});
    }


    @GetMapping("/ex/ex4")
    public void ex4(Model model) {
        model.addAttribute(" iu", "아이유 사진");
    }
}


