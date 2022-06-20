package creational_patterns.abstract_factory;

/**
 * @Author Dooby Kim
 * @Date 2022/6/20 9:34 下午
 * @Version 1.0
 */
public class Client {

    public static void main(String[] args) {
        // 医用口罩
        IFactory surgicalMaskFactory = new SurgicalMaskFactory();
        System.out.println(surgicalMaskFactory.create());

        // N95 口罩
        IFactory n95MaskFactory = new N95MaskFactory();
        System.out.println(n95MaskFactory.create());

    }
}
