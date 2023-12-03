package pro.qyoga.clients.pages.therapist.clients

import io.kotest.inspectors.forAny
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldMatch
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import pro.qyoga.assertions.PageMatcher
import pro.qyoga.assertions.shouldBeElement
import pro.qyoga.assertions.shouldHave
import pro.qyoga.core.clients.internal.Client
import pro.qyoga.infra.html.*
import pro.qyoga.infra.html.Input.Companion.text


object ClientsListPage : QYogaPage {

    override val path = "/therapist/clients"

    override val title = "Список клиентов"

    object ClientSearchForm : QYogaForm("clientSearch", FormAction.hxGet("$path/search")) {

        val lastName = text("lastName")
        val firstName = text("firstName")
        val middleName = text("middleName")
        val phoneNumber = text("phoneNumber")

        override val components: List<Input> = listOf(
            lastName,
            firstName,
            middleName,
            phoneNumber
        )

    }

    val updateAction = "$path/{id}"
    val updateActionPattern = updateAction.replace("{id}", "(\\d+)").toRegex()

    val deleteAction = "$path/delete/{id}"
    val deleteActionPattern = deleteAction.replace("{id}", "(\\d+)").toRegex()

    private val addLink = Link(CreateClientPage, "Добавить клиента")
    private val addButton = Button("addClient", "Добавить клиента")

    private const val CLIENT_ROW = "tbody tr"

    override fun match(element: Element) {
        element.select("title").text() shouldBe title

        element.getElementById(ClientSearchForm.id)!! shouldBeElement ClientSearchForm

        element shouldHave addLink
        element shouldHave addButton
    }

    fun clientId(document: Document, rowIdx: Int): Long {
        val deleteUrl = document.select(CLIENT_ROW)[rowIdx].select("td button").attr("hx-delete")
        val matcher = deleteActionPattern.matchEntire(deleteUrl)
        checkNotNull(matcher)
        return matcher.groups[1]!!.value.toLong()
    }

    fun clientRow(client: Client): PageMatcher = object : PageMatcher {
        override fun match(element: Element) {
            element.select(CLIENT_ROW).forAny { row ->
                row.select("td:nth-child(1)").text() shouldBe client.fullName()
                row.select("td a[name=update]").attr("href") shouldMatch updateActionPattern
                row.select("td button[name=delete]").attr("hx-delete") shouldMatch deleteActionPattern
            }
        }
    }

    fun clientRows(document: Document): Elements =
        document.select(CLIENT_ROW)

}