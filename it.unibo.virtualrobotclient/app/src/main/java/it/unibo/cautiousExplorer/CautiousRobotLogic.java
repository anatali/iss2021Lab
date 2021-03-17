package it.unibo.cautiousExplorer;

import it.unibo.resumablebw.RobotMovesInfo;
import it.unibo.supports.IssCommSupport;

public class CautiousRobotLogic {
    private IssCommSupport rs ;
    private boolean usearil          = false;
    private int moveInterval         = 500;
    private RobotMovesInfo robotInfo;

    public CautiousRobotLogic(IssCommSupport support, boolean usearil, boolean doMap){
        rs           = support;
        this.usearil = usearil;
        robotInfo    = new RobotMovesInfo(doMap);
    }

    public void nextStep( String move, boolean obstacle, boolean robotHalted ){

    }

    public void backStep( String move, boolean obstacle, boolean robotHalted ){

    }

    }
