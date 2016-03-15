package ua.home.kubic.zilab2;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText key;
    EditText message;
    EditText result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        key = (EditText) findViewById(R.id.key);
        message = (EditText) findViewById(R.id.message);
        result = (EditText) findViewById(R.id.result);

        if (savedInstanceState != null){
            key.setText(savedInstanceState.getString("key"));
            message.setText(savedInstanceState.getString("message"));
            result.setText(savedInstanceState.getString("result"));
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString("key", key.getText().toString());
        outState.putString("message", message.getText().toString());
        outState.putString("result", result.getText().toString());
    }

    public void onClick(View v) {
        Toast toast = null;
        if (key.getText().toString().isEmpty()) {
            if (toast != null) toast.cancel();
            toast = Toast.makeText(this, "Please enter key!", Toast.LENGTH_SHORT);
            toast.show();
            return;
        } else {
            String[] keyArray = key.getText().toString().split("-");
            String str = message.getText().toString();
            switch (v.getId()) {
                case R.id.btn_encrypt:
                    result.setText("" + encrypt(cutString(str, keyArray.length), keyArray));
                    break;
                case R.id.btn_decrypt:
                    result.setText("" + decrypt(decryptArray(str, keyArray), keyArray, str.length()));
                    break;
            }
        }
    }

    private String encrypt(String[] sArray, String[] keyArray) {
        StringBuilder builder = new StringBuilder();
        int index = 0;
        for (int i = 0; i < keyArray.length; i++) {
            for (int j = 0; j < keyArray.length; j++) {
                if (i == Integer.parseInt(keyArray[j]) - 1) {
                    index = j;
                    break;
                }
            }
            for (int j = 0; j < sArray.length; j++) {
                try {
                    builder.append(sArray[j].charAt(index));
                } catch (StringIndexOutOfBoundsException e) {
                    continue;
                }
            }
        }
        return builder.toString();
    }

    private String decrypt(String[] sArray, String[] keyArray, int size) {
        char[] res = new char[size];
        for (int i = 0; i < keyArray.length; i++) {
            for (int j = 0; j < sArray[i].length(); j++) {
                try {
                    res[i + (j *  keyArray.length)] = sArray[(Integer.parseInt(keyArray[i])) - 1].charAt(j);
                } catch (StringIndexOutOfBoundsException e) {
                    continue;
                } catch (ArrayIndexOutOfBoundsException e){
                    continue;
                }
            }
        }
        return String.copyValueOf(res);
    }

    private String[] decryptArray(String s, String[] keyArray) {
        int k = 0;
        String[] sArray = new String[keyArray.length];
        int koef = (int) Math.ceil((double) s.length() / keyArray.length);
        for (int i = 0; i < sArray.length; i++) {
            sArray[i] = s.substring(k, koef);
            k = koef;
            koef += s.length() / keyArray.length;
        }
        return sArray;
    }

    private String[] cutString(String s, int key) {
        String[] array = new String[(int) Math.ceil((double) s.length() / key)];
        for (int i = 0; i < array.length; i++) {
            int koef = key * i;
            if (koef >= s.length() - 2) {
                array[i] = s.substring(koef, s.length());
            } else {
                array[i] = s.substring(koef, key + koef);
            }
        }
        return array;
    }
}
