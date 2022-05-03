package io.wisoft.webapplication

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait

@Component
class RestHandler {

    private val webClient: WebClient = WebClient.create()

    suspend fun handle(request: ServerRequest): ServerResponse {
        webClient
            .get()
            .uri("http://localhost:8081/rest")
            .retrieve()
            .bodyToMono<String>()
            .awaitSingle()
        return ServerResponse.ok().bodyValueAndAwait("success REST")
    }

}