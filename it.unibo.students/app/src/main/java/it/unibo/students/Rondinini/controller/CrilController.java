package it.unibo.students.Rondinini.controller;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import it.unibo.students.Rondinini.communication.*;
import it.unibo.supports2021.ActorBasicJava;
import it.unibo.supports2021.IssWsHttpJavaSupport;


public class CrilController extends ActorBasicJava {
    private final CrilStrategy<CrilCommand, CrilResult, CrilEvent> strategy;
    final IssWsHttpJavaSupport communicationSupport;
    Optional<Date> stoppedTime;

    public CrilController(final String name,
                          final CrilStrategy<CrilCommand,
                                  CrilResult, CrilEvent> strategy) {
        super(name);
        this.strategy = strategy;
        this.communicationSupport = IssWsHttpJavaSupport.createForWs("localhost:8091");
        this.communicationSupport.registerActor(this);
        this.stoppedTime = Optional.empty();
    }



    @Override
    protected void handleInput(String s) {
//        System.out.println("CrilController - receive |---- " + s);
        if (s.equals("start") && this.strategy.hasNextCommand()) {
            this.strategy.nextCommand().toCril().ifPresent(this.communicationSupport::forward);
            System.out.println("started");
        } else {
            CrilResult.toCrilResult(s)
                    .ifPresentOrElse(r -> {
                                this.strategy.setResult(r);
                                if (((this.stoppedTime.isPresent()
                                        && (new Date()).getTime() - this.stoppedTime.get().getTime() >= 2000)
                                        || this.stoppedTime.isEmpty())
                                        && this.strategy.hasNextCommand()) {
                                    this.stoppedTime = Optional.empty();
                                    this.strategy.nextCommand().toCril().ifPresent(this.communicationSupport::forward);
                                }
                            },
                            () -> CrilEvent.toCrilEvent(s)
                                    .ifPresent(e -> {
                                        this.strategy.setEvent(e);
                                        if (stoppedTime.isEmpty() && this.strategy.hasNextCommand()) {
                                            final CrilCommand command = this.strategy.nextCommand();
                                            command.toCril().ifPresent(this.communicationSupport::forward);
                                            if (command.equals(CrilCommand.ALARM)) {
                                                System.out.println("STOOOOOOOOOOOOOOP");
                                                stoppedTime =
                                                        Optional.of(new Date());
                                            }
                                        } else if (this.stoppedTime.isPresent()
                                                && (new Date()).getTime() - this.stoppedTime.get().getTime() >= 2000) {
                                            final CrilCommand command = this.strategy.nextCommand();
                                            command.toCril().ifPresent(this.communicationSupport::forward);
                                            stoppedTime = Optional.empty();
                                        }
                                    }));
        }
    }
}
