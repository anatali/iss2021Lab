/**
 IssOperations.java
 ===============================================================
 Defines high-level interaction operation

 ===============================================================
 */
package it.unibo.interaction;

public interface IssOperations {
    void forward( String msg ) ;
    String request( String msg );
}
