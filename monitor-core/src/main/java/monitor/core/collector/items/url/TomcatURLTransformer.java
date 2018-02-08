package monitor.core.collector.items.url;

import javassist.*;
import monitor.core.collector.base.transformer.SpecifiedClassTransformer;
import monitor.core.util.MonitorJavassistUtil;

import java.io.ByteArrayInputStream;
import java.security.ProtectionDomain;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by bjlizhitao on 2018/1/12.
 * tomcat url transformer，拦截 web 容器 url 返回码
 */
public class TomcatURLTransformer implements SpecifiedClassTransformer {
    /**
     * 需要转换的 tomcat 类
     */
    private static final String TRANSFORMER_TOMCAT_CLASS = "org/apache/catalina/core/StandardContextValve";
    /**
     * tomcat Request 类
     */
    private static final String TOMCAT_REQUEST_CLASS = "org.apache.catalina.connector.Request";
    /**
     * tomcat Response 类
     */
    private static final String TOMCAT_RESPONSE_CLASS = "org.apache.catalina.connector.Response";
    /**
     * 需要 transformer 的方法名
     */
    private static final String TRANSFORMER_METHOD = "invoke";

    @Override
    public Set<String> getClassNames() {
        Set<String> classNames = new HashSet<String>();
        classNames.add(TRANSFORMER_TOMCAT_CLASS);
        return classNames;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws Exception {
        ClassPool classPool = ClassPool.getDefault();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(classfileBuffer);
        CtClass ctClass = classPool.makeClass(byteArrayInputStream);

        CtClass request = MonitorJavassistUtil.getClass(TOMCAT_REQUEST_CLASS, loader);
        CtClass response = MonitorJavassistUtil.getClass(TOMCAT_RESPONSE_CLASS, loader);
        CtClass[] methodParams = new CtClass[]{request, response};
        CtMethod ctMethod = ctClass.getDeclaredMethod(TRANSFORMER_METHOD, methodParams);
        if (ctMethod != null) {
            this.addInterceptor(ctMethod, ctClass);
        }

        byte[] classBytecode = ctClass.toBytecode();
        ctClass.defrost();

        return classBytecode;
    }

    private void addInterceptor(CtMethod method, CtClass clazz) throws CannotCompileException, NotFoundException, ClassNotFoundException {
        String before = UrlHelper.generateBeforeCode("$1");
        String collectorClassName = UrlStatsCollector.class.getName();
        String catchthrwoable = "{ " + collectorClassName + ".onThrowable($e); throw $e; }";
        StringBuilder after = new StringBuilder();
        after.append("{ org.apache.catalina.connector.Response res = (org.apache.catalina.connector.Response)$2;");
        after.append(collectorClassName + ".onFinally(res.getStatus()); }");
        String finallydo = after.toString();
        createProxyMethod(method, clazz, before, catchthrwoable, finallydo);
    }

    private void createProxyMethod(CtMethod method, CtClass clazz, String before, String catchThrowable, String doFinally) throws CannotCompileException, NotFoundException {
        CtMethod newMethod = CtNewMethod.copy(method, clazz, null);
        String oldName = method.getName() + "$sentryProxy";
        method.setName(oldName);
        if (java.lang.reflect.Modifier.isPublic(method.getModifiers())) {
            int body = method.getModifiers() - 1 + 2;
            method.setModifiers(body);
        }

        StringBuilder body1 = new StringBuilder();
        if (method.getReturnType() == CtClass.voidType) {
            body1.append("{ " + oldName + "($$);");
        } else if (method.getReturnType() == CtClass.intType) {
            body1.append("{ int result = " + oldName + "($$);");
        } else if (method.getReturnType() == CtClass.booleanType) {
            body1.append("{ boolean result = " + oldName + "($$);");
        } else if (method.getReturnType() == CtClass.byteType) {
            body1.append("{ byte result  = " + oldName + "($$);");
        } else if (method.getReturnType() == CtClass.charType) {
            body1.append("{ char result = " + oldName + "($$);");
        } else if (method.getReturnType() == CtClass.doubleType) {
            body1.append("{ double result = " + oldName + "($$);");
        } else if (method.getReturnType() == CtClass.floatType) {
            body1.append("{ float result = " + oldName + "($$);");
        } else if (method.getReturnType() == CtClass.longType) {
            body1.append("{ long result = " + oldName + "($$);");
        } else if (method.getReturnType() == CtClass.shortType) {
            body1.append("{ short result = " + oldName + "($$);");
        } else {
            body1.append("{ Object result = " + oldName + "($$);");
        }

        if (method.getReturnType() == CtClass.voidType) {
            body1.append("}");
        } else {
            body1.append("return result;}");
        }

        newMethod.setBody(body1.toString());
        newMethod.insertBefore(before);
        ClassPool pool = ClassPool.getDefault();
        CtClass etype = pool.get("java.lang.Throwable");
        newMethod.addCatch(catchThrowable, etype);
        newMethod.insertAfter(doFinally, true);
        clazz.addMethod(newMethod);
    }
}
