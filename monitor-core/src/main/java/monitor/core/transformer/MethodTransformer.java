package monitor.core.transformer;

import javassist.*;
import monitor.core.annotation.Monitor;
import monitor.core.MonitorLogFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by lizhitao on 2018/1/5.
 * MethodTransformer，使用 Javassist 增加被监控的方法，记录方法的执行 rt、错误数、最大并发、异常等信息
 */
public class MethodTransformer implements ClassFileTransformer {
    private static Logger LOG = MonitorLogFactory.getLogger(MethodTransformer.class);
    /**
     * 不需要监控的方法名称
     */
    private static final Set<String> IGNORED_METHOD = new HashSet<String>();

    static {
        IGNORED_METHOD.add("getClass");
        IGNORED_METHOD.add("hashCode");
        IGNORED_METHOD.add("wait");
        IGNORED_METHOD.add("equals");
        IGNORED_METHOD.add("clone");
        IGNORED_METHOD.add("toString");
        IGNORED_METHOD.add("notify");
        IGNORED_METHOD.add("notifyAll");
        IGNORED_METHOD.add("finalize");
        IGNORED_METHOD.add("main");
    }

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) throws IllegalClassFormatException {
        if (!className.startsWith("sun/") && !className.startsWith("java/")) {
            if (className.startsWith("me/flyness/monitor/agent")) {
                return classfileBuffer;
            }

            ByteArrayInputStream classFileByteArrayInputStream = null;
            try {
                classFileByteArrayInputStream = new ByteArrayInputStream(classfileBuffer);
                ClassPool classPool = ClassPool.getDefault();
                CtClass ctClass = classPool.makeClass(classFileByteArrayInputStream);

                if (!ctClass.isInterface() && !ctClass.isAnnotation() && !ctClass.isEnum() && !ctClass.isArray()) {

                }

                CtMethod[] methods = ctClass.getDeclaredMethods();
                if (methods != null && methods.length != 0) {
                    for (CtMethod method : methods) {
                        if (!IGNORED_METHOD.contains(method.getName())) {
                            if (method.hasAnnotation(Monitor.class)) {


                            }
                        }
                    }
                }
            } catch (IOException e) {
                String errorMsg = "makeClass error, className is: " + className;
                LOG.log(Level.SEVERE, errorMsg, e);
            } finally {
                if (null != classFileByteArrayInputStream) {
                    try {
                        classFileByteArrayInputStream.close();
                    } catch (IOException e) {
                        String errorMsg = "classFileByteArrayInputStream error";
                        LOG.log(Level.SEVERE, errorMsg, e);
                    }
                }
            }

            return classfileBuffer;
        }
        return classfileBuffer;
    }

    private void addInterceptor(CtMethod method, CtClass clazz, int index) throws CannotCompileException, NotFoundException, ClassNotFoundException {
//        CtMethod newMethod = CtNewMethod.copy(method, clazz, null);
//        if (null != method.getGenericSignature()) {
//            newMethod.setGenericSignature(method.getGenericSignature());
//        }
//
//        String oldName = method.getName() + "$sentryProxy" + index;
//        method.setName(oldName);
//        if (Modifier.isPublic(method.getModifiers())) {
//            int collectorName = method.getModifiers() - 1 + 2;
//            method.setModifiers(collectorName);
//        }
//
//        String collectorName1 = JavaMethodCollector.class.getName();
//        String statsClassName = MethodStatsVo.class.getName();
//        StringBuilder body = new StringBuilder();
//        body.append("{");
//        body.append("long startTime=0;");
//        body.append("boolean isEnable =").append(collectorName1).append(".isEnabled();");
//        body.append(statsClassName + " stats =null;");
//        body.append("try{");
//        body.append("if(isEnable){");
//        body.append("startTime=System.nanoTime();");
//        body.append("stats=").append(collectorName1).append(".onStart(").append(resourceId.intValue()).append(");");
//        body.append("}");
//        if (method.getReturnType() == CtClass.voidType) {
//            body.append(oldName + "($$);");
//        } else if (method.getReturnType() == CtClass.intType) {
//            body.append("int result = " + oldName + "($$);");
//        } else if (method.getReturnType() == CtClass.booleanType) {
//            body.append("boolean result = " + oldName + "($$);");
//        } else if (method.getReturnType() == CtClass.byteType) {
//            body.append("byte result  = " + oldName + "($$);");
//        } else if (method.getReturnType() == CtClass.charType) {
//            body.append("char result = " + oldName + "($$);");
//        } else if (method.getReturnType() == CtClass.doubleType) {
//            body.append("double result = " + oldName + "($$);");
//        } else if (method.getReturnType() == CtClass.floatType) {
//            body.append("float result = " + oldName + "($$);");
//        } else if (method.getReturnType() == CtClass.longType) {
//            body.append("long result = " + oldName + "($$);");
//        } else if (method.getReturnType() == CtClass.shortType) {
//            body.append("short result = " + oldName + "($$);");
//        } else {
//            body.append("Object result = " + oldName + "($$);");
//        }
//
//        if (method.getReturnType() != CtClass.voidType) {
//            body.append("return result;");
//        }
//
//        body.append("}");
//        body.append("catch(Throwable t){");
//        body.append("if(stats!=null)stats.onThrowable(t);");
//        body.append("throw t;");
//        body.append("}");
//        body.append("finally{if(stats!=null){");
//        body.append("long endTime=System.nanoTime();");
//        body.append("long timeUsed=endTime-startTime;");
//        body.append("stats.onFinally(timeUsed);");
//        body.append("}");
//        body.append("}");
//        body.append("}");
//        newMethod.setBody(body.toString());
//        JavassistUtil.copyMethodAnnotationdAttributes(method.getMethodInfo(), newMethod.getMethodInfo());
//        JavassistUtil.removeAnnotationAttribute(method.getMethodInfo());
//        clazz.addMethod(newMethod);
//        LOG.info(" Interceptor added for " + cam1.getClassName() + "." + cam1.getMethodName() + ",resourceId[[" + resourceId + "]]");
    }
}