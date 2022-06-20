package creational_patterns.abstract_factory.db_example;

/**
 * @Author Dooby Kim
 * @Date 2022/6/20 10:19 下午
 * @Version 1.0
 */
public class MysqlUser implements IUser {
    @Override
    public void getUser() {
        System.out.println("Mysql---->getUser");
    }
}
