package tech.blur.armory.presentation.rooms

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import tech.blur.armory.R
import tech.blur.armory.common.TrueTime
import tech.blur.armory.data.providers.ResourceProvider
import tech.blur.armory.databinding.ItemRoomsRoomBinding
import tech.blur.armory.databinding.ItemRoomsTitleBinding
import tech.blur.armory.domain.models.Room
import kotlin.math.roundToInt

class RoomsAdapter(
    private val resourceProvider: ResourceProvider,
    private val onRoomClicked: (Room) -> Unit
) :
    RecyclerView.Adapter<RoomsAdapter.BaseViewHolder>() {

    private var items: List<Room> = emptyList()

    fun setItems(newItems: List<Room>) {
        items = newItems
        notifyDataSetChanged() // todo replace with diff
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            TYPE_TITLE
        } else {
            TYPE_ROOM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        TYPE_TITLE -> {
            BaseViewHolder.TitleViewHolder(
                ItemRoomsTitleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
        TYPE_ROOM -> {
            BaseViewHolder.RoomViewHolder(
                resourceProvider,
                onRoomClicked,
                ItemRoomsRoomBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
        else -> error("Unexpected viewType: $viewType")
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        when (holder) {
            is BaseViewHolder.RoomViewHolder -> {
                holder.bind(items[position - 1])
            }
            is BaseViewHolder.TitleViewHolder -> {
            }
        }
    }

    override fun getItemCount(): Int = items.size + 1

    sealed class BaseViewHolder(protected val binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        class RoomViewHolder(
            private val resourceProvider: ResourceProvider,
            private val onRoomClicked: (Room) -> Unit,
            binding: ItemRoomsRoomBinding
        ) : BaseViewHolder(binding) {
            fun bind(room: Room) {
                with(binding as ItemRoomsRoomBinding) {
                    root.setOnClickListener {
                        onRoomClicked(room)
                    }
                    imageViewRoomLed.setImageResource(
                        if (room.led) R.drawable.ic_room_led else R.drawable.ic_room_led_disabled
                    )

                    imageViewRoomMic.setImageResource(
                        if (room.mic) R.drawable.ic_room_mic else R.drawable.ic_room_mic_disabled
                    )

                    imageViewRoomVideo.setImageResource(
                        if (room.video) R.drawable.ic_room_video else R.drawable.ic_room_video_disabled
                    )

                    imageViewRoomWifi.setImageResource(
                        if (room.wifi) R.drawable.ic_room_wifi else R.drawable.ic_room_wifi_disabled
                    )

                    val status = run {
                        val now = TrueTime.nowUtc()

                        val nearestEvent = room.nearEvents.sortedBy { it.startTime }.let { rooms ->
                            if (rooms.isEmpty()) {
                                null
                            } else {
                                rooms.first {
                                    it.startTime >= now || it.endTime >= now || (it.startTime <= now || it.endTime <= now)
                                }
                            }
                        }

                        when {
                            nearestEvent == null -> RoomStatus.Free
                            nearestEvent.startTime <= now && nearestEvent.endTime >= now -> RoomStatus.Busy
                            nearestEvent.startTime >= now && (nearestEvent.startTime - now).minutes < 90 -> {
                                RoomStatus.NearBusy("${(nearestEvent.startTime - now).minutes.roundToInt()} мин.")
                            }
                            else -> RoomStatus.Free
                        }
                    }

                    when (status) {
                        RoomStatus.Free -> {
                            viewRoomIndicator.setBackgroundColor(COLOR_FREE)
                            textViewRoomStatus.text = "Свободна"
                            textViewRoomStatus.setTextColor(COLOR_FREE)
                        }
                        is RoomStatus.NearBusy -> {
                            viewRoomIndicator.setBackgroundColor(COLOR_NEAR_BUSY)
                            textViewRoomStatus.text = "Бронь через ${status.time}"
                            textViewRoomStatus.setTextColor(COLOR_NEAR_BUSY)
                        }
                        RoomStatus.Busy -> {
                            viewRoomIndicator.setBackgroundColor(COLOR_BUSY)
                            textViewRoomStatus.text = "Занята"
                            textViewRoomStatus.setTextColor(COLOR_BUSY)
                        }
                    }

                    textViewRoomCapacity.text = "${room.capacity} чел."
                    textViewRoomSquare.text = "${room.square}"
                    textViewRoomFlour.text = "${room.flor}"
                    textViewRoomName.text = room.name
                }
            }
        }

        class TitleViewHolder(binding: ItemRoomsTitleBinding) : BaseViewHolder(binding)
    }

    sealed class RoomStatus {
        object Free : RoomStatus()
        object Busy : RoomStatus()
        data class NearBusy(val time: String) : RoomStatus()
    }

    companion object {
        private const val TYPE_TITLE = 0
        private const val TYPE_ROOM = 1

        private val COLOR_FREE: Int = Color.parseColor("#FF2BD451")
        private val COLOR_NEAR_BUSY: Int = Color.parseColor("#FFF9B300")
        private val COLOR_BUSY: Int = Color.parseColor("#FFD42B2B")
    }
}