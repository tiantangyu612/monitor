//package monitor.core.collector.items.url;
//
//import javassist.*;
//import monitor.core.collector.base.transformer.SpecifiedClassTransformer;
//import monitor.core.util.MonitorJavassistUtil;
//
//import java.io.ByteArrayInputStream;
//import java.security.ProtectionDomain;
//import java.util.HashSet;
//import java.util.Set;
//
///**
// * Created by bjlizhitao on 2018/1/12.
// * tomcat url transformer，拦截 web 容器 url 返回码
// */
//public class TomcatURLTransformer implements SpecifiedClassTransformer {
//    /**
//     * 需要转换的 tomcat 类
//     */
//    private static final String TRANSFORMER_TOMCAT_CLASS = "org/apache/catalina/core/StandardContextValve";
//    /**
//     * tomcat Request 类
//     */
//    private static final String TOMCAT_REQUEST_CLASS = "org.apache.catalina.connector.Request";
//    /**
//     * tomcat Response 类
//     */
//    private static final String TOMCAT_REPONSE_CLASS = "org.apache.catalina.connector.Response";
//    /**
//     * 需要 transformer 的方法名
//     */
//    private static final String TRANSFORMER_METHOD = "invoke";
//
//    @Override
//    public Set<String> getClassNames() {
//        Set<String> classNames = new HashSet<String>();
//        classNames.add(TRANSFORMER_TOMCAT_CLASS);
//        return classNames;
//    }
//
//    @Override
//    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws Exception {
//        ClassPool classPool = ClassPool.getDefault();
//        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(classfileBuffer);
//        CtClass ctClass = classPool.makeClass(byteArrayInputStream);
//
//        CtClass request = MonitorJavassistUtil.getClass(TOMCAT_REQUEST_CLASS, loader);
//        CtClass response = MonitorJavassistUtil.getClass(TOMCAT_REPONSE_CLASS, loader);
//        CtClass[] methodParams = new CtClass[]{request, response};
//        CtMethod ctMethod = ctClass.getDeclaredMethod(TRANSFORMER_METHOD, methodParams);
//        if (ctMethod != null) {
//            this.addInterceptor(ctMethod, ctClass);
//        }
//
//        byte[] classBytecode = ctClass.toBytecode();
//        ctClass.defrost();
//
//        return classBytecode;
//    }
//
//    private void addInterceptor(CtMethod method, CtClass clazz) throws CannotCompileException, NotFoundException, ClassNotFoundException {
//        String before = UrlHelper.generateBeforeCode("$1");
//        String collectorClassName = UrlStatsCollector.class.getName();
//        String catchthrwoable = "{ " + collectorClassName + ".onThrowable($e); throw $e; }";
//        StringBuilder after = new StringBuilder();
//        after.append("{ org.apache.catalina.connector.Response res = (org.apache.catalina.connector.Response)$2;");
//        after.append(collectorClassName + ".onFinally(res.getStatus()); }");
//        String finallydo = after.toString();
//        MonitorJavassistUtil.createProxyMethod(method, clazz, before, catchthrwoable, finallydo);
//        LOG.info("add  interceptor for tomcat container sucessfully");
//    }
//}
