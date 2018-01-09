package monitor.core.util;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.AttributeInfo;
import javassist.bytecode.ParameterAnnotationsAttribute;

import java.util.*;

/**
 * Created by lizhitao on 2018/1/9.
 * javassist 工具类
 */
public class JavassistUtil {
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

    private JavassistUtil() {
    }

    /**
     * 是否为不需要增强的 java method
     *
     * @param methodName
     * @return
     */
    public static boolean isIgnoredMethod(String methodName) {
        return IGNORED_METHOD.contains(methodName) || methodName.contains("$monitorProxy");
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
     * 将类添加到 classpath
     *
     * @param className
     * @param loader
     * @return
     * @throws ClassNotFoundException
     * @throws NotFoundException
     */
    public static CtClass addToClassPathIfNotExist(String className, ClassLoader loader) throws ClassNotFoundException, NotFoundException {
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
     * @param signature
     * @return
     */
    public static MethodInfo getMethodInfo(String signature) {
        int idx1 = signature.indexOf("(");
        int idx2 = signature.indexOf(")");
        String source = signature.substring(idx1 + 1, idx2);
        String returnTypeString = signature.substring(idx2 + 1);
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
                MethodInfo methodInfo = new MethodInfo(classTypeStrings, methodName.toString(), methodParameterSignature.getClassTypeString());
                return methodInfo;
            }

            char paramClassList = source.charAt(index);
            if (paramClassList == 91) {
                sb.append(paramClassList);
                ++index;
                paramClassList = source.charAt(index);
                if (paramClassList == 91) {
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
        if (ch == 76) {
            do {
                ch = source.charAt(index++);
                sb.append(ch);
            } while (ch != 59);
        }

        return index;
    }

    /**
     * 拷贝 Annotation 注解到新方法上
     *
     * @param oldMethod
     * @param newMethod
     */
    public static void copyMethodAnnotationAttributes(javassist.bytecode.MethodInfo oldMethod, javassist.bytecode.MethodInfo newMethod) {
        if (oldMethod != null && newMethod != null) {
            AnnotationsAttribute oldAnnotations = (AnnotationsAttribute) oldMethod.getAttribute("RuntimeVisibleAnnotations");
            ParameterAnnotationsAttribute oldParamAnnotations = (ParameterAnnotationsAttribute) oldMethod.getAttribute("RuntimeVisibleParameterAnnotations");
            AnnotationsAttribute newAnnotations = (AnnotationsAttribute) newMethod.getAttribute("RuntimeVisibleAnnotations");
            ParameterAnnotationsAttribute newParamAnnotations = (ParameterAnnotationsAttribute) newMethod.getAttribute("RuntimeVisibleParameterAnnotations");
            AttributeInfo attributeInfo;
            if (oldAnnotations != null && newAnnotations == null) {
                attributeInfo = oldAnnotations.copy(newMethod.getConstPool(), Collections.EMPTY_MAP);
                newMethod.addAttribute(attributeInfo);
            }

            if (oldParamAnnotations != null && newParamAnnotations == null) {
                attributeInfo = oldParamAnnotations.copy(newMethod.getConstPool(), Collections.EMPTY_MAP);
                newMethod.addAttribute(attributeInfo);
            }
        }
    }

    /**
     * 移除方法 Annotation 注解
     *
     * @param methodInfo
     */
    public static void removeAnnotationAttribute(javassist.bytecode.MethodInfo methodInfo) {
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
    public static class MethodInfo {
        private List<String> paramClassList;
        private String methodSignature;
        private String returnClass;

        public MethodInfo(List<String> paramClassList, String methodSignature, String returnClass) {
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
            return "MethodInfo{" +
                    "paramClassList=" + paramClassList +
                    ", methodSignature='" + methodSignature + '\'' +
                    ", returnClass='" + returnClass + '\'' +
                    '}';
        }
    }
}
