package com.example.memesx
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {
    var currentImageUrl:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadMeme()
    }
    private fun loadMeme()
    {
        val proBar=findViewById<ProgressBar>(R.id.progressBar1)
        progressBar1.visibility=View.VISIBLE
        val url="https://meme-api.herokuapp.com/gimme"
        val image=findViewById<ImageView>(R.id.imageInput)
        val jsonObjectRequest=JsonObjectRequest(
            Request.Method.GET,url,null,
            { response->
                currentImageUrl =response.getString("url")
                Glide.with(this).load(currentImageUrl).listener(object:RequestListener<Drawable>{
                   override fun onLoadFailed(
                       e: GlideException?,
                       model:Any?,
                       target: Target<Drawable>?,
                       isFirstResource:Boolean
                   ): Boolean{
                       proBar.visibility=View.GONE
                       nextButton.isEnabled=true
                       return false
                   }
                    override fun onResourceReady(
                        resource:Drawable?,
                        model:Any?,
                        target: Target<Drawable>,
                        dataSource: DataSource?,
                        isFirstResource:Boolean
                    ):Boolean{
                        progressBar1.visibility=View.GONE
                        nextButton.isEnabled=true
                        ShareButton.isEnabled=true
                        return false
                    }



                })into(image)
            }, {
                Toast.makeText(this,"Something went wrong",Toast.LENGTH_LONG).show()
            }
        )
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

    }

    fun shareMeme(view: View) {
        val intent=Intent(android.content.Intent.ACTION_SEND)
        intent.type="text/plain"
//       type mein ham ye likhenge ki kaise app mein ham share kar rahe hai jaise plain ,text like message app ,instagram app
        Intent.putExtra(Intent.EXTRA_TEXT,"Hey Checkout this cool meme i got from reddit $currentImageUrl")
        val chooser= android.content.Intent.createChooser(intent,"Share this image using...")
        startActivity(chooser)

    }
    fun nextMeme(view: View) {
        loadMeme()
    }
}