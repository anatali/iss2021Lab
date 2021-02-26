package it.unibo.interaction;

public interface IssOperations {
    public void forward( String msg ) throws Exception;
    public String request( String msg );

}
