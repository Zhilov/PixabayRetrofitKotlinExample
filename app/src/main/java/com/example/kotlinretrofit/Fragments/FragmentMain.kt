package com.example.kotlinretrofit.Fragments

import android.app.AlertDialog
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinretrofit.Adapter.OneAdapter
import com.example.kotlinretrofit.Adapter.TwoAdapter
import com.example.kotlinretrofit.Common.Common
import com.example.kotlinretrofit.Interface.RetrofitServices
import com.example.kotlinretrofit.Model.Hits
import com.example.kotlinretrofit.Model.Labels
import com.example.kotlinretrofit.R
import com.jakewharton.rxbinding4.widget.textChanges
import dmax.dialog.SpotsDialog
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_main.*
import java.util.concurrent.TimeUnit


class FragmentMain : Fragment() {
    private lateinit var mService: RetrofitServices
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var adapterOne: OneAdapter
    private lateinit var adapterTwo: TwoAdapter
    lateinit var dialog: AlertDialog
    lateinit var switchCompat: SwitchCompat
    private lateinit var recyclerView: RecyclerView
    lateinit var editText: EditText
    lateinit var labels: Labels
    lateinit var toolbar: Toolbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_main, container, false)
        switchCompat = view.findViewById(R.id.customSwitch)

        recyclerView = view.findViewById(R.id.recyclerPictures)
        toolbar = view.findViewById(R.id.toolbar)
        editText = view.findViewById(R.id.edit_search)
        mService = Common.retrofitService
        labels = Labels()
        toolbar.visibility = View.GONE
        dialog = SpotsDialog.Builder().setCancelable(true).setContext(context).build()
        linearLayoutManager = LinearLayoutManager(context)
        gridLayoutManager = GridLayoutManager(context, 2)

            getData(editText.text.toString())
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                labels = it
                toolbar.visibility = View.VISIBLE
                dialog.dismiss()
                adapterOne = OneAdapter(requireContext(), labels)
                adapterTwo = TwoAdapter(requireContext(), labels)
                if (switchCompat.isChecked) {
                    setOneAdapter()
                } else {
                    setTwoAdapter()
                }
            }, {}, {
                Log.d("Tag", "Complete")
            })

        switchCompat.setOnCheckedChangeListener { buttonView, isChecked ->
            if (switchCompat.isChecked) {
                setOneAdapter()
            } else {
                setTwoAdapter()
            }
        }

        editText.textChanges()
            .debounce(1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.computation())
            .subscribe{
                getData(editText.text.toString())
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        if (it.hits.isNotEmpty()){
                            if (recyclerView.visibility == View.INVISIBLE){
                                recyclerView.visibility = View.VISIBLE
                                textEmptyList.visibility = View.INVISIBLE
                            }
                            labels = it
                            adapterOne.updateList(labels.hits)
                            adapterTwo.updateList(labels.hits)
                            Log.d("Tag", "edittext: " + it.hits.get(0).user.toString())
                            } else {
                                recyclerView.visibility = View.INVISIBLE
                                textEmptyList.visibility = View.VISIBLE
                        }
                        }
            }
        return view
    }

    private fun getData(search: String): Observable<Labels>{
        return Observable.create { sub ->
            sub.onNext(mService.getPicturesList(search).execute().body())
            sub.onComplete()
        }
    }

    private fun setOneAdapter() {
        recyclerView.layoutManager = linearLayoutManager
//        recyclerView.swapAdapter(adapterOne, false)
        recyclerView.adapter = adapterOne
    }

    private fun setTwoAdapter() {
        recyclerView.layoutManager = gridLayoutManager
//        recyclerView.swapAdapter(adapterTwo, false)
        recyclerView.adapter = adapterTwo
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("labels", labels)
    }

}