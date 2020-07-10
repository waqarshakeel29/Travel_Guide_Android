package Fragments


import Models.Main_Menu_Model
import Utility.GPS_LocationCheck
import adapters.Main_Menu_Adapter
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.waqar.travel_guide.Exit_Activity
import com.waqar.travel_guide.MainActivity
import com.waqar.travel_guide.R
import kotlinx.android.synthetic.main.fragment_main_menu.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class Main_Menu_Fragment : Fragment() {


    lateinit var cntxt: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cntxt = activity!!
        mmRecyclerView.layoutManager = androidx.recyclerview.widget.GridLayoutManager(
            activity!!,
            2
        )
//        mmRecyclerView.layoutManager = androidx.recyclerview.widget.StaggeredGridLayoutManager(
//            2,1
//        )


        //Creating MAIN MENU
        var list = mutableListOf<Main_Menu_Model>()
        list.add(
            Main_Menu_Model(
                R.drawable.my_location, cntxt.resources.getString(
                    R.string.MyLocation
                )
            )
        )
        list.add(
            Main_Menu_Model(
                R.drawable.near_by, cntxt.resources.getString(
                    R.string.NearBy
                )
            )
        )
        //list.add(Main_Menu_Model(R.drawable.favorite, cntxt.resources.getString(R.string.Favorite)))
//        list.add(
//            Main_Menu_Model(
//                R.drawable.find_route, cntxt.resources.getString(
//                    R.string.FindRoute
//                )
//            )
//        )
        list.add(
            Main_Menu_Model(
                R.drawable.wonder_of_world, cntxt.resources.getString(
                    R.string.Wonder
                )
            )
        )
        list.add(
            Main_Menu_Model(
                R.drawable.sug, cntxt.resources.getString(
                    R.string.suggestion
                )
            )
        )
//        list.add(
//            Main_Menu_Model(
//                R.drawable.share, cntxt.resources.getString(
//                    R.string.Share
//                )
//            )
//        )
//        list.add(
//            Main_Menu_Model(
//                R.drawable.rating, cntxt.resources.getString(
//                    R.string.Rating
//                )
//            )
//        )
//        list.add(
//            Main_Menu_Model(
//                R.drawable.privacy_policy, cntxt.resources.getString(
//                    R.string.Privacy_Policy
//                )
//            )
//        )
//        list.add(
//            Main_Menu_Model(
//                R.drawable.more_apps1, cntxt.resources.getString(
//                    R.string.More_Apps
//                )
//            )
//        )
        list.add(
            Main_Menu_Model(
                R.drawable.exit, cntxt.resources.getString(
                    R.string.Exit
                )
            )
        )


        //Setting RecyclerView f Main Menu
        mmRecyclerView.adapter = Main_Menu_Adapter(activity!!, list)
        {

            if (it.title == list[0].title) {
                //GPS CHECK
                //Application_Class.instance!!.startLocationService()
                if (MainActivity.permission()) {
                    var manager: LocationManager =
                        activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager

                    if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        (activity as MainActivity).fragmentTransaction(
                            CurrentLocation(), list[0].title
                        )
                    } else{}
                        GPS_LocationCheck(
                            activity!!
                        )

                }


            } else if (it.title == list[1].title) {
                if (MainActivity.permission()) {
                    var manager: LocationManager =
                        activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager

                    if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        (activity as MainActivity).fragmentTransaction(
                            Nearby_Items_Fragment(), list[1].title
                        )
                    } else {
                    }
                    GPS_LocationCheck(
                        activity!!
                    )

                }
            }

//            else if (it.title == list[2].title)
//                (activity as MainActivity).fragmentTransaction(Favorite(),list[2].title)
//            else if (it.title == list[2].title) {
//
//                if (MainActivity.permission()) {
//
//                    var manager: LocationManager =
//                        activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
//                    if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//                        (activity as MainActivity).fragmentTransaction(
//                            Route_Fragment(), list[2].title
//                        )
//                    } else{}
////                        GPS_LocationCheck(
////                            activity!!
////                        )
//                }
//            }
            else if (it.title == list[2].title) {
                (activity as MainActivity).fragmentTransaction(
                    Wonder_of_World(), list[2].title
                )
            } else if (it.title == list[3].title) {
                (activity as MainActivity).fragmentTransaction(
                    Suggestion_Fragment(), list[3].title
                )
            }
//            else if (it.title == list[4].title)
//                Share()
//            else if (it.title == list[5].title) {
//                val appPackageName = cntxt.packageName // getPackageName() from Context or Activity object
////                OpenPlayStoreUrl("https://play.google.com/store/apps/details?id=$appPackageName")
//            } else if (it.title == list[6].title)
////                OpenPlayStoreUrl("https://expertisezain.000webhostapp.com/Near-By-Places-And-Route-Finder/")
//            else if (it.title == list[7].title)
////                OpenPlayStoreUrl("https://play.google.com/store/apps/developer?id=Faran+Iqbal")
            else
                exit()
        }


    }

    fun Share() {
        val Body = "Download \"Near By Places and Route Finder Free\"\nGet it from here https://bit.ly/2xj31lZ"
        val callingIntent = Intent(android.content.Intent.ACTION_SEND)
        callingIntent.setType("text/plain")
        callingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Body)
        cntxt.startActivity(callingIntent)
    }

    fun OpenPlayStoreUrl(url: String) {
        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(url)
                )
            )
        } catch (e: Exception) {
            Log.d("TAG", "OpenPlayStoreUrl Exp:\n" + e.message)
        }
    }

    fun exit() {
        val builder = AlertDialog.Builder(cntxt)
        builder.setTitle("Alert")
        builder.setMessage("Do you really want to exit ?")
        builder.setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
        builder.setPositiveButton("Yes") { dialog, _ ->
            dialog.dismiss()
//            Exit_Activity.exitApplicationAnRemoveFromRecent(
//                cntxt
//            )
            activity!!.onBackPressed()
        }
        builder.create()
        builder.show()
    }

}
