package com.beannote.beannoteapi.config

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.listeners.ProjectListener
import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.Spec
import org.testcontainers.containers.MongoDBContainer

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class UseMongoDBTestContainer

object MongoDBTestContainerManager {
    private var mongoDBContainer: MongoDBContainer? = null

    // 컨테이너 시작 및 URI 설정
    fun startContainer() {
        if (mongoDBContainer == null) {
            mongoDBContainer =
                MongoDBContainer("mongo:8.0.3").apply {
                    withReuse(true)
                    withExposedPorts(27017)
                    start()
                }
            System.setProperty("spring.data.mongodb.uri", mongoDBContainer!!.replicaSetUrl)
        }
    }

    // 컨테이너 정리
    fun stopContainer() {
        mongoDBContainer?.stop()
        mongoDBContainer = null
    }

    fun getMongoUri() = mongoDBContainer?.replicaSetUrl
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
