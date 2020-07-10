package Fragments


import Models.Main_Menu_Model
import adapters.Main_Menu_Adapter
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.model.LatLng
import com.waqar.travel_guide.MainActivity
import com.waqar.travel_guide.R
import kotlinx.android.synthetic.main.fragment_wonder_of__world.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class Wonder_of_World : Fragment() {



    var list = mutableListOf<Main_Menu_Model>()
    lateinit var cntxt: Context
    lateinit var gmmIntentUri: Uri


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        cntxt = activity!!

        return inflater.inflate(R.layout.fragment_wonder_of__world, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        wwRecyclerView = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.wwRecyclerView)
        wwRecyclerView.layoutManager = androidx.recyclerview.widget.GridLayoutManager(
            activity!!,
            2
        ) as androidx.recyclerview.widget.RecyclerView.LayoutManager

        list.add(
            Main_Menu_Model(
                R.drawable.great_wall_of_china, cntxt.resources.getString(
                    R.string.Great_Wall_of_China
                )
            )
        )
        list.add(
            Main_Menu_Model(
                R.drawable.christ_the_redeemer_statue, cntxt.resources.getString(
                    R.string.Christ_the_Redeemer_Statue
                )
            )
        )
        list.add(
            Main_Menu_Model(
                R.drawable.machu_picchu, cntxt.resources.getString(
                    R.string.Machu_Picchu
                )
            )
        )
        list.add(
            Main_Menu_Model(
                R.drawable.chichen_itza, cntxt.resources.getString(
                    R.string.Chichen_Itza
                )
            )
        )
        list.add(
            Main_Menu_Model(
                R.drawable.the_roman_colosseum, cntxt.resources.getString(
                    R.string.The_Roman_Colosseum
                )
            )
        )
        list.add(
            Main_Menu_Model(
                R.drawable.taj_mahal, cntxt.resources.getString(
                    R.string.Taj_Mahal
                )
            )
        )
        list.add(
            Main_Menu_Model(
                R.drawable.petra, cntxt.resources.getString(
                    R.string.Petra
                )
            )
        )
        list.add(
            Main_Menu_Model(
                R.drawable.eiffel_tower, cntxt.resources.getString(
                    R.string.Eiffel_Tower
                )
            )
        )
        list.add(
            Main_Menu_Model(
                R.drawable.leaning_tower, cntxt.resources.getString(
                    R.string.Leaning_Tower
                )
            )
        )

        wwRecyclerView.adapter = Main_Menu_Adapter(activity!!, list) {
//            Toast.makeText(activity, "CLicked", Toast.LENGTH_SHORT).show()

//            MainActivity.fragmentTransaction(Main_Menu_Fragment())

//            gmmIntentUri = Uri.parse("geo:0,0?q=" + "Great Wall of China")

            var lat: Double = 0.0
            var longi: Double = 0.0
            var isStreetViewAvailable: Boolean = true
            var place : String = ""
            if (it.title == list[0].title) {
                lat = 40.4319
                longi = 116.5704
            } else if (it.title == list[1].title) {
                lat = 22.9519
                longi = 43.2105
                place = list[1].title
                isStreetViewAvailable = false
            } else if (it.title == list[2].title) {
                lat = 13.1631
                longi = 72.5450
                place = list[2].title
                isStreetViewAvailable = false
            } else if (it.title == list[3].title) {
                lat = 20.6843
                longi = 88.5678
                place = list[3].title
                isStreetViewAvailable = false
            } else if (it.title == list[4].title) {
                lat = 41.8902
                longi = 12.4922
            } else if (it.title == list[5].title) {
                lat = 27.1750
                longi = 78.0422
            } else if (it.title == list[6].title) {
                lat = 30.3285
                longi = 35.4444
            } else if (it.title == list[7].title) {
                lat = 48.8584
                longi = 2.2945
            } else {
                lat = 43.7230
                longi = 10.3966
            }




            val mainActivityRef = activity!! as MainActivity
            if(isStreetViewAvailable) {
                mainActivityRef.OpenInterstitialAd("google.streetview:cbll=$lat,$longi" + "&cbp=1,90,,0,1.0&mz=8")
            }
            else
            {
//                mainActivityRef.OpenInterstitialAd("geo:$lat,$longi")
                mainActivityRef.OpenInterstitialAd("geo:0,0?q=" + place + "")
            }
        }

//        val mainActivityRef = activity!! as MainActivity
//        mainActivityRef.OpenInterstitialAd()
    }
}
