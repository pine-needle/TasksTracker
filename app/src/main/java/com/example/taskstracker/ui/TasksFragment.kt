package com.example.taskstracker.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskstracker.R
import com.example.taskstracker.databinding.FragmentTasksBinding
import com.example.taskstracker.utils.UiStatus

private const val TAG = "TasksFragment"
class TasksFragment : BaseFragment() {

    private val binding by lazy {
        FragmentTasksBinding.inflate(layoutInflater)
    }

    private val tasksAdapter by lazy {
        TasksAdapter(
            onCompleteTask = { task ->
                task.completed = true
                viewModel.insertTask(task)
            },
            onDeleteTask = {task ->
                viewModel.deleteTask(task)
            }
        )
    }

    //Request Permission Launcher
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){
        granted ->
        if (!granted){
            //show when user rejects permission request
            Toast.makeText(requireContext(), "Permission request denied", Toast.LENGTH_LONG).show()
        }
    }

    //onCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handlePermissions() //Handle permission
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding.tasksList.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = tasksAdapter
        }

        binding.logoutBtn.setOnClickListener {
            viewModel.logOut()
        }

        binding.addTask.setOnClickListener {
            findNavController().navigate(R.id.action_tasks_to_add_task)
        }

        updateTasks()
        viewModel.getTasks()
        handleLogOut()

        return binding.root
    }

    private fun updateTasks(){
        viewModel.tasksState.observe(viewLifecycleOwner){state ->
            when (state){
                is UiStatus.ERROR -> {
                    Log.e(TAG, "error: ${state.error.localizedMessage}", )
                    Toast.makeText(requireContext(),"Something went wrong",Toast.LENGTH_LONG).show()
                }
                UiStatus.LOADING -> {

                }
                is UiStatus.SUCCESS -> {
                    tasksAdapter.updateTasks(state.message)
                }
            }
        }
        viewModel.deleteTask.observe(viewLifecycleOwner){state ->
            when (state){
                is UiStatus.ERROR -> {
                    Log.e(TAG, "error: ${state.error.localizedMessage}", )
                    Toast.makeText(requireContext(),"Something went wrong",Toast.LENGTH_LONG).show()
                }
                UiStatus.LOADING -> {

                }
                is UiStatus.SUCCESS -> {
                    Toast.makeText(requireContext(),"Task deleted successfully",Toast.LENGTH_LONG).show()
                    viewModel.getTasks()
                }
            }
        }
        viewModel.insertTask.observe(viewLifecycleOwner){state ->
            when (state){
                is UiStatus.ERROR -> {
                    Log.e(TAG, "error: ${state.error.localizedMessage}", )
                    Toast.makeText(requireContext(),"Something went wrong",Toast.LENGTH_LONG).show()
                }
                UiStatus.LOADING -> {

                }
                is UiStatus.SUCCESS -> {
                    Toast.makeText(requireContext(),"Task updated successfully",Toast.LENGTH_LONG).show()
                    viewModel.getTasks()
                }
            }
        }
    }

    private fun handleLogOut(){
        viewModel.logoutState.observe(viewLifecycleOwner){state ->
            when(state){
                is UiStatus.ERROR -> {
                    Log.e(TAG, "error: ${state.error.localizedMessage}", )
                    Toast.makeText(requireContext(),"Something went wrong",Toast.LENGTH_LONG).show()
                }
                UiStatus.LOADING -> {

                }
                is UiStatus.SUCCESS -> {
                    findNavController().navigate(R.id.action_tasks_to_login)
                }
            }
        }
    }

    private fun handlePermissions()
    {
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
          val permissions =  arrayListOf(Manifest.permission.POST_NOTIFICATIONS)
           permissions.forEach{
               if(checkSelfPermission(requireContext(), it) != PackageManager.PERMISSION_GRANTED){
                   requestPermissionLauncher.launch(it)
               }
           }
        }
    }

}