package com.indramahkota.footballapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.indramahkota.footballapp.R
import com.indramahkota.footballapp.data.source.locale.entity.LeagueEntity
import com.indramahkota.footballapp.data.source.remote.model.ClassementModel
import com.indramahkota.footballapp.data.source.repository.Result
import com.indramahkota.footballapp.ui.adapter.ClassementAdapter
import com.indramahkota.footballapp.viewmodel.LeagueDetailsViewModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_classement.*
import javax.inject.Inject

class ClassementFragment : DaggerFragment() {
    companion object {
        private const val ARG_SECTION_FRAGMENT = "section_fragment"
        private const val ARG_SAVE_DATA = "save_data"

        fun newInstance(fragment: String) = ClassementFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_SECTION_FRAGMENT, fragment)
            }
        }
    }

    private var allClassementData: ArrayList<ClassementModel>? = null

    private lateinit var allClassementAdapter: ClassementAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_classement, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_all_classement.setHasFixedSize(true)

        val listTeamData = mutableListOf<ClassementModel>()
        allClassementAdapter =
            ClassementAdapter(listTeamData) {}
        rv_all_classement.adapter = allClassementAdapter

        if (allClassementData != null) {
            initialize(allClassementData)
        } else {
            getAllClassementData()
        }
    }

    private fun getAllClassementData() {
        val viewModel = activity?.let {
            ViewModelProvider(it, viewModelFactory).get(
                LeagueDetailsViewModel::class.java
            )
        }
        viewModel?.allClassementData?.observe(
            viewLifecycleOwner,
            {
                checkState(it)
            })
    }

    private fun checkState(it: Result<List<ClassementModel>?>) {
        when (it) {
            is Result.Success -> {
                initialize(it.data)
            }
            is Result.Error -> Toast.makeText(activity, it.exception.toString(), Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun initialize(it: List<ClassementModel>?) {
        if (it.isNullOrEmpty()) {
            no_data.visibility = View.VISIBLE
        } else {
            no_data.visibility = View.INVISIBLE
        }

        allClassementData = it?.let { ArrayList(it) }
        it?.let { allClassementAdapter.replace(it) }

        shimmer_view_container.visibility = View.GONE
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val fake = arrayListOf<LeagueEntity>()
        outState.putParcelableArrayList(ARG_SAVE_DATA, fake)
    }
}
