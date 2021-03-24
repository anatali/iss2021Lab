package it.unibo.javaactorsUsage;

import it.unibo.supports2021.ActorBasicJava;

public class MainJavaActorUsage {

    public void doTest( ){
        ActorBasicJava a0 = new Actor0("a0");
        a0.send("hello from doTest");
        ActorBasicJava a1 = new Actor1("a1", a0);
        a1.send("welcome");
        ActorBasicJava.delay(1000);
        System.out.println("MainJavaActorUsage | doTest: " + ActorBasicJava.aboutThreads());
        a0.terminate();
        a1.terminate();
    }
    public static void main(String[] args) throws Exception {
        System.out.println("MainJavaActorUsage | BEGIN " + ActorBasicJava.aboutThreads());
        MainJavaActorUsage appl = new MainJavaActorUsage();
        appl.doTest( );
        System.out.println("MainJavaActorUsage | END1 " + ActorBasicJava.aboutThreads());
        ActorBasicJava.delay(1000);
        System.out.println("MainJavaActorUsage | END2 " + ActorBasicJava.aboutThreads());
    }

}
