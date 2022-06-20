package creational_patterns.singleton.lazy;

/**
 * @Author Dooby Kim
 * @Date 2022/6/20 11:33 下午
 * @Version 1.0
 */
public class Singleton3 {

    private static volatile Singleton3 instance = null;

    private Singleton3() {
    }

    public static Singleton3 getInstance() {
        if (instance == null) {
            synchronized (Singleton3.class) {
                if (instance == null) {
                    instance = new Singleton3();
                }
            }
        }
        return instance;
    }

}
