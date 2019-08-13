package acme.burhanshakir.com.acmesignatureapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements DrawingListener {

    DrawingView drawingView ;
    ConstraintLayout layout;
    static final Integer WRITE_EXST = 0x3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        drawingView = findViewById(R.id.drawingView);
        layout = findViewById(R.id.drawing_layout);
        askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,WRITE_EXST);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){

            case R.id.action_refresh:

                drawingView.clearDrawingCanvas();
                break;

            case R.id.action_save:

                drawingView.saveSignature(this);
                break;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void showErrorMsgOnSave() {
        Toast.makeText(this, "Please draw a signature first to save.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMsgOnSave() {
        Toast.makeText(this, "Signature saved!", Toast.LENGTH_SHORT).show();
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
            }
        } else {
            Log.d("Permission Granted","WRITING EXTERNAL MEMORY");
        }
    }
}
