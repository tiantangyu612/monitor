package monitor.core.collector.items.method;

import javassist.*;
import monitor.core.annotation.Monitor;
import monitor.core.log.MonitorLogFactory;
import monitor.core.util.JavassistUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by lizhitao on 2018/1/5.
 * JavaMethodTransformer，使用 Javassist 增加被监控的方法，记录方法的执行 rt、错误数、最大并发、异常等信息
 */
public class JavaMethodTransformer implements ClassFileTransformer {
    private static Logger LOG = MonitorLogFactory.getLogger(JavaMethodTransformer.class);
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
            if (className.startsWith("monitor/agent") || className.startsWith("monitor/core")) {
                return classfileBuffer;
            }

            ByteArrayInputStream classFileByteArrayInputStream = null;
            try {
                classFileByteArrayInputStream = new ByteArrayInputStream(classfileBuffer);
                ClassPool classPool = ClassPool.getDefault();
                CtClass ctClass = classPool.makeClass(classFileByteArrayInputStream);

                if (!ctClass.isInterface() && !ctClass.isAnnotation() && !ctClass.isEnum() && !ctClass.isArray()) {
                    Class superClass = null;
                    try {
                        if (classBeingRedefined != null) {
                            superClass = classBeingRedefined.getSuperclass();
                            if (superClass != null) {
                                JavassistUtil.addToClassPathIfNotExist(superClass.getName(), loader);
                            }
                        }
                    } catch (Exception e) {
                        String allMethodToAddInterceptor = superClass == null ? "null" : superClass.getName();
                        LOG.log(Level.SEVERE, "failed to load super class::" + allMethodToAddInterceptor + ",origin class:" + className, e);
                    }

                    CtMethod[] methods = ctClass.getDeclaredMethods();
                    if (methods != null && methods.length != 0) {
                        int methodIndex = 0;
                        for (CtMethod method : methods) {
                            if (!IGNORED_METHOD.contains(method.getName())) {
                                if (method.hasAnnotation(Monitor.class)) {
                                    try {
                                        addInterceptor(method, ctClass, loader, methodIndex);
                                        methodIndex++;
                                    } catch (Exception e) {
                                        LOG.log(Level.SEVERE, "javassist enhance error, cause: ", e);
                                    }
                                }
                            }
                        }
                        return ctClass.toBytecode();
                    }
                }
            } catch (Exception e) {
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

    private void addInterceptor(CtMethod method, CtClass clazz, ClassLoader loader, int index) throws CannotCompileException, NotFoundException, ClassNotFoundException {
        String methodName = method.getName();
        JavassistUtil.MethodInfo methodInfo = JavassistUtil.getMethodInfo(method.getSignature());
        Iterator fullName = methodInfo.getParamClassList().iterator();

        while (fullName.hasNext()) {
            String cam = (String) fullName.next();
            JavassistUtil.addToClassPathIfNotExist(cam, loader);
        }

        if (methodInfo.getReturnClassString() != null) {
            JavassistUtil.addToClassPathIfNotExist(methodInfo.getReturnClassString(), loader);
        }

        String fullName1 = methodName + methodInfo.getMethodSignature();
        ClassAndMethod cam1 = new ClassAndMethod(clazz.getName(), fullName1);
        Integer resourceId = JavaMethodCollectorItem.RESOURCE_FACTORY.registerResource(cam1);
        if (resourceId == null) {
            LOG.severe("not enough resource:" + cam1.toString());
        } else {
            CtMethod newMethod = CtNewMethod.copy(method, clazz, null);
            if (null != method.getGenericSignature()) {
                newMethod.setGenericSignature(method.getGenericSignature());
            }

            String oldName = method.getName() + "$sentryProxy" + index;
            method.setName(oldName);
            if (java.lang.reflect.Modifier.isPublic(method.getModifiers())) {
                int collectorName = method.getModifiers() - 1 + 2;
                method.setModifiers(collectorName);
            }

            String javaMethodCollectorName = JavaMethodCollector.class.getName();
            String collectInfoClassName = JavaMethodCollectInfo.class.getName();
            StringBuilder body = new StringBuilder();
            body.append("{");
            body.append("long startTime=0;");
            body.append("boolean isEnable =").append(javaMethodCollectorName).append(".isEnabled();");
            body.append(collectInfoClassName + " stats = null;");
            body.append("try{");
            body.append("if(isEnable){");
            body.append("startTime=System.nanoTime();");
            body.append("stats=").append(javaMethodCollectorName).append(".onStart(").append(resourceId.intValue()).append(");");
            body.append("}");
            if (method.getReturnType() == CtClass.voidType) {
                body.append(oldName + "($$);");
            } else if (method.getReturnType() == CtClass.intType) {
                body.append("int result = " + oldName + "($$);");
            } else if (method.getReturnType() == CtClass.booleanType) {
                body.append("boolean result = " + oldName + "($$);");
            } else if (method.getReturnType() == CtClass.byteType) {
                body.append("byte result  = " + oldName + "($$);");
            } else if (method.getReturnType() == CtClass.charType) {
                body.append("char result = " + oldName + "($$);");
            } else if (method.getReturnType() == CtClass.doubleType) {
                body.append("double result = " + oldName + "($$);");
            } else if (method.getReturnType() == CtClass.floatType) {
                body.append("float result = " + oldName + "($$);");
            } else if (method.getReturnType() == CtClass.longType) {
                body.append("long result = " + oldName + "($$);");
            } else if (method.getReturnType() == CtClass.shortType) {
                body.append("short result = " + oldName + "($$);");
            } else {
                body.append("Object result = " + oldName + "($$);");
            }

            if (method.getReturnType() != CtClass.voidType) {
                body.append("return result;");
            }

            body.append("}");
            body.append("catch(Throwable t){");
            body.append("if(stats!=null)stats.onThrowable(t);");
            body.append("throw t;");
            body.append("}");
            body.append("finally{if(stats!=null){");
            body.append("long endTime=System.nanoTime();");
            body.append("long timeUsed=endTime-startTime;");
            body.append("stats.onFinally(timeUsed);");
            body.append("}");
            body.append("}");
            body.append("}");
            newMethod.setBody(body.toString());
            JavassistUtil.copyMethodAnnotationdAttributes(method.getMethodInfo(), newMethod.getMethodInfo());
            JavassistUtil.removeAnnotationAttribute(method.getMethodInfo());
            clazz.addMethod(newMethod);
            LOG.info(" Interceptor added for " + cam1.getClassName() + "." + cam1.getMethodName() + ",resourceId[[" + resourceId + "]]");
        }
    }
}