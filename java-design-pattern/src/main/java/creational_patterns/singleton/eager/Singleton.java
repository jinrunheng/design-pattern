package creational_patterns.singleton.eager;

/**
 * @Author Dooby Kim
 * @Date 2022/6/20 11:14 下午
 * @Version 1.0
 */
public class Singleton {
    private static Singleton instance = new Singleton();

    private Singleton() {

    }

    public static Singleton getInstance() {
        return instance;
    }
}
