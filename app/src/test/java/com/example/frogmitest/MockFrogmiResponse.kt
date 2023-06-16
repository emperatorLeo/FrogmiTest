package com.example.frogmitest

import com.example.frogmitest.data.entities.Attributes
import com.example.frogmitest.data.entities.Data
import com.example.frogmitest.data.entities.FrogmiResponse
import com.example.frogmitest.data.entities.Links

object MockFrogmiResponse {
    fun provideFrogmiResponse() = FrogmiResponse(data = provideDataList, links =  provideLinkObject)

    private val provideDataList = listOf(
        Data(Attributes("name1", "code1", "fullAddress1")),
        Data(Attributes("name2", "code2", "fullAddress2")),
        Data(Attributes("name3", "code3", "fullAddress3"))
    )

    private val provideLinkObject =  Links("https://neo.frogmi.com/api/v3/stores?per_page=10&page=2")
}