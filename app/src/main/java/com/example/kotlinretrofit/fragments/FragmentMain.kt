package com.example.kotlinretrofit.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinretrofit.adapter.RecyclerViewAdapter
import com.example.kotlinretrofit.common.Common
import com.example.kotlinretrofit.retrofitInterface.RetrofitServices
import com.example.kotlinretrofit.model.Labels
import com.example.kotlinretrofit.R
import com.jakewharton.rxbinding4.widget.textChanges
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_main.*
import java.util.concurrent.TimeUnit


class FragmentMain : Fragment() {
    private lateinit var mService: RetrofitServices
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var adapter: RecyclerViewAdapter
    private lateinit var recyclerView: RecyclerView
    lateinit var editText: EditText
    lateinit var labels: Labels
    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Initialize non-view objects
        mService = Common.retrofitService
        labels = Labels()

        getData("", 1).subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                labels = it
            }, {}, {
                activity?.findViewById<ProgressBar>(R.id.progressbar)?.visibility = View.GONE
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_main, container, false)
        initViews(view)

        linearLayoutManager = LinearLayoutManager(context)
        gridLayoutManager = GridLayoutManager(context, 2)

        adapter = RecyclerViewAdapter(requireContext(), labels)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = adapter

        var loading = true
        var pastVisiblesItems: Int
        var visibleItemCount: Int
        var totalItemCount: Int
        var page = 2

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) { //check for scroll down
                    visibleItemCount = linearLayoutManager.getChildCount()
                    totalItemCount = linearLayoutManager.getItemCount()
                    pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition()
                    if (loading) {
                        if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                            loading = false
                            getData(editText.text.toString(), page)
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe {
                                    if (it.hits.isNotEmpty()) {
                                        labels.hits.addAll(it.hits)
                                        adapter.updateList(labels.hits)
                                    }
                                }
                            page += 1
                            loading = true
                        }
                    }
                }
            }
        })
        editText.textChanges()
            .debounce(500, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.computation())
            .subscribe {
                getData(editText.text.toString(), 1)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        if (it.hits.isNotEmpty()) {
                            if (recyclerView.visibility == View.INVISIBLE) {
                                recyclerView.visibility = View.VISIBLE
                                textEmptyList.visibility = View.INVISIBLE
                            }
                            labels = it
                            adapter.updateList(labels.hits)
                        } else {
                            recyclerView.visibility = View.INVISIBLE
                            textEmptyList.visibility = View.VISIBLE
                        }
                    }
            }

        return view
    }

    private fun getData(search: String, page: Int): Observable<Labels> {
        return Observable.create { sub ->

            if (mService.getPicturesList(search, page).execute().body() != null){
                sub.onNext(mService.getPicturesList(search, page).execute().body())
            }
            sub.onComplete()
        }
    }

    private fun initViews(view: View) {
        //Initialize view elements
        recyclerView = view.findViewById(R.id.recyclerPictures)
        toolbar = view.findViewById(R.id.toolbar)
        editText = view.findViewById(R.id.edit_search)
    }
}