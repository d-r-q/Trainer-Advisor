package pro.qyoga.tests.fixture.backgrounds

import org.springframework.beans.factory.support.GenericBeanDefinition
import org.springframework.context.support.GenericApplicationContext
import org.springframework.stereotype.Component
import pro.qyoga.core.therapy.exercises.api.ExercisesService
import pro.qyoga.core.therapy.exercises.internal.ExercisesRepo
import pro.qyoga.core.therapy.exercises.internal.ExercisesServiceImpl
import pro.qyoga.platform.file_storage.api.FilesStorage
import pro.qyoga.tests.platform.spring.context.getBean
import java.util.concurrent.ConcurrentHashMap


@Component
class SpringBackgrounds(
    private val context: GenericApplicationContext
) {

    private val beansCache = ConcurrentHashMap<String, Any>()

    fun createExercisesService(
        exercisesRepo: ExercisesRepo? = null,
        exerciseStepsImagesStorage: FilesStorage? = null
    ): ExercisesService {
        val key = "mockExerciseService($exerciseStepsImagesStorage, $exercisesRepo)"
        val bean = beansCache.getOrPut(key) {
            val bd = GenericBeanDefinition().apply {
                setBeanClass(ExercisesService::class.java)
                setInstanceSupplier {
                    ExercisesServiceImpl(
                        exercisesRepo ?: context.getBean(),
                        exerciseStepsImagesStorage ?: context.getBean(
                            "exerciseStepsImagesStorage",
                            FilesStorage::class.java
                        ),
                        context.getBean()
                    )
                }
            }
            context.registerBeanDefinition(key, bd)
            context.getBean(key, ExercisesService::class.java)
        }

        return bean as ExercisesService
    }

}