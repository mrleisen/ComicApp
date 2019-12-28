package com.rafahcf.comicapp

import android.app.ProgressDialog
import android.app.usage.NetworkStats
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.GridView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.rafahcf.comicapp.Adapters.GridViewAdapter
import com.rafahcf.comicapp.Adapters.ListViewAdapter
import com.rafahcf.comicapp.Utils.NetworkStatus
import com.rafahcf.comicapp.Webservice.ComicsWebservice

class MainActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    private val VIEW_HOME_LISTVIEW = 0
    private val VIEW_HOME_GRIDVIEW = 1

    private var stubList: ViewStub? = null
    private var stubGrid: ViewStub? = null

    private var listView:ListView? = null
    private var gridView:GridView? = null

    private var listViewAdapter:ListViewAdapter? = null
    private var gridViewAdapter: GridViewAdapter? = null

    private var currentViewMode = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        stubList = findViewById(R.id.stub_list)
        stubGrid = findViewById(R.id.stub_grid)

        stubList!!.inflate()
        stubGrid!!.inflate()


        val sharedPreferences = getSharedPreferences("ViewMode", Context.MODE_PRIVATE)
        currentViewMode = sharedPreferences.getInt("currentViewMode", VIEW_HOME_LISTVIEW)

        if(!NetworkStatus.NetworkOn(this)){
            noInternetDialog()
        }else{
            switchView()
        }

    }

    private fun switchView(){
        if(VIEW_HOME_LISTVIEW == currentViewMode){
            stubList!!.visibility = View.VISIBLE
            stubGrid!!.visibility = View.GONE
        } else {
            stubList!!.visibility = View.GONE
            stubGrid!!.visibility = View.VISIBLE
        }
        obtainingComics()
    }

    private fun setAdapters() {

        if(VIEW_HOME_LISTVIEW == currentViewMode){
            listViewAdapter = ListViewAdapter(this, R.layout.list_item, ComicsWebservice.comic_list)
            listView = findViewById(R.id.myListView)
            listView!!.adapter = listViewAdapter
            listView!!.onItemClickListener = this
        } else {
            gridViewAdapter = GridViewAdapter(this, R.layout.grid_item, ComicsWebservice.comic_list)
            gridView = findViewById(R.id.myGridView)
            gridView!!.adapter = gridViewAdapter
            gridView!!.onItemClickListener = this
        }
    }

    private fun obtainingComics(){
        val loading = ProgressDialog.show(this, getString(R.string.getting_comic_list), getString(R.string.just_a_sec), false, false)
        ComicsWebservice.getComics(this){ complete ->
            if(complete){
                setAdapters()
                loading.dismiss()
            }else{
                loading.dismiss()
                Toast.makeText(this, getString(R.string.error_getting_info), Toast.LENGTH_LONG).show()
            }
        }
    }


    override fun onItemClick(parent:AdapterView<*>, view : View, position: Int, id:Long){
        val comic = ComicsWebservice.comic_list[position]
        val intent = Intent(this, ComicItemActivity::class.java)
        intent.putExtra("detail_url", comic.api_detail_url)
        intent.putExtra("image", comic.image)
        this.startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            R.id.item_menu_1 -> {
                if(currentViewMode != VIEW_HOME_LISTVIEW){
                    currentViewMode = VIEW_HOME_LISTVIEW
                }
            }
            R.id.item_menu_2 -> {
                if(currentViewMode != VIEW_HOME_GRIDVIEW){
                    currentViewMode = VIEW_HOME_GRIDVIEW
                }
            }
        }

        switchView()

        val sharedPreferences = getSharedPreferences("ViewMode", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("currentViewMode", currentViewMode)
        editor.commit()

        return super.onOptionsItemSelected(item)
    }

    private fun noInternetDialog(){

        val builder = AlertDialog.Builder(this)

        val viewInflated: View = LayoutInflater.from(this).inflate(R.layout.dialog_no_internet_available, null)
        builder.setView(viewInflated)

        builder.setNegativeButton("Ok") { _, _ -> }

        val dialog: AlertDialog = builder.create()
        dialog.show()

    }


}

