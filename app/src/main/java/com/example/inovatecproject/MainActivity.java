package com.example.inovatecproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener  {
    public static final String TAG_LOG = "LOG_TTS";
    TextView viewStatusoff;
    Button btnEscoltar;
    Button bntEncerrar;
    user user = new user();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO,
                Manifest.permission.SEND_SMS,
        },PackageManager.PERMISSION_GRANTED);

        viewStatusoff = (TextView) findViewById(R.id.status);
        btnEscoltar = (Button) findViewById(R.id.button);
        bntEncerrar =(Button)  findViewById(R.id.button2);

    }
    // captura a fala vinda do microfone
    private void capturarFala() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Fale naturalmente...");

        try{
            startActivityForResult(intent, 10);
        }catch (ActivityNotFoundException a){
            Toast.makeText(this,"Reconhecimento de voz não suportado",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 10){

            if (resultCode == RESULT_OK && null != data) {

                ArrayList<String> result =
                        data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                String capturaFala = result.get(0);

                processarMachineLearning(capturaFala);


            }
        }
    }

    private void processarMachineLearning(String capturaFala) {

        Log.i(TAG_LOG, "processarMachineLearning: " + capturaFala);

        // Desligar

        if (capturaFala.toUpperCase().contains("SOCORRO")) {

            // executar qualquer ação desejada
           sendMenssage();
           enviarEmail();


            //finish();
        }
        if(capturaFala.toUpperCase().contains("LARGA MEU BRAÇO")) {
            sendMenssage();
        }
        if (capturaFala.toUpperCase().contains("VOCÊ NÃO É BEM VINDO AQUI")){
            sendMenssage();
        }
        if (capturaFala.toUpperCase().contains("PARA DE ME BATER")){
            sendMenssage();
        }
    }
        //INICIA A IA DE RECONHECIMENTO DE VOZ
    public void onClick(View view) {
        viewStatusoff.setText("Escolta ativada");
        capturarFala();
    }
    public void encerrarEscolta(View view){
        viewStatusoff.setText("Escolta Desativada");
    }

    public void sendMenssage(){
        //valores da mensagem
        String contatoEmergencia="994616740";
        String menssage="Maria esta em perigo";
        String name = user.name;
        String location =  user.location;
        //checar condições
        if(!contatoEmergencia.equals("") && !menssage.equals("")){
            //inicio sms manager
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(contatoEmergencia,null,menssage,null ,null);
            Toast.makeText(getApplicationContext()
            ,"Pedido de Ajuda enviado",Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(getApplicationContext()
            ,"enter value first",Toast.LENGTH_LONG).show();
        }
    }
    //sms
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode ==100 && grantResults.length>0 && grantResults[0]
        == PackageManager.PERMISSION_GRANTED){
            sendMenssage();
        }else{
            //Toast.makeText(getApplicationContext(),
              //    "permissão negada pelo usuario ",Toast.LENGTH_SHORT).show();
        }


    }
}