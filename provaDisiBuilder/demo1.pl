%====================================================================================
% demo1 description
%====================================================================================
system( demo1, fsm  ).

context(ctxdemo1, "localhost",  "TCP", "8095").
qactor( sender,   ctxdemo1, "it.unibo.sender.sender").
qactor( receiver, ctxdemo1, "it.unibo.receiver.receiver").
