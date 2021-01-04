%====================================================================================
% demo0 description   
%====================================================================================
system( demo0, msgdriven ).

context(ctxdemo0, "localhost",  "TCP", "8095").
 qactor( sender,   ctxdemo0, _).
 qactor( receiver, ctxdemo0, _).
 %% qactor( sender, ctxdemo0, "it.unibo.sender.sender").
 %% qactor( perceiver, ctxdemo0, "it.unibo.perceiver.perceiver").
