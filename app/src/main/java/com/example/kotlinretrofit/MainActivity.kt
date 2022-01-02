    package com.example.kotlinretrofit
    import android.content.Context
    import android.net.ConnectivityManager
    import android.os.Bundle
    import android.util.Log
    import android.view.View
    import androidx.appcompat.app.AppCompatActivity
    import androidx.fragment.app.Fragment
    import androidx.fragment.app.FragmentTransaction
    import androidx.fragment.app.add
    import androidx.fragment.app.commit
    import com.example.kotlinretrofit.Fragments.FragmentMain
    import com.google.android.material.snackbar.Snackbar
    import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
    import io.reactivex.rxjava3.core.Observable
    import io.reactivex.rxjava3.schedulers.Schedulers
    import kotlinx.android.synthetic.main.activity_main.*

    class MainActivity : AppCompatActivity(R.layout.activity_main) {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            startFragment(savedInstanceState)
            progressbar.visibility = View.VISIBLE

            fab_retry.setOnClickListener{
                startFragment(savedInstanceState)
            }
        }

        fun switchContent(id: Int, fragment: Fragment, bundle: Bundle) {
            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            fragment.arguments = bundle
            ft.add(id, fragment, fragment.toString())
            ft.addToBackStack(null)
            ft.commit()
        }

        private fun startFragment(savedInstanceState: Bundle?){
            checkConnection()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (it){
                        text_connection.visibility = View.GONE
                        fab_retry.visibility = View.GONE
                        if (savedInstanceState == null) {
                            supportFragmentManager.commit {
                                setReorderingAllowed(true)
                                add<FragmentMain>(R.id.fragment_container_view)
                            }
                        }
                    } else {
                        Snackbar.make(fab_retry, "No internet connection!", Snackbar.LENGTH_SHORT).show()
                        progressbar.visibility = View.GONE
                    }
                }
        }

        private fun checkConnection(): Observable<Boolean>{
            return Observable.create {
                sub ->
                if (isOnline()){
                    sub.onNext(true)
                    Log.d("Tag", "True online")
                    sub.onComplete()
                } else{
                    sub.onNext(false)
                    Log.d("Tag", "False online ")
                }
            }
        }

        fun isOnline(): Boolean {
            val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                return cm.activeNetworkInfo?.isConnectedOrConnecting != null

        }
    }