package seoft.co.kr.launcherq.ui.select

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.databinding.ActivitySelectBinding
import seoft.co.kr.launcherq.ui.arrange.ArrangeActivity.Companion.IS_EXCEPT
import seoft.co.kr.launcherq.ui.arrange.ArrangeActivity.Companion.LABEL
import seoft.co.kr.launcherq.ui.arrange.ArrangeActivity.Companion.PKG_NAME
import seoft.co.kr.launcherq.utill.observeActMsg
import seoft.co.kr.launcherq.utill.setupActionBar

class SelectActivity : AppCompatActivity() {

    val TAG = "SelectActivity#$#"

    val COLUMN_COUNT = 5

    private lateinit var binding: ActivitySelectBinding
    private val selectRecyclerViewAdapter = SelectRecyclerViewAdapter {

        val rstIntent = Intent()
            .apply {
                putExtra(PKG_NAME,it.pkgName)
//                putExtra(DETAIL_NAME,it.detailName)
                putExtra(LABEL,it.label)
                putExtra(IS_EXCEPT,it.isExcept)
            }

        setResult(Activity.RESULT_OK,rstIntent)
        finish()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_select)

        val vm = ViewModelProviders.of(this,SelectViewModel(Repo).create()).get(SelectViewModel::class.java)
        binding.vm = vm
        binding.executePendingBindings()

        vm.observeActMsg(this, Observer {
            when(it) {

            }
        })

        binding.gvApps.layoutManager = GridLayoutManager(this,COLUMN_COUNT)
        binding.gvApps.adapter = selectRecyclerViewAdapter
        binding.gvApps.addItemDecoration(GridSpacingItemDecoration(COLUMN_COUNT,30))
        vm.liveDataCommonApps.observe(this,
            Observer { it?.let {
                selectRecyclerViewAdapter.replaceData(it)
            } })

        vm.showOptions = intent.getBooleanExtra(SHOW_OPTIONS,false)

        vm.start()

        initToolbar()
    }


    private fun initToolbar(){
        setupActionBar(R.id.toolbar) {
            setHomeAsUpIndicator(R.drawable.ic_arrow_back_white)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        val SHOW_OPTIONS = "SHOW_OPTIONS"
    }

    inner class GridSpacingItemDecoration(
        private val spanCount: Int,
        private val spacing: Int
    ) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            val position = parent.getChildAdapterPosition(view) // item position
            val column = position % spanCount // item column

            outRect.left = spacing - column * spacing / spanCount // spacing - column * ((1f / spanCount) * spacing)
            outRect.right = (column + 1) * spacing / spanCount // (column + 1) * ((1f / spanCount) * spacing)

//            if (position < spanCount) { // top edge
//                outRect.top = spacing
//            }
//            outRect.bottom = ( spacing * 1.6 ).toInt() // item bottom
        }
    }
}