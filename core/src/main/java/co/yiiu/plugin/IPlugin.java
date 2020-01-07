package co.yiiu.plugin;

/**
 * Created by tomoya at 2019/4/10
 * <p>
 * 这个接口就是为了约束插件里必须要有init方法的
 */
public interface IPlugin {

    void init() throws Exception;
}
