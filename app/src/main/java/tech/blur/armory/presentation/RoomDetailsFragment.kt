package tech.blur.armory.presentation

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import tech.blur.armory.R
import tech.blur.armory.common.TrueTime
import tech.blur.armory.databinding.FragmentRoomdetailsBinding
import tech.blur.armory.domain.models.Room
import tech.blur.armory.presentation.common.BindingFragment
import tech.blur.armory.presentation.rooms.RoomsAdapter
import kotlin.math.roundToInt

class RoomDetailsFragment : BindingFragment<FragmentRoomdetailsBinding>() {
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentRoomdetailsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val room = getRoomFromArgs(requireArguments())

        with(binding) {
            Glide.with(this@RoomDetailsFragment)
                .load("https://www.d2k.ru/upload/iblock/10a/peregovorka.jpg")
                .into(imageViewRoomDetailsImage)

            imageButtonRoomDetailsBack.setOnClickListener {
                findNavController().popBackStack()
            }

            textViewRoomDetailsBack.setOnClickListener {
                findNavController().popBackStack()
            }

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
                    nearestEvent == null -> RoomsAdapter.RoomStatus.Free
                    nearestEvent.startTime <= now && nearestEvent.endTime >= now -> RoomsAdapter.RoomStatus.Busy
                    nearestEvent.startTime >= now && (nearestEvent.startTime - now).minutes < 90 -> {
                        RoomsAdapter.RoomStatus.NearBusy("${(nearestEvent.startTime - now).minutes.roundToInt()} мин.")
                    }
                    else -> RoomsAdapter.RoomStatus.Free
                }
            }

            when (status) {
                RoomsAdapter.RoomStatus.Free -> {
                    textViewRoomDetailsStatus.text = "Свободна"
                    textViewRoomDetailsStatus.setTextColor(COLOR_FREE)
                }
                is RoomsAdapter.RoomStatus.NearBusy -> {
                    textViewRoomDetailsStatus.text = "Бронь через ${status.time}"
                    textViewRoomDetailsStatus.setTextColor(COLOR_NEAR_BUSY)
                }
                RoomsAdapter.RoomStatus.Busy -> {
                    textViewRoomDetailsStatus.text = "Занята"
                    textViewRoomDetailsStatus.setTextColor(COLOR_BUSY)
                }
            }

            imageViewRoomDetailsLed.setImageResource(
                if (room.led) R.drawable.ic_room_led else R.drawable.ic_room_led_disabled
            )

            imageViewRoomDetailsMic.setImageResource(
                if (room.mic) R.drawable.ic_room_mic else R.drawable.ic_room_mic_disabled
            )

            imageViewRoomDetailsVideo.setImageResource(
                if (room.video) R.drawable.ic_room_video else R.drawable.ic_room_video_disabled
            )

            imageViewRoomDetailsWifi.setImageResource(
                if (room.wifi) R.drawable.ic_room_wifi else R.drawable.ic_room_wifi_disabled
            )

            textViewRoomDetailsFlor.text = "${room.flor}"
            textViewRoomDetailsSquare.text = "${room.square}"
            textViewRoomDetailsCapacity.text = "${room.capacity} чел."

            textViewRoomDetailsSchedule.setOnClickListener {

            }

            imageButtonRoomDetailsSchedule.setOnClickListener {

            }

            buttonRoomDetailsBook.setOnClickListener {

            }
        }
    }

    companion object {
        private const val ARG_ROOM = "room"

        private val COLOR_FREE: Int = Color.parseColor("#FF2BD451")
        private val COLOR_NEAR_BUSY: Int = Color.parseColor("#FFF9B300")
        private val COLOR_BUSY: Int = Color.parseColor("#FFD42B2B")

        fun getRoomFromArgs(arguments: Bundle) = arguments.getSerializable(ARG_ROOM) as Room

        fun bundleArgs(room: Room) = bundleOf(
            ARG_ROOM to room
        )
    }
}