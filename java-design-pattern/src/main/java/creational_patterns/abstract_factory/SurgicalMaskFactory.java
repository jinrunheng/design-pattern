package creational_patterns.abstract_factory;

/**
 * @Author Dooby Kim
 * @Date 2022/6/20 9:28 下午
 * @Version 1.0
 */
public class SurgicalMaskFactory implements IFactory {

    @Override
    public Mask create() {
        return new SurgicalMask();
    }
}
