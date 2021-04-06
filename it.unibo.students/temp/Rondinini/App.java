package it.unibo.students.Rondinini;


import it.unibo.students.Rondinini.controller.CrilController;
import it.unibo.students.Rondinini.controller.CrilStrategyFactory;

public class App {
    public static void main(String[] args) {
        new CrilController("controller",
                CrilStrategyFactory.createCautiousExplorationStrategy(
                        CrilStrategyFactory.createExplorationStrategy())).send("start");
    }
}
