package com.rafahcf.comicapp.Models

data class Comic(
    var image:String,
    var date_added:String,
    var name:String,
    var issue_number:String,
    var api_detail_url:String

){
    fun date_added_refactor():String{
        val date_added_array = date_added.split(" ")
        val date = date_added_array[0]
        val time = date_added_array[1]

        val date_array = date.split("-")
        val year_number = date_array[0]
        val month_number = date_array[1]
        val day_number = date_array[2]

        var month_name = ""

        when(month_number){
            "01" -> month_name = "January"
            "02" -> month_name = "February"
            "03" -> month_name = "March"
            "04" -> month_name = "April"
            "05" -> month_name = "May"
            "06" -> month_name = "June"
            "07" -> month_name = "July"
            "08" -> month_name = "August"
            "09" -> month_name = "September"
            "10" -> month_name = "October"
            "11" -> month_name = "November"
            "12" -> month_name = "December"
        }

        val date_added_refactored = "$month_name $day_number, $year_number"

        return date_added_refactored
    }
}