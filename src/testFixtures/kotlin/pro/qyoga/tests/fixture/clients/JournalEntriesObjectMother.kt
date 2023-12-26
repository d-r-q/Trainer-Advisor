package pro.qyoga.tests.fixture.clients

import pro.qyoga.app.therapist.clients.journal.edit_entry.edit.EditJournalEntryRequest
import pro.qyoga.tests.fixture.data.randomRecentLocalDate
import pro.qyoga.tests.fixture.data.randomSentence
import java.time.LocalDate


object JournalEntriesObjectMother {

    fun journalEntry(
        date: LocalDate = randomRecentLocalDate(),
        therapeuticTaskName: String = randomSentence(1, 3),
        text: String = randomSentence(1, 100)
    ) = EditJournalEntryRequest(date, therapeuticTaskName, text)

}