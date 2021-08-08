package com.example.kotlinretrofit.Fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
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
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.Job
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FragmentMain : Fragment() {
    lateinit var mService: RetrofitServices
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var gridLayoutManager: GridLayoutManager
    lateinit var adapterOne: OneAdapter
    lateinit var adapterTwo: TwoAdapter
    lateinit var dialog: AlertDialog
    lateinit var swtichCompat: SwitchCompat
    lateinit var recyclerView: RecyclerView
    lateinit var editText: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       var view: View = inflater.inflate(R.layout.fragment_main, container, false)
        swtichCompat = view.findViewById(R.id.customSwitch)
        swtichCompat.visibility = View.GONE
        recyclerView = view.findViewById(R.id.recyclerPictures)
        editText = view.findViewById(R.id.edit_search)
        mService = Common.retrofitService
        linearLayoutManager = LinearLayoutManager(context)
        gridLayoutManager = GridLayoutManager(context, 2)
        recyclerView.layoutManager = gridLayoutManager
        dialog = SpotsDialog.Builder().setCancelable(true).setContext(context).build()
        return view
    }

    private fun getAllPicturesList(search: String) {
        dialog.show()
        mService.getPicturesList(search).enqueue(object : Callback<Labels>{
            override fun onResponse(call: Call<Labels>, response: Response<Labels>) {
                swtichCompat.visibility = View.VISIBLE
                adapterOne = OneAdapter(context!!, response.body() as Labels)
                adapterTwo = TwoAdapter(context!!, response.body() as Labels)
                adapterOne.notifyDataSetChanged()
                adapterTwo.notifyDataSetChanged()
                recyclerPictures.adapter = adapterTwo
                dialog.dismiss()
                swtichCompat.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                    if (swtichCompat.isChecked){
                        recyclerView.layoutManager = linearLayoutManager
                        recyclerView.adapter = adapterOne
                        adapterOne.notifyDataSetChanged()
                    } else {
                        recyclerView.layoutManager = gridLayoutManager
                        recyclerView.adapter = adapterTwo
                        adapterTwo.notifyDataSetChanged()
                    }
                })
            }

            override fun onFailure(call: Call<Labels>, t: Throwable) {

            }

        })
    }

    override fun onResume() {
        super.onResume()
        editText.setOnKeyListener(object : View.OnKeyListener{
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if (keyCode == EditorInfo.IME_ACTION_SEARCH ||
                    keyCode == EditorInfo.IME_ACTION_DONE ||
                    event?.getAction() == KeyEvent.ACTION_DOWN &&
                    event.getKeyCode() == KeyEvent.KEYCODE_ENTER && !editText.text.isEmpty()) {
                    getAllPicturesList(editText.text.toString())
                    return true
                } else{
                    return false
                }
            }
        })
        getAllPicturesList("hello")
        editText.text.clear()
        swtichCompat.isChecked = false
    }


}