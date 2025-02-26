package com.example.taskstracker.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.taskstracker.R
import com.example.taskstracker.data.room.Task
import com.example.taskstracker.databinding.FragmentAddTaskBinding
import com.example.taskstracker.utils.UiStatus
import java.time.LocalDate
import java.util.Calendar

private const val TAG = "AddTaskFragment"

class AddTaskFragment : BaseFragment() {

    private val binding by lazy {
        FragmentAddTaskBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding.dateEt.setOnClickListener {
            showDatePickerDialog()
        }

        binding.addTaskBtn.setOnClickListener {
            val task = verifyValues()
            if (task != null) {
                viewModel.insertTask(task)
            } else {
                Toast.makeText(requireContext(), "Verify your information", Toast.LENGTH_LONG).show()
            }
        }

        viewModel.insertTask.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiStatus.ERROR -> {
                    Log.e(TAG, "onCreateView: ")
                    Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_LONG).show()
                }

                UiStatus.LOADING -> {}

                is UiStatus.SUCCESS -> {
                    viewModel.getTasks()
                    findNavController().navigate(R.id.action_add_task_to_tasks)
                }
            }
        }

        return binding.root
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = android.app.DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                binding.dateEt.setText(formattedDate)
            },
            year, month, day
        )

        datePickerDialog.show()
    }

    private fun verifyValues(): Task? {
        return try {
            with(binding) {
                val title = if (titleEt.text.isNotEmpty()) titleEt.text.toString() else throw Exception("Title is empty")
                val description =
                    if (descriptionEt.text.isNotEmpty()) descriptionEt.text.toString() else throw Exception("Description is empty")

                val dateText = dateEt.text.toString()
                if (dateText.isEmpty()) throw Exception("Date is empty")

                val dateParts = dateText.split("/")
                val day = dateParts[0].toInt()
                val month = dateParts[1].toInt()
                val year = dateParts[2].toInt()

                val date = LocalDate.of(year, month, day)

                Task(
                    title = title,
                    description = description,
                    dueDate = date
                )
            }
        } catch (e: Exception) {
            null
        }
    }
}
