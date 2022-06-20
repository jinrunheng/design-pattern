package creational_patterns.abstract_factory.db_example;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * @Author Dooby Kim
 * @Date 2022/6/20 10:23 下午
 * @Version 1.0
 */
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
