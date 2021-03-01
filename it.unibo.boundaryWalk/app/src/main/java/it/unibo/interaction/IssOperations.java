/**
 IssOperations.java
 ==========================================================================
 Defines high-level interaction operation
 These operations are designed with reference to message-passing
 rather than procedure-call.
 Thus, forward is just 'fire and forget', while
 request assumes that the called will execute a reply reltaed to that request

 requestSynch is introduced to help the transition to the new paradigm,

 ==========================================================================
 */
package it.unibo.interaction;

public interface IssOperations {
    void forward( String msg ) ;
    void request( String msg );
    void reply( String msg );
    String requestSynch( String msg );
}
