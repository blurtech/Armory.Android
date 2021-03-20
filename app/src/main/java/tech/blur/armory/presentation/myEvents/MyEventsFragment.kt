package tech.blur.armory.presentation.myEvents

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.blur.armory.R
import tech.blur.armory.databinding.FragmentMyeventsBinding
import tech.blur.armory.presentation.common.BindingFragment
import tech.blur.armory.presentation.common.ErrorPopup
import tech.blur.armory.presentation.common.dp
import tech.blur.armory.presentation.common.observeNonNull

class MyEventsFragment : BindingFragment<FragmentMyeventsBinding>() {
    private val viewModel: MyEventsViewModel by viewModel()

    private lateinit var eventsAdapter: MyEventsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (!::eventsAdapter.isInitialized) {
            eventsAdapter = MyEventsAdapter(
                onEventClicked = {
//                        findNavController().navigate(
//                            R.id.action_roomsFragment_to_roomDetailsFragment,
//                            RoomDetailsFragment.bundleArgs(it)
//                        )
                })
        }

        with(binding) {
            chipGroupMyEventsFilter.setOnCheckedChangeListener { group, checkedId ->
                when (checkedId) {
                    R.id.chip_myEvents_all -> viewModel.filterEvents(MyEventsViewModel.Filter.ALL)
                    R.id.chip_myEvents_mine -> viewModel.filterEvents(MyEventsViewModel.Filter.MINE)
                    R.id.chip_myEvents_invites -> viewModel.filterEvents(MyEventsViewModel.Filter.INVITES)
                }

                if (chipMyEventsAll.isChecked) animateElevation(
                    chipMyEventsAll,
                    chipMyEventsAll.elevation,
                    6f.dp
                )
                else {
                    animateElevation(chipMyEventsAll, chipMyEventsAll.elevation, 0f)
                }

                if (chipMyEventsMine.isChecked) animateElevation(
                    chipMyEventsMine,
                    chipMyEventsMine.elevation,
                    6f.dp
                )
                else {
                    animateElevation(chipMyEventsMine, chipMyEventsMine.elevation, 0f)
                }

                if (chipMyEventsInvites.isChecked) animateElevation(
                    chipMyEventsInvites,
                    chipMyEventsInvites.elevation,
                    6f.dp
                )
                else {
                    animateElevation(chipMyEventsInvites, chipMyEventsInvites.elevation, 0f)
                }
            }

            recyclerViewMyEvents.adapter = eventsAdapter
            recyclerViewMyEvents.overScrollMode = View.OVER_SCROLL_NEVER
            recyclerViewMyEvents.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if ((recyclerViewMyEvents.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition() == 0) {
                        if (linearLayoutMyEventsToolbar.elevation == 0f) return
                        animateElevation(linearLayoutMyEventsToolbar, 4f.dp, 0f)

                    } else {
                        if (linearLayoutMyEventsToolbar.elevation > 0f) return
                        linearLayoutMyEventsToolbar.elevation = 4f.dp
                    }
                }
            })
        }

        viewModel.state.observeNonNull(viewLifecycleOwner) { state ->
            if (state.myId != -1) {
                eventsAdapter.myId = state.myId
                eventsAdapter.setItems(state.events)

                binding.chipMyEventsAll.isEnabled = true
                binding.chipMyEventsMine.isEnabled = true
                binding.chipMyEventsInvites.isEnabled = true
            }

            state.onError.handle {
                it.printStackTrace()
                ErrorPopup.show(
                    requireContext(),
                    "Ошибка",
                    "При загрузке произошла ошибка",
                    it
                )
            }
        }
    }

    private fun animateElevation(view: View, from: Float, to: Float) {
        ValueAnimator.ofFloat(from, to).setDuration(250).apply {
            startDelay = 0
            addUpdateListener {
                view.elevation = it.animatedValue as Float
            }
            start()
        }
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentMyeventsBinding.inflate(inflater, container, false)
}