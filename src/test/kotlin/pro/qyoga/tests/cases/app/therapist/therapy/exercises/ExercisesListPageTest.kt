package pro.qyoga.tests.cases.app.therapist.therapy.exercises

import io.kotest.inspectors.forAll
import io.kotest.matchers.collections.shouldHaveSize
import org.junit.jupiter.api.Test
import pro.qyoga.core.therapy.exercises.api.CreateExerciseRequest
import pro.qyoga.core.therapy.exercises.api.ExerciseSearchDto
import pro.qyoga.core.therapy.exercises.api.ExerciseType
import pro.qyoga.tests.assertions.shouldBe
import pro.qyoga.tests.assertions.shouldHave
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.clients.pages.therapist.therapy.exercises.ExercisesListPage
import pro.qyoga.tests.fixture.therapy.therapeutic_tasks.ExercisesObjectMother.createExerciseRequest
import pro.qyoga.tests.fixture.therapy.therapeutic_tasks.ExercisesObjectMother.createExerciseRequests
import pro.qyoga.tests.fixture.therapy.therapeutic_tasks.toDto
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest


class ExercisesListPageTest : QYogaAppIntegrationBaseTest() {

    @Test
    fun `Exercises list page should be correctly rendered when there are no exercises`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.exercises.getExercisesListPage()

        // Then
        document shouldBe ExercisesListPage
        ExercisesListPage.exercisesRows(document) shouldHaveSize 0
    }

    @Test
    fun `Exercises list page should render 10 rows when enough clients exists`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()
        val pageSize = 10
        val exercises = createExerciseRequests(pageSize + 1)
        val firstPage = exercises.sortedBy { it.title.lowercase() }
            .take(pageSize)
            .map(CreateExerciseRequest::toDto)
        backgrounds.exercises.createExercises(exercises)


        // When
        val document = therapist.exercises.getExercisesListPage()

        // Then
        document shouldBe ExercisesListPage
        ExercisesListPage.exercisesRows(document) shouldHaveSize pageSize
        firstPage.forAll {
            document shouldHave ExercisesListPage.exerciseRow(it)
        }
    }

    @Test
    fun `When user submits search from, response should contain only rows matching query`() {
        // Given
        val title = "Планка"
        val exerciseType = ExerciseType.STRENGTHENING
        val fullMatch1 = createExerciseRequest(title, exerciseType = exerciseType)
        val fullMatch2 = createExerciseRequest(title, exerciseType = exerciseType)
        val nonMatchByTitle = createExerciseRequest("Стойка на руках", exerciseType = exerciseType)
        val nonMatchByType = createExerciseRequest(title, exerciseType = ExerciseType.WARM_UP)

        backgrounds.exercises.createExercises(listOf(fullMatch1, fullMatch2, nonMatchByTitle, nonMatchByType))

        val searchForm = ExerciseSearchDto(title, exerciseType)

        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.exercises.searchExercises(searchForm)

        // Then
        ExercisesListPage.exercisesRows(document) shouldHaveSize 2
        document shouldHave ExercisesListPage.exerciseRow(fullMatch1.toDto())
        document shouldHave ExercisesListPage.exerciseRow(fullMatch2.toDto())
    }

}