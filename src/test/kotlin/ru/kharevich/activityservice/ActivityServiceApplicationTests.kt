package ru.kharevich.activityservice

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@Disabled("Disabled for CI - requires database configuration")
class ActivityServiceApplicationTests {

    @Test
    fun contextLoads() {
    }

}
