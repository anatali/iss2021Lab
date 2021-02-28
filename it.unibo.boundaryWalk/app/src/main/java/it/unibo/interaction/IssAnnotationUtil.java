package it.unibo.interaction;

import java.io.FileInputStream;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ProtocolInfo{
    IssProtocolSpec.issProtocol protocol;
    String url;
    public ProtocolInfo(IssProtocolSpec.issProtocol protocol, String url){
        this.protocol = protocol;
        this.url      = url;
    }

    public ProtocolInfo(String protocolName, String url){
        System.out.println("ProtocolInfo | protocolName =" + protocolName);
        //protocolName = protocolName.replace("\"","");
        switch( protocolName ){
            case "HTTP" : protocol=IssProtocolSpec.issProtocol.HTTP; break;
            case "WS"   : protocol=IssProtocolSpec.issProtocol.WS;   break;
            case "UDP"  : protocol=IssProtocolSpec.issProtocol.UDP;  break;
            case "TCP"  : protocol=IssProtocolSpec.issProtocol.TCP;  break;
            case "MQTT" : protocol=IssProtocolSpec.issProtocol.MQTT; break;
            case "COAP" : protocol=IssProtocolSpec.issProtocol.COAP; break;
            default     : protocol=null;
        }
        if( protocolName.equals("HTTP") ) this.protocol=IssProtocolSpec.issProtocol.HTTP;
        this.url = url;
    }
}

public class IssAnnotationUtil {

    public  static ProtocolInfo getProtocol(Object element ){
        Class<?> clazz            = element.getClass();
        Annotation[] annotations  = clazz.getAnnotations();
        ProtocolInfo protocolInfo = null;
        for (Annotation annot : annotations) {
            if (annot instanceof IssProtocolSpec) {
                IssProtocolSpec info = (IssProtocolSpec) annot;
                protocolInfo    = checkProtocolConfigFile(info);
                if( protocolInfo == null ) {
                    protocolInfo = new ProtocolInfo( info.protocol(), info.url() );
                }
                System.out.println("IssAnnotationUtil | getProtocol protocolInfo=" + protocolInfo );
             }
        }
        return  protocolInfo;
    }

    public static void getMoveTimes( Object obj, HashMap<String, Integer> mvtimeMap){
        Class<?> clazz = obj.getClass();
        Annotation[] annotations = clazz.getAnnotations();
        fillMap(mvtimeMap, annotations);
    }

    public static void fillMap(HashMap<String, Integer> mvtimeMap, Annotation[] annots) {
        for (Annotation annotation : annots) {
            if (annotation instanceof RobotMoveTimeSpec) {
                //Priority to the con
                RobotMoveTimeSpec info = (RobotMoveTimeSpec) annotation;
                if( ! checkRobotConfigFile(info, mvtimeMap) ) {
                    mvtimeMap.put("w", info.wtime());
                    mvtimeMap.put("s", info.stime());
                    mvtimeMap.put("l", info.ltime());
                    mvtimeMap.put("r", info.rtime());
                    mvtimeMap.put("h", info.htime());
                }
                System.out.println("IssAnnotationUtil | fillMap  ltime="  + info.ltime());
            }
        }
    }



    protected static ProtocolInfo checkProtocolConfigFile(IssProtocolSpec info) {
        try {
            String configFileName = info.configFile(); //default=IssProtocolConfig.txt
            //spec( protocol("HTTP"), url( "http://localHost:8090/api/move" ) ).
            System.out.println("IssAnnotationUtil | checkProtocolConfigFile configFileName=" + configFileName);
            FileInputStream fis = new FileInputStream(configFileName);
            Scanner sc = new Scanner(fis);
            String line = sc.nextLine();
            System.out.println("IssAnnotationUtil | line=" + line);
            String[] items = line.split(",");

            String protocol = getProtocolConfigInfo("protocol", items[0]);
            System.out.println("IssAnnotationUtil | protocol=" + protocol);

            String url = getProtocolConfigInfo("url", items[1]);
            System.out.println("IssAnnotationUtil | url=" + url);

            ProtocolInfo protinfo = new ProtocolInfo(protocol, url);
            return protinfo;
        } catch (Exception e) {
            System.out.println("IssAnnotationUtil | ERROR=" + e.getMessage());
            return null;
        }
    }


    //Quite bad: we will replace with Prolog parser
    protected static String getProtocolConfigInfo(String functor, String line){
        Pattern pattern = Pattern.compile(functor);
        Matcher matcher = pattern.matcher(line);
        String content = null;
        if(matcher.find()) {
            int end = matcher.end() ;
            content = line.substring( end, line.indexOf(")") )
                    .replace("\"","")
                    .replace("(","").trim();
            System.out.println("content = " + content);
         }
        return content;
    }

    protected static boolean checkRobotConfigFile(
            RobotMoveTimeSpec info, HashMap<String, Integer> mvtimeMap){
        try{
            String configFileName = info.configFile(); //default=IssRobotConfig.txt
            //spec( htime( 100 ),  ltime( 300 ), rtime( 300 ),  wtime( 600 ), wstime( 600 ) ).
            System.out.println("IssAnnotationUtil | checkRobotConfigFile configFileName=" + configFileName);
            FileInputStream fis = new FileInputStream(configFileName);
            Scanner sc = new Scanner(fis);
            String line = sc.nextLine();
            System.out.println("IssAnnotationUtil | line=" + line);
            String[] items = line.split(",");

            return false;
        } catch (Exception e) {
            System.out.println("IssAnnotationUtil | ERROR=" + e.getMessage());
            return false;
        }
    }

    protected static String getRobotConfigInfo(String functor, String line){
        Pattern pattern = Pattern.compile(functor);
        Matcher matcher = pattern.matcher(line);
        String content = null;
        if(matcher.find()) {
            int end = matcher.end() ;
            content = line.substring( end, line.indexOf(")") )
                    .replace("\"","")
                    .replace("(","").trim();
            System.out.println("content = " + content);
        }
        return content;
    }

}


    /*
    public static HashMap<String, Integer> getMoveTimes( Class element  ) {
        HashMap<String, Integer> mvtimeMap = new HashMap<String, Integer>();
        try {
            //Class element       = Class.forName("it.unibo.interaction.IssAnnotationUtil");
            Annotation[] annots = element.getAnnotations();
            fillMap(mvtimeMap, annots);
            return mvtimeMap;
        } catch (Exception exception) {
            System.out.println("IssAnnotationUtil | mvtimeMap ERROR:" + exception.getMessage());
            mvtimeMap.put("w", 600);
            mvtimeMap.put("s", 600);
            mvtimeMap.put("l", 300);
            mvtimeMap.put("r", 300);
            mvtimeMap.put("h", 100);
        }
        return mvtimeMap;
    }
*/