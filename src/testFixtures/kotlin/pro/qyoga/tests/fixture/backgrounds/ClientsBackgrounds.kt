package pro.qyoga.tests.fixture.backgrounds

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import org.springframework.ui.ExtendedModelMap
import pro.qyoga.app.therapist.clients.ClientsListPageController
import pro.qyoga.core.clients.cards.api.Client
import pro.qyoga.core.clients.cards.api.ClientCardDto
import pro.qyoga.core.clients.cards.api.ClientsService
import pro.qyoga.tests.fixture.clients.ClientsObjectMother
import pro.qyoga.tests.fixture.therapists.THE_THERAPIST_ID
import pro.qyoga.tests.fixture.therapists.theTherapistUserDetails

@Component
class ClientsBackgrounds(
    private val clientsService: ClientsService,
    private val clientsListPageController: ClientsListPageController
) {

    fun createClients(clients: List<ClientCardDto>, therapistId: Long = THE_THERAPIST_ID): Iterable<Client> {
        return clientsService.createClients(therapistId, clients)
    }

    fun createClients(count: Int, therapistId: Long = THE_THERAPIST_ID): Iterable<Client> {
        return clientsService.createClients(therapistId, ClientsObjectMother.createClientCardDtos(count))
    }

    fun getAllClients(): Page<Client> {
        val model = ExtendedModelMap()
        clientsListPageController.getClients(theTherapistUserDetails, Pageable.ofSize(Int.MAX_VALUE), model)
        return ClientsListPageController.getClients(model)
    }

}