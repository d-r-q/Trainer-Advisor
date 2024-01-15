package pro.qyoga.tests.fixture.backgrounds

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import org.springframework.ui.ExtendedModelMap
import pro.qyoga.app.therapist.therapy.exercises.ExercisesListPageController
import pro.qyoga.core.therapy.exercises.api.ExercisesService
import pro.qyoga.core.therapy.exercises.api.dtos.CreateExerciseRequest
import pro.qyoga.core.therapy.exercises.api.dtos.ExerciseSearchDto
import pro.qyoga.core.therapy.exercises.api.dtos.ExerciseSummaryDto
import pro.qyoga.core.therapy.exercises.api.model.Exercise
import pro.qyoga.platform.file_storage.api.StoredFile
import pro.qyoga.tests.fixture.therapists.THE_THERAPIST_ID

@Component
class ExerciseBackgrounds(
    private val exercisesService: ExercisesService,
    private val exercisesListPageController: ExercisesListPageController
) {

    fun createExercises(
        exercises: List<CreateExerciseRequest>,
        images: Map<Int, StoredFile> = emptyMap()
    ): Iterable<Exercise> {
        return exercisesService.addExercises(exercises.map { it to images }, THE_THERAPIST_ID)
    }

    fun findExerciseSummary(exerciseSearchDto: ExerciseSearchDto): ExerciseSummaryDto? {
        val model = ExtendedModelMap()
        exercisesListPageController.getExercisesFiltered(exerciseSearchDto, Pageable.ofSize(2), model)
        val page = ExercisesListPageController.getExercises(model)
        check(page.content.size <= 1)
        return page.content.firstOrNull()
    }

    fun getExerciseStepImage(exerciseId: Long, stepIdx: Int): ByteArray? {
        return exercisesService.getStepImage(exerciseId, stepIdx)
            ?.inputStream
            .use {
                it?.readAllBytes()
            }
    }

    fun findExercise(exerciseId: Long): Exercise =
        exercisesService.findById(exerciseId)

}