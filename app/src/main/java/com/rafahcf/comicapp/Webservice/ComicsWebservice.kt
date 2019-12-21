package com.rafahcf.comicapp.Webservice

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.rafahcf.comicapp.Constants.COMIC_API_URL
import com.rafahcf.comicapp.Constants.FORMAT_JSON
import com.rafahcf.comicapp.Constants.MY_COMIC_API_KEY
import com.rafahcf.comicapp.Models.Comic
import com.rafahcf.comicapp.Models.Credits
import com.rafahcf.comicapp.Models.ItemCredits
import com.rafahcf.comicapp.R
import org.json.JSONException
import org.json.JSONObject

object ComicsWebservice {

    var comic_list = arrayListOf<Comic>()
    var comic_single:Comic? = null
    var characterCredits = arrayListOf<Credits>()
    var teamCredits = arrayListOf<Credits>()
    var locationCredits = arrayListOf<Credits>()
    var conceptCredits = arrayListOf<Credits>()

    var characters_list = arrayListOf<ItemCredits>()
    var concepts_list = arrayListOf<ItemCredits>()
    var locations_list = arrayListOf<ItemCredits>()
    var teams_list = arrayListOf<ItemCredits>()

    fun getComics(context: Context, complete: (Boolean) -> Unit){
        val queue = Volley.newRequestQueue(context)
        val request = JsonObjectRequest(Request.Method.GET, "${COMIC_API_URL}issues/?$MY_COMIC_API_KEY&format=json", null, Response.Listener<JSONObject> { response ->
            try {
                val jsonArray = response.getJSONArray(context.getString(R.string.comics_api_str_results))
                for ( i in 0 until jsonArray.length()){
                    val comic = jsonArray.getJSONObject(i)
                    val image = comic.getJSONObject(context.getString(R.string.comics_api_str_image))
                    comic_list.add(
                        Comic(
                            image.getString(context.getString(R.string.comics_api_str_original_url)),
                            comic.getString(context.getString(R.string.comics_api_str_date_added)),
                            comic.getString(context.getString(R.string.comics_api_str_name)),
                            comic.getString(context.getString(R.string.comics_api_str_issue_number)),
                            comic.getString(context.getString(R.string.comics_api_str_detail_url))
                        )
                    )
                }
                complete(true)
            } catch ( e:JSONException ) {
                complete(false)
                e.printStackTrace()
            }
        }, Response.ErrorListener { complete(false) })
        queue.add(request)
    }

    fun getComic(context: Context, comic_url_detail:String, complete: (Boolean) -> Unit){
        val queue = Volley.newRequestQueue(context)
        val request = JsonObjectRequest(Request.Method.GET, "$comic_url_detail?$MY_COMIC_API_KEY&$FORMAT_JSON", null, Response.Listener<JSONObject> { response ->
            try {
                val comic = response.getJSONObject(context.getString(R.string.comics_api_str_results))
                val image = comic.getJSONObject(context.getString(R.string.comics_api_str_image))
                val comic_image = image.getString(context.getString(R.string.comics_api_str_original_url))
                val characterCreditsJsonArray = comic.getJSONArray("character_credits")
                val conceptCreditsJsonArray = comic.getJSONArray("concept_credits")
                val locationCreditsJsonArray = comic.getJSONArray("location_credits")
                val teamCreditsJsonArray = comic.getJSONArray("team_credits")

                for (a in 0 until characterCreditsJsonArray.length()){
                    characterCredits.add(
                        Credits(
                            characterCreditsJsonArray.getJSONObject(a).getString("api_detail_url"),
                            characterCreditsJsonArray.getJSONObject(a).getString("id"),
                            characterCreditsJsonArray.getJSONObject(a).getString("name"),
                            characterCreditsJsonArray.getJSONObject(a).getString("site_detail_url")
                        )
                    )
                }
                for (a in 0 until conceptCreditsJsonArray.length()){
                    conceptCredits.add(
                        Credits(
                            conceptCreditsJsonArray.getJSONObject(a).getString("api_detail_url"),
                            conceptCreditsJsonArray.getJSONObject(a).getString("id"),
                            conceptCreditsJsonArray.getJSONObject(a).getString("name"),
                            conceptCreditsJsonArray.getJSONObject(a).getString("site_detail_url")
                        )
                    )
                }
                for (a in 0 until locationCreditsJsonArray.length()){
                    locationCredits.add(
                        Credits(
                            locationCreditsJsonArray.getJSONObject(a).getString("api_detail_url"),
                            locationCreditsJsonArray.getJSONObject(a).getString("id"),
                            locationCreditsJsonArray.getJSONObject(a).getString("name"),
                            locationCreditsJsonArray.getJSONObject(a).getString("site_detail_url")
                        )
                    )
                }
                for (a in 0 until teamCreditsJsonArray.length()){
                    teamCredits.add(
                        Credits(
                            teamCreditsJsonArray.getJSONObject(a).getString("api_detail_url"),
                            teamCreditsJsonArray.getJSONObject(a).getString("id"),
                            teamCreditsJsonArray.getJSONObject(a).getString("name"),
                            teamCreditsJsonArray.getJSONObject(a).getString("site_detail_url")
                        )
                    )
                }

                complete(true)
            } catch ( e:JSONException ) {
                complete(false)
                e.printStackTrace()
            }
        }, Response.ErrorListener { complete(false) })
        queue.add(request)
    }

    fun getCharacterItemCredits(context: Context, type:String, detail_url:String, complete: (Boolean) -> Unit){
        val queue = Volley.newRequestQueue(context)
        val request = JsonObjectRequest(Request.Method.GET, "$detail_url?$MY_COMIC_API_KEY&$FORMAT_JSON", null, Response.Listener<JSONObject> { response ->
            try {
                val itemCredits = response.getJSONObject(context.getString(R.string.comics_api_str_results))
                val image = itemCredits.getJSONObject(context.getString(R.string.comics_api_str_image))
                val itemCreditsImage = image.getString(context.getString(R.string.comics_api_str_original_url))
                val itemCreditsName = itemCredits.getString("name")

                when(type){
                    "character" -> {
                        characters_list.add(
                            ItemCredits(
                                itemCreditsImage,
                                itemCreditsName
                            )
                        )
                    }
                    "concept" -> {
                        concepts_list.add(
                            ItemCredits(
                                itemCreditsImage,
                                itemCreditsName
                            )
                        )
                    }
                    "location" -> {
                        locations_list.add(
                            ItemCredits(
                                itemCreditsImage,
                                itemCreditsName
                            )
                        )
                    }
                    "team" -> {
                        teams_list.add(
                            ItemCredits(
                                itemCreditsImage,
                                itemCreditsName
                            )
                        )
                    }
                }

                complete(true)
            } catch ( e:JSONException ) {
                complete(false)
                e.printStackTrace()
            }
        }, Response.ErrorListener { complete(false) })
        queue.add(request)
    }

    fun cleanLists(){
        characterCredits.clear()
        conceptCredits.clear()
        locationCredits.clear()
        teamCredits.clear()
        characters_list.clear()
        concepts_list.clear()
        locations_list.clear()
        teams_list.clear()
    }
}