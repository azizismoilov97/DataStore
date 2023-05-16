package com.example.datastore.presenter.screen.fragment

import admiral.group.irregularverbs.common.base.BaseFragment
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.datastore.R
import com.example.datastore.databinding.FragmentHomeBinding
import com.example.datastore.domain.entity.SortOrder
import com.example.datastore.presenter.screen.adapter.TasksAdapter
import com.example.datastore.presenter.vm.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val adapter = TasksAdapter()

    private val viewModel: HomeViewModel by viewModels()

    override fun getViewBinding(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    override fun setObserver() {

        viewModel.tasksUiModel.observe(this) { tasksUiModel ->
            adapter.submitList(tasksUiModel.tasks)
            updateSort(tasksUiModel.sortOrder)
            binding.showCompletedSwitch.isChecked = tasksUiModel.showCompleted
        }
    }

    override fun setupViews() {

        setupRecyclerView()
        setupFilterListeners(viewModel)
        setupSort()

    }

    private fun setupFilterListeners(viewModel: HomeViewModel) {
        binding.showCompletedSwitch.setOnCheckedChangeListener { _, checked ->
            viewModel.showCompletedTasks(checked)
        }
    }

    private fun setupRecyclerView() {
        // add dividers between RecyclerView's row items
        val decoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        binding.list.addItemDecoration(decoration)

        binding.list.adapter = adapter
    }

    private fun setupSort() {
        binding.sortDeadline.setOnCheckedChangeListener { _, checked ->
            viewModel.enableSortByDeadline(checked)
        }
        binding.sortPriority.setOnCheckedChangeListener { _, checked ->
            viewModel.enableSortByPriority(checked)
        }
    }

    private fun updateSort(sortOrder: SortOrder) {
        binding.sortDeadline.isChecked =
            sortOrder == SortOrder.BY_DEADLINE || sortOrder == SortOrder.BY_DEADLINE_AND_PRIORITY
        binding.sortPriority.isChecked =
            sortOrder == SortOrder.BY_PRIORITY || sortOrder == SortOrder.BY_DEADLINE_AND_PRIORITY
    }


}