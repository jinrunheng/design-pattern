package creational_patterns.singleton.lazy;

/**
 * @Author Dooby Kim
 * @Date 2022/6/20 11:30 下午
 * @Version 1.0
 */
public class Singleton2 {

    private static Singleton2 instance = null;
    private static int count = 0;

    private Singleton2() {
        System.out.println("Singleton2 私有的构造方法被实例化了 " + (++count) + " 次");
    }

    public static Singleton2 getInstance() {
        synchronized (Singleton2.class) {
            if (instance == null) {
                instance = new Singleton2();
            }
        }
        return instance;
    }

    public static void main(String[] args) {
        // Singleton2 这种懒汉式写法是线程安全的
        Runnable task = () -> {
            String threadName = Thread.currentThread().getName();
            Singleton2 s2 = Singleton2.getInstance();
            System.out.println("线程 " + threadName + "\t => " + s2.hashCode());
        };
        // 模拟多线程环境下使用 Singleton 类获得对象
        for (int i = 0; i < 100; i++) {
            new Thread(task, "" + i).start();
        }
    }

}
