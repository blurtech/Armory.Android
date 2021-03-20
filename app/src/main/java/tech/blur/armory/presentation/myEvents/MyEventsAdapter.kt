package tech.blur.armory.presentation.myEvents

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.soywiz.klock.DateFormat
import com.soywiz.klock.DateTime
import com.soywiz.klock.KlockLocale
import com.soywiz.klock.locale.russian
import tech.blur.armory.databinding.ItemMyeventsEventBinding
import tech.blur.armory.domain.models.Booking
import tech.blur.armory.presentation.common.dp
import kotlin.properties.Delegates

class MyEventsAdapter(
    private val onEventClicked: (Booking) -> Unit
) : RecyclerView.Adapter<MyEventsAdapter.EventViewHolder>() {

    private var items: List<Booking> = emptyList()
    var myId: Int by Delegates.notNull()

    fun setItems(newItems: List<Booking>) {
        if (items.isEmpty()) {
            items = newItems
            notifyDataSetChanged()
        } else {
            val diffResult = DiffUtil.calculateDiff(
                EventsDiffUtilCallback(items, newItems),
                false
            )

            items = newItems
            diffResult.dispatchUpdatesTo(this)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = EventViewHolder(
        onEventClicked,
        ItemMyeventsEventBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(items[position], myId)
    }

    override fun getItemCount(): Int = items.size

    class EventViewHolder(
        private val onEventClicked: (Booking) -> Unit,
        private val binding: ItemMyeventsEventBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Booking, myId: Int) {
            with(binding) {
                root.setOnClickListener {
                    onEventClicked(event)
                }

                textViewMyEventsEventName.text = "«Делаем вид, что работаем»" // todo event.name
                textViewMyEventsFlor.text = "Этаж ${event.meetingRoomBooking.flor}"
                textViewMyEventsRoomName.text = event.meetingRoomBooking.name

                val startLocal = event.startTime.toOffset(DateTime.nowLocal().offset)
                val endLocal = event.endTime.toOffset(DateTime.nowLocal().offset)

                textViewMyEventsDate.text =
                    startLocal.format(DateFormat("dd MMMM',' E").withLocale(KlockLocale.russian))
                textViewMyEventsTime.text =
                    "${startLocal.format("HH:mm")} - ${endLocal.format("HH:mm")}"

                if (event.owner.id == myId) {
                    viewMyEventsIndicator.setBackgroundColor(COLOR_MINE)
                    textViewMyEventsInvitedByTitle.isVisible = false
                    textViewMyEventsInvitedBy.text = "Моя комната"

                    ConstraintSet().apply {
                        clone(binding.constraintLayoutEvent)

                        connect(
                            textViewMyEventsInvitedBy.id,
                            ConstraintSet.START,
                            textViewMyEventsInvitedByTitle.id,
                            ConstraintSet.END,
                            16.dp
                        )
                    }
                } else {
                    viewMyEventsIndicator.setBackgroundColor(COLOR_INVITE)
                    textViewMyEventsInvitedByTitle.isVisible = true
                    textViewMyEventsInvitedBy.text =
                        "${event.owner.firstName} ${event.owner.lastName.first()}."

                    ConstraintSet().apply {
                        clone(binding.constraintLayoutEvent)

                        connect(
                            textViewMyEventsInvitedBy.id,
                            ConstraintSet.START,
                            textViewMyEventsInvitedByTitle.id,
                            ConstraintSet.END,
                            10.dp
                        )
                    }
                }
            }
        }
    }

    private class EventsDiffUtilCallback(
        private val oldList: List<Booking>,
        private val newList: List<Booking>
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList.getOrNull(oldItemPosition)?.id == newList.getOrNull(newItemPosition)?.id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList.getOrNull(oldItemPosition)
            val newItem = newList.getOrNull(newItemPosition)

            return oldItem == newItem
        }
    }


    companion object {
        private val COLOR_MINE: Int = Color.parseColor("#FF005ECC")
        private val COLOR_INVITE: Int = Color.parseColor("#FF9700CC")
    }
}