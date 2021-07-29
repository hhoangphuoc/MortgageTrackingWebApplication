package util;

import java.util.Arrays;

import javax.xml.crypto.dsig.keyinfo.KeyValue;

public class ObjectUtil {

    public static String objectToString(Object obj){
        if (obj == null){
            return "null";
        }
        var clazz = obj.getClass();
        String typeName = clazz.getName();
        String str = typeName + "{\n";

        var fields = clazz.getFields();
        for (var field: fields){
            var name = field.getName();
            Object value;
            try {
                value = field.get(obj);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                continue;
            }
            str += KeyValue(name, value);
        }
        
        var methods = clazz.getMethods();
        for (var getter: methods){
            var methodName = getter.getName();
            if (methodName.equals("getClass")){
                continue;
            }
            if (!methodName.startsWith("get")){
                continue;
            }
            var name = methodName.replaceFirst("get", "");
            name = Character.toLowerCase(name.charAt(0)) + name.substring(1);

            Object value;
            try {
                value = getter.invoke(obj);
            } catch (Exception e) {
                continue;
            }
            
            str += KeyValue(name, value);
        }
        str += "}";
        return str;
    }
    private static String KeyValue(String key, Object value){
        var valueString = "" + value;
        if (value instanceof byte[]){
            valueString = bytesToHex((byte[])value);
        }
        if (valueString.startsWith("model.")){
            var lines = ObjectUtil.objectToString(value).split("\n");
            valueString = "";
            for (String line: lines){
                valueString += "\n  " + line;
            }
        }
        return "  " + key + " = " + valueString + ",\n";
    }
    private static final String hexDigits = "0123456789abcdef";
    public static String bytesToHex(byte[] bytes){
        String hex = "";
        for (byte b: bytes){
            hex += hexDigits.charAt(b&15);
            b >>>= 4;
            hex += hexDigits.charAt(b&15);
        }
        return hex;
    }
}
