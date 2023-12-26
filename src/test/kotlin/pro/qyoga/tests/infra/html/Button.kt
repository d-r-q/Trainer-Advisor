package pro.qyoga.tests.infra.html

import io.kotest.matchers.shouldBe
import org.jsoup.nodes.Element
import org.jsoup.parser.Tag


data class Button(
    override val name: String,
    val text: String
) : Component {

    override fun selector(): String =
        buildString {
            append("button[name=$name]")
            if (text.isNotEmpty()) {
                append(":contains($text)")
            }
        }

    override fun match(element: Element) {
        element.tag() shouldBe Tag.valueOf("button")
        element.attr("name") shouldBe name
    }

}