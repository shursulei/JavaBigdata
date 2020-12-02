package com.shursulei.http;

import akka.actor.ActorSystem;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.IncomingConnection;
import akka.http.javadsl.ServerBinding;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.javadsl.Source;

import java.util.concurrent.CompletionStage;

public class Test {
    public static void main(String[] args) {
        //创建根节点
        ActorSystem system=ActorSystem.create("sys");
        //流式处理
//        Materializer mat= ActorMaterializer.create(system);
//        Source<IncomingConnection, CompletionStage<ServerBinding>> bindSource= (Source<IncomingConnection, CompletionStage<ServerBinding>>) Http.get(system).
//                bind(ConnectHttp.toHost("localhost",8089));
//        CompletionStage<ServerBinding> bindFuture=bindSource
    }
}
