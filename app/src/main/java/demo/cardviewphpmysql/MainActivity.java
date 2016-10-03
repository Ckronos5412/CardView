package demo.cardviewphpmysql;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, BlankFragment.OnFragmentInteractionListener{

    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private CustomAdapter adapter;
    private ArrayList<MyData> data_list;

    private Integer str;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.content, new BlankFragment());
        tx.commit();
    }

    private void cargaTodo(){
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        data_list  = new ArrayList<>();

        gridLayoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(gridLayoutManager);

        load_data_from_server(0);

        adapter = new CustomAdapter(data_list);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if(gridLayoutManager.findLastCompletelyVisibleItemPosition() == data_list.size()-1){
                    Log.d("Tasando: ",String.valueOf(data_list.get(data_list.size()-1).getId()));
                    Log.d("Tasando: ",String.valueOf(data_list.size()));
                    load_data_from_server(data_list.get(data_list.size()-1).getId());
                }

            }
        });
    }

    private void load_data_from_server(final int id) {

            AsyncTask<Integer,Void,Void> task = new AsyncTask<Integer, Void, Void>() {
                @Override
                protected Void doInBackground(Integer... integers) {

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://undermetal.esy.es/android/getNoticiaIdCV.php?IdNoticia="+integers[0])
                            .build();

                    try {
                        Response response = client.newCall(request).execute();

                        JSONArray array = new JSONArray(response.body().string());

                        for (int i=0; i<array.length(); i++){

                            JSONObject object = array.getJSONObject(i);

                            MyData data = new MyData(object.getInt("IdNoticia"),object.getString("cuerpoNoticia"),
                                    "http://undermetal.esy.es/resourses/images/noticia/" + object.getString("IdNoticia") + ".jpg");

                            data_list.add(data);
                        }

                        //onCallBack.onSuccess(data_list);



                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        System.out.println("End of content");
                        System.out.println(e.toString());
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    adapter.notifyDataSetChanged();
                }
            };

        task.execute(id);
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public interface CallBack {
        void onSuccess(ArrayList<MyData> data_list);

        void onFail(String msg);
    }
}
