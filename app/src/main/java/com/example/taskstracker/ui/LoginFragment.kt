package com.example.taskstracker.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.taskstracker.R
import com.example.taskstracker.databinding.FragmentLoginBinding
import com.example.taskstracker.utils.UiStatus
class LoginFragment : BaseFragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.loginButton.setOnClickListener {
            viewModel.authenticate(requireContext())
        }

        viewModel.loginState.observe(viewLifecycleOwner) {
            when (it) {
                is UiStatus.ERROR -> {
                    Toast.makeText(requireContext(), "Login failed", Toast.LENGTH_LONG).show()
                }
                UiStatus.LOADING -> {}
                is UiStatus.SUCCESS -> {
                    Toast.makeText(requireContext(), "Login succeed", Toast.LENGTH_LONG).show()
                    findNavController().navigate(R.id.action_login_to_tasks)
                }
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // Prevent memory leaks
    }
}
