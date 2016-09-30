package demo.cardviewphpmysql;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Detalle extends AppCompatActivity {

    private TextView textview;
    private ImageView img;
    private Animation anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        agregarToolbar();

        Intent intent = getIntent();
        String Datos = intent.getExtras().getString("descrip");

        textview = (TextView)findViewById(R.id.textView);
        img = (ImageView)findViewById(R.id.imageView);

        anim = AnimationUtils.loadAnimation(this,R.anim.fade_in);


        img.setAnimation(anim);
        textview.setText(Datos);
    }

    private void agregarToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setDisplayShowHomeEnabled(true);
        }
    }
}
