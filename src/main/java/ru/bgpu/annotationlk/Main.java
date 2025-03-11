package ru.bgpu.annotationlk;


public class Main {

    static {
        AppConfigWorker.configProcessing(
                "ru.bgpu.annotationlk",
                "config.properties"
        );
    }

    public static void main(String[] args) {
        System.out.println(new A());
        System.out.println(new B());
    }

}
