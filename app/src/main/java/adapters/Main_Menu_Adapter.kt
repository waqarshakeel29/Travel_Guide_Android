package adapters

import Models.Main_Menu_Model
import Utility.inflate
import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.waqar.travel_guide.R
import kotlinx.android.synthetic.main.main_menu_row.view.*


class Main_Menu_Adapter(
    val mContext: Context,
    val list: List<Main_Menu_Model>,
    var listener: (Main_Menu_Model) -> Unit
) : androidx.recyclerview.widget.RecyclerView.Adapter<Main_Menu_Adapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Main_Menu_Adapter.ViewHolder {

        return ViewHolder(parent.inflate(R.layout.main_menu_row))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Main_Menu_Adapter.ViewHolder, position: Int) {
        holder.bind(list[position], listener)
        Log.d("TAG", "" + list.get(position).title + " --- " + list.get(position).reference)
//        setAnimation(holder.itemView)
    }

    private fun setAnimation(viewToAnimate: View) {
        // If the bound view wasn't previously displayed on screen, it's animated
//        if (position > lastPosition) {
        val animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left)
        viewToAnimate.startAnimation(animation)

//        }
    }

    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        //PARAMENTER   //RETURN_TYPE
        fun bind(item: Main_Menu_Model, listener: (Main_Menu_Model) -> Unit) = with(itemView)
        {
            mmTextView.text = item.title
//            mmImageView.setImageResource(item.reference)
            Glide.
                with(context).
                load(item.reference).
                diskCacheStrategy(DiskCacheStrategy.RESULT).
                placeholder(R.drawable.logo).
                    into(mmImageView)

            setOnClickListener { listener(item) }
        }
    }
}