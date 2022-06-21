package creational_patterns.prototype;

/**
 * @Author Dooby Kim
 * @Date 2022/6/22 1:05 上午
 * @Version 1.0
 */
public class AgentSmith implements Cloneable {

    public void say() {
        System.out.println("can you hear me");
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
