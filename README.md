# design-pattern
设计模式(Java 版本)
## 构建型模式（Creational Patterns）
### 工厂模式（Factory）
### 抽象工厂模式（Abstract Factory）

抽象工厂模式与普通工厂模式相比有哪些优点？

普通的工厂模式在客户端下是这样进行调用的：
```java
public class Client {

    public static void main(String[] args) {
        // 医用口罩
        SurgicalMaskFactory surgicalMaskFactory = new SurgicalMaskFactory();
        surgicalMaskFactory.create();

        // N95 口罩
        N95MaskFactory n95MaskFactory = new N95MaskFactory();
        n95MaskFactory.create();
    }
}
```

而所谓的抽象工厂，就是定义出一个抽象的工厂类/接口，让具体的工厂类继承并重写抽象工厂的抽象方法：

*IFactory*
```java
public interface IFactory {
    Mask create();
}
```
*N95MaskFactory*
```java
public class N95MaskFactory implements IFactory {

    @Override
    public Mask create() {
        return new N95Mask();
    }
}
```
*SurgicalMaskFactory*
```java
public class SurgicalMaskFactory implements IFactory {

    @Override
    public Mask create() {
        return new SurgicalMask();
    }
}
```
抽象工厂模式在客户端的调用是这样的：
```java
public class Client {

    public static void main(String[] args) {
        // 医用口罩
        IFactory surgicalMaskFactory = new SurgicalMaskFactory();
        surgicalMaskFactory.create();

        // N95 口罩
        IFactory n95MaskFactory = new N95MaskFactory();
        n95MaskFactory.create();
    }
}
```
你可能还没有体会到抽象工厂模式的好处，可是一旦抽象工厂模式和**反射**结合在一起，那么其作用就可以很好地体现出来了。

假设我们来模拟一个场景，目前，我们使用的数据库是 Mysql，但是老板现在要更换成 Oracle，未来又指不定要换成 Sql Server... ...

在客户端的调用是这样的：
```java
public class Client {
    public static void main(String[] args) throws Exception {
        IFactory factory = new MySqlFactory();
        // IFactory factory = new OracleFactory();
        // IFactory factory = new SqlServerFactory();
        // ... ...
        IUser iUser = factory.createUser();
        iUser.getUser();
    }
}
```
我们发现，当我们需要更换数据库时，就要在客户端修改代码，这是因为业务逻辑与数据访问产生耦合导致。

我们可以使用反射，通过修改配置文件而不是修改业务代码的方式，来进行解耦，修改后的代码为：

*db.properties*
```properties
# 我们可以修改配置文件，而不是修改业务代码，完成数据和业务的解耦
# db=creational_patterns.abstract_factory.db_example.MysqlFactory
db=creational_patterns.abstract_factory.db_example.OracleFactory
```
*Client*
```java
public class Client {
    public static void main(String[] args) throws Exception {
        File propertiesFile = new File("java-design-pattern/src/main/resources/db.properties");
        InputStreamReader propertiesReader = new InputStreamReader(new FileInputStream(propertiesFile.getAbsolutePath()), "UTF-8");
        Properties properties = new Properties();
        properties.load(propertiesReader);
        String db = properties.getProperty("db");
        IFactory factory = (IFactory) Class.forName(db).getConstructor().newInstance();
        IUser iUser = factory.createUser();
        iUser.getUser();
    }
}
```

### 单例模式（Singleton）

单例模式有两种主要的实现方式：
- 饿汉式（eager instantiation）
- 懒汉式（lazy instantiation）

#### 饿汉式
所谓的饿汉式指的是变量在声明时便初始化：
```java
public class Singleton {
    private static Singleton instance = new Singleton();

    private Singleton() {

    }

    public static Singleton getInstance() {
        return instance;
    }
}
```
单例模式的构造方法定义为 `private`，这样就可以保证其他类无法实例化该类，只能通过 `getInstance()` 方法才能获取到该类唯一的实例。
饿汉式在写法上非常简便，但是它也有很明显的缺点，那便是即使这个单例不需要使用，我们也已经在类加载时，将这个单例创建出来，并占用了一块内存。
#### 懒汉式
而所谓的懒汉式表示先声明一个变量，等到需要使用时，才进行初始化。相比于饿汉式，懒汉式解决了饿汉式的弊端，其好处是按需加载，避免了内存浪费，同时也减少了类初始化的时间。

我们可以写出这样的代码：
```java
public class Singleton1 {

    private static Singleton1 instance = null;

    private Singleton1() {
    }

    public static Singleton1 getInstance() {
        if (instance == null) {
            instance = new Singleton1();
        }
        return instance;
    }
}
```
乍一看貌似没有什么问题，不过这段逻辑并不是线程安全的。如果有多个线程同时调用 `getInstance()` 方法，那么 `instance` 可能被实例化多次。

代码验证：
```java
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
```
代码执行结果：
```text
Singleton1 私有的构造方法被实例化了 2 次
Singleton1 私有的构造方法被实例化了 4 次
Singleton1 私有的构造方法被实例化了 1 次
Singleton1 私有的构造方法被实例化了 3 次
线程 6	 => 142414706
线程 0	 => 142414706
线程 7	 => 1627545691
线程 3	 => 456279605
... ...
```
出现问题的原因便在于 `if(instance == null)` 这句话，我们可以在判空的过程上加锁，便可以保证多个线程在调用 `getInstance()` 方法时，一次最多只有一个线程执行判空操作：
```java
public class Singleton2 {

    private static Singleton2 instance = null;

    private Singleton2() {
    }

    public static Singleton2 getInstance() {
        synchronized (Singleton2.class) {
            if (instance == null) {
                instance = new Singleton2();
            }
        }
        return instance;
    }
}
``` 
这样做虽然可以保证线程安全，但是却严重影响了效率；优化的手段其实也非常简单，我们只需要在同步化操作之前再加上一层检查：
```java
public class Singleton3 {

    private static Singleton3 instance = null;

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
```
这样，在多线程的情况下，如果有线程检测到 `instance ！= null`，那么就不会进入到同步化操作中，就可以大大提升效率。

不过，问题到这里还没有结束......

我们思考：
```java
instance = new Singleton();
```
这句话具备原子性么？

在上一段代码中，`new` 一个对象实际上可以拆分成三个步骤：
1. 为对象分配内存
2. 初始化对象
3. 将变量 `instance` 指向分配的内存

我们知道，CPU 的运行速度要比内存读写速度快很多，JVM 为了提高性能，在没有数据依赖性的前提下，编译器有可能会对指令重排，即，先执行 CPU 自己就可以执行的指令；如果指令发生重排，那么这一句话实际上执行的顺序为：
```text
1
3
2
```
我们模拟一个场景：

线程一在执行到步骤三，还未执行步骤二时，此时线程二提前通过双检锁外层的判空检查，判断 `instance` 不等于空，而此时线程一还没有初始化 `instance` 对象，线程二便会抛出空指针异常。

为了解决这个问题，我们可以通过 `volatile` 关键字。

`volatile` 是 Java 虚拟机提供的最轻量级的同步机制，其具备两个功能：
1. 使用 `volatile` 声明的变量对所有线程可见；也就是当一个线程修改了这个变量的值，新值对于其他线程来说是立刻可以得知的。
2. 使用 `volatile` 声明的变量初始化时，会禁止 JVM 对指令的重排序优化。

进而，我们的懒汉式代码可以改进为，使用 `volatile` 修饰 `instance` 变量：
```java
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
```

除了双检锁+`volatile` 关键字外，还有一种常见的懒汉式单例的写法——即：静态内部类方式
```java
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

```
那么静态内部类方式是如何实现懒加载的？它又是如何保证线程安全的？

我们知道，类加载的顺序为 ： 
1. 加载
2. 链接(验证，准备，解析)
3. 初始化

初始化阶段的作用是为类的静态变量赋初始值，例如：`public static int a = 1;`，在初始化阶段，会为静态变量 `a` 赋初始值 `1`。

而在初始化阶段并不会立即加载内部类，内部类会在使用时才进行加载。当我们调用 `getInstance()` 方法时，由于使用了静态内部类 `SingletonHolder` 的静态变量 `instance`， 所以这个时候才会去初始化内部类，这便实现了懒加载。

那么如何保证静态内部类的懒汉式是线程安全的呢？

在初始化阶段，虚拟机会调用类的 `<clinit>` 方法执行静态变量的赋值与执行静态代码块，所以，`public static Singleton4 instance = new Singleton4();` 这句话中，静态变量 `instance` 是在初始化阶段完成赋值的。

而虚拟机在加载类的 `<clinit>` 方法时，早就考虑到多线程并发的情况，所以自然会保证 `<clinit>` 在多线程中被正确加锁，同步。即便是多个线程同时去初始化一个类，也只能有一个线程可以执行 `<clinit>` 方法，从而保证了线程安全。



## 结构型模式（Structural Patterns）
## 行为型模式（Behavioral Patterns）
