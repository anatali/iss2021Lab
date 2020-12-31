%====================================================================================
% demo1 description
%====================================================================================
system( demo1, msgbased ).

context(ctxdemo1, "localhost",  "TCP", "8095").
 qactor( demo1, ctxdemo1, "it.unibo.demo1.demo1").
 %% qactor( sender, ctxdemo1, "it.unibo.sender.sender").
 %% qactor( perceiver, ctxdemo1, "it.unibo.perceiver.perceiver").
