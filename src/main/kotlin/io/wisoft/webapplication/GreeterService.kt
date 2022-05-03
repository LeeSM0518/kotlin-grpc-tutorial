package io.wisoft.webapplication

import io.grpc.ManagedChannel
import org.springframework.stereotype.Component
import proto.GreeterGrpcKt.GreeterCoroutineStub
import proto.HelloReply
import proto.HelloRequest
import java.io.Closeable
import java.util.concurrent.TimeUnit

@Component
class GreeterService(
    private val grpcChannel: ManagedChannel,
) : Closeable {

    private val stub: GreeterCoroutineStub = GreeterCoroutineStub(grpcChannel)

//    suspend fun sayHello(request: HelloRequest): HelloReply {
//        val sayHello = stub.sayHello(request)
//        println(sayHello)
//        return sayHello
//    }

    override fun close() {
        grpcChannel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
    }

}