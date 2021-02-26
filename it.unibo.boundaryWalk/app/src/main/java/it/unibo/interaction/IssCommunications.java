package it.unibo.interaction;

import java.lang.annotation.Annotation;

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
    public IssOperations create( Object obj){
        ProtocolInfo protocolInfo = getProtocol(  obj );
        switch( protocolInfo.protocol ){
            case HTTP  : {
                return new IssHttpSupport( protocolInfo.url );
            }
            case WS  : {
                return new IssWsSupport( protocolInfo.url );
            }
            default: return new IssHttpSupport( protocolInfo.url ); //TODO
        }
    }

//------------------------------------------------------------------------
protected  ProtocolInfo getProtocol(Object element ){
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
/*
    protected  ProtocolInfo  getProtocol(Object object)   {
        //println("initializeObject object=" + object);
        Class<?> clazz = object.getClass();
        for (Method method : clazz.getDeclaredMethods()) {
            //System.out.println("initializeObject method=" + method);
            if (method.isAnnotationPresent(IssProtocolSpec.class)) {
                IssProtocolSpec info = (IssProtocolSpec) method.getAnnotation(IssProtocolSpec.class);
                //String url =  (String) method.getDeclaredAnnotations()[1];
                return  new ProtocolInfo( info.protocol(), info.url() ) ;
            }
        };
        return new ProtocolInfo( IssProtocolSpec.issProtocol.TCP, "unknown" );
    }
*/
}
