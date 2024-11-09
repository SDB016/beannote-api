package com.beannote.beannoteapi.config

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.listeners.ProjectListener
import org.testcontainers.containers.MongoDBContainer

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class UseMongoDBTestContainer

object MongoDBContainerExtension : ProjectListener {
    private val mongoDBContainer: MongoDBContainer by lazy {
        MongoDBContainer("mongo:8.0.3").apply {
            withReuse(true)
            withExposedPorts(27017)
            start()
        }
    }

    override suspend fun beforeProject() {
        // Testcontainer 시작시 MongoDB URI 속성 설정
        System.setProperty("spring.data.mongodb.uri", mongoDBContainer.replicaSetUrl)
    }

    override suspend fun afterProject() {
        // 프로젝트 종료 시 Testcontainer 정리
        mongoDBContainer.stop()
    }

    fun getMongoUri() = mongoDBContainer.replicaSetUrl

}


object ProjectConfig : AbstractProjectConfig() {
    override fun listeners() = listOf(MongoDBContainerExtension)
}
