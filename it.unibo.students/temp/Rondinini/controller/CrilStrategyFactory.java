package it.unibo.students.Rondinini.controller;

import it.unibo.students.Rondinini.communication.*;
import java.util.Optional;
import java.util.Stack;

public class CrilStrategyFactory {
    private static int SUPPOSED_WIDTH = 50;

    public static CrilStrategy<CrilCommand, CrilResult, CrilEvent> createExplorationStrategy() {
        return new CrilStrategy<>() {
            private Optional<CrilResult> lastResult = Optional.empty();
            private int resets = 0;
            private int steps = 0;

            @Override
            public boolean hasNextCommand() {
                return true;
            }

            @Override
            public CrilCommand nextCommand() {
                if (resets + steps < SUPPOSED_WIDTH) {
                    steps = steps + 1;
                    return CrilCommand.FORWARD;
                } else {
                    steps = 1;
                    return CrilCommand.LEFT;
                }
            }



            //@Override
            public void setResult(CrilResult result) {
                lastResult = Optional.of(result);
            }

            //@Override
            public void setEvent(CrilEvent event) {
                // do nothing
            }

            @Override
            public void reset() {
                lastResult = Optional.empty();
                resets = resets + 1;
                steps = 0;
            }
        };
    }

    public static CrilStrategy<CrilCommand, CrilResult, CrilEvent> createCautiousExplorationStrategy(final CrilStrategy<CrilCommand, CrilResult, CrilEvent> explorationStrategy) {
        return new CrilStrategy<>() {
            private Optional<CrilResult> lastResult = Optional.empty();
            private Optional<CrilEvent> lastEvent = Optional.empty();
            private Stack<CrilCommand> explorationCommands = new Stack<>();
            private boolean isExploring = true;

            @Override
            public boolean hasNextCommand() {
                return (isExploring && explorationStrategy.hasNextCommand()) || (!explorationCommands.isEmpty() && !isExploring);
            }

            @Override
            public CrilCommand nextCommand() {
                if (!hasNextCommand()) {
                    throw new IllegalStateException("No command present");
                }
                if (isExploring
                        && ((lastResult.isPresent() && lastResult.get().equals(CrilResult.FALSE))
                        || (lastEvent.isPresent() && lastEvent.get().equals(CrilEvent.COLLISION))
                        || (lastEvent.isPresent() && lastEvent.get().equals(CrilEvent.SENSED)))
                ) {
                    explorationCommands.push(CrilCommand.LEFT);
                    explorationCommands.push(CrilCommand.LEFT);
                    isExploring = false;
                    if (lastEvent.isPresent() && lastEvent.get().equals(CrilEvent.SENSED)) {
                        return CrilCommand.ALARM;
                    }
                }
                lastResult = Optional.empty();
                lastEvent = Optional.empty();
                if (isExploring) {
                    return explorationCommands.push(explorationStrategy.nextCommand());
                } else {
                    final CrilCommand command = explorationCommands.pop();
                    return command.equals(CrilCommand.LEFT)
                            ? CrilCommand.RIGHT
                            : command;
                }
            }

            @Override
            public void setResult(CrilResult result) {
                lastResult = Optional.of(result);
                explorationStrategy.setResult(result);
            }

            @Override
            public void setEvent(CrilEvent event) {
                lastEvent = Optional.of(event);
                explorationStrategy.setEvent(event);
            }

            @Override
            public void reset() {
                lastResult = Optional.empty();
                lastEvent = Optional.empty();
                explorationCommands = new Stack<>();
                isExploring = true;
            }
        };
    }
}

