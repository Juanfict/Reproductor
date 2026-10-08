package com.example.reproductor

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.reproductor.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var mediaPlayer: MediaPlayer? = null
    private val handler = Handler(Looper.getMainLooper())

    private val runnable = object : Runnable {
        override fun run() {
            if(mediaPlayer?.isPlaying == true)

                binding.seekBar.progress = mediaPlayer!!.currentPosition/1000
                handler.postDelayed(this, 1000)
        }
    }
    private var enCurso:Int = 0
    private lateinit var datos: MutableList<Cancion>

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Reflejar el movimiento del usuario
        binding.seekBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {

                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    binding.tContador.text =
                        "$progress"
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    mediaPlayer?.seekTo(seekBar!!.progress*1000)
                }
            }
        )
        datos = mutableListOf<Cancion>(
                        Cancion("Titulo 1","Autor 1",R.raw.tension)
                        , Cancion("Titulo 2","Autor 2",R.raw.suspense)
                        , Cancion("Titulo 3","Autor 3",R.raw.frighten)
                        , Cancion("Titulo 4","Autor 4",R.raw.thinking_time)
        )

        cargarCancion()

    }

    private fun cargarCancion(){
        handler.removeCallbacks(runnable)
        mediaPlayer?.release()
        MediaPlayer.create(this,datos[enCurso].pista)
        binding.tTitulo.text = datos[enCurso].titulo
        binding.tArtista.text = datos[enCurso].autor
        binding.tTotal.text = (mediaPlayer!!.duration/1000).toString()

        binding.seekBar.max = mediaPlayer!!.duration/1000
        binding.seekBar.progress = 0

    }

}