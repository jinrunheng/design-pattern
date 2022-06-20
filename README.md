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

lanhan
https://blog.csdn.net/OOC_ZC/article/details/78477565
## 结构型模式（Structural Patterns）
## 行为型模式（Behavioral Patterns）
