package pro.qyoga.app.therapist.therapy.therapeutic_tasks

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import pro.qyoga.core.therapy.therapeutic_tasks.api.TherapeuticTask
import pro.qyoga.core.therapy.therapeutic_tasks.internal.TherapeuticTasksRepo
import pro.qyoga.platform.spring.mvc.modelAndView
import pro.qyoga.platform.spring.sdj.withSortBy

private val therapeuticTaskListPage = Pageable.ofSize(10)
    .withSortBy(TherapeuticTask::name)

@Controller
@RequestMapping("/therapist/therapeutic-tasks/list")
class TherapeuticTasksListsPageController(
    private val therapeuticTasksRepo: TherapeuticTasksRepo
) {

    @GetMapping
    fun getTherapeuticTasksList(): ModelAndView {
        return modelAndView("therapist/therapy/therapeutic-tasks/therapeutic-tasks-list") {
            "tasks" bindTo therapeuticTasksRepo.findByNameContaining(null, therapeuticTaskListPage)
        }
    }

}