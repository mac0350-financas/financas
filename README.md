# Finature
Finature é o seu sistema de gerenciamento de finanças e investimentos. Utiliza Javascript no front-end, com o framework React+Vite, e Kotlin no back-end, com o framework Ktor. 

## Configurando o front-end com React + Vite

1. Acesse o diretório do projeto com `cd`.
2. Dentro do repositório, crie a estrutura do front-end com Vite e React.
  ```
  npm create vite@latest client --template react
  ```
3. Entre do diretório `client` criado dentro do diretório do projeto e instale as dependências.
  ```
  cd client
  npm install
  ```
4. Inicie o servidor de desenvolvimento do front-end
  ```
  npm run dev
  ```
O front-end estará rodando em [localhost:3000](http://localhost:3000)

## Configurando o back-end com Ktor

1. Acesse o diretório do projeto com `cd`.
2. Dentro do diretório, crie uma pasta para o back-end, por exemplo `server` e acesse esse novo diretório com `cd`.
```
mkdir server
cd server
```
3. Crie o projeto Kotlin com o template Ktor Server no IntelliJ IDEA, ou configure manualmente o projeto com o Gradle. Se estiver usando IntelliJ IDEA, crie um projeto Kotlin com Ktor dentro do diretório `server`, e adicione as dependências do Ktor no arquivo `build.gradle.kts`. Se estiver configurando manualmente, adicione o seguinte no mesmo arquivo `build.gradle.kts`:
```
plugins {
    kotlin("jvm") version "1.8.21"
    application
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-netty:2.3.0")
    implementation("io.ktor:ktor-server-core:2.3.0")
    implementation("io.ktor:ktor-server-auth:2.3.0")
    implementation("io.ktor:ktor-server-sessions:2.3.0")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.0")
    testImplementation("io.ktor:ktor-server-tests:2.3.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:1.8.21")
}
```
4. No arquivo `Application.kt`, adicione uma rota simples:
```
package com.myapp

import io.ktor.application.*
import io.ktor.features.ContentNegotiation
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.serialization.kotlinx.json
import io.ktor.features.StatusPages

fun main() {
    embeddedServer(Netty, port = 8080) {
        install(ContentNegotiation) {
            json()
        }

        routing {
            get("/") {
                call.respond(HttpStatusCode.OK, "Bem-vindo ao Finature API!")
            }
        }
    }.start(wait = true)
}
```
5. Rode o back-end usando o comando `./gradlew run` dentro do diretório `server`. O servidor Ktor estará rodando em [localhost:8080](http://localhost:8080)
