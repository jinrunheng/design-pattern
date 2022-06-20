package creational_patterns.factory;

/**
 * @Author Dooby Kim
 * @Date 2022/6/20 9:28 下午
 * @Version 1.0
 */
public class N95MaskFactory {

    public Mask create() {
        return new N95Mask();
    }
}
