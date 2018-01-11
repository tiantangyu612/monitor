package monitor.core.collector.items.method;

import javassist.*;
import monitor.core.annotation.Monitor;
import monitor.core.collector.base.transformer.MatchedClass;
import monitor.core.collector.base.transformer.MatchedClassTransformer;
import monitor.core.log.MonitorLogFactory;
import monitor.core.util.JavassistUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.instrument.IllegalClassFormatException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by lizhitao on 2018/1/5.
 * JavaMethodTransformer，使用 Javassist 增加被监控的方法，记录方法的执行 rt、错误数、最大并发、异常等信息
 */
public class JavaMethodTransformer implements MatchedClassTransformer {
    private static final Logger LOGGER = MonitorLogFactory.getLogger(JavaMethodTransformer.class);

    /**
     * 获取匹配的类信息
     *
     * @param className
     * @param classfileBuffer
     * @return
     */
    @Override
    public MatchedClass getMatchedClass(ClassLoader loader, String className, byte[] classfileBuffer, Class<?> classBeingRedefined) {
        if (JavassistUtil.isIgnoredClass(className)) {
            return null;
        }

        ByteArrayInputStream classFileByteArrayInputStream = null;
        try {
            classFileByteArrayInputStream = new ByteArrayInputStream(classfileBuffer);
            ClassPool classPool = ClassPool.getDefault();
            CtClass ctClass = classPool.makeClass(classFileByteArrayInputStream);

            if (!ctClass.isInterface() && !ctClass.isAnnotation() && !ctClass.isEnum() && !ctClass.isArray()) {
                addParentClassToClassPathIfNotExists(className, classBeingRedefined, loader);

                MatchedClass matchedClass = new MatchedClass(loader, ctClass);
                CtMethod[] methods = ctClass.getDeclaredMethods();
                if (methods != null && methods.length != 0) {
                    for (CtMethod method : methods) {
                        if (!JavassistUtil.isIgnoredMethod(method.getName())) {
                            // 只有标注了 @Monitor 注解的方法才能被监控
                            if (method.hasAnnotation(Monitor.class)) {
                                matchedClass.addCtMethods(method);
                            }
                        }
                    }
                }

                if (null == matchedClass.getCtMethods()) {
                    return null;
                }

                return matchedClass;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        } finally {
            if (null != classFileByteArrayInputStream) {
                try {
                    classFileByteArrayInputStream.close();
                } catch (IOException e) {
                    String errorMsg = "close classFileByteArrayInputStream error";
                    LOGGER.log(Level.SEVERE, errorMsg, e);
                }
            }
        }
    }

    /**
     * 转换类字节码，增强方法
     *
     * @param matchedClass
     * @return
     * @throws IllegalClassFormatException
     */
    @Override
    public byte[] transform(MatchedClass matchedClass) throws Exception {
        CtClass ctClass = matchedClass.getCtClass();
        int methodIndex = 0;
        for (CtMethod ctMethod : matchedClass.getCtMethods()) {
            doEnhanceJavaMethod(ctMethod, ctClass, matchedClass.getClassLoader(), methodIndex++);
        }

        return ctClass.toBytecode();
    }

    /**
     * 将父类添加到 classPath
     *
     * @param className
     * @param classBeingRedefined
     * @param loader
     */
    private void addParentClassToClassPathIfNotExists(String className, Class<?> classBeingRedefined, ClassLoader loader) {
        Class superClass = null;
        try {
            if (classBeingRedefined != null) {
                superClass = classBeingRedefined.getSuperclass();
                if (superClass != null) {
                    JavassistUtil.addToClassPathIfNotExist(superClass.getName(), loader);
                }
            }
        } catch (Exception e) {
            String addedSuperClass = superClass == null ? "null" : superClass.getName();
            LOGGER.log(Level.SEVERE, "addParentClassToClasspath error, super class is: " + addedSuperClass + ", origin class:" + className, e);
        }
    }

    /**
     * 执行 java method 字节码增强，添加数据收集
     *
     * @param method
     * @param clazz
     * @param loader
     * @param index
     * @throws CannotCompileException
     * @throws NotFoundException
     * @throws ClassNotFoundException
     */
    private void doEnhanceJavaMethod(CtMethod method, CtClass clazz, ClassLoader loader, int index) throws CannotCompileException, NotFoundException, ClassNotFoundException {
        String methodName = method.getName();
        JavassistUtil.MethodInfo methodInfo = JavassistUtil.getMethodInfo(method.getSignature());
        Iterator<String> paramClassListIterator = methodInfo.getParamClassList().iterator();

        while (paramClassListIterator.hasNext()) {
            String paramClass = paramClassListIterator.next();
            JavassistUtil.addToClassPathIfNotExist(paramClass, loader);
        }

        if (methodInfo.getReturnClass() != null) {
            JavassistUtil.addToClassPathIfNotExist(methodInfo.getReturnClass(), loader);
        }

        String methodFullName = methodName + methodInfo.getMethodSignature();
        ClassAndMethod classAndMethod = new ClassAndMethod(clazz.getName(), methodFullName);
        Integer resourceId = JavaMethodCollectorItem.RESOURCE_FACTORY.registerResource(classAndMethod);
        if (resourceId == null) {
            LOGGER.severe("not enough resource:" + classAndMethod.toString());
        } else {
            CtMethod newMethod = CtNewMethod.copy(method, clazz, null);
            if (null != method.getGenericSignature()) {
                newMethod.setGenericSignature(method.getGenericSignature());
            }
            newMethod.setBody(buildNewMethodBody(method, index, resourceId));

            JavassistUtil.copyMethodAnnotationAttributes(method.getMethodInfo(), newMethod.getMethodInfo());
            JavassistUtil.removeAnnotationAttribute(method.getMethodInfo());
            clazz.addMethod(newMethod);
            LOGGER.info(" Interceptor added for " + classAndMethod.getClassName() + "." + classAndMethod.getMethodName() + ",resourceId[[" + resourceId + "]]");
        }
    }

    /**
     * 构建新方法的方法体
     *
     * @param method
     * @param index
     * @param resourceId
     * @return
     * @throws NotFoundException
     */
    private static String buildNewMethodBody(CtMethod method, int index, int resourceId) throws NotFoundException {
        String oldName = method.getName() + "$monitorProxy" + index;
        method.setName(oldName);
        if (Modifier.isPublic(method.getModifiers())) {
            int privateModifier = method.getModifiers() + 1;
            method.setModifiers(privateModifier);
        }

        String javaMethodCollectorName = JavaMethodCollector.class.getName();
        String collectInfoClassName = JavaMethodCollectInfo.class.getName();

        StringBuilder body = new StringBuilder();
        body.append("{");
        body.append("long startTime=0;");
        body.append("boolean isEnable =").append(javaMethodCollectorName).append(".isEnabled();");
        body.append(collectInfoClassName + " methodCollectInfo = null;");
        body.append("try{");
        body.append("if(isEnable){");
        body.append("startTime=System.nanoTime();");
        body.append("methodCollectInfo = ").append(javaMethodCollectorName).append(".onStart(").append(resourceId).append(");");
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
        body.append("if(methodCollectInfo!=null)methodCollectInfo.onThrowable(t);");
        body.append("throw t;");
        body.append("}");
        body.append("finally{if(methodCollectInfo!=null){");
        body.append("long endTime=System.nanoTime();");
        body.append("long timeUsed=endTime-startTime;");
        body.append("methodCollectInfo.onFinally(timeUsed);");
        body.append("}");
        body.append("}");
        body.append("}");

        return body.toString();
    }
}