package creational_patterns.abstract_factory.db_example;

/**
 * @Author Dooby Kim
 * @Date 2022/6/20 10:18 下午
 * @Version 1.0
 */
public class MysqlFactory implements IFactory {
    @Override
    public IUser createUser() {
        return new MysqlUser();
    }
}
