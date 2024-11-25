package com.beannote.beannoteapi

import com.beannote.beannoteapi.config.UseMongoDBTestContainer
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@UseMongoDBTestContainer
@ActiveProfiles("dev")
@SpringBootTest
class BeannoteApiApplicationTests {
    @Test
    fun contextLoads() {
    }
}
