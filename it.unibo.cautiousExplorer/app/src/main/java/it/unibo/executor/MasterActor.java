package it.unibo.executor;

import it.unibo.cautiousExplorer.AbstractRobotActor;
import it.unibo.interaction.IJavaActor;
import mapRoomKotlin.mapUtil;
import org.json.JSONObject;

public class MasterActor extends AbstractRobotActor {

    private IJavaActor executor;
    private IJavaActor runaway;
    private int numSpiral   = 1;
    private boolean working = false;
    String curPathTodo = "";
    String path0       =  "wwlr";
    String path1       =  "wwlwwl";

    private String getSpiralPath( int n){
        String path  = "";
        String ahead = "";
        for( int i=1; i<=n; i++){
            ahead = ahead + "w";
        }
        path = ahead+"l"+ahead+"l"+ahead+"l"+ahead+"l";
        return path;
    }

    public MasterActor(String name) {
        super(name);
        executor = new ExecutorActor("executor", this);
    }

    @Override
    protected void msgDriven(JSONObject mJson) {
        if( mJson.has(ApplMsgs.activateId) && ! working){  //while working no more
            working = true;
            System.out.println(myname + " | mJson=" + mJson );
            //String pathTodo = mJson.getString(ApplMsgs.activateId);
            curPathTodo     = getSpiralPath(numSpiral);
            String msg      = ApplMsgs.executorstartMsg.replace("PATHTODO", curPathTodo);
            System.out.println(myname + " | msg=" + msg + " numSpiral=" + numSpiral );
            if( executor != null ) executor.send(msg);
        }//if not working, the message is lost. We could store it in a local queue
        else if( mJson.has(ApplMsgs.runawyEndId)){
            //Back to home
            System.out.println(myname + " | Back to home" +  " numSpiral=" + numSpiral );
            numSpiral++;
            curPathTodo = getSpiralPath(numSpiral);
            String msg  = ApplMsgs.executorstartMsg.replace("PATHTODO", curPathTodo);
            System.out.println(myname + " | msg=" + msg + " numSpiral=" + numSpiral);
            //this.waitUser();
            executor.send(msg);
        }
        else if( mJson.has(ApplMsgs.executorEndId)){
            System.out.println(myname + " | mJson=" + mJson );
            String result        = mJson.getString(ApplMsgs.executorEndId);
             System.out.println(myname + " | result of execution=" + result );
             if( result.equals("ok")){
                 numSpiral++;
                 curPathTodo = getSpiralPath(numSpiral);
                 String msg  = ApplMsgs.executorstartMsg.replace("PATHTODO", curPathTodo);
                 System.out.println(myname + " | msg=" + msg + " numSpiral=" + numSpiral);
                 //this.waitUser();
                 executor.send(msg);
             }
             else{
                 String pathStillTodo = curPathTodo.replaceFirst(result,"");
                 System.out.println(myname + " | pathStillTodo=" + pathStillTodo + " over:"+curPathTodo);
                 //working = false;
                 curPathTodo = reverse( result  ) +"ll"; //.replace("l","r")
                 String msg  = ApplMsgs.runawyStartMsg.replace("PATHTODO", curPathTodo);
                 System.out.println(myname + " | msg=" + msg + " numSpiral=" + numSpiral);
                 this.waitUser();
                 executor.send(msg);
             }
        }
    }//msgdriven

    protected String reverse( String s  ){
        if( s.length() <= 1 )  return s;
        else return reverse( s.substring(1) ) + s.charAt(0) ;
    }
}
