package monitor.core.collector.items.url;

import javassist.*;
import monitor.core.collector.base.transformer.SpecifiedClassTransformer;
import monitor.core.util.JavassistUtil;

import java.io.ByteArrayInputStream;
import java.security.ProtectionDomain;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by bjlizhitao on 2018/1/12.
 * tomcat url transformer
 */
public class TomcatUrlTransformer implements SpecifiedClassTransformer {
    @Override
    public Set<String> getClassNames() {
        Set<String> classNames = new HashSet<String>();
        classNames.add("org/apache/catalina/core/StandardContextValve");
        return classNames;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws Exception {
        ClassPool classPool = ClassPool.getDefault();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(classfileBuffer);
        CtClass ctClass = classPool.makeClass(byteArrayInputStream);
        CtClass[] methodParams = new CtClass[]{JavassistUtil.addToClassPathIfNotExist("org.apache.catalina.connector.Request", loader),
                JavassistUtil.addToClassPathIfNotExist("org.apache.catalina.connector.Response", loader)};
        CtMethod ctMethod = ctClass.getDeclaredMethod("invoke", methodParams);
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
        JavassistUtil.createProxyMethod(method, clazz, before, catchthrwoable, finallydo);
        LOG.info("add  interceptor for tomcat container sucessfully");
    }
}
