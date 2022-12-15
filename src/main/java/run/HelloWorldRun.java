package run;

import java.lang.reflect.Method;

public class HelloWorldRun {


    public static void main(String[] args) throws Exception {
        Class<?> clazz = Class.forName("sample.HelloWorld");
        Method m = clazz.getDeclaredMethod("test");

        Object instance = clazz.newInstance();
        m.invoke(instance);
    }
}
