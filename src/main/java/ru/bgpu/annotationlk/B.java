package ru.bgpu.annotationlk;

public class B {

    @AppConfig
    static public String host;

    @AppConfig
    static public String[] sth;

    @Override
    public String toString() {
        return "B{" +
                "host='" + host + '\'' +
                ", sth=" + java.util.Arrays.toString(sth) +
                '}';
    }

}
