package pro.qyoga.app.therapist.appointments.core.schedule

import org.springframework.web.servlet.ModelAndView
import pro.azhidkov.platform.spring.sdj.ergo.hydration.resolveOrThrow
import pro.qyoga.core.appointments.core.Appointment
import pro.qyoga.core.appointments.core.AppointmentStatus
import pro.qyoga.l10n.russianDayOfMonthLongFormat
import pro.qyoga.l10n.russianTimeFormat
import pro.qyoga.l10n.systemLocale
import java.time.*
import java.time.format.TextStyle


/**
 * Модель страницы календаря расписания приёмов.
 *
 * Состоит из:
 * * Контрола выбора дня в нативном календаре
 * * Сетки календаря с приёмами
 * * Контрла быстрого выбора дня
 *
 * Календарь состоит из трёх столбцов - выбранный день +/- 1 день
 * и пачки строк - от мин(7 утра, минимальный час начала приём) до макс(22, максимальный час конца приёма).
 * Поверх сетки отображаются карточки приёмов
 *
 * Контрол быстрого выбора дня состоит из строки с 7 днями - выбранный день +/- 3 дня
 *
 */
data class CalendarPageModel(
    val date: LocalDate,
    val currentUserTimeZone: ZoneId,
    val timeMarks: List<TimeMark>,
    val calendarDays: Collection<CalendarDay>,
    val appointmentId: Long?
) : ModelAndView("therapist/appointments/schedule.html") {

    init {
        addObject("date", date)
        addObject("timeMarks", timeMarks)
        addObject("calendarDays", calendarDays)
        addObject("selectedDayLabel", date.format(russianDayOfMonthLongFormat))
        addObject("focusAppointmentId", appointmentId)
    }

    companion object {

        fun of(date: LocalDate, currentUserTimeZone: ZoneId, appointments: Iterable<Appointment>, appointment: Long? = null): CalendarPageModel {
            val timeMarks = generateTimeMarks(appointments, date, currentUserTimeZone)
            val weekCalendar = generateDaysAround(date)
            return CalendarPageModel(date, currentUserTimeZone, timeMarks, weekCalendar, appointment)
        }

        private fun generateTimeMarks(
            appointments: Iterable<Appointment>,
            date: LocalDate,
            currentUserTimeZone: ZoneId
        ): List<TimeMark> {
            val minHour = (appointments.minOfOrNull { if (it.startsBeforeMidnight(date, currentUserTimeZone)) 0 else it.calendarDateTime(currentUserTimeZone).hour } ?: DEFAULT_START_HOUR)
                .coerceAtMost(DEFAULT_START_HOUR)
            val maxHour = (appointments.maxOfOrNull { if (it.endsAfterMidnight(currentUserTimeZone)) 23 else it.endCalendarDateTime(currentUserTimeZone).hour } ?: DEFAULT_END_HOUR)
                .coerceAtLeast(DEFAULT_END_HOUR)

            val timesMarks = generateSequence(LocalTime.of(minHour, 0)) {
                it + TimeMark.length
            }
                .take((maxHour - minHour + 1) * (TimeMark.marksPerHour))

            val days = generateSequence(date.minusDays(1)) {
                it.plusDays(1)
            }
                .take(3)

            val timeMarkRows = timesMarks.map { time ->
                val timeMarkAppointments = days
                    .map { day ->
                        val dateTimeAppointment = appointments.find { it.fallsIn(day.atTime(time), TimeMark.length, currentUserTimeZone) }
                        day to dateTimeAppointment?.let {
                            AppointmentCard(it, day, it.calendarDateTime(currentUserTimeZone), it.endCalendarDateTime(currentUserTimeZone))
                        }
                    }
                    .toList()

                TimeMark(time, timeMarkAppointments)
            }
                .toList()

            return timeMarkRows
        }

        private fun Appointment.calendarDateTime(currentUserTimeZone: ZoneId) =
                this.dateTime.atZone(currentUserTimeZone).toLocalDateTime()

        private fun Appointment.endCalendarDateTime(currentUserTimeZone: ZoneId) =
                (this.dateTime + this.duration).atZone(currentUserTimeZone).toLocalDateTime()

        private fun Appointment.fallsIn(wallClockDateTime: LocalDateTime, span: Duration, currentUserTimeZone: ZoneId) =
                (this.calendarDateTime(currentUserTimeZone) >= wallClockDateTime
                        && this.calendarDateTime(currentUserTimeZone) < wallClockDateTime + span)
                        || (wallClockDateTime.hour == 0 && wallClockDateTime.minute == 0
                        && this.calendarDateTime(currentUserTimeZone) < wallClockDateTime
                        && this.endCalendarDateTime(currentUserTimeZone) >= wallClockDateTime)

        private fun Appointment.startsBeforeMidnight(date: LocalDate, currentUserTimeZone: ZoneId) =
                this.calendarDateTime(currentUserTimeZone).dayOfMonth != this.endCalendarDateTime(currentUserTimeZone).dayOfMonth
                        && this.endCalendarDateTime(currentUserTimeZone).minute != 0
                        && this.calendarDateTime(currentUserTimeZone).dayOfMonth != date.plusDays(1).dayOfMonth

        private fun Appointment.endsAfterMidnight(currentUserTimeZone: ZoneId) =
                this.calendarDateTime(currentUserTimeZone).dayOfMonth != this.endCalendarDateTime(currentUserTimeZone).dayOfMonth

        private fun generateDaysAround(date: LocalDate) =

            generateSequence(date.minusDays(3)) {
                it.plusDays(1)
            }
                .map {
                    CalendarDay(
                        it,
                        it.dayOfWeek.getDisplayName(TextStyle.SHORT, systemLocale),
                        it.dayOfMonth,
                        it.dayOfWeek in setOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY),
                        it == date
                    )
                }
                .take(DAYS_IN_WEEK)
                .toList()

        const val DAYS_IN_CALENDAR = 3
        const val DAYS_IN_WEEK = 7
        const val DEFAULT_START_HOUR = 7
        const val DEFAULT_END_HOUR = 22

    }

}

/**
 * Строка календаря - время дня (+15 минут) и приёмы, начинающиеся в это время в
 * выбранные дни
 */
data class TimeMark(
    val time: LocalTime,
    val days: List<Pair<LocalDate, AppointmentCard?>>
) {

    companion object {
        val length: Duration = Duration.ofMinutes(15)
        val marksPerHour = Duration.ofHours(1).dividedBy(length).toInt()
    }

}

data class AppointmentCard(
    val id: Long,
    val period: String,
    val client: String,
    val type: String,
    val statusClass: String,
    val timeMarkOffsetPercent: Double,
    val timeMarkLengthPercent: Double,
) {

    constructor(app: Appointment, day: LocalDate, calendarDateTime: LocalDateTime, endCalendarDateTime: LocalDateTime) : this(
        app.id,
        calendarDateTime.format(russianTimeFormat) + " - " + endCalendarDateTime.format(russianTimeFormat),
        app.clientRef.resolveOrThrow().fullName(),
        app.typeRef.resolveOrThrow().name,
        appointmentStatusClasses.getValue(app.status),
        timeMarkOffsetPercent(day, calendarDateTime),
        timeMarkLengthPercent(app, day, calendarDateTime, endCalendarDateTime)
    )

    companion object {
        val appointmentStatusClasses = mapOf(
            AppointmentStatus.PENDING to "pending",
            AppointmentStatus.CLIENT_CAME to "client-came",
            AppointmentStatus.CLIENT_DO_NOT_CAME to "client-do-not-came",
        )

        private fun timeMarkOffsetPercent(day: LocalDate, calendarDateTime: LocalDateTime) =
                if (calendarDateTime.dayOfMonth == day.dayOfMonth)
                    (calendarDateTime.minute % 15) / 15.0 * 3
                else
                    0.0

        private fun timeMarkLengthPercent(app: Appointment, day: LocalDate, calendarDateTime: LocalDateTime, endCalendarDateTime: LocalDateTime) =
            if (calendarDateTime.dayOfMonth != endCalendarDateTime.dayOfMonth
                    && calendarDateTime.dayOfMonth == day.dayOfMonth)
                (24 * 60 * 60 - calendarDateTime.toLocalTime().toSecondOfDay()) / (60 * 5.0)
            else if (calendarDateTime.dayOfMonth != endCalendarDateTime.dayOfMonth
                    && endCalendarDateTime.dayOfMonth == day.dayOfMonth)
                endCalendarDateTime.toLocalTime().toSecondOfDay() / (60 * 5.0)
            else
                app.duration.toMinutes() / 5.0
    }

}

data class CalendarDay(
    val date: LocalDate,
    val label: String,
    val day: Int,
    val holiday: Boolean,
    val selected: Boolean
)

