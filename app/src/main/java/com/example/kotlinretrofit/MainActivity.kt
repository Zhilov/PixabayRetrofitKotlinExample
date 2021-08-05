    package com.example.kotlinretrofit
    import android.os.Bundle
    import androidx.appcompat.app.AppCompatActivity
    import androidx.fragment.app.Fragment
    import androidx.fragment.app.FragmentTransaction
    import androidx.fragment.app.add
    import androidx.fragment.app.commit
    import com.example.kotlinretrofit.Fragments.FragmentMain

    class MainActivity : AppCompatActivity(R.layout.activity_main) {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            if (savedInstanceState == null) {
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    add<FragmentMain>(R.id.fragment_container_view)
                }
            }
        }

        fun switchContent(id: Int, fragment: Fragment) {
            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            ft.replace(id, fragment, fragment.toString())
            ft.addToBackStack(null)
            ft.commit()
        }
    }