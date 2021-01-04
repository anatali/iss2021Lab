%====================================================================================
% demo0 description   
%====================================================================================
system( demo0, msgdriven ).

context(ctxdemo0, "localhost",  "TCP", "8085").
 qactor( sender,   ctxdemo0, _).
 qactor( receiver, ctxdemo0, _).

