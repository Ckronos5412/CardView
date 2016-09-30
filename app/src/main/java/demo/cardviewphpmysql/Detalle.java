package demo.cardviewphpmysql;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Detalle extends AppCompatActivity {

    private TextView textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        Intent intent = getIntent();
        String Datos = intent.getExtras().getString("descrip");

        textview = (TextView)findViewById(R.id.textView);

        textview.setText(Datos);
    }
}
