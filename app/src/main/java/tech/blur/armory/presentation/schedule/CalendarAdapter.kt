package tech.blur.armory.presentation.schedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.soywiz.klock.Date
import com.soywiz.klock.DateTime
import com.soywiz.klock.days
import com.soywiz.klock.plus
import tech.blur.armory.R
import tech.blur.armory.common.TrueTime
import tech.blur.armory.databinding.ItemDayBinding
import kotlin.properties.Delegates

class CalendarAdapter(
    private val onDateChanged: (DateTime) -> Unit,
    private val onDayClicked: (Day) -> Unit
) : RecyclerView.Adapter<CalendarAdapter.Holder>() {
    private val dayNames = listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс").map { Day(it) }
    private var days = emptyList<Day>()
    var currentDateTime: DateTime by Delegates.notNull()
        private set

    init {
        buildList(TrueTime.nowUtc())
    }

    fun buildList(dateTime: DateTime) {
        currentDateTime = dateTime
        val date = Date(dateTime.yearMonth, 1)
        val startDay = date.dayOfWeekInt

        days = dayNames + generateSequence { Day("") }.take((startDay - 1).let {
            if (it < 0) 6 else it
        })

        var offset = 0.0

        while (true) {
            days = days + Day((date + offset.days).day.toString())

            if (offset >= date.month.days(date.year) - 1) break

            offset++
        }

        notifyDataSetChanged()
        onDateChanged(currentDateTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            onDayClicked = { day ->
                days.forEach {
                    it.checked = it == day
                }
                onDayClicked(day)
                notifyDataSetChanged()
            },
            binding = ItemDayBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(days[position])
    }

    override fun getItemCount() = days.size

    class Holder(
        private val onDayClicked: (Day) -> Unit,
        private val binding: ItemDayBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(day: Day) {
            with(binding) {
                textViewDayDay.text = day.day

                val cardColor = if (day.checked) {
                    root.context.getColor(R.color.blue)
                } else {
                    root.context.getColor(R.color.white)
                }

                val textColor = if (day.checked) {
                    root.context.getColor(R.color.white)
                } else {
                    root.context.getColor(R.color.black)
                }

                cardViewDay.setCardBackgroundColor(cardColor)
                textViewDayDay.setTextColor(textColor)

                cardViewDay.setOnClickListener(null)

                if (day.day == "") {
                    cardViewDay.visibility = View.INVISIBLE
                    cardViewDay.visibility = View.INVISIBLE
                } else {
                    try {
                        day.day.toInt()
                        cardViewDay.visibility = View.VISIBLE
                        cardViewDay.setOnClickListener {
                            onDayClicked(day)
                        }
                    } catch (e: Exception) {
                        cardViewDay.visibility = View.INVISIBLE
                    }
                }
            }
        }
    }

    data class Day(val day: String, var checked: Boolean = false)
}