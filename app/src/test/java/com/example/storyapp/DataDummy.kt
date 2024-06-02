package com.example.storyapp

import com.example.storyapp.api.ListStoryItem

object DataDummy {

    fun generateDummyStorieResponse():List<ListStoryItem>{
        val item:MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100){
            val story = ListStoryItem(
                i.toString(),
                "CreateAt + $i",
                "name $i",
            )
            item.add(story)
        }
        return item

    }
}