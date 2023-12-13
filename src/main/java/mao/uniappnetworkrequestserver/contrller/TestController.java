package mao.uniappnetworkrequestserver.contrller;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import mao.uniappnetworkrequestserver.entity.R;
import mao.uniappnetworkrequestserver.handler.WebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

/**
 * TestController
 */

@Slf4j
@RestController
@RequestMapping("/api")
public class TestController
{
    @Autowired
    private WebSocketHandler webSocketHandler;

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

    @PostMapping("/test/upload")
    public R<String> test5(MultipartFile file)
    {
        String originalFilename = file.getOriginalFilename();
        log.info(originalFilename);
        return R.success(originalFilename);
    }

    @SneakyThrows
    @GetMapping("/test/download")
    public void test6(HttpServletResponse response)
    {
        log.info("下载文件");
        response.getOutputStream().write("1".repeat(10000000).getBytes(StandardCharsets.UTF_8));
    }

    @PostMapping("/websocket/sendAll")
    public R<Boolean> test7(@RequestParam String message)
    {
        webSocketHandler.sendAllMessage(message);
        return R.success(null);
    }
}
