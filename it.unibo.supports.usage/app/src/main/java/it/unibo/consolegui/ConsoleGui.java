/**
 * ConsoleGui
 * @author AN - DISI - Unibo
===============================================================
The user hoits a button and a message with the same name is
sent to the WEnv by using WEnvConnSupportNoChannel.sendMessage
===============================================================
 */
package it.unibo.consolegui;
import it.unibo.resumableWalker.RobotApplActorInputController;
import it.unibo.supports2021.ActorBasicJava;
import java.util.Observable;
import java.util.Observer;

public class ConsoleGui implements  Observer{	//Observer deprecated in 11 WHY?
private String[] buttonLabels  = new String[]  { "STOP", "RESUME" };
private ActorBasicJava controller ;

	public ConsoleGui(ActorBasicJava controller) {
		GuiUtils.showSystemInfo();
		ButtonAsGui concreteButton = ButtonAsGui.createButtons( "", buttonLabels );
		concreteButton.addObserver( this );
		this.controller = controller;
 	}

	public void update( Observable o , Object arg ) {	//Observable deprecated WHY?
		String move = arg.toString();
		//System.out.println("GUI input move=" + move);
		String robotCmd = (move == "STOP") ? "{\"robotcmd\":\"STOP\" }" : "{\"robotcmd\":\"RESUME\" }";
		//System.out.println("GUI input robotCmd=" + robotCmd );
		//controller.handleInfo( robotCmd );
		controller.send(robotCmd);
	}
	
	public static void main( String[] args) {
		new ConsoleGui(  new RobotApplActorInputController(null, true,true));
	}
}

