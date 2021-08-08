package com.example.kotlinretrofit.Fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
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
import com.example.kotlinretrofit.Model.Labels
import com.example.kotlinretrofit.R
import dmax.dialog.SpotsDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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
        switchCompat.setOnCheckedChangeListener { buttonView, isChecked ->
            if (switchCompat.isChecked) {
                setOneAdapter(labels)
            } else {
                setTwoAdapter(labels)
            }
        }
        editText.setOnKeyListener(object : View.OnKeyListener{
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if (keyCode == EditorInfo.IME_ACTION_SEARCH ||
                    keyCode == EditorInfo.IME_ACTION_DONE ||
                    event?.action == KeyEvent.ACTION_DOWN &&
                    event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    getAllPicturesList(editText.text.toString())
                    return true
                } else{
                    return false
                }
            }
        })
        return view
    }

    private fun setOneAdapter(labels: Labels) {
        adapterOne = OneAdapter(context, labels)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = adapterOne
        adapterOne.notifyDataSetChanged()
    }

    private fun setTwoAdapter(labels: Labels) {
        adapterTwo = TwoAdapter(context, labels)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter = adapterTwo
        adapterTwo.notifyDataSetChanged()
    }

    private fun getAllPicturesList(search: String) {
        dialog.show()
        mService.getPicturesList(search).enqueue(object : Callback<Labels> {
            override fun onResponse(call: Call<Labels>, response: Response<Labels>) {
                labels = response.body() as Labels
                toolbar.visibility = View.VISIBLE
                        if (switchCompat.isChecked) {
                            setOneAdapter(labels)
                        } else {
                            setTwoAdapter(labels)
                        }
                dialog.dismiss()
            }

            override fun onFailure(call: Call<Labels>, t: Throwable) {
            }
        })
    }

    override fun onResume() {
        super.onResume()
        getAllPicturesList(editText.text.toString())
    }


}