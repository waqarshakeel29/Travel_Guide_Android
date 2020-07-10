package Fragments


import Utility.showToast
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.JsonObject
import com.koushikdutta.async.future.FutureCallback
import com.koushikdutta.ion.Ion

import com.waqar.travel_guide.R
import kotlinx.android.synthetic.main.fragment_suggestion.*
import java.lang.Exception

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class Suggestion_Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_suggestion, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_submit_sug.setOnClickListener {

            if(et_suggestion.text.toString() != "") {
                var url = "http://android.fastappdevelopers.com/travel_guide/suggestion.php"

                var jsonObject = JsonObject()
                jsonObject.addProperty("suggestion",et_suggestion.text.toString())
                Log.d("RESPONSE_", "Url -> $url")
                Ion.with(activity)
                    .load("put",url)
                    .setJsonObjectBody(jsonObject)
                    .asJsonObject()
                    .setCallback(object: FutureCallback<JsonObject>{
                        override fun  onCompleted(e: Exception?, result: JsonObject?) {
                            if(result.toString().contains("success")){
                                "Thank You for suggestion!".showToast(activity!!)
                                et_suggestion.setText("")
                            }else{
                                "There is some problem with connection!".showToast(activity!!)
                            }
                            Log.d("RESPONSE_", "RES -> $result")
                        }

                    })
            }

        }

    }

}
