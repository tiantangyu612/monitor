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
    private ClassLoader classLoader;
    private CtClass ctClass;
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
                "ctClass=" + ctClass +
                ", ctMethods=" + ctMethods +
                '}';
    }
}
