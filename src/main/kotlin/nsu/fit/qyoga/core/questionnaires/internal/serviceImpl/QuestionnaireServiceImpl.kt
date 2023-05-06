package nsu.fit.qyoga.core.questionnaires.internal.serviceImpl

import nsu.fit.qyoga.core.questionnaires.api.dtos.*
import nsu.fit.qyoga.core.questionnaires.api.errors.QuestionnaireException
import nsu.fit.qyoga.core.questionnaires.api.model.Answer
import nsu.fit.qyoga.core.questionnaires.api.model.Decoding
import nsu.fit.qyoga.core.questionnaires.api.model.Question
import nsu.fit.qyoga.core.questionnaires.api.model.Questionnaire
import nsu.fit.qyoga.core.questionnaires.api.services.QuestionnaireService
import nsu.fit.qyoga.core.questionnaires.internal.repository.QuestionnaireJdbcTemplateRepo
import nsu.fit.qyoga.core.questionnaires.internal.repository.QuestionnaireRepo
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class QuestionnaireServiceImpl(
    private val questionnaireRepo: QuestionnaireRepo,
    private val questionnaireJdbcTemplateRepo: QuestionnaireJdbcTemplateRepo
) : QuestionnaireService {

    override fun findQuestionnaires(
        questionnaireSearchDto: QuestionnaireSearchDto,
        pageable: Pageable
    ): Page<QuestionnaireDto> {
        return questionnaireJdbcTemplateRepo.findQuestionnaireOnPage(
            title = questionnaireSearchDto.title ?: "",
            pageable = pageable
        )
    }

    override fun saveQuestionnaire(createQuestionnaireDto: CreateQuestionnaireDto): Long {
        return questionnaireRepo.save(
            Questionnaire(
                title = createQuestionnaireDto.title,
                question = createQuestionnaireDto.question.map { question -> questionDtoToQuestion(question) }.toSet(),
                decoding = createQuestionnaireDto.decoding.map { decoding -> decodingDtoToDecoding(decoding) }.toSet()
            )
        ).id
    }

    override fun findQuestionnaire(id: Long): QuestionnaireDto {
        val questionnaire = questionnaireRepo.findById(id).orElse(null)
            ?: throw QuestionnaireException("Выбранный опросник не найден")
        return QuestionnaireDto(questionnaire.id, questionnaire.title)
    }

    override fun findQuestionnaireWithQuestions(id: Long): CreateQuestionnaireDto {
        val questionnaire = questionnaireRepo.findById(id).orElse(null)
            ?: throw QuestionnaireException("Выбранный опросник не найден")
        val questionnaireDto = CreateQuestionnaireDto(
            id = questionnaire.id,
            title = questionnaire.title,
            question = mutableListOf(),
            decoding = mutableListOf()
        )
        questionnaireDto.question.addAll(
            questionnaire.question.map { question -> questionToQuestionDto(question) }
        )
        questionnaireDto.decoding.addAll(
            questionnaire.decoding.map { decoding -> decodingToDecodingDto(decoding) }
        )
        return questionnaireDto
    }

    fun questionDtoToQuestion(questionDto: CreateQuestionDto): Question {
        return Question(
            title = questionDto.title,
            questionType = questionDto.questionType,
            imageId = questionDto.imageId,
            answers = questionDto.answers.map { answer ->
                Answer(
                    title = answer.title,
                    lowerBound = answer.bounds.lowerBound,
                    lowerBoundText = answer.bounds.lowerBoundText,
                    upperBound = answer.bounds.upperBound,
                    upperBoundText = answer.bounds.upperBoundText,
                    score = answer.score,
                    imageId = answer.imageId
                )
            }.toMutableSet()
        )
    }

    fun questionToQuestionDto(question: Question): CreateQuestionDto {
        return CreateQuestionDto(
            question.id,
            question.title,
            question.questionType,
            question.imageId,
            question.answers.map { answer ->
                CreateAnswerDto(
                    answer.id,
                    answer.title,
                    AnswerBoundsDto(
                        lowerBound = answer.lowerBound,
                        lowerBoundText = answer.lowerBoundText,
                        upperBound = answer.upperBound,
                        upperBoundText = answer.upperBoundText,
                    ),
                    answer.score,
                    answer.imageId
                )
            }.toMutableList()
        )
    }

    fun decodingDtoToDecoding(decodingDto: DecodingDto): Decoding {
        return Decoding(
            lowerBound = decodingDto.lowerBound,
            upperBound = decodingDto.upperBound,
            result = decodingDto.result
        )
    }

    fun decodingToDecodingDto(decoding: Decoding): DecodingDto {
        return DecodingDto(
            decoding.id,
            decoding.lowerBound,
            decoding.upperBound,
            decoding.result
        )
    }
}