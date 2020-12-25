/*
 PrefixedStrings
*/
package disi.builder

object builtin{
		
/*
 SYSTEM RULES (sysRules.pl")
*/
val sysRules ="""
%==============================================
% CONTEXT HANDLING UTILTY
%==============================================
getCtxNames(CTXNAMES) :-
	findall( NAME, context( NAME, _, _, _ ), CTXNAMES).
getCtxPortNames(PORTNAMES) :-
	findall( PORT, context( _, _, _, PORT ), PORTNAMES).
getTheContexts(CTXS) :-
	findall( context( CTX, HOST, PROTOCOL, PORT ), context( CTX, HOST, PROTOCOL, PORT ), CTXS).
getTheActors(ACTORS) :-
	findall( qactor( A, CTX, CLASS ), qactor( A, CTX, CLASS ), ACTORS).


getOtherContexts(OTHERCTXS, MYSELF) :-
	findall( 
		context( CTX, HOST, PROTOCOL, PORT ), 
		(context( CTX, HOST, PROTOCOL, PORT ), CTX \== MYSELF), 	 
		OTHERCTXS
	).
getOtherContextNames(OTHERCTXS, MYSELF) :-
	findall(
		CTX,
		(context( CTX, HOST, PROTOCOL, PORT ), CTX \== MYSELF),
		OTHERCTXS
	).
	
getTheActors(ACTORS,CTX) :-
	findall( qactor( A, CTX, CLASS ), qactor( A, CTX, CLASS ),   ACTORS).
getActorNames(ACTORS,CTX) :-
    findall( NAME, qactor( NAME, CTX, CLASS ),   ACTORS).

getCtxHost( NAME, HOST )  :- context( NAME, HOST, PROTOCOL, PORT ).
getCtxPort( NAME,  PORT ) :- context( NAME, HOST, PROTOCOL, PORT ).
getCtxProtocol( NAME,  PROTOCOL ) :- context( NAME, HOST, PROTOCOL, PORT ).

getMsgId( msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM ) , MSGID  ).
getMsgType( msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM ) , MSGTYPE ).
getMsgSenderId( msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM ) , SENDER ).
getMsgReceiverId( msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM ) , RECEIVER ).
getMsgContent( msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM ) , CONTENT ).
getMsgNum( msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM ) , SEQNUM ).

checkMsg( MSG, MSGLIST, PLANLIST, RES ) :- 
	%%stdout <- println( checkMsg( MSG, MSGLIST, PLANLIST, RES ) ),
	checkTheMsg(MSG, MSGLIST, PLANLIST, RES).	
checkTheMsg( MSG, [], _, dummyPlan ).
checkTheMsg( msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM ), [ MSGID | _ ], [ PLAN | _ ], PLAN ):-!.
checkTheMsg( MSG, [_|R], [_|P], RES ):- 
	%%stdout <- println( checkMsg( MSG, R, P, RES ) ),
	checkTheMsg(MSG, R, P, RES).

msgContentToPlan( MSG, CONTENTLIST,PLANLIST,RES ):-
	%stdout <- println( msgContentToPlan( MSG, CONTENTLIST,PLANLIST,RES) ),
	msgContentToAPlan( MSG, CONTENTLIST,PLANLIST,RES ).
msgContentToAPlan( MSG, [], _, dummyPlan ).
msgContentToAPlan( msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM ), [ CONTENT | _ ], [ PLAN | _ ], PLAN ):-!.
msgContentToAPlan( event(  CONTENT  ), [ CONTENT | _ ], [ PLAN | _ ], PLAN ):-!.
msgContentToAPlan( MSG, [_|R], [_|P], RES ):- 
	%stdout <- println( msgContentToAPlan( MSG, R, P, RES ) ),
	msgContentToPlan(MSG, R, P, RES).	

removeCtx( CtxName, HOST, PORT ) :-
	%% stdout <- println( removeCtx(  CtxName ) ),
	retract( context( CtxName, HOST, _ , PORT ) ),!,
	removeAllActors( CtxName ).
	 
removeAllActors( CtxName ) :-
	retract( qactor( NAME, CtxName, _ ) ),
	removeAllActors( CtxName ).
removeAllActors( CtxName ).  

showSystemConfiguration :-
	stdout <- println("&&&&&&&&&&&&&&&&&&SysRules&&&&&&&&&&&&&&&&&&&&"),
  	getTheContexts(CTXS),
	stdout <- println('CONTEXTS IN THE SYSTEM:'),
	showElements(CTXS),
	stdout <- println('ACTORS   IN THE SYSTEM:'),
	getTheActors(ACTORS),
	showElements(ACTORS),
	stdout <- println("&&&&&&&&&&&&&&&&&&SysRules&&&&&&&&&&&&&&&&&&&&").
 
showElements(ElementListString):- 
	text_term( ElementListString, ElementList ),
 	showListOfElements(ElementList).
showListOfElements([]).
showListOfElements([C|R]):-
	stdout <- println( C ),
	showElements(R).

unify(A,B)    :-  A = B.

assign( I,V ) :-  retract( value(I,_) ),!, assert( value( I,V )).
assign( I,V ) :-  assert( value( I,V )).
getVal( I, V ):-  value(I,V), !.
getVal( I, fail ).
inc(I,K,N):- value( I,V ), N is V + K, assign( I,N ).
dec(I,K,N):- value( I,V ), N is V - K, assign( I,N ).

addRule( Rule ):-
	%%output( addRule( Rule ) ),
	assert( Rule ).
removeRule( Rule ):-
	retract( Rule ),
	%%output( removedFact(Rule) ),
	!.
removeRule( A  ):- 
	%%output( remove(A) ),
	retract( A :- B ),!.
removeRule( _  ).

replaceRule( Rule, NewRule ):-
	removeRule( Rule ),addRule( NewRule ).

%==============================================
% MEMENTO
%==============================================
%%% :- stdout <- println( hello ).
%%% --------------------------------------------------
% context( NAME, HOST, PROTOCOL, PORT )
% PROTOCOL : "TCP" | "UDP" | "SERIAL" | "PROCESS" | ...
%%% --------------------------------------------------

%%% --------------------------------------------------
% msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM )
% MSGTYPE : dispatch request answer
%%% --------------------------------------------------
"""

/*
 GRADLE RULES (build.gradle")
*/
fun genGradleRules( sysName: String ) : String{
val gradleRules ="""
/*
================================================================================
build.gradle
GENERATED ONLY ONCE
USAGE:	 
  	gradle  -b build.gradle eclipse		     //to set the dependency on the library
	gradle  -b build.gradle distZip
	gradle  -b build.gradle -q tasks --all
	gradle  -b build.gradle cleanDistDir	//to clean the src directory
================================================================================
*/
plugins {
    id 'java'
    id 'eclipse'
    id 'application'
    id 'org.jetbrains.kotlin.jvm' version '1.3.71'
}

version '1.0'

sourceCompatibility = 1.8

repositories {
    mavenCentral()
    jcenter()
    flatDir {   dirs '../unibolibs'	 }
}

dependencies {
    implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk8"
    testCompile group: 'junit', name: 'junit', version: '4.12'
//COROUTINE
    compile group: 'org.jetbrains.kotlinx', name: 'kotlinx-coroutines-core-common', version: '1.1.0'
    compile group: 'org.jetbrains.kotlinx', name: 'kotlinx-coroutines-core', version: '1.1.0'
//ARDUINO
    // https://mvnrepository.com/artifact/org.scream3r/jssc
    compile group: 'org.scream3r', name: 'jssc', version: '2.8.0'
//MQTT
// https://mvnrepository.com/artifact/org.eclipse.paho/org.eclipse.paho.client.mqttv3
    compile group: 'org.eclipse.paho', name: 'org.eclipse.paho.client.mqttv3', version: '1.2.1'
//JSON
    // https://mvnrepository.com/artifact/org.json/json
    compile group: 'org.json', name: 'json', version: '20160810'
//COAP
	// https://mvnrepository.com/artifact/org.eclipse.californium/californium-core
	compile group: 'org.eclipse.californium', name: 'californium-core', version: '2.0.0-M12'
	// https://mvnrepository.com/artifact/org.eclipse.californium/californium-proxy
	compile group: 'org.eclipse.californium', name: 'californium-proxy', version: '2.0.0-M12'
//LOG4j	
	compile group: 'org.slf4j', name: 'slf4j-log4j12', version: '1.7.25' 
//MONGO
	// https://mvnrepository.com/artifact/org.mongodb/mongo-java-driver
	//compile group: 'org.mongodb', name: 'mongo-java-driver', version: '3.8.2'

//CUSTOM
    compile name: 'uniboInterfaces'
    compile name: '2p301'
    compile name: 'it.unibo.qakactor-2.4'
 
    //For p2p two-way connections (TCP, UDP, BTH, Serial ...)
    compile name: 'unibonoawtsupports'

	//BLS
	//compile name: 'it.unibo.bls19Local-1.0'
	//RADAR
	compile name: 'radarPojo'
	//RADAR GUI
	// https://mvnrepository.com/artifact/org.pushingpixels/trident
	compile group: 'org.pushingpixels', name: 'trident', version: '1.3'
	//ROBOT nano (SAM)
    //compile name: 'labbaseRobotSam'
	//compile mame: 'pi4j-core' //2-12 SNAPSHOT non in gradle
 	//PLANNER
    //compile name: 'it.unibo.planner'
 	// https://mvnrepository.com/artifact/com.googlecode.aima-java/aima-core
	compile group: 'com.googlecode.aima-java', name: 'aima-core', version: '3.0.0'
}

compileKotlin {
    kotlinOptions.jvmTarget = "1.8"
}
compileTestKotlin {
    kotlinOptions.jvmTarget = "1.8"
}

sourceSets {
    main.java.srcDirs += 'src'
    main.java.srcDirs += 'resources'
    test.java.srcDirs += 'test'		//test is specific
}
 
 
/*
mainClassName = 'it.unibo.${sysName}Kt'

jar {
    println("executing jar")
    from sourceSets.main.allSource
    manifest {
        attributes 'Main-Class': "it.unibo.${sysName}Kt"
    }
}
*/
 
"""
	return gradleRules
}
 

}//object builtin
	