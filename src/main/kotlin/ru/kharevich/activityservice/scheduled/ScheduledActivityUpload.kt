package ru.kharevich.activityservice.scheduled

import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import ru.kharevich.activityservice.kafka.ActivityMessageProducer
import ru.kharevich.activityservice.service.ActivityService

@Component
@RequiredArgsConstructor
class ScheduledActivityUpload (
    private val activityProducer: ActivityMessageProducer,
    private var activityService: ActivityService
) {

    @Scheduled(fixedRateString = "\${app.scheduling.frequency_of_actions_uploading_in_millis}")
        fun uploadActivity() {
        //TODO: изменить upload

    }
}