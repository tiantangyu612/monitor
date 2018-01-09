package monitor.core.util;

/**
 * Created by lizhitao on 2018/1/9.
 * java 方法参数签名
 */
public class MethodParameterSignature {
    private String paramSignature;

    public MethodParameterSignature(String paramSignature) {
        this.paramSignature = paramSignature;
    }

    /**
     * 获取类类型
     *
     * @return
     */
    public String getClassTypeString() {
        String classTypeString;
        if (this.paramSignature.startsWith("[[L")) {
            classTypeString = this.paramSignature.substring(3, this.paramSignature.length() - 1);
            classTypeString = classTypeString.replaceAll("/", ".");
            return classTypeString;
        } else if (this.paramSignature.startsWith("[L")) {
            classTypeString = this.paramSignature.substring(2, this.paramSignature.length() - 1);
            classTypeString = classTypeString.replaceAll("/", ".");
            return classTypeString;
        } else if (this.paramSignature.startsWith("L")) {
            classTypeString = this.paramSignature.substring(1, this.paramSignature.length() - 1);
            classTypeString = classTypeString.replaceAll("/", ".");
            return classTypeString;
        } else {
            return null;
        }
    }

    public String toDisplayString() {
        String ss;
        String c1;
        if (this.paramSignature.startsWith("[[L")) {
            c1 = this.paramSignature.substring(3, this.paramSignature.length() - 1);
            ss = this.getDisplay(c1);
            return ss + "[][]";
        } else if (this.paramSignature.startsWith("[L")) {
            c1 = this.paramSignature.substring(2, this.paramSignature.length() - 1);
            ss = this.getDisplay(c1);
            return ss + "[]";
        } else if (this.paramSignature.startsWith("L")) {
            c1 = this.paramSignature.substring(1, this.paramSignature.length() - 1);
            return this.getDisplay(c1);
        } else {
            char c = this.paramSignature.charAt(this.paramSignature.length() - 1);
            ss = this.getTypeString(c);
            return this.paramSignature.startsWith("[[") ? ss + "[][]" : (this.paramSignature.startsWith("[") ? ss + "[]" : ss);
        }
    }

    private String getDisplay(String ss) {
        ss = ss.replaceAll("/", ".");
        int idx = ss.lastIndexOf(".");
        return idx < 0 ? ss : ss.substring(idx + 1, ss.length());
    }

    private String getTypeString(char ch) {
        if (ch == 90) {
            return "boolean";
        } else if (ch == 67) {
            return "char";
        } else if (ch == 66) {
            return "byte";
        } else if (ch == 73) {
            return "int";
        } else if (ch == 70) {
            return "float";
        } else if (ch == 74) {
            return "long";
        } else if (ch == 68) {
            return "double";
        } else if (ch == 83) {
            return "short";
        } else {
            throw new RuntimeException("wrong type:" + ch);
        }
    }
}
