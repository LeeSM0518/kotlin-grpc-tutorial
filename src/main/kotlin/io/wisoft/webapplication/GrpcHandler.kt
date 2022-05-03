package io.wisoft.webapplication

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import proto.BatteryData
import proto.HelloRequest

@Component
class GrpcHandler(
    private val service: PredictService,
) {

//    suspend fun handle(request: ServerRequest): ServerResponse {
//        val dto = HelloRequest.newBuilder().setName("sangmin").build()
//        service.sayHello(dto)
//        return ServerResponse.ok().bodyValueAndAwait("success gRPC")
//    }

    suspend fun handle(request: ServerRequest): ServerResponse {
        val dto = BatteryData
            .newBuilder()
            .setTemperature(31.2)
            .setCurrent(12.3)
            .setVoltage(35.2)
            .build()
        service.predict(dto)
        return ServerResponse.ok().bodyValueAndAwait("success predict")
    }

}