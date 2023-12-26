package pro.qyoga.tests.infra.html

import io.kotest.assertions.withClue
import io.kotest.inspectors.forAll
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.jsoup.nodes.Element
import org.jsoup.parser.Tag
import pro.qyoga.platform.kotlin.LabeledEnum

data class Option(
    val value: String,
    val title: String
) {

    companion object {

        fun of(enumValue: LabeledEnum): Option =
            Option(enumValue.name, enumValue.label)

    }

}

data class Select(
    override val name: String,
    override val required: Boolean,
    val options: List<Option>? = null
) : InputBase {

    override fun match(element: Element) {
        element.tag() shouldBe Tag.valueOf("select")
        element.attr("name") shouldBe name
        matchRequired(element)

        options?.forAll {
            withClue("Cannot find option $it") {
                element.select("option[value=${it.value}]:contains(${it.title})") shouldHaveSize 1
            }
        }
    }

    override fun value(element: Element): String =
        element.select("${selector()} option[selected]").`val`()

    override fun selector(): String = "select[name=$name]"

}