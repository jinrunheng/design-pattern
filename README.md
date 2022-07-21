## 一、设计模式
什么是设计模式？

![](https://files.mdnice.com/user/19026/c7129bb6-d60e-41d6-b97e-026b0649d8a2.png)

在 1994 年，由 Erich Gamma、Richard Helm、Ralph Johnson 和 John Vlissides（四人帮，Gang of Four）出版了一本书籍，书中系统化地提出了三大类型，共计 23 种经典的可以解决常见软件设计问题的可复用设计方案——即设计模式。

简而言之，**设计模式就是一种用来解决编程中特定问题的通用模版**。

## 二、设计模式的分类

![](https://files.mdnice.com/user/19026/1364e9a1-aca3-4a40-9373-6aa4814a907e.png)

设计模式可以分为三大类型：

1. 创建型模式
2. 结构型模式
3. 行为型模式

创建型模式（Creational Patterns）就是用来创建对象的设计模式；创建型模式将创建对象的过程进行封装，客户端只需要使用对象，而不再需要关注创建对象的具体逻辑。

结构型模式（Structural Patterns）用于将各种对象进行关联或组装，以便于获得更加灵活的结构。

行为型模式（Behavioral Patterns）负责对象之间的职责委派。

## 三、创建型模式

创建型模式包含：

- 工厂方法模式
- 抽象工厂模式
- 单例模式
- 建造者模式
- 原型模式

### 1. 工厂方法模式(Factory)

假设我们需要生产两种类型的口罩：N95 口罩以及医用口罩。

构建对象最常用的方式就是 new 一个对象：

```java
public class Client {

    public static void main(String[] args) {
        // 医用口罩
        SurgicalMask surgicalMask = new SurgicalMask();
        // N95 口罩
        N95Mask n95Mask = new N95Mask();

    }
}
```
这样做看上去似乎没有什么问题。

不过在客户端程序中，客户端需要管理对象的创建，也就必须知道 N95 口罩以及医用口罩的构造方法。这也就形成了一种耦合关系——当我们生产的口罩需要进行一些改变，进而需要新的构造方法时：
```java
Color black = new Color("black");
SurgicalMask surgicalMask = new SurgicalMask(black);
```
我们在客户端程序中，也需要修改代码来迎合创建对象方式的改变。

而更好的实现方式是，我们有一个生产口罩的工厂类，客户端程序只需要与工厂类打交道，而不需要知道 N95 口罩和医用口罩具体的生产细节：

**口罩类**：

*Mask*
```java
public abstract class Mask {
}
```
*N95Mask*
```java
public class N95Mask extends Mask{
    @Override
    public String toString() {
        return "这是 N95 口罩";
    }
}
```
*SurgicalMask*
```java
public class SurgicalMask extends Mask{
    @Override
    public String toString() {
        return "这是医用口罩";
    }
}
```
**工厂类**：

*N95MaskFactory*
```java
public class N95MaskFactory {

    public Mask create() {
        return new N95Mask();
    }
}
```
*SurgicalMaskFactory*
```java
public class SurgicalMaskFactory {
    public Mask create() {
        return new SurgicalMask();
    }
}
```
**客户端**：

*Client*
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

这样一来，我们让客户端屏蔽了生产口罩的具体细节，而是让工厂类来负责口罩对象的创建。当口罩对象创建方式有所改变时，我们只需要修改工厂类里面的代码，而客户端程序则无需作出任何改变。

### 2. 抽象工厂模式(Abstract Factory)

抽象工厂模式实际上也是工厂模式的一种。

还是使用口罩来进行举例，我们知道简单工厂模式客户端的调用程序：
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
而抽象工厂模式就是定义出一个抽象的工厂类/接口，让具体的工厂类继承/实现抽象的工厂类/接口，并重写抽象工厂的抽象方法。

**工厂类**：

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
**客户端**：

*Client*
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
看到这里，你可能还没有体会到抽象工厂模式的好处。而一旦抽象工厂模式和**反射**结合在一起，便可以发挥出抽象工厂模式真正的威力。

我们来模拟一个场景。

公司中，目前使用的数据库是 Mysql，老板想要换成 Oracle，未来指不定又要换成 Sql Server 或者是其他的什么数据库....

客户端的调用程序是这样的：
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
我们发现，当需要更换数据库时，客户端就要修改代码，这是因为业务逻辑与数据访问产生了耦合。

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
我们通过抽象工厂模式 + 反射，将客户端的业务代码与数据访问进行了解耦。

体会到了工厂模式的好处后，我们再来谈一下工厂模式带来的弊端。

假设 `IFactory` 接口需要新增一个抽象方法，那么便会影响到所有实现了 `IFactory` 的工厂类；当我们有很多的产品时，相应地就会有很多的工厂类，这种纵向扩展带来的改动几乎是灾难性的。

所以，**抽象工厂模式适用于增加同类工厂这样的横向扩展需求，而不适合新增功能这样的纵向扩展需求**。

### 3. 单例模式(Singleton)

当我们需要某个对象全局只有一个实例时，便可以使用单例模式来创建该对象。

举个 🌰：

我们都知道 Windows 系统的回收站。当我们重复点击回收站图标时，也只会弹出一个回收站窗口，这便是单例模式的一个实现。

单例模式的优点在于：
1. 能够避免对象的重复创建，节约空间并提升效率
2. 能够避免操作不同实例而导致的逻辑错误

而单例模式主要有两种实现方式：

- 饿汉式（eager instantiation）
- 懒汉式（lazy instantiation）

#### 饿汉式

![](https://files.mdnice.com/user/19026/63da7ae9-0f01-401b-a765-a10ab06c4ae8.png)

饿汉式单例指的是变量在声明时便初始化：
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
无论是饿汉式还是懒汉式，单例模式的构造方法定义一定为 `private`，这样就可以保证其他类无法实例化该类，只能通过 `getInstance()` 方法才能获取到该类唯一的实例。 

饿汉式在写法上非常简单，但是它也有很明显的缺点，那便是即使这个单例不需要使用，我们也已经在类加载时，将这个单例创建出来，并占用了一块内存。

#### 懒汉式

![](https://files.mdnice.com/user/19026/086c0709-b22a-444a-ae58-6f4db8176390.png)

懒汉式单例会先声明一个变量，等到需要使用时，才进行初始化。

相比于饿汉式，懒汉式解决了饿汉式的弊端，其好处是按需加载，避免了内存浪费，同时也减少了类初始化的时间。

我们可能会写出这样的代码：
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
```java
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
不难想到，我们的代码出现问题的原因便在于 `if(instance == null)` 这句话，我们可以在判空的过程上加锁，便可以保证多个线程在调用 `getInstance()` 方法时，一次最多只有一个线程执行判空操作：
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
这样，在多线程的情况下，如果有线程检测到 `instance ！= null`，那么就不会进入到`synchronized` 代码块中，便可以大大提升效率。

不过，问题到这里还没有结束......

我们思考：

```java
instance = new Singleton();
```
这句话是否具备原子性？

这段代码中，`new` 一个对象实际上可以拆分成三个步骤：

1. 为对象分配内存
2. 初始化对象
3. 将变量 `instance` 指向分配的内存

我们知道，CPU 的运行速度要比内存读写速度快很多，JVM 为了提高性能，在没有数据依赖性的前提下，编译器有可能会对指令重排，即，先执行 CPU 自己就可以执行的指令；如果指令发生重排，那么这一句话实际上执行的顺序为：
```text
1
3
2
```
我们来模拟一个场景：

线程一在执行到步骤三，还未执行步骤二时，此时线程二提前通过双检锁外层的判空检查，判断 `instance` 不等于空，而此时线程一还没有初始化 `instance` 对象，线程二便会抛出空指针异常。

为了解决这个问题，我们可以使用 `volatile` 关键字。

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
以上代码是通过双检锁 + `volatile` 关键字来实现懒汉式单例的，除了这种方式外，还有一种常见的懒汉式单例的写法——即：静态内部类方式：
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

我们知道，类加载的顺序。

![](https://files.mdnice.com/user/19026/0f04694e-d5d7-4942-911d-40b9526a47ad.png)

初始化阶段的作用是为类的静态变量赋初始值，例如：`public static int a = 1;`，在初始化阶段，会为静态变量 `a` 赋初始值 `1`。

而在初始化阶段并不会立即加载内部类，内部类会在使用时才进行加载。当我们调用 `getInstance()` 方法时，由于使用了静态内部类 `SingletonHolder` 的静态变量 `instance`， 所以这个时候才会去初始化内部类，这便实现了懒加载。

那么如何保证静态内部类的懒汉式单例是线程安全的呢？

在初始化阶段，虚拟机会调用类的 `<clinit>` 方法执行静态变量的赋值与执行静态代码块，所以，`public static Singleton4 instance = new Singleton4();` 这句话中，静态变量 `instance` 是在初始化阶段完成赋值的。

而虚拟机在加载类的 `<clinit>` 方法时，早就考虑到多线程并发的情况，所以自然会保证 `<clinit>` 在多线程中被正确加锁，同步。即便是多个线程同时去初始化一个类，也只能有一个线程可以执行 `<clinit>` 方法，从而保证了线程安全。

### 4. 建造者模式(Builder)

如果大家有读过《Effective Java》，那么就一定接触过建造者模式。

《Effective Java》Iterm2：
> Consider a builder when faced with many constructor parameters

这句话翻译过来就是，当构造方法的参数过多时，我们应该考虑使用 Builder 模式来创建对象。

假设我们有一个食品营养成分标签类，该类中有许多参数，譬如每次建议的摄入量，每罐的卡路里，以及总脂肪等等 ......

如果我们使用构造方法传参的形式来创建对象，便会导致很难编写客户端的代码：
```java
public class NutritionFacts {
    private final int servingSize;
    private final int servings;
    private final int calories;
    private final int fat;
    private final int sodium;
    private final int carbohydrate;

    public NutritionFacts(int servingSize, int servings, int calories, int fat, int sodium, int carbohydrate) {
        this.servingSize = servingSize;
        this.servings = servings;
        this.calories = calories;
        this.fat = fat;
        this.sodium = sodium;
        this.carbohydrate = carbohydrate;
    }

    public static void main(String[] args) {

        NutritionFacts cocaCola = new NutritionFacts(240, 8, 100, 0, 35, 27);
    }
}
```
首先，编写者在传参时，需要严格对照构造器，稍有不留神便会写出很难察觉的 bug；其次，这种方式也会给阅读这段代码的人造成不便。

第二种方式便是通过 JavaBean 的 getter/setter 来设置和获取参数。

```java
NutritionFacts cocaCola = new NutritionFacts();
cocaCola.setServingSize(240);
cocaCola.setServings(8);
cocaCola.setCalories(100);
cocaCola.setSodium(35);
cocaCola.setCarbohydrate(27);
```
虽然通过 JavaBean 的方式改善了构造器传参不易读的缺点，但是这种方式还是有一些缺陷：

1. 代码冗长
2. 属性不能设置为 final，违反了类的不可变性

取而代之，最好的实践方式便是建造者模式。

*NutritionFacts*
```java
public class NutritionFacts {
    private final int servingSize;
    private final int servings;
    private final int calories;
    private final int fat;
    private final int sodium;
    private final int carbohydrate;

    public static class Builder {
        // Required parameters
        private final int servingSize;
        private final int servings;
        // Optional parameters - initialized to default values
        private int calories = 0;
        private int fat = 0;
        private int sodium = 0;
        private int carbohydrate = 0;

        public Builder(int servingSize, int servings) {
            this.servingSize = servingSize;
            this.servings = servings;
        }

        public Builder calories(int val) {
            calories = val;
            return this;
        }

        public Builder fat(int val) {
            fat = val;
            return this;
        }

        public Builder sodium(int val) {
            sodium = val;
            return this;
        }

        public Builder carbohydrate(int val) {
            carbohydrate = val;
            return this;
        }

        public NutritionFacts build() {
            return new NutritionFacts(this);
        }
    }

    private NutritionFacts(Builder builder) {
        servingSize = builder.servingSize;
        servings = builder.servings;
        calories = builder.calories;
        fat = builder.fat;
        sodium = builder.sodium;
        carbohydrate = builder.carbohydrate;
    }

    public static void main(String[] args) {
        NutritionFacts cocaCola = new Builder(240, 8)
                .calories(100)
                .sodium(35)
                .carbohydrate(27)
                .build();
    }
}
```
建造者模式综合了构造器方式以及 JavaBean 方式的优点：

1. 代码易于编写，易于阅读，并且链式调用也非常直观简洁
2. 确保了参数的不可变性

### 5. 原型模式(Prototype)

原型模式是通过给出一个原型对象来指明所有创建的对象的类型，然后用复制这个原型对象的办法创建出更多同类型的对象。我们可以简单理解为，原型模式就是用来克隆对象。

在 Java 语言中，`Object` 类的 `clone()` 方法就是原型模式的一种实现。

提起克隆，我最先想到的便是《黑客帝国》中的特工史密斯。

![](https://files.mdnice.com/user/19026/0b6f66c6-1485-4c76-a8d9-552d10525bc7.png)

*AgentSmith*
```java
public class AgentSmith implements Cloneable {

    public void say() {
        System.out.println("I'm agent Smith");
    }

    @Override
    protected AgentSmith clone() throws CloneNotSupportedException {
        return (AgentSmith) super.clone();
    }

    public static void main(String[] args) throws CloneNotSupportedException {
        AgentSmith smith = new AgentSmith();
        AgentSmith clone1 = smith.clone();
        AgentSmith clone2 = smith.clone();
        clone1.say();
        clone2.say();
    }
}
```
我们通过实现 `Cloneable` 接口，重写 `clone()` 方法，让 `AgentSmith` 拥有了复制/克隆的能力。

在这里，我想重新谈一下深拷贝与浅拷贝的问题～

浅拷贝与深拷贝都是复制对象的一种方法。

浅拷贝（Shallow Copy）是指，如果有一个原始对象 A，我们将创建一个副本 B，并将 A 的字段逐个地复制到 B 中。这种复制方式称为 field-to-field copy，即我们所说的浅拷贝。如果该字段是原始数据类型，则复制原始类型的值；如果该字段是引用类型，则将复制该引用（内存地址），这会导致副本 B 与原始对象 A 产生了共享的对象，因此，如果这些被共享的对象之一被修改（从 A或 B)，这种变化在另一个对象中是可见的。

深拷贝（Deep Copy）是指，如果有一个原始对象 A，我们创建一个副本 B，和 field-to-field copy 不同，深拷贝会为被引用的对象创建新的副本对象，并将对这些对象的引用放置在 B 中。这就使得，副本 B 引用的对象与 A 引用的对象是不同的，并且是独立的。由于需要创建额外的对象，深拷贝比浅拷贝更加“昂贵“。

`Object` 类的 `clone()` 方法是一种浅拷贝。

验证代码：
```java
public class Person implements Cloneable {

    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void info() {
        System.out.println("My name is " + name + ", I'm " + age + " years old.");
    }

    @Override
    public Object clone() {
        Person p = null;
        try {
            p = (Person) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return p;
    }

    public static void main(String[] args) {
        Person p1 = new Person("kim", 20);
        Person p2 = (Person) p1.clone();
        System.out.println(p1.getName() == p2.getName() ? "shallow copy" : "deep copy");
    }
}
```
该程序输出的结果为：
```text
shallow copy
```
不理解的童鞋可以看下浅拷贝对象内存分配的示意图：

![](https://files.mdnice.com/user/19026/a5c03a8a-b994-4c4f-b5dc-34d24cfbefed.png)

那么如何实现深拷贝呢？

第一种方式就是通过序列化与反序列化。

首先，我们要拷贝的对象的类应该实现了 `Serializable` 接口；然后，我们可以借助第三方库，譬如 `Apache Commons Lang`，`Gson`，`Jackson` 等提供的序列化与反序列化方法实现一个对象的深拷贝，示例代码如下：
```java
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.apache.commons.lang3.SerializationUtils;

import java.io.IOException;
import java.io.Serializable;

public class Person implements Serializable {

    private String name;
    private int age;
    
    public Person() {
    }


    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void info() {
        System.out.println("My name is " + name + ", I'm " + age + " years old.");
    }
    
    public static void main(String[] args) throws IOException {
        Person p1 = new Person("kim", 20);

        // 1. 使用 Apache Commons Lang 包的序列化实现深拷贝
        Person p2 = SerializationUtils.clone(p1);
        System.out.println(p1.getName() == p2.getName() ? "shallow copy" : "deep copy");

        // 2. 使用 google Gson 序列化实现深拷贝
        Gson gson = new Gson();
        Person p3 = gson.fromJson(gson.toJson(p1), Person.class);
        System.out.println(p1.getName() == p3.getName() ? "shallow copy" : "deep copy");

        // 3. 使用 Jackson 序列化实现深拷贝
        ObjectMapper objectMapper = new ObjectMapper();
        Person p4 = objectMapper.readValue(objectMapper.writeValueAsString(p1), Person.class);
        System.out.println(p1.getName() == p4.getName() ? "shallow copy" : "deep copy");
    }
}
```
程序运行结果如下：
```text
deep copy
deep copy
deep copy
```
当然，我们也可以手动实现深拷贝：
```java
public static void main(String[] args) throws IOException {
    Person p1 = new Person("kim", 20);

    String name = new String("kim");
    Person p2 = new Person(name,p1.getAge());
        
    System.out.println(p1.getName() == p2.getName() ? "shallow copy" : "deep copy");
}
```
深拷贝对象内存示意图如下：

![](https://files.mdnice.com/user/19026/b3f3f077-7d7f-4b22-a6a1-1642cbc9f3d3.png)

谈完深拷贝与浅拷贝后，我们来看一下原型模式在 Spring 框架中的应用。

我们知道，Spring 框架中，一个 Bean 可以设置为以下五种作用域：

1. singleton
2. prototype
3. request
4. session
5. globalSession

今天的文章中，我已经向大家介绍了 Singleton 与 Prototype 这两种设计模式。大家不难想到，当一个 Bean 的 scope 设置为 singleton 时（默认），Spring 只会为这个 Bean 创建一个实例；而当一个 Bean 的 scope 设置为 prototype 时，每一次请求都会产生一个新的 Bean 实例。

验证代码：

*Component1*
```java
@Component
@Scope("singleton")
public class Component1 {
}
```

*Component2*
```java
@Component
@Scope("prototype")
public class Component2 {
}
```

*ComtextUtils*
```java
@Component
public class ContextUtils implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static ApplicationContext getContext() {
        return context;
    }

    public static Object getBean(String beanName) {
        return context.getBean(beanName);
    }

    public static Object getBean(Class<?> aClass) {
        return context.getBean(aClass);
    }
}
```

*DemoApplication*
```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication implements ApplicationRunner {

    @Autowired
    private ContextUtils contextUtils;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {


        Object bean1 = contextUtils.getBean(Component1.class);
        Object bean2 = contextUtils.getBean(Component1.class);
        System.out.println(bean1 == bean2);

        Object bean3 = contextUtils.getBean(Component2.class);
        Object bean4 = contextUtils.getBean(Component2.class);
        System.out.println(bean3 == bean4);
    }
}
```
该程序的输出结果为：
```text
true
false
```
那么 Spring 框架中，是如何实现 Bean 作用域的呢？

其实答案就在 `AbstractBeanFactory#doGetBean` 方法中：

![](https://files.mdnice.com/user/19026/1dc37744-bc66-4ee9-a4a8-32c78824a319.png)

如果检测到一个 Bean 的作用域为 singleton，那么 Spring 将会调用 `getSingleton()` 方法返回唯一的实例；而如果一个 Bean 的作用域为 prototype，就通过 `createBean()` 方法每次创建一个新的实例来返回。

## 四、总结

在今天的文章中，我向大家介绍了设计模式中的创建型模式。

五种创建型模式为：

1. 工厂方法模式
2. 抽象工厂模式
3. 单例模式
4. 建造者模式
5. 原型模式

创建型模式是用来创建对象的设计模式；它让对象的使用和创建变得更加灵活。其中，工厂模式（简单工厂以及抽象工厂），单例模式为学习与面试考察的重点。

好啦，至此为止，这篇文章就到这里了，后续我会带领大家逐渐学习剩下的设计模式，敬请期待～

欢迎大家关注我的公众号，在这里希望你可以收获更多的知识，我们下一期再见！

    