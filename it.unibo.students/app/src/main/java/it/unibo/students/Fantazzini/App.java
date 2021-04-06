package it.unibo.students.Fantazzini;

import it.unibo.supports2021.IssWsHttpJavaSupport;

public class App {
    public static void main(String[] args) {
        try {

            String name = "cautious_actor";
            IssWsHttpJavaSupport support = IssWsHttpJavaSupport.createForWs("localhost:8091");
            ExplorationStrategy strategy = new WhateverStrategy();
            CautiousActor actor = new CautiousActor(name, support, strategy);
            actor.send("start");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
