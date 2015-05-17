package es.unizar.aisolutions.aimovie.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import es.unizar.aisolutions.aimovie.R;
import es.unizar.aisolutions.aimovie.contentprovider.MovieManager;
import es.unizar.aisolutions.aimovie.data.Movie;
import es.unizar.aisolutions.aimovie.email.MailHelper;

public class UserInfo extends ActionBarActivity {
    public static final String EXTRA_MOVIE_ID = "movie_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        final MovieManager mgr = new MovieManager(this);
        final long idMovie = this.getIntent().getExtras().getLong(EXTRA_MOVIE_ID);
        final Movie m = mgr.fetchMovie(idMovie);


        Button acceptButton = (Button) findViewById(R.id.activity_user_info_button_order);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<Movie, Void, Boolean>() {
                    @Override
                    protected Boolean doInBackground(Movie... title) {
                        TextView mail = (TextView) findViewById(R.id.activity_user_info_email);
                        TextView name = (TextView) findViewById(R.id.activity_user_info_name);
                        TextView surname = (TextView) findViewById(R.id.activity_user_info_surname);

                        MailHelper mh = new MailHelper(mail.getText().toString());
                        mh.fillEmail(name.getText().toString(), surname.getText().toString(), m.getTitle());
                        Boolean ok = mh.sendMail("[Order] Movie Rented");

                        m.setStock(m.getStock() - 1);
                        m.setRented(m.getRented() + 1);
                        mgr.updateMovie(m);

                        return ok;
                    }

                    @Override
                    protected void onPostExecute(Boolean b) {
                        String message = b ? "Order sent" : "Order not sent";
                        AlertDialog.Builder builder = new AlertDialog.Builder(UserInfo.this);
                        builder.setTitle("Mail");
                        final TextView ms = new TextView(UserInfo.this);
                        ms.setText(message);
                        builder.setView(ms);
                        builder.setPositiveButton(getString(R.string.accept), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                UserInfo.this.finish();
                            }
                        });
                        builder.show();
                    }
                }.execute(m);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
