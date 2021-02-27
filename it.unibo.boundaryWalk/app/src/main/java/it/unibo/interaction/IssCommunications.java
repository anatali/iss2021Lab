/**
 IssCommunications.java
 ===============================================================
 Use the virtual robot with a protocol set with an annotation
 ===============================================================
 */
package it.unibo.interaction;

import java.lang.annotation.Annotation;

//Used to represent protocol-related information
class ProtocolInfo{
    IssProtocolSpec.issProtocol protocol;
    String url;
    public ProtocolInfo(IssProtocolSpec.issProtocol protocol, String url){
        this.protocol = protocol;
        this.url      = url;
    }
}

public class IssCommunications {
    //Factory Method
    public static IssOperations create( Object obj ){
        ProtocolInfo protocolInfo = getProtocol(  obj );
        switch( protocolInfo.protocol ){
            case HTTP  : {  return new IssHttpSupport( protocolInfo.url );  }
            case WS    : {  return new IssWsSupport( protocolInfo.url );    }
            default: return new IssHttpSupport( protocolInfo.url ); //TODO
        }
    }

//------------------------------------------------------------------------
//Java introspection
protected  static ProtocolInfo getProtocol(Object element ){
        Class<?> clazz = element.getClass();
        Annotation[] annotations = clazz.getAnnotations();
        for (Annotation annot : annotations) {
            if (annot instanceof IssProtocolSpec) {
                IssProtocolSpec info = (IssProtocolSpec) annot;
                ProtocolInfo protocolInfo = new ProtocolInfo( info.protocol(), info.url() );
                return  protocolInfo;
            }
        }
    return new ProtocolInfo( IssProtocolSpec.issProtocol.TCP, "unknown" );
}

}
