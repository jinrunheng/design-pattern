package creational_patterns.singleton.lazy;

/**
 * @Author Dooby Kim
 * @Date 2022/6/20 11:15 下午
 * @Version 1.0
 */
public class Singleton1 {

    private static Singleton1 instance = null;
    private static int count;

    private Singleton1() {
        System.out.println("Singleton1 私有的构造方法被实例化了 " + (++count) + " 次");
    }

    public static Singleton1 getInstance() {
        if (instance == null) {
            instance = new Singleton1();
        }
        return instance;
    }

    public static void main(String[] args) {
        // Singleton1 这种懒汉式写法并不是线程安全的
        Runnable task = () -> {
            String threadName = Thread.currentThread().getName();
            Singleton1 s1 = Singleton1.getInstance();
            System.out.println("线程 " + threadName + "\t => " + s1.hashCode());
        };
        // 模拟多线程环境下使用 Singleton 类获得对象
        for (int i = 0; i < 100; i++) {
            new Thread(task, "" + i).start();
        }
    }
}
