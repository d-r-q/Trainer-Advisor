package pro.qyoga.infra.test_config

import pro.qyoga.app.therapist.TherapistWebAppConfig
import pro.qyoga.core.clients.ClientsConfig
import pro.qyoga.core.programs.exercises.ExercisesConfig
import pro.qyoga.fixture.ClientsBackgrounds
import pro.qyoga.infra.files.FilesConfig


object ClientsTestConfig {

    private val filesConfig by lazy { FilesConfig(SdjTestConfig.sdjConfig) }

    private val clientsConfig by lazy { ClientsConfig(SdjTestConfig.sdjConfig) }

    private val exercisesConfig by lazy { ExercisesConfig(SdjTestConfig.sdjConfig, filesConfig) }

    private val therapistWebAppConfig by lazy { TherapistWebAppConfig(clientsConfig, exercisesConfig) }

    val clientListPageController by lazy { therapistWebAppConfig.clientsListPageController() }

    val clientsBackgrounds by lazy { ClientsBackgrounds(clientsConfig.clientsService()) }

}