package pro.qyoga.app.therapist.therapy.exercises

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import pro.azhidkov.platform.spring.http.hxRedirect
import pro.azhidkov.platform.spring.mvc.modelAndView
import pro.qyoga.app.platform.ResponseEntityExt
import pro.qyoga.app.platform.notFound
import pro.qyoga.app.therapist.therapy.exercises.EditExercisePageController.ExerciseStepProcessor.processSteps
import pro.qyoga.core.therapy.exercises.ExercisesService
import pro.qyoga.core.therapy.exercises.dtos.ExerciseSummaryDto
import pro.qyoga.core.therapy.exercises.model.ExerciseStep


@Controller
@RequestMapping("/therapist/exercises/{exerciseId}")
class EditExercisePageController(
    private val exercisesService: ExercisesService
) {

    @GetMapping
    fun getEditExercisePage(@PathVariable exerciseId: Long): ModelAndView {
        val exercise = exercisesService.findById(exerciseId)
            ?: return notFound

        return modelAndView("therapist/therapy/exercises/exercise-create") {
            "exercise" bindTo exercise
        }
    }


    object ExerciseStepProcessor {
        const val NO_IMAGE = "/img/no-image.png"
        fun processSteps(
            steps: List<ExerciseStep>?,
            exerciseId: Long
        ): List<ProcessingExerciseStep> {
            return if (steps.isNullOrEmpty()) {
                listOf(ProcessingExerciseStep("", NO_IMAGE))
            } else {
                steps.mapIndexed { idx, step ->
                    val imageUrl =
                        step.imageId?.let { "/therapist/exercises/$exerciseId/step-images/$idx" }
                            ?: NO_IMAGE
                    ProcessingExerciseStep(description = step.description, imageUrl = imageUrl)
                }
            }
        }
    }

    @GetMapping("/modal")
    fun getEditExercisePageModal(@PathVariable exerciseId: Long): ModelAndView {
        val exercise = exercisesService.findById(exerciseId)
            ?: return notFound

        val processedSteps = processSteps(
            exercise.steps,
            exerciseId
        )

        return modelAndView("therapist/therapy/exercises/exercise-modal") {
            "steps" bindTo processedSteps
            "exercise" bindTo exercise
        }
    }

    data class ProcessingExerciseStep(
        var description: String,
        var imageUrl: String = "/img/no-image.png",
        var fileName: String? = null
    )


    @PutMapping
    fun editExercise(
        @PathVariable exerciseId: Long,
        @ModelAttribute exerciseSummaryDto: ExerciseSummaryDto,
    ): ResponseEntity<Unit> {

        exercisesService.updateExercise(exerciseId, exerciseSummaryDto)

        return hxRedirect("/therapist/exercises")
    }

    @GetMapping("/step-images/{stepIdx}")
    fun getStepImage(
        @PathVariable exerciseId: Long,
        @PathVariable stepIdx: Int
    ): Any {
        val imageStream = exercisesService.getStepImage(exerciseId, stepIdx)
            ?: return notFound

        return ResponseEntityExt.ok(imageStream)
    }

}