package org.jetbrains.kotlin.demo

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.atomic.AtomicLong

@RestController
class REST_Controller {

    val counter = AtomicLong()

    @GetMapping("/get")
    fun getData(@RequestParam(value = "", defaultValue = "") name: String) =
            GetDataResponse(counter.incrementAndGet(), SampleData().getData)

    @PostMapping("/post")
    fun postData(@RequestParam(value = "", defaultValue = "") name: String) =
            PostDataResponse(counter.incrementAndGet(), SampleData().postData)

}