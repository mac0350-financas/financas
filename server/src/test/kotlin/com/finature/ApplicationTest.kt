package com.finature

import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {

    @Test
    fun testRoot() = testApplication {
        application {
            module()
        }
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }

    @Test
    fun testRotaInexistente() = testApplication {
        application {
            module()
        }
        client.get("/nao-existe").apply {
            assertEquals(HttpStatusCode.NotFound, status)
        }
    }

}
