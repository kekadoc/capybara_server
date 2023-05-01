package com.kekadoc.project.capybara.server.di

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.serializersModuleOf
import org.koin.dsl.module
import java.util.*

private object UUIDSerializer : KSerializer<UUID> {
    override val descriptor = PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): UUID {
        return UUID.fromString(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: UUID) {
        encoder.encodeString(value.toString())
    }
}

val jsonModule = module {

    single<Json> {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            coerceInputValues = true

            this.serializersModule = serializersModuleOf(UUID::class, UUIDSerializer)
        }
    }

}