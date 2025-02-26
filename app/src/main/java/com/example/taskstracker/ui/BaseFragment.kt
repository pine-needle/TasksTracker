package com.example.taskstracker.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.taskstracker.vm.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class BaseFragment : Fragment() {

    protected val viewModel by viewModels<LoginViewModel>()

}