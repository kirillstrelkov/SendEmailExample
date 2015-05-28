package ee.kirill.sendemailexample;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class SendEmailActivity extends Activity {

    public static final String TAG = "SendEmailActivity";
    public static final int REQUEST_CODE = 0;
    private EditText textSubject;
    private EditText textTo;
    private EditText textMessage;
    private TextView textAttachedFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);

        textSubject = (EditText) findViewById(R.id.textSubject);
        textTo = (EditText) findViewById(R.id.textTo);
        textMessage = (EditText) findViewById(R.id.textMessage);
        textAttachedFile = (TextView) findViewById(R.id.textAttachedFile);
    }

    public void attachFile(View view) {
        Log.v(TAG, "Attaching file");
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(Intent.createChooser(intent, "Attach file"), REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            textAttachedFile.setText(uri.toString());
        }
    }

    public void sendMessage(View view) {
        Log.v(TAG, "Sending textMessage");

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{textTo.getText().toString()});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, textSubject.getText().toString());
        emailIntent.putExtra(Intent.EXTRA_TEXT, textMessage.getText().toString());
        String filePath = textAttachedFile.getText().toString();
        if (!filePath.equals("") && !filePath.equals("No file")) {
            emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(textAttachedFile.getText().toString()));
        }

        try {
            startActivity(Intent.createChooser(emailIntent, "Send email"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
