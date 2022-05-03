package io.wisoft.webapplication

import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.coRouter

@Component
class Router(
    private val grpcHandler: GrpcHandler,
    private val restHandler: RestHandler,
) {

    @Bean
    fun applicationRoutes(): RouterFunction<ServerResponse> =
        coRouter {
            GET("/grpc", grpcHandler::handle)
            GET("/rest", restHandler::handle)
        }

}