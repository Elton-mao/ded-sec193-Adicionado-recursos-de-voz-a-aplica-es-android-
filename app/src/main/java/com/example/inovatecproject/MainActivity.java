package com.example.inovatecproject;


import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener  {
    public static final String TAG_LOG = "LOG_TTS";
    TextView viewStatusoff;
    Button btnEscoltar;
    Button bntEncerrar;
    user user = new user();
    TextToSpeech textToSpeech;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO,
                Manifest.permission.SEND_SMS
        },PackageManager.PERMISSION_GRANTED);

        viewStatusoff = (TextView) findViewById(R.id.status);
        btnEscoltar = (Button) findViewById(R.id.button);
        bntEncerrar =(Button)  findViewById(R.id.button2);
        textToSpeech = new TextToSpeech(getApplicationContext(),new TextToSpeech.OnInitListener(){
            @Override
            public void onInit(int status) {
            if(status != TextToSpeech.ERROR){
                textToSpeech.setLanguage(new Locale("pt", "BR"));
                setTextToSpeech("Seja bem-vindo ao Nubios App, a plataforma dedicada a auxiliar pessoas com deficiência visual a pegar ônibus com facilidade.click na tela e fale naturalmete o seu destino ");
            }
            }
        });

    }
    public void setTextToSpeech(String text) {
        if (textToSpeech != null && !text.isEmpty()) {
            String utteranceId = UUID.randomUUID().toString();

            Bundle params = new Bundle();
            params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId);

            float speechRate = 1.0f; // Defina a velocidade da fala aqui
            textToSpeech.setSpeechRate(speechRate);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                    @Override
                    public void onStart(String utteranceId) {
                        // O texto de fala começou a ser reproduzido
                    }

                    @Override
                    public void onDone(String utteranceId) {
                        // O texto de fala foi reproduzido completamente
                    }

                    @Override
                    public void onError(String utteranceId) {
                        // Ocorreu um erro na reprodução do texto de fala
                    }
                });
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, params, utteranceId);
            } else {
                HashMap<String, String> hashMapParams = new HashMap<>();
                hashMapParams.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId);
                textToSpeech.setOnUtteranceCompletedListener(new TextToSpeech.OnUtteranceCompletedListener() {
                    @Override
                    public void onUtteranceCompleted(String utteranceId) {
                        // O texto de fala foi reproduzido completamente
                    }
                });
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, hashMapParams);
            }
        }
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

        if (capturaFala.toUpperCase().contains("LÍRIO DO VALE")) {
            // executar qualquer ação desejada
            sendMenssage();
            setTextToSpeech("A linha de ônibus que passa no LIRIO DO VALE é a 405. 652. A MELHOR OPÇÃO É O 405, QUE CHEGARA EM 10 MINUTOS. Peça assistência, se necessário");
            //finish();

        }
        if(capturaFala.toUpperCase().contains("CENTRO")) {
            sendMenssage();
            setTextToSpeech("A linha de ônibus que passa no centro é a 604. 706. A MELHOR OPÇÃO É O 604, QUE CHEGARA EM 3 MINUTOS. Peça assistência, se necessário");
        }
        if (capturaFala.toUpperCase().contains("CACHOEIRINHA")){
            sendMenssage();
            setTextToSpeech("A linha de ônibus que passa na cachoeirinha é a 401. 644. A MELHOR OPÇÃO É O 401, QUE CHEGARA EM 5 MINUTOS. Peça assistência, se necessário");
        }
        if (capturaFala.toUpperCase().contains("COMPENSA")){
            sendMenssage();
            setTextToSpeech("A linha de ônibus que passa na compensa é a 405. 652 A MELHOR OPÇÃO É O 405, QUE CHEGARA EM 6 MINUTOS. Peça assistência, se necessário");
        }
        if (capturaFala.toUpperCase().contains("ALVORADA")){
            sendMenssage();
            setTextToSpeech("A linha de ônibus que passa no alvorada é a  655. 767. A MELHOR OPÇÃO É O 767, QUE CHEGARA EM 5 MINUTOS. Peça assistência, se necessário");
        }
        if (capturaFala.toUpperCase().contains("ADRIANÓPOLIS")){
            sendMenssage();
            setTextToSpeech("A linha de ônibus que passa no ADRIANÓPOLIS é a 300. 44. 762. A MELHOR OPÇÃO É O 300, QUE CHEGARA EM 2 MINUTOS. Peça assistência, se necessário");
        }
        if (capturaFala.toUpperCase().contains("PONTA NEGRA")){
            sendMenssage();
            setTextToSpeech("A linha de ônibus que passa no ponta negra é a 405. 652. 767 A MELHOR OPÇÃO É O 405, Ponta Negra em Manaus é um bairro encantador, conhecido por sua bela praia às margens do Rio Negro, além do icônico Complexo Turístico ");
        }
        if (capturaFala.toUpperCase().contains("PARQUE 10")){
            sendMenssage();
            setTextToSpeech("A linha de ônibus que passa no parque 10 de novembro é a 400. 677. 044 A MELHOR OPÇÃO É O 400");

        }
        if (capturaFala.toUpperCase().contains("FLORES")){
            sendMenssage();
            setTextToSpeech("A linha de ônibus que passa em flores é a 222.  421. 534 A MELHOR OPÇÃO É O 421 que chega em 3 minutos");

        }
        if (capturaFala.toUpperCase().contains("PETRÓPOLIS")){
            sendMenssage();
            setTextToSpeech("A linha de ônibus que passa em petrópolis é a 444. 333. 534 A MELHOR OPÇÃO É O 444, que chega em 2 minutos");
        }
        if (capturaFala.toUpperCase().contains("NOSSA SENHORA DAS GRAÇAS")){
            sendMenssage();
            setTextToSpeech("A linha de ônibus que passa no nossa senhora das graças é a 231 A MELHOR OPÇÃO É O 231");
        }
        if (capturaFala.toUpperCase().contains("SÃO JORGE")){
            sendMenssage();
            setTextToSpeech("A linha de ônibus que passa no são jorge é a  534");
        }
        if (capturaFala.toUpperCase().contains("TARUMÃ")){
            sendMenssage();
            setTextToSpeech("A linha de ônibus que passa no TARUMÃ  é a 555. 999. 643 A MELHOR OPÇÃO É O 555 que chega em 3 minutos");
        }
        if (capturaFala.toUpperCase().contains("SANTA ETELVINA")){
            sendMenssage();
            setTextToSpeech("A linha de ônibus que passa no santa etelvina é a 223 A MELHOR OPÇÃO É O 223 que chega em 2 minutos");
        }
        if (capturaFala.toUpperCase().contains("JAPIIM")){
            sendMenssage();
            setTextToSpeech("A linha de ônibus que passa no japiim é a 320. 999. 643 A MELHOR OPÇÃO É O 320 que chega em 5 minutos");
        }
        if (capturaFala.toUpperCase().contains("JORGE TEIXEIRA")){
            sendMenssage();
            setTextToSpeech("A linha de ônibus que passa no jorge teixeira é a 677. se preferir pode ir de lotação estilo velozes e furiosos");

        }
        if (capturaFala.toUpperCase().contains("DOM PEDRO")){
            sendMenssage();
            setTextToSpeech("A linha de ônibus que passa no dom pedro é a 320. 999. 643 A MELHOR OPÇÃO É O 320 que chega em 5 minutos");

        }
        if (capturaFala.toUpperCase().contains("COLÔNIA OLIVEIRA MACHADO")){
            sendMenssage();
            setTextToSpeech("A linha de ônibus que passa no colonia oliveira machado é a 999. 643 A MELHOR OPÇÃO É O 320 que chega em 5 minutos");

        } if (capturaFala.toUpperCase().contains("REDENÇÃO")){
            sendMenssage();
            setTextToSpeech("A linha de ônibus que passa na redenção é a  643 A MELHOR OPÇÃO É O 643 que chega em 5 minutos");

        }
        if (capturaFala.toUpperCase().contains("ALEIXO")){
            sendMenssage();
            setTextToSpeech("A linha de ônibus que passa no ALEIXO é a  560 A MELHOR OPÇÃO É O 570 que chega em 3 minutos");

        }
        if (capturaFala.toUpperCase().contains("NOVA ESPERANÇA")){
            sendMenssage();
            setTextToSpeech("A linha de ônibus que passa no NOVA ESPERANÇA é a  560 A MELHOR OPÇÃO É O 570 que chega em 3 minutos");

        }

    }




    public void sendMenssage(){
        //valores da mensagem
        String contatoEmergencia="92982603401";
        String menssage="está em uma viagem de ônibus";
        String name = user.name;
        String location =  user.location;
        //checar condições
        if(!contatoEmergencia.equals("") && !menssage.equals("")){
            //inicio sms manager
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(contatoEmergencia,null,menssage,null ,null);
            Toast.makeText(getApplicationContext()
                    ,"um contato de segurança foi notificado",Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(getApplicationContext()
                    ,"enter value first",Toast.LENGTH_LONG).show();
        }
    }
        //INICIA A IA DE RECONHECIMENTO DE VOZ
    public void onClick(View view) {
        viewStatusoff.setText("mensagem enviada para contato de emergência");
        capturarFala();
    }
    public void encerrarEscolta(View view){
        viewStatusoff.setText("aplicativo desativado");
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