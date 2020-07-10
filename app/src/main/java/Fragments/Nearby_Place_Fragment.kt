package Fragments

import Utility.DataParser
import Utility.showToast
import Utility.startLocationService
import Utility.stopLocationService
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.common.api.Status
import com.google.android.gms.dynamic.IObjectWrapper
import com.google.android.gms.location.places.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.PlaceLikelihood
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.koushikdutta.async.future.FutureCallback
import com.koushikdutta.ion.Ion
import com.tramsun.libs.prefcompat.Pref
import com.waqar.travel_guide.GetLocation_Service
import com.waqar.travel_guide.MainActivity
import com.waqar.travel_guide.R
import kotlinx.android.synthetic.main.fragment_current_location.*
import kotlinx.android.synthetic.main.fragment_current_location.clCurrentLocation
import kotlinx.android.synthetic.main.fragment_current_location.clFloatingButton
import kotlinx.android.synthetic.main.fragment_nearby__place.*
import org.json.JSONObject
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


/**
 * A simple [Fragment] subclass.
 */
open class Nearby_Place_Fragment : Fragment(), OnMapReadyCallback {

//    lateinit var geoDataClient: GeoDataClient
//    lateinit var placeDetectionClient : PlaceDetectionClient
    lateinit var firstMarker : LatLng
    lateinit var list : ArrayList<LatLng>

    var nameReview = ""
    var placeId = ""
    lateinit var  placesClient: PlacesClient
    var placeField : List<Place.Field> = Arrays.asList(Place.Field.ID,Place.Field.NAME,Place.Field.ADDRESS)

    var mMap: GoogleMap? = null
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
        return inflater.inflate(R.layout.fragment_nearby__place, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the SDK
        Places.initialize(activity!!, activity!!.getString(R.string.google_maps_key))
        // Create a new Places client instance
        placesClient = Places.createClient(activity!!)

        //ClickListener
        onClickRouteButton()

        //Load Current Location from Pref
        if (Lati == 0.0 && Longi == 0.0)
        {
            loadLocation()
        }

        //Start Service
        MainActivity.instance.startLocationService()
    }


    fun loadLocation() {
        Lati = Pref.getDouble(GetLocation_Service.LOCATION_UPDATE_LATITUDE, 0.0)
        Longi = Pref.getDouble(GetLocation_Service.LOCATION_UPDATE_LONGITUDE, 0.0)
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this@Nearby_Place_Fragment)
        list = ArrayList()
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {

        if (mMap != null)
            mMap!!.clear()

        mMap = googleMap
        //mMap!!.isMyLocationEnabled = true

        var keyWord = arguments?.get("KEYWORD").toString()
        var url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+Lati+","+Longi+"&radius=1500&type="+keyWord+"&key="+resources.getString(R.string.google_maps_key)

        Ion.with(activity)
            .load(url)
            .asJsonObject()
            .setCallback(object: FutureCallback<JsonObject>{
                override fun onCompleted(e: Exception?, result: JsonObject?) {
                    if(result != null){
                        var resultArray = result.getAsJsonArray("results")
                        for(i in resultArray){

                            Log.d("TAG_RESULT", i.asJsonObject.get("name").toString())
                            var lat = i.asJsonObject.get("geometry").asJsonObject.get("location").asJsonObject.get("lat").asDouble
                            var lng = i.asJsonObject.get("geometry").asJsonObject.get("location").asJsonObject.get("lng").asDouble
                            addMarkers(LatLng(lat,lng),i.asJsonObject.get("name").toString())
                            nameReview = i.asJsonObject.get("name").toString()
                            firstMarker = LatLng(lat,lng)
                            Log.d("TAG_RESULT", i.asJsonObject.get("geometry").asJsonObject.get("location").toString())
                        }
                    }else{
                        Log.d("TAG_RESULT","No Result Found!")
                    }
                }
            })


        mMap!!.setOnMarkerClickListener{
            it.showInfoWindow()
            if(list != null)
                list.clear()
            list.add(LatLng(Lati,Longi))
            list.add(it.position)
            nameReview = it.title
            true
//
//            var urlDetail = "https://maps.googleapis.com/maps/api/place/details/json?place_id=ChIJN1t_tDeuEmsRUsoyG83frY4&fields=name,rating,formatted_phone_number&key=YOUR_API_KEY"
//            Ion.with(activity!!)
//                .load("")
//            it.snippet
        }
    }

    open fun onClickRouteButton() {

        clFloatingButtonReview.setOnClickListener {

            if(nameReview != ""){
                (activity!! as MainActivity).OpenInterstitialAd("geo:0,0?q=" + nameReview + "")
            }

        }

        clFloatingButton.setOnClickListener{

            if(list.isEmpty()){
                list.add(LatLng(Lati,Longi))
                list.add(firstMarker)
            }

            var url = getUrl(list[0],list[1])
            Ion.with(activity)
                .load(url)
                .asString()
                .setCallback(object: FutureCallback<String>{
                    override fun onCompleted(e: Exception?, result: String?) {
                        if(result != null){
//                            Log.d("DIRECTIONN",result)
                            mMap!!.clear()

                            TaskParser().execute(result)
                        }else{
                            Log.d("DIRECTIONN","RESULT IS NULL")
                        }

                    }

                })
//            intentForRoute(Lati, Longi)
        }
    }

    fun intentForRoute(fromLatitude: Double, fromLongitude: Double) {

        val mainActivityRef = activity!! as MainActivity
        mainActivityRef.OpenInterstitialAd("https://maps.google.com/maps?saddr=$fromLatitude,$fromLongitude&daddr=&#8221")
        //"https://maps.google.com/maps?saddr=" + "$fromLatitude,$fromLongitude&daddr=" + "&#8221"

    }

    fun addMarkers(latLng: LatLng,name : String) {

        var max = 4.5
        var min = 2.0

        mMap!!.addMarker(
            MarkerOptions().position(latLng).draggable(false).icon(
                BitmapDescriptorFactory.defaultMarker(
                    BitmapDescriptorFactory.HUE_RED
                )
            ).alpha(0.7f).            //OPACITY
                flat(false).            //Flattens the icon
                anchor(0.5f,0.5f).    //0.5,0.5 makes the center of the icon its base point
//                rotation(90f).
                //zIndex(10.0f).
                title(name).snippet("Rating: " + (String.format("%.1f", Random().nextFloat() * (max - min) + min)) +" -> Details")
        ).showInfoWindow()

        val cameraPosition = CameraPosition.Builder()
            .target(latLng)      // Sets the center of the map to Mountain View
            .zoom(15f)                   // Sets the zoom
            .bearing(90f)                // Sets the orientation of the camera to east
            .tilt(30f)                   // Sets the tilt of the camera to 30 degrees
            .build()
        mMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

    }

    fun addMarkerAt(latLng: LatLng,title : String) {

        mMap!!.addMarker(
            MarkerOptions().position(latLng).draggable(false).icon(
                BitmapDescriptorFactory.defaultMarker(
                    BitmapDescriptorFactory.HUE_RED
                )
            ).alpha(0.7f).            //OPACITY
                flat(false)         //Flattens the icon
                //anchor(0.5f,0.5f).    //0.5,0.5 makes the center of the icon its base point
                //rotation(90f).
                //zIndex(10.0f)
                .title(title)
        ).showInfoWindow()
    }

    fun animateCameraAt(latLng: LatLng) {
        val cameraPosition = CameraPosition.Builder()
            .target(latLng)      // Sets the center of the map to Mountain View
            .zoom(16f)                   // Sets the zoom
            .bearing(80f)                // Sets the orientation of the camera to east
            .tilt(60f)                   // Sets the tilt of the camera to 30 degrees
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



    fun getUrl(origin: LatLng,dest: LatLng) : String {

        Log.d("DIRECTIONN",origin.toString() + "____" + dest.toString())

        var org = "origin=" + origin.latitude + "," + origin.longitude
        var des = "destination=" + dest.latitude + "," + dest.longitude
        var sensor = "sensor=false"
        var mode = "mode=driving"
        var key = "key=${resources.getString(R.string.google_maps_key)}"
        var params = "$org&$des&$sensor&$mode&$key"
        var output = "json"
        var url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + params
        return url
    }


    inner class TaskParser : AsyncTask<String, Void, List<List<HashMap<String, String>>>>() {
        override fun doInBackground(vararg strings: String?): List<List<HashMap<String, String>>> {

//            Log.d("DIRECTIONN",strings[0])
            var jsonObject = JSONObject(strings[0])
            var dataParser = DataParser()
            return dataParser.parse(jsonObject)

        }

        override fun onPostExecute(result: List<List<HashMap<String, String>>>?) {

//            Log.d("DIRECTIONN",result.toString())
            var points : ArrayList<LatLng>? = null
            var polylineOptions: PolylineOptions? = null

            for(path in result!!){
                Log.d("DIRECTIONN",path.toString())
                points = ArrayList()
                polylineOptions = PolylineOptions()

                for(point in path){
                    var lat = point.get("lat")!!.toDouble()
                    var lng = point.get("lng")!!.toDouble()

                    points.add(LatLng(lat,lng))
                }

                Log.d("DIRECTIONN",points.toString())
                polylineOptions.addAll(points)
                polylineOptions.width(15f)
                polylineOptions.color(Color.BLUE)
                polylineOptions.geodesic(true)

            }

            if(polylineOptions != null){
//                Log.d("DIRECTIONN","PLY___ " + polylineOptions.toString())
                addMarkerAt(list[0],"Source")
                addMarkerAt(list[1],"Destination")
                mMap!!.addPolyline(polylineOptions!!)
                animateCameraAt(list[0])
            }else{
                "Direction Not Found".showToast(activity!!)
            }
        }
    }
}


