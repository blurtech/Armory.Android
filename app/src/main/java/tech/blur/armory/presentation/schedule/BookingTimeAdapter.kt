package tech.blur.armory.presentation.schedule

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tech.blur.armory.databinding.ItemSheduleBookingBinding

class BookingTimeAdapter(private val items: List<String>) :
    RecyclerView.Adapter<BookingTimeAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            ItemSheduleBookingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    class Holder(private val binding: ItemSheduleBookingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(booking: String) {
            with(binding) {
                if (booking.isEmpty()) {
                    textViewBooking.text = "Комната свободна весь день"
                    textViewBooking.setTextColor(Color.parseColor("#2BD451"))
                } else {
                    textViewBooking.text = booking
                    textViewBooking.setTextColor(Color.parseColor("#D42B2B"))
                }
            }
        }
    }

}