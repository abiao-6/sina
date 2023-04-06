package com.abiao.sina.forest

import com.abiao.sina.feign.entity.RealTimeKeyword
import com.abiao.web.infrastructure.model.JsonResultMessage
import com.dtflys.forest.annotation.JSONBody
import com.dtflys.forest.annotation.Post
import com.openaddr.ktCloud.KissForest
import com.openaddr.ktCloud.ServiceName


@ServiceName(serviceName = "bot")
interface BotSinaClient {

    companion object : KissForest<BotSinaClient>()

    @Post("/bot/sina/query/realTimeKeyword")
    fun query(@JSONBody realTimeKeyword: RealTimeKeyword?): JsonResultMessage<List<RealTimeKeyword?>?>?
}