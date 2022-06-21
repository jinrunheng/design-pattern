package creational_patterns.singleton.lazy;

/**
 * @Author Dooby Kim
 * @Date 2022/6/21 11:14 下午
 * @Version 1.0
 */
public class Singleton4 {

    private static class SingletonHolder {
        public static Singleton4 instance = new Singleton4();
    }

    private Singleton4() {
    }

    public static Singleton4 getInstance() {
        return SingletonHolder.instance;
    }
}
