package io.wisoft.webapplication

import io.grpc.ManagedChannel
import org.springframework.stereotype.Component
import proto.BatteryData
import proto.PredictServiceGrpcKt.PredictServiceCoroutineStub
import proto.Result
import java.io.Closeable
import java.util.concurrent.TimeUnit

@Component
class PredictService(
    private val grpcChannel: ManagedChannel,
): Closeable {

    private val stub: PredictServiceCoroutineStub = PredictServiceCoroutineStub(grpcChannel)

    suspend fun predict(data: BatteryData): Result {
        return stub.predict(data)
    }

    override fun close() {
        grpcChannel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
    }

}