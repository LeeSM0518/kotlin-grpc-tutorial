package io.wisoft.webapplication

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GrpcConfiguration {

    @Bean
    fun grpcChannel(): ManagedChannel =
        ManagedChannelBuilder
            .forAddress("localhost", 50051)
            .usePlaintext()
            .build()

}