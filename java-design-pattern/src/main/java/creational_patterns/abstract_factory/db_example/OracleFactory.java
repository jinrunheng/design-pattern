package creational_patterns.abstract_factory.db_example;

/**
 * @Author Dooby Kim
 * @Date 2022/6/20 10:19 下午
 * @Version 1.0
 */
public class OracleFactory implements IFactory {
    @Override
    public IUser createUser() {
        return new OracleUser();
    }
}
