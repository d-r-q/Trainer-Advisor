package nsu.fit.qyoga.core.exercises.api

import nsu.fit.platform.web.pages.Page
import nsu.fit.qyoga.core.exercises.api.dtos.ExerciseDto
import nsu.fit.qyoga.core.exercises.api.model.ExerciseType
import org.springframework.data.domain.Pageable

interface ExercisesService {
    fun getExercises(
        title: String?,
        contradiction: String?,
        duration: String?,
        exerciseType: ExerciseType?,
        therapeuticPurpose: String?,
        pageable: Pageable
    ): Page<ExerciseDto>

    fun getExerciseTypes(): List<ExerciseType>
}