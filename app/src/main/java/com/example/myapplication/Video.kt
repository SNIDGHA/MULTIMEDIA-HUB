package com.example.myapplication

import android.net.Uri
import java.io.Serializable


class Video(var id : Long, var data : Uri, var title : String, var duration : String):Serializable