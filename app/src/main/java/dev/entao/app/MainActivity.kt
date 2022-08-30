package dev.entao.app

import android.os.Bundle
import dev.entao.app.page.PageActivity

class MainActivity : PageActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentPage(MainPage(this))
    }
}
