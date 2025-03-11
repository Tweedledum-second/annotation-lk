package ru.bgpu.annotationlk;

public class A {

    @AppConfig
    static private String host;

    @AppConfig
    static public Integer port;

    @AppConfig(defValue="?")
    static private String[] intArray;

    @Override
    public String toString() {
        return "A{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", sth=" + java.util.Arrays.toString(intArray) +
                '}';
    }
}
