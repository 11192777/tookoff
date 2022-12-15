package pers.qingyu.tookoff.controller;

import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import run.HelloWorldTransformCore;
import sample.HelloWorld;

import java.lang.reflect.Method;

/**
 * <H1></H1>
 *
 * @author Qingyu.Meng
 * @version 1.0
 * @date 2022/12/15 17:45
 */
@RestController
public class Controller {



    @SneakyThrows
    @GetMapping(value = "/api/v1/get")
    public void get() {
        HelloWorldTransformCore helloWorldTransformCore = new HelloWorldTransformCore();

        Class<?> clazz = Class.forName("sample.HelloWorld");
        Method m = clazz.getDeclaredMethod("test");
        Object instance = clazz.newInstance();
        m.invoke(instance);

        System.out.println("----------------");

        HelloWorld helloWorld = new HelloWorld();
        helloWorld.test();
        helloWorld.test1();
    }
}
