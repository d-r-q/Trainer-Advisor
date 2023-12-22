package pro.qyoga.tests.fixture.backgrounds

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.stereotype.Component
import pro.qyoga.app.therapist.clients.journal.create_entry.CreateJournalEntryRequest
import pro.qyoga.app.therapist.clients.journal.create_entry.CreateJournalEntryWorkflow
import pro.qyoga.core.clients.journals.api.JournalEntry
import pro.qyoga.core.clients.journals.api.JournalPageRequest
import pro.qyoga.core.clients.journals.api.JournalsService
import pro.qyoga.core.clients.journals.api.hydrate
import pro.qyoga.core.therapy.therapeutic_tasks.api.TherapeuticTasksService
import pro.qyoga.core.users.internal.QyogaUserDetails
import pro.qyoga.tests.fixture.clients.JournalEntriesObjectMother
import pro.qyoga.tests.fixture.data.randomRecentLocalDate


@Component
class ClientJournalBackgrounds(
    private val createJournalEntryWorkflow: CreateJournalEntryWorkflow,
    private val journalsService: JournalsService,
    private val therapeuticTasksService: TherapeuticTasksService
) {

    fun createJournalEntry(
        clientId: Long,
        createJournalEntryRequest: CreateJournalEntryRequest,
        therapist: QyogaUserDetails
    ): JournalEntry {
        return createJournalEntryWorkflow.createJournalEntry(clientId, createJournalEntryRequest, therapist)
    }

    fun createEntries(clientId: Long, therapist: QyogaUserDetails, count: Int): List<JournalEntry> {
        val uniqueDates = generateSequence { randomRecentLocalDate() }
            .distinct()
            .asIterable()
        return (1..count).zip(uniqueDates).map { (_, date) ->
            createJournalEntry(clientId, JournalEntriesObjectMother.journalEntry(date = date), therapist)
        }
    }

    fun hydrate(journalEntries: List<JournalEntry>): List<JournalEntry> =
        journalEntries.hydrate(therapeuticTasksService::findAllById)

    fun getWholeJournal(clientId: Long): Page<JournalEntry> {
        var journal = journalsService.getJournalPage(JournalPageRequest.wholeJournal(clientId))

        journal = PageImpl(
            journal.content.hydrate(therapeuticTasksService::findAllById),
            journal.pageable,
            journal.totalElements
        )

        return journal
    }

}