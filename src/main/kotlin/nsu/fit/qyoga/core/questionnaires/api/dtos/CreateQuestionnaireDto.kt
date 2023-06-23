package nsu.fit.qyoga.core.questionnaires.api.dtos

data class CreateQuestionnaireDto(
    val id: Long = 0,
    val title: String = "",
    val question: MutableList<CreateQuestionDto> = mutableListOf(),
    val decoding: MutableList<DecodingDto> = mutableListOf()
)
