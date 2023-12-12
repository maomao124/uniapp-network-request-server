package mao.uniappnetworkrequestserver.contrller;

import lombok.extern.slf4j.Slf4j;
import mao.uniappnetworkrequestserver.entity.R;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * TestController
 */

@Slf4j
@RestController
@RequestMapping("/api")
public class TestController
{
    @GetMapping("/test/get")
    public R<String> test1(@RequestParam String key)
    {
        log.info("key:" + key);
        return R.success("hello get");
    }

    @PostMapping("/test/post")
    public R<String> test2(@RequestParam String key)
    {
        log.info("key:" + key);
        return R.success("hello post");
    }

    @PostMapping("/test/post2")
    public R<String> test3(@RequestParam String key)
    {
        log.info("key:" + key);
        return R.fail("hello post");
    }

    @PostMapping("/test/post3")
    public R<String> test4(HttpServletResponse response)
    {
        response.setStatus(403);
        return R.fail("错误");
    }
}
