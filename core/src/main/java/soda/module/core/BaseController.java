package soda.module.core;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Describe
 * @Author orang
 * @Create 2019/4/21 16:57
 **/
//@RestController
public class BaseController {
    @PostMapping("/index")
    public String index(){
        return "hahahahah";
    }
}
