package Fragments


import Utility.startLocationService
import Utility.stopLocationService
import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.tramsun.libs.prefcompat.Pref
import com.waqar.travel_guide.GetLocation_Service
import com.waqar.travel_guide.MainActivity
import com.waqar.travel_guide.R
import kotlinx.android.synthetic.main.fragment_current_location.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
open class CurrentLocation : androidx.fragment.app.Fragment(), OnMapReadyCallback {

    private var mMap: GoogleMap? = null
    var Lati: Double = 0.0
    var Longi: Double = 0.0



    open val mBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            //Toast.makeText(context,"Received", Toast.LENGTH_LONG).show()
            Lati = intent.getDoubleExtra("Latitude", 0.0)
            Longi = intent.getDoubleExtra("Longitude", 0.0)


        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_current_location, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //Click Listeners
        onClickCurrentLocationButton()
//        onClickRouteButton()

        //Load Current Location from Pref
        if (Lati == 0.0 && Longi == 0.0)
        {
            loadLocation()
        }
        //Show and load Ad
//        val mainActivityRef = activity!! as MainActivity
//        mainActivityRef.OpenInterstitialAd()

        //Start Service
        MainActivity.instance.startLocationService()
    }





    fun loadLocation() {
        Lati = Pref.getDouble(GetLocation_Service.LOCATION_UPDATE_LATITUDE, 0.0)
        Longi = Pref.getDouble(GetLocation_Service.LOCATION_UPDATE_LONGITUDE, 0.0)
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this@CurrentLocation)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {

        if (mMap != null)
            mMap!!.clear()

        mMap = googleMap
        //mMap!!.isMyLocationEnabled = true

        val location = LatLng(Lati, Longi)
        addMarkerAt(location)
        animateCameraAt(location)
    }

    open fun onClickRouteButton() {

        clFloatingButton.setOnClickListener {
            intentForRoute(Lati, Longi)
        }
    }

    open fun onClickCurrentLocationButton() {
        if (mMap != null)
            mMap!!.clear()

        clCurrentLocation.setOnClickListener {
            loadLocation()
        }
    }

    fun intentForRoute(fromLatitude: Double, fromLongitude: Double) {

        val mainActivityRef = activity!! as MainActivity
        mainActivityRef.OpenInterstitialAd("https://maps.google.com/maps?saddr=$fromLatitude,$fromLongitude&daddr=&#8221")
        //"https://maps.google.com/maps?saddr=" + "$fromLatitude,$fromLongitude&daddr=" + "&#8221"

    }

    fun addMarkerAt(latLng: LatLng) {
        if (mMap != null)
            mMap!!.clear()

        mMap!!.addMarker(
            MarkerOptions().position(latLng).draggable(false).icon(
                BitmapDescriptorFactory.defaultMarker(
                    BitmapDescriptorFactory.HUE_RED
                )
            ).alpha(0.7f).            //OPACITY
                flat(false).            //Flattens the icon
                //anchor(0.5f,0.5f).    //0.5,0.5 makes the center of the icon its base point
                //rotation(90f).
                //zIndex(10.0f).
                title("YOUR LOCATION").snippet("I am here!")
        ).showInfoWindow()

    }

    fun animateCameraAt(latLng: LatLng) {
        val cameraPosition = CameraPosition.Builder()
            .target(latLng)      // Sets the center of the map to Mountain View
            .zoom(15f)                   // Sets the zoom
            .bearing(90f)                // Sets the orientation of the camera to east
            .tilt(30f)                   // Sets the tilt of the camera to 30 degrees
            .build()
        mMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(activity!!)
            .registerReceiver(mBroadcastReceiver, IntentFilter("LocationUpdates"))
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(activity!!).unregisterReceiver(mBroadcastReceiver)
    }


    override fun onDestroy() {
        super.onDestroy()
        MainActivity.instance.stopLocationService()
    }
}
