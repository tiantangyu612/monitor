package monitor.core.util;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.AttributeInfo;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.ParameterAnnotationsAttribute;

import java.util.*;

/**
 * Created by lizhitao on 2018/1/9.
 * monitor javassist 工具类
 */
public class MonitorJavassistUtil {
    /**
     * 被增强的 monitor 方法后缀
     */
    public static final String MONITOR_METHOD_SUFFIX = "$monitorProxy";
    /**
     * 不需要增强的方法名称
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

    private MonitorJavassistUtil() {
    }

    /**
     * 是否为不需要增强的 java 类
     *
     * @param className
     * @return
     */
    public static boolean isIgnoredClass(String className) {
        return className.startsWith("sun/") || className.startsWith("java/")
                || className.startsWith("monitor/agent") || className.startsWith("monitor/core");
    }

    /**
     * 是否为不需要增强的 java method，已经被增强的 monitor 方法也不会被再次增强
     *
     * @param methodName
     * @return
     */
    public static boolean isIgnoredMethod(String methodName) {
        return IGNORED_METHOD.contains(methodName) || methodName.contains(MonitorJavassistUtil.MONITOR_METHOD_SUFFIX);
    }

    /**
     * 获取 CtClass，若获取不到则将类添加到 classpath
     *
     * @param className
     * @param loader
     * @return
     * @throws ClassNotFoundException
     * @throws NotFoundException
     */
    public static CtClass getClass(String className, ClassLoader loader) throws ClassNotFoundException, NotFoundException {
        ClassPool pool = ClassPool.getDefault();

        try {
            return pool.get(className);
        } catch (NotFoundException e) {
            Class clazz = loader.loadClass(className);
            pool.insertClassPath(new ClassClassPath(clazz));
            return pool.get(className);
        }
    }

    /**
     * 获取方法信息
     *
     * @param methodSignature 方法签名
     * @return
     */
    public static EnhancedMethodInfo getMethodInfo(String methodSignature) {
        int idx1 = methodSignature.indexOf("(");
        int idx2 = methodSignature.indexOf(")");
        String source = methodSignature.substring(idx1 + 1, idx2);
        String returnTypeString = methodSignature.substring(idx2 + 1);
        int index = 0;
        List<MethodParameterSignature> methodParameterSignatures = new ArrayList<MethodParameterSignature>();
        StringBuilder sb = new StringBuilder();

        while (true) {
            if (sb.length() != 0) {
                methodParameterSignatures.add(new MethodParameterSignature(sb.toString()));
                sb.delete(0, sb.length());
            }

            if (index == source.length()) {
                List<String> classTypeStrings = new ArrayList<String>();
                StringBuilder methodName = new StringBuilder();
                methodName.append("(");

                for (int returnSign = 0; returnSign < methodParameterSignatures.size(); ++returnSign) {
                    MethodParameterSignature methodParameterSignature = methodParameterSignatures.get(returnSign);
                    String classTypeString = methodParameterSignature.getClassTypeString();
                    if (classTypeString != null) {
                        classTypeStrings.add(classTypeString);
                    }

                    if (returnSign == 0) {
                        methodName.append(methodParameterSignature.toDisplayString());
                    } else {
                        methodName.append(",");
                        methodName.append(methodParameterSignature.toDisplayString());
                    }
                }

                methodName.append(")");
                MethodParameterSignature methodParameterSignature = new MethodParameterSignature(returnTypeString);
                EnhancedMethodInfo enhancedMethodInfo = new EnhancedMethodInfo(classTypeStrings, methodName.toString(), methodParameterSignature.getClassTypeString());
                return enhancedMethodInfo;
            }

            char paramClassList = source.charAt(index);
            if (paramClassList == CharUtils.LEFT_SQUARE_BRACKET) {
                sb.append(paramClassList);
                ++index;
                paramClassList = source.charAt(index);
                if (paramClassList == CharUtils.LEFT_SQUARE_BRACKET) {
                    sb.append(paramClassList);
                    ++index;
                    index = getTypeString(sb, source, index);
                } else {
                    index = getTypeString(sb, source, index);
                }
            } else {
                index = getTypeString(sb, source, index);
            }
        }
    }

    private static int getTypeString(StringBuilder sb, String source, int index) {
        char ch = source.charAt(index++);
        sb.append(ch);
        if (ch == 'L') {
            do {
                ch = source.charAt(index++);
                sb.append(ch);
            } while (ch != CharUtils.SEMICOLON);
        }

        return index;
    }

    /**
     * 拷贝方法 Annotation 以及方法参数 Annotation 注解到另一个方法上
     *
     * @param oneMethod
     * @param anotherMethod
     */
    public static void copyMethodAnnotationAttributes(MethodInfo oneMethod, MethodInfo anotherMethod) {
        if (oneMethod != null && anotherMethod != null) {
            AnnotationsAttribute oneMethodAnnotations = (AnnotationsAttribute) oneMethod.getAttribute("RuntimeVisibleAnnotations");
            ParameterAnnotationsAttribute oneMethodParamAnnotations = (ParameterAnnotationsAttribute) oneMethod.getAttribute("RuntimeVisibleParameterAnnotations");
            AnnotationsAttribute anotherMethodAnnotations = (AnnotationsAttribute) anotherMethod.getAttribute("RuntimeVisibleAnnotations");
            ParameterAnnotationsAttribute anotherMethodParamAnnotations = (ParameterAnnotationsAttribute) anotherMethod.getAttribute("RuntimeVisibleParameterAnnotations");
            AttributeInfo attributeInfo;
            if (oneMethodAnnotations != null && anotherMethodAnnotations == null) {
                attributeInfo = oneMethodAnnotations.copy(anotherMethod.getConstPool(), Collections.EMPTY_MAP);
                anotherMethod.addAttribute(attributeInfo);
            }

            if (oneMethodParamAnnotations != null && anotherMethodParamAnnotations == null) {
                attributeInfo = oneMethodParamAnnotations.copy(anotherMethod.getConstPool(), Collections.EMPTY_MAP);
                anotherMethod.addAttribute(attributeInfo);
            }
        }
    }

    /**
     * 移除方法 Annotation 以及方法参数 Annotation 注解
     *
     * @param methodInfo
     */
    public static void removeAnnotationAttribute(MethodInfo methodInfo) {
        if (methodInfo != null) {
            List allAttributes = methodInfo.getAttributes();
            if (allAttributes != null) {
                Iterator attributeIterator = allAttributes.iterator();

                while (true) {
                    Object attribute;
                    do {
                        if (!attributeIterator.hasNext()) {
                            return;
                        }

                        attribute = attributeIterator.next();
                    }
                    while (!(attribute instanceof AnnotationsAttribute) && !(attribute instanceof ParameterAnnotationsAttribute));

                    attributeIterator.remove();
                }
            }
        }
    }

    /**
     * 方法信息
     */
    public static class EnhancedMethodInfo {
        /**
         * 方法参数列表
         */
        private List<String> paramClassList;
        /**
         * 方法签名
         */
        private String methodSignature;
        /**
         * 方法返回类型
         */
        private String returnClass;

        public EnhancedMethodInfo(List<String> paramClassList, String methodSignature, String returnClass) {
            this.paramClassList = paramClassList;
            this.methodSignature = methodSignature;
            this.returnClass = returnClass;
        }

        public String getMethodSignature() {
            return this.methodSignature;
        }

        public List<String> getParamClassList() {
            return this.paramClassList;
        }

        public void setParamClassList(List<String> paramClassList) {
            this.paramClassList = paramClassList;
        }

        public String getReturnClass() {
            return this.returnClass;
        }

        @Override
        public String toString() {
            return "EnhancedMethodInfo{" +
                    "paramClassList=" + paramClassList +
                    ", methodSignature='" + methodSignature + '\'' +
                    ", returnClass='" + returnClass + '\'' +
                    '}';
        }
    }
}
