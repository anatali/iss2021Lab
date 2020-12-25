%====================================================================================
% demo0 description   
%====================================================================================
system( demo0, msgdriven ).

context(ctxdemo0, "localhost",  "TCP", "8095").
 qactor( demo, ctxdemo0, "it.unibo.demo.demo").
 %% qactor( sender, ctxdemo0, "it.unibo.sender.sender").
 %% qactor( perceiver, ctxdemo0, "it.unibo.perceiver.perceiver").
