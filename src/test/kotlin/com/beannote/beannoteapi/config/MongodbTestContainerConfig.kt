package com.beannote.beannoteapi.config

import io.github.oshai.kotlinlogging.KotlinLogging
import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.listeners.ProjectListener
import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.Spec
import org.springframework.context.annotation.Import
import org.testcontainers.containers.MongoDBContainer

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Import(TestcontainersConfiguration::class)
annotation class UseMongoDBTestContainer

object MongoDBTestContainerManager {
    private var mongoDBContainer: MongoDBContainer? = null

    val logger = KotlinLogging.logger { }

    // 컨테이너 시작 및 URI 설정
    fun startContainer(): MongoDBContainer {
        if (mongoDBContainer == null) {
            mongoDBContainer =
                MongoDBContainer("mongo:8.0.3").apply {
                    withReuse(true)
                    withExposedPorts(27017)
                    start()
                }
            System.setProperty("spring.data.mongodb.uri", mongoDBContainer!!.replicaSetUrl)
            logger.info { "MongoDB TestContainer 시작, URI: ${getMongoUri()}" }
        } else {
            logger.info { "MongoDB TestContainer 이미 실행중. URI: ${getMongoUri()}" }
        }
        return mongoDBContainer as MongoDBContainer
    }

    // 컨테이너 정리
    fun stopContainer() {
        if (mongoDBContainer != null) {
            logger.info { "Stopping MongoDB TestContainer..." }
            mongoDBContainer?.stop()
            mongoDBContainer = null
        } else {
            logger.info { "종료할 MongoDB TestContainer가 없음." }
        }
    }

    fun getMongoUri(): String? = mongoDBContainer?.replicaSetUrl
}

object ConditionalMongoDBContainerListener : TestListener, ProjectListener {
    override suspend fun beforeSpec(spec: Spec) {
        // @UseMongoDBTestContainer 붙은 Spec에서만 testcontainer 시작
        if (spec::class.annotations.any { it is UseMongoDBTestContainer }) {
            MongoDBTestContainerManager.startContainer()
        }
    }

    override suspend fun afterProject() {
        MongoDBTestContainerManager.stopContainer()
    }
}

object ProjectConfig : AbstractProjectConfig() {
    override fun extensions() = listOf(ConditionalMongoDBContainerListener)
}
