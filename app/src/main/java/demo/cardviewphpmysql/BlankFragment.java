package demo.cardviewphpmysql;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BlankFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BlankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private CustomAdapter adapter;
    private List<MyData> data_list;

    private ArrayList<Person> persons = new ArrayList<>();
    private RVAdapter rvAdapter;

    ArrayList titulo = new ArrayList();
    ArrayList descripicon = new ArrayList();

    String retorna;

    private RecyclerView rv;

    RequestQueue requestQueue;
    //String showUrl = "http://undermetal.esy.es/android/getNoticias.php";
    String showUrl = "http://undermetal.esy.es/android/getNoticiaIdCV.php?IdNoticia=";

    public BlankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BlankFragment newInstance(String param1, String param2) {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        //View view = inflater.inflate(R.layout.fragment_blank_fragment_op2, container, false);

        //retorna = "";

        rv=(RecyclerView) view.findViewById(R.id.recycler_view);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        rv.setLayoutManager(gridLayoutManager);
        rv.setHasFixedSize(true);

        //load_data_from_server(0);
        cargaDatos(0, new CallBack() {
            @Override
            public void onSuccess(ArrayList<Person> persons) {
                // Do Stuff
                rvAdapter = new RVAdapter(getContext(),persons);
                rv.setAdapter(rvAdapter);
            }

            @Override
            public void onFail(String msg) {
                // Do Stuff
            }
        });



        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if(gridLayoutManager.findLastCompletelyVisibleItemPosition() == persons.size()-1){
                    cargaDatos(Integer.valueOf(persons.get(persons.size()-1).getId()),new CallBack() {
                        @Override
                        public void onSuccess(ArrayList<Person> persons) {

                             Log.d("ASD: ", String.valueOf(persons.get(persons.size()-1).getId()));
                             rvAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onFail(String msg) {
                            // Do Stuff
                        }
                    });
                }

            }
        });

        return view;
    }

    private void opt1(){
        //recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        data_list  = new ArrayList<>();
        //load_data_from_server(0);



        gridLayoutManager = new GridLayoutManager(getContext(),1);
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter = new CustomAdapter(data_list);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if(gridLayoutManager.findLastCompletelyVisibleItemPosition() == data_list.size()-1){
                    Log.d("Tasando: ",String.valueOf(data_list.get(data_list.size()-1).getId()));
                    Log.d("Tasando: ",String.valueOf(data_list.size()));
                    //load_data_from_server(data_list.get(data_list.size()-1).getId());
                }

            }
        });


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void cargaDatos(int valor, final CallBack onCallBack){

        //showUrl = showUrl + valor;
        Log.d("ASD: ",String.valueOf(valor));

        final ProgressDialog progres = new ProgressDialog(getActivity());
        progres.setMessage("Cargando...");
        progres.show();




        requestQueue = com.android.volley.toolbox.Volley.newRequestQueue(getActivity());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                showUrl+valor, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());

                progres.dismiss();

                try {
                    JSONArray locales = response.getJSONArray("Noticia");
                    for (int i = 0; i < locales.length(); i++) {
                        JSONObject local = locales.getJSONObject(i);

                        persons.add(new Person(local.getString("IdNoticia"), local.getString("cuerpoNoticia")));
                    }
                    //onCallBack.onSuccess(persons);

                } catch (JSONException e) {
                    persons.add(new Person("16","No hay más datos"));
                    e.printStackTrace();
                }

                onCallBack.onSuccess(persons);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.append(error.getMessage());

            }
        });
        requestQueue.add(jsonObjectRequest);

    }

    private void initializeAdapter(){
        RVAdapter adapter = new RVAdapter(getContext(),persons);
        rv.setAdapter(adapter);
    }

    public interface CallBack {
        void onSuccess(ArrayList<Person> persons);

        void onFail(String msg);
    }
}
