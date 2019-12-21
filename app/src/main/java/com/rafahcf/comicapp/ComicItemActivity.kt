package com.rafahcf.comicapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rafahcf.comicapp.Adapters.RecyclerAdapter
import com.rafahcf.comicapp.Constants.FORMAT_JSON
import com.rafahcf.comicapp.Constants.MY_COMIC_API_KEY
import com.rafahcf.comicapp.Webservice.ComicsWebservice
import com.squareup.picasso.Picasso

class ComicItemActivity : AppCompatActivity() {

    var recyclerView_CharacterCredits: RecyclerView? = null
    var recyclerView_ConceptCredits: RecyclerView? = null
    var recyclerView_LocationCredits: RecyclerView? = null
    var recyclerView_TeamCredits: RecyclerView? = null

    var mAdapterCharacterCredits: RecyclerAdapter? = null
    var mAdapterConceptCredits: RecyclerAdapter? = null
    var mAdapterLocationCredits: RecyclerAdapter? = null
    var mAdapterTeamCredits: RecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comic_item)

        val bundle:Bundle = intent.extras!!
        val detailUrl = bundle.getString("detail_url")
        val image = bundle.getString("image")
        getComicAndCredits(detailUrl!!)

        //setting comic main image on ui
        val imageViewComicItemImage = findViewById<ImageView>(R.id.imageViewComicItemImage)
        Picasso.get().load(image).into(imageViewComicItemImage)

        recyclerView_CharacterCredits = findViewById(R.id.recyclerView_CharacterCredits)
        recyclerView_ConceptCredits = findViewById(R.id.recyclerView_ConceptCredits)
        recyclerView_LocationCredits = findViewById(R.id.recyclerView_LocationCredits)
        recyclerView_TeamCredits = findViewById(R.id.recyclerView_TeamCredits)

        //setting recycleviews
        recyclerView_CharacterCredits!!.setHasFixedSize(true)
        recyclerView_ConceptCredits!!.setHasFixedSize(true)
        recyclerView_LocationCredits!!.setHasFixedSize(true)
        recyclerView_TeamCredits!!.setHasFixedSize(true)

        //gridlayout with 2 columns
        recyclerView_CharacterCredits!!.layoutManager = GridLayoutManager(this, 2)
        recyclerView_ConceptCredits!!.layoutManager = GridLayoutManager(this, 2)
        recyclerView_LocationCredits!!.layoutManager = GridLayoutManager(this, 2)
        recyclerView_TeamCredits!!.layoutManager = GridLayoutManager(this, 2)

        mAdapterCharacterCredits = RecyclerAdapter()
        mAdapterConceptCredits = RecyclerAdapter()
        mAdapterLocationCredits = RecyclerAdapter()
        mAdapterTeamCredits = RecyclerAdapter()

        //sending empty list of items for initialization
        mAdapterCharacterCredits!!.RecyclerAdapter(ComicsWebservice.characters_list, this)
        mAdapterConceptCredits!!.RecyclerAdapter(ComicsWebservice.concepts_list, this)
        mAdapterLocationCredits!!.RecyclerAdapter(ComicsWebservice.locations_list, this)
        mAdapterTeamCredits!!.RecyclerAdapter(ComicsWebservice.teams_list, this)

        //setting adapters
        recyclerView_CharacterCredits!!.adapter = mAdapterCharacterCredits
        recyclerView_ConceptCredits!!.adapter = mAdapterConceptCredits
        recyclerView_LocationCredits!!.adapter = mAdapterLocationCredits
        recyclerView_TeamCredits!!.adapter = mAdapterTeamCredits

        //cleaning lists every time this activity starts
        ComicsWebservice.cleanLists()
    }

    private fun getComicAndCredits(detail_url:String){
        ComicsWebservice.getComic(this, detail_url){ complete ->
            if(complete){
                //GET ITEM CREDITS IMAGE AND NAME
                getItemCreditsInfo()
            }else{
                //SHOW MESSAGE ERROR
                Toast.makeText(this, getString(R.string.error_getting_info), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getItemCreditsInfo() {

        //getting data from api for every item credit
        for(i in 0 until ComicsWebservice.characterCredits.size){
            ComicsWebservice.getCharacterItemCredits(this, "character", ComicsWebservice.characterCredits[i].api_detail_url){ complete ->
                if(complete){
                    showItemCreditsOnUI()
                }
            }
        }
        for(i in 0 until ComicsWebservice.conceptCredits.size){
            ComicsWebservice.getCharacterItemCredits(this, "concept", ComicsWebservice.conceptCredits[i].api_detail_url){ complete ->
                if(complete){
                    showItemCreditsOnUI()
                }
            }
        }
        for(i in 0 until ComicsWebservice.locationCredits.size){
            ComicsWebservice.getCharacterItemCredits(this, "location", ComicsWebservice.locationCredits[i].api_detail_url){ complete ->
                if(complete){
                    showItemCreditsOnUI()
                }
            }
        }
        for(i in 0 until ComicsWebservice.teamCredits.size){
            ComicsWebservice.getCharacterItemCredits(this, "team", ComicsWebservice.teamCredits[i].api_detail_url){ complete ->
                if(complete){
                    showItemCreditsOnUI()
                }
            }
        }

    }

    fun showItemCreditsOnUI(){

        //reseting adapters and notifying changes
        recyclerView_CharacterCredits!!.setHasFixedSize(true)
        recyclerView_ConceptCredits!!.setHasFixedSize(true)
        recyclerView_LocationCredits!!.setHasFixedSize(true)
        recyclerView_TeamCredits!!.setHasFixedSize(true)

        recyclerView_CharacterCredits!!.layoutManager = GridLayoutManager(this, 2)
        recyclerView_ConceptCredits!!.layoutManager = GridLayoutManager(this, 2)
        recyclerView_LocationCredits!!.layoutManager = GridLayoutManager(this, 2)
        recyclerView_TeamCredits!!.layoutManager = GridLayoutManager(this, 2)

        mAdapterCharacterCredits = RecyclerAdapter()
        mAdapterConceptCredits = RecyclerAdapter()
        mAdapterLocationCredits = RecyclerAdapter()
        mAdapterTeamCredits = RecyclerAdapter()

        mAdapterCharacterCredits!!.RecyclerAdapter(ComicsWebservice.characters_list, this)
        mAdapterConceptCredits!!.RecyclerAdapter(ComicsWebservice.concepts_list, this)
        mAdapterLocationCredits!!.RecyclerAdapter(ComicsWebservice.locations_list, this)
        mAdapterTeamCredits!!.RecyclerAdapter(ComicsWebservice.teams_list, this)

        recyclerView_CharacterCredits!!.adapter = mAdapterCharacterCredits
        recyclerView_ConceptCredits!!.adapter = mAdapterConceptCredits
        recyclerView_LocationCredits!!.adapter = mAdapterLocationCredits
        recyclerView_TeamCredits!!.adapter = mAdapterTeamCredits

        recyclerView_CharacterCredits!!.adapter!!.notifyDataSetChanged()
        recyclerView_ConceptCredits!!.adapter!!.notifyDataSetChanged()
        recyclerView_LocationCredits!!.adapter!!.notifyDataSetChanged()
        recyclerView_TeamCredits!!.adapter!!.notifyDataSetChanged()
    }
}
