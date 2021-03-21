package tech.blur.armory.presentation.findRoom

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.navigation.fragment.findNavController
import com.soywiz.klock.*
import com.soywiz.klock.locale.russian
import tech.blur.armory.R
import tech.blur.armory.common.TrueTime
import tech.blur.armory.databinding.FragmentFindroomBinding
import tech.blur.armory.presentation.common.BindingFragment
import tech.blur.armory.presentation.rooms.Filter
import tech.blur.armory.presentation.rooms.RoomsFragment

class FindRoomFragment : BindingFragment<FragmentFindroomBinding>() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            imageButtonFindRoomBack.setOnClickListener {
                findNavController().popBackStack()
            }

            textViewFindRoomBack.setOnClickListener {
                findNavController().popBackStack()
            }

            var date: Date
            var dateTime: DateTimeTz

            TrueTime.nowLocal().let { now ->
                date = Date(now.yearInt, now.month0, now.dayOfMonth)
                dateTime = now

                textViewFindRoomTime.text = now.format("HH:mm")
                textViewFindRoomDate.text =
                    now.format(DateFormat("dd MMM").withLocale(KlockLocale.russian))

                cardViewFindRoomDate.setOnClickListener {
                    DatePickerDialog(requireContext()).apply {
                        setOnDateSetListener { _, year, month, dayOfMonth ->
                            date = Date(year, month + 1, dayOfMonth)

                            textViewFindRoomDate.text =
                                date.format(DateFormat("dd MMM").withLocale(KlockLocale.russian))
                        }

                        datePicker.minDate = now.utc.unixMillisLong
                        datePicker.updateDate(
                            date.year,
                            date.month.index0,
                            date.day
                        )
                    }.show()
                }

                cardViewFindRoomTime.setOnClickListener {
                    TimePickerDialog(
                        requireContext(),
                        { _, hourOfDay, minutes ->
                            dateTime = DateTimeTz.local(
                                DateTime(date, Time(hourOfDay, minutes)),
                                now.offset
                            )
                            textViewFindRoomTime.text = dateTime.format("HH:mm")
                        },
                        dateTime.hours,
                        dateTime.minutes,
                        true
                    ).show()
                }
            }

            imageButtonFindRoomDecreaseCapacity.isEnabled = false

            imageButtonFindRoomDecreaseCapacity.setOnClickListener {
                handleCapacityChange(binding, CapacityChange.DECREASE)
            }

            imageButtonFindRoomIncreaseCapacity.setOnClickListener {
                handleCapacityChange(binding, CapacityChange.INCREASE)
            }

            var led = false
            imageButtonFindRoomLed.setOnClickListener {
                if (led) {
                    imageButtonFindRoomLed.setImageResource(R.drawable.ic_room_led_disabled)
                } else {
                    imageButtonFindRoomLed.setImageResource(R.drawable.ic_room_led)
                }

                led = !led
            }

            var mic = false
            imageButtonFindRoomMic.setOnClickListener {
                if (mic) {
                    imageButtonFindRoomMic.setImageResource(R.drawable.ic_room_mic_disabled)
                } else {
                    imageButtonFindRoomMic.setImageResource(R.drawable.ic_room_mic)
                }

                mic = !mic
            }

            var video = false
            imageButtonFindRoomVideo.setOnClickListener {
                if (video) {
                    imageButtonFindRoomVideo.setImageResource(R.drawable.ic_room_video_disabled)
                } else {
                    imageButtonFindRoomVideo.setImageResource(R.drawable.ic_room_video)
                }

                video = !video
            }

            var wifi = false
            imageButtonFindRoomWifi.setOnClickListener {
                if (wifi) {
                    imageButtonFindRoomWifi.setImageResource(R.drawable.ic_room_wifi_disabled)
                } else {
                    imageButtonFindRoomWifi.setImageResource(R.drawable.ic_room_wifi)
                }

                wifi = !wifi
            }

            var square = 0

            with(seekBarFindRoomSquare) {
                this.max = 64

                setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        seekBar: SeekBar?,
                        progress: Int,
                        fromUser: Boolean
                    ) {
                        textViewFindRoomSquare.text = if (progress == 0) {
                            "Любая"
                        } else {
                            progress.toString()
                        }

                        square = progress
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit

                    override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
                })
            }

            buttonFindRoomShowRooms.setOnClickListener {
                findNavController().navigate(
                    R.id.action_findRoomFragment_to_roomsFragment, RoomsFragment.buildArgs(
                        Filter(
                            led = led,
                            mic = mic,
                            video = video,
                            wifi = wifi,
                            capacity = binding.textViewFindRoomCapacity.text!!.toString().toInt(),
                            square = square
                        )
                    )
                )
            }
        }
    }

    private fun handleCapacityChange(
        binding: FragmentFindroomBinding,
        capacityChange: CapacityChange
    ) {
        with(binding) {
            when (capacityChange) {
                CapacityChange.DECREASE -> {
                    textViewFindRoomCapacity.text =
                        (textViewFindRoomCapacity.text.toString().toInt() - 1).toString()
                }
                CapacityChange.INCREASE -> {
                    textViewFindRoomCapacity.text =
                        (textViewFindRoomCapacity.text.toString().toInt() + 1).toString()
                }
            }

            imageButtonFindRoomDecreaseCapacity.isEnabled =
                textViewFindRoomCapacity.text.toString().toInt() > 4

            imageButtonFindRoomIncreaseCapacity.isEnabled =
                textViewFindRoomCapacity.text.toString().toInt() < 50
        }
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentFindroomBinding.inflate(inflater, container, false)

    enum class CapacityChange {
        DECREASE,
        INCREASE
    }
}