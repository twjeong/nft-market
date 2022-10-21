package com.mosaicsquare.service.sqs

import com.mosaicsquare.service.sqs.event.SqsEventType

class SqsConfig(
    val url: String,
    val sqsEventType: SqsEventType
) {
    var workers = 20
    var waitTime = 20
    var maxNumberMessage = 10
}
