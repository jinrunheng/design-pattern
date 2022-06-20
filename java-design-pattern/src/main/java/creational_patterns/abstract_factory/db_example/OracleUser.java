package creational_patterns.abstract_factory.db_example;

/**
 * @Author Dooby Kim
 * @Date 2022/6/20 10:19 下午
 * @Version 1.0
 */
public class OracleUser implements IUser {
    @Override
    public void getUser() {
        System.out.println("Oracle---->getUser");
    }
}
