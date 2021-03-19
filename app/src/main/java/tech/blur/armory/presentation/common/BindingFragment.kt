package tech.blur.armory.presentation.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BindingFragment<Binding : ViewBinding> : Fragment() {
    private var _binding: Binding? = null

    /** This property is only valid between onCreateView and
     * onDestroyView.
     * */
    protected val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateBinding(inflater, container)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    abstract fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): Binding
}