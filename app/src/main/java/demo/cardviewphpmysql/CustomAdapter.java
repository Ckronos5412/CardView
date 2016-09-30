package demo.cardviewphpmysql;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by filipp on 9/16/2016.
 */
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private Context context;
    private List<MyData> my_data;

    public CustomAdapter(Context context, List<MyData> my_data) {
        this.context = context;
        this.my_data = my_data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card,parent,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

            holder.description.setText(my_data.get(position).getDescription());
        Glide.with(context).load(my_data.get(position).getImage_link()).into(holder.imageView);



    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }


    public  class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView description;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            description = (TextView) itemView.findViewById(R.id.description);
            imageView = (ImageView) itemView.findViewById(R.id.image);
        }

        @Override
        public void onClick(View v) {
            TextView textView = (TextView) v.findViewById(R.id.description);
            String text = textView.getText().toString();
            Toast toast = Toast.makeText(v.getContext(), text, Toast.LENGTH_LONG);
            //toast.show();

            Intent intent = new Intent(v.getContext(),Detalle.class);
            intent.putExtra("descrip",text);
            v.getContext().startActivity(intent);
        }
    }
}
