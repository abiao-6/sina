package com.abiao.sina.forest

import com.abiao.sina.entity.Bot.InformationTemplate
import com.abiao.web.infrastructure.model.JsonResultMessage
import com.dtflys.forest.annotation.JSONBody
import com.dtflys.forest.annotation.Post
import com.openaddr.ktCloud.KissForest
import com.openaddr.ktCloud.ServiceName


@ServiceName(serviceName = "bot")
interface BotClient {

    companion object : KissForest<BotClient>()

    @Post("/bot/sendMsg")
    fun sendMsg(@JSONBody informationTemplate: InformationTemplate?): JsonResultMessage<*>?
}
