package monitor.core.collector.base.transformer;

import javassist.CtClass;
import javassist.CtMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bjlizhitao on 2018/1/11.
 * 匹配到的需要增加方法监控字节码的类信息
 */
public class MatchedClass {
    /**
     * 加载该类的 ClassLoader
     */
    private ClassLoader classLoader;
    /**
     * 该类的 CtClass
     */
    private CtClass ctClass;
    /**
     * 需要监控的 CtMethod 列表，标注了 @Monitor 的方法会被监控
     */
    private List<CtMethod> ctMethods;

    public MatchedClass() {
    }

    public MatchedClass(ClassLoader classLoader, CtClass ctClass) {
        this.classLoader = classLoader;
        this.ctClass = ctClass;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public CtClass getCtClass() {
        return ctClass;
    }

    public void setCtClass(CtClass ctClass) {
        this.ctClass = ctClass;
    }

    public List<CtMethod> getCtMethods() {
        return ctMethods;
    }

    public void addCtMethods(CtMethod ctMethod) {
        if (null == ctMethods) {
            ctMethods = new ArrayList<CtMethod>();
        }

        ctMethods.add(ctMethod);
    }

    @Override
    public String toString() {
        return "MatchedClass{" +
                "classLoader=" + classLoader +
                ", ctClass=" + ctClass +
                ", ctMethods=" + ctMethods +
                '}';
    }
}
