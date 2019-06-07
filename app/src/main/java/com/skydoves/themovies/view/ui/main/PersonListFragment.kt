package com.skydoves.themovies.view.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.skydoves.baserecyclerviewadapter.RecyclerViewPaginator
import com.skydoves.themovies.R
import com.skydoves.themovies.databinding.MainFragmentStarBinding
import com.skydoves.themovies.extension.vm
import com.skydoves.themovies.models.Status
import com.skydoves.themovies.models.entity.Person
import com.skydoves.themovies.view.adapter.PeopleAdapter
import com.skydoves.themovies.view.ui.details.person.PersonDetailActivity
import com.skydoves.themovies.view.viewholder.PeopleViewHolder
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.main_fragment_movie.*
import javax.inject.Inject

/**
 * Developed by skydoves on 2018-08-11.
 * Copyright (c) 2018 skydoves rights reserved.
 */

@Suppress("SpellCheckingInspection")
class PersonListFragment : Fragment(), PeopleViewHolder.Delegate {

  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory
  private val viewModel by lazy { vm(viewModelFactory, MainActivityViewModel::class) }
  private lateinit var binding: MainFragmentStarBinding
  private lateinit var paginator: RecyclerViewPaginator

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment_star, container, false)
    binding.viewModel = viewModel
    binding.lifecycleOwner = this
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initializeUI()
  }

  override fun onAttach(context: Context) {
    AndroidSupportInjection.inject(this)
    super.onAttach(context)
    loadMore(page = 1)
  }

  private fun initializeUI() {
    recyclerView.adapter = PeopleAdapter(this)
    recyclerView.layoutManager = GridLayoutManager(context, 2)
    paginator = RecyclerViewPaginator(
      recyclerView = recyclerView,
      isLoading = { viewModel.getPeopleValues()?.status == Status.LOADING },
      loadMore = { loadMore(it) },
      onLast = { viewModel.getPeopleValues()?.onLastPage!! })
  }

  private fun loadMore(page: Int) {
    viewModel.postPeoplePage(page)
  }

  override fun onItemClick(person: Person, view: View) {
    PersonDetailActivity.startActivity(this, activity, person, view)
  }
}
