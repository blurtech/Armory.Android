package tech.blur.armory.presentation.rooms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.blur.armory.R
import tech.blur.armory.data.providers.ResourceProvider
import tech.blur.armory.databinding.FragmentRoomsBinding
import tech.blur.armory.presentation.common.BindingFragment
import tech.blur.armory.presentation.common.ErrorPopup


class RoomsFragment : BindingFragment<FragmentRoomsBinding>() {
    private val viewModel: RoomsViewModel by viewModel()
    private val resourceProvider: ResourceProvider by inject()


    lateinit var roomsAdapter: RoomsAdapter


    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRoomsBinding {
        return FragmentRoomsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (!::roomsAdapter.isInitialized) {
            roomsAdapter = RoomsAdapter(resourceProvider)
        }

        with(binding) {
            recyclerViewRooms.adapter = roomsAdapter
            fabRoomsFindRoom.setOnClickListener {
                findNavController().navigate(R.id.action_roomsFragment_to_findRoomFragment)
            }

            recyclerViewRooms.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0) {
                        fabRoomsFindRoom.hide()
                    } else {
                        fabRoomsFindRoom.show()
                    }
                    super.onScrolled(recyclerView, dx, dy)
                }
            })
        }

        viewModel.state.observe(viewLifecycleOwner) {
            roomsAdapter.setItems(it.rooms)
            it.onError.handle {
                it.printStackTrace()
                ErrorPopup.show(
                    requireContext(),
                    "Ошибка",
                    "При входе произошла ошибка, попробуйте еще раз",
                    it
                )
            }
        }
    }
}