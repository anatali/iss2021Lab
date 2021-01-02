%====================================================================================
% prova0 description   
%====================================================================================
context(ctxprova0, "localhost",  "TCP", "8095").
 qactor( prova0actor, ctxprova0, "it.unibo.prova0actor.Prova0actor").
  qactor( msgproducer, ctxprova0, "it.unibo.msgproducer.Msgproducer").
