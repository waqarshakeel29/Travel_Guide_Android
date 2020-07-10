package Fragments


import Models.Main_Menu_Model
import Utility.showToast
import adapters.Main_Menu_Adapter
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.waqar.travel_guide.MainActivity
import com.waqar.travel_guide.R
import kotlinx.android.synthetic.main.fragment_nearby_items.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class Nearby_Items_Fragment : Fragment() {


    var list = mutableListOf<Main_Menu_Model>()
    lateinit var cntxt: Context


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nearby_items, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        nbRecyclerView = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.nbRecyclerView)
        nbRecyclerView.layoutManager = androidx.recyclerview.widget.GridLayoutManager(activity!!, 2)
        cntxt = activity!!

        list.add(
            Main_Menu_Model(
                R.drawable.restaurant, cntxt.resources.getString(
                    R.string.Restaurants
                )
            )
        )
        list.add(
            Main_Menu_Model(
                R.drawable.atm, cntxt.resources.getString(
                    R.string.ATM
                )
            )
        )
        list.add(
            Main_Menu_Model(
                R.drawable.bank, cntxt.resources.getString(
                    R.string.Banks
                )
            )
        )
        list.add(
            Main_Menu_Model(
                R.drawable.petrol_station, cntxt.resources.getString(
                    R.string.petrol_station
                )
            )
        )
        list.add(
            Main_Menu_Model(
                R.drawable.cng_station, cntxt.resources.getString(
                    R.string.CNG_Station
                )
            )
        )
        list.add(
            Main_Menu_Model(
                R.drawable.diesel_station, cntxt.resources.getString(
                    R.string.Diesel_Station
                )
            )
        )
        list.add(
            Main_Menu_Model(
                R.drawable.hospital, cntxt.resources.getString(
                    R.string.Hospitals
                )
            )
        )
        list.add(
            Main_Menu_Model(
                R.drawable.pharmacy, cntxt.resources.getString(
                    R.string.Pharmacy
                )
            )
        )
        list.add(
            Main_Menu_Model(
                R.drawable.airport, cntxt.resources.getString(
                    R.string.Airports
                )
            )
        )
        list.add(
            Main_Menu_Model(
                R.drawable.metro_station, cntxt.resources.getString(
                    R.string.Metro_Station
                )
            )
        )
        list.add(
            Main_Menu_Model(
                R.drawable.subway, cntxt.resources.getString(
                    R.string.Subway
                )
            )
        )
        list.add(
            Main_Menu_Model(
                R.drawable.bus_stop, cntxt.resources.getString(
                    R.string.Bus_stop
                )
            )
        )
        list.add(
            Main_Menu_Model(
                R.drawable.police, cntxt.resources.getString(
                    R.string.Police_Station
                )
            )
        )
        list.add(
            Main_Menu_Model(
                R.drawable.fire_station, cntxt.resources.getString(
                    R.string.Fire_station
                )
            )
        )
//        list.add(
//            Main_Menu_Model(
//                R.drawable.old_age_house, cntxt.resources.getString(
//                    R.string.Old_Age_House
//                )
//            )
//        )
        list.add(
            Main_Menu_Model(
                R.drawable.orphanage, cntxt.resources.getString(
                    R.string.Orphanage
                )
            )
        )
        list.add(
            Main_Menu_Model(
                R.drawable.parking, cntxt.resources.getString(
                    R.string.Parking
                )
            )
        )
        list.add(
            Main_Menu_Model(
                R.drawable.disco, cntxt.resources.getString(
                    R.string.Disco
                )
            )
        )
        list.add(
            Main_Menu_Model(
                R.drawable.bar, cntxt.resources.getString(
                    R.string.Bar
                )
            )
        )
        list.add(
            Main_Menu_Model(
                R.drawable.country_club, cntxt.resources.getString(
                    R.string.Country_Club
                )
            )
        )
//        list.add(
//            Main_Menu_Model(
//                R.drawable.hotel, cntxt.resources.getString(
//                    R.string.Hotel
//                )
//            )
//        )
        list.add(
            Main_Menu_Model(
                R.drawable.apartment, cntxt.resources.getString(
                    R.string.Apartment
                )
            )
        )
//        list.add(
//            Main_Menu_Model(
//                R.drawable.cinema, cntxt.resources.getString(
//                    R.string.Cinema
//                )
//            )
//        )
        list.add(
            Main_Menu_Model(
                R.drawable.shopping_mall, cntxt.resources.getString(
                    R.string.Shopping_Mall
                )
            )
        )
        list.add(
            Main_Menu_Model(
                R.drawable.mart, cntxt.resources.getString(
                    R.string.Mart
                )
            )
        )
        list.add(
            Main_Menu_Model(
                R.drawable.stadium, cntxt.resources.getString(
                    R.string.Stadium
                )
            )
        )
//        list.add(
//            Main_Menu_Model(
//                R.drawable.school, cntxt.resources.getString(
//                    R.string.School
//                )
//            )
//        )
        list.add(
            Main_Menu_Model(
                R.drawable.college, cntxt.resources.getString(
                    R.string.College
                )
            )
        )
//        list.add(
//            Main_Menu_Model(
//                R.drawable.university, cntxt.resources.getString(
//                    R.string.University
//                )
//            )
//        )
        list.add(
            Main_Menu_Model(
                R.drawable.court, cntxt.resources.getString(
                    R.string.Court
                )
            )
        )


        nbRecyclerView.adapter = Main_Menu_Adapter(activity!!, list) {

            it.title.toLowerCase().showToast(activity!!)
            (activity!! as MainActivity).fragmentTransaction(Nearby_Place_Fragment(),"Nearby_Place_Fragment",it.title.toLowerCase())
//            val mainActivityRef = activity!! as MainActivity
//            mainActivityRef.OpenInterstitialAd("geo:0,0?q=" + it.title + "")
        }

//        val mainActivityRef = activity!! as MainActivity
//        mainActivityRef.OpenInterstitialAd()
    }
}
