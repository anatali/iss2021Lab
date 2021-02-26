package it.unibo.interaction;

public interface IssOperations {
    public void forward( String msg ) throws Exception;
    public String request( String msg );
    public void doRequest( String msg );
    public void doReply( String msg );

}
