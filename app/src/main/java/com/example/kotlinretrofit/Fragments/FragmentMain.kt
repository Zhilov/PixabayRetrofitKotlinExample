package com.example.kotlinretrofit.Fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Adapter
import android.widget.EditText
import androidx.annotation.NonNull
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
import com.example.kotlinretrofit.Model.Labels
import com.example.kotlinretrofit.R
import com.jakewharton.rxbinding4.widget.textChanges
import dmax.dialog.SpotsDialog
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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

        getData("")
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
            object : DisposableSingleObserver<Labels?>() {
                override fun onSuccess(t: Labels?) {
                    labels = t!!
                    toolbar.visibility = View.VISIBLE
                    dialog.dismiss()
                    adapterOne = OneAdapter(requireContext(), labels)
                    adapterTwo = TwoAdapter(requireContext(), labels)
                    if (switchCompat.isChecked) {
                        setOneAdapter()
                    } else {
                        setTwoAdapter()
                    }
                    dispose()
                }

                override fun onError(e: Throwable?) {
                    Log.d("Error", e!!.localizedMessage.toString())
                }
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
                    .subscribeWith(
                        object : DisposableSingleObserver<Labels?>() {
                            override fun onSuccess(t: Labels?) {
                                labels = t!!
                                recyclerView.adapter!!.notifyDataSetChanged()
                            }

                            override fun onError(e: Throwable?) {
                                Log.d("Error", "key " + e!!.localizedMessage.toString())
                            }
                        })
            }
        return view
    }

    private fun getData(search: String): Single<Labels>{
        return Single.create{
            sub ->
            sub.onSuccess(mService.getPicturesList(search).execute().body())
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

}