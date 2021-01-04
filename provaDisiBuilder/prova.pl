%====================================================================================
% prova description   
%====================================================================================
context(ctxprova, "localhost",  "TCP", "8095").
 qactor( provaactor, ctxprova, "it.unibo.provaactor.Provaactor").
  qactor( msgproducer, ctxprova, "it.unibo.msgproducer.Msgproducer").
