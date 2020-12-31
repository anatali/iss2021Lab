%====================================================================================
% demoqak description   
%====================================================================================
context(ctxdemooqak, "localhost",  "TCP", "8095").
 qactor( demoactor, ctxdemooqak, "it.unibo.demoactor.Demoactor").
  qactor( msgproducer, ctxdemooqak, "it.unibo.msgproducer.Msgproducer").
