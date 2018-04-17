package com.example.opet.appphoto;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DashboardActivity extends Activity {

    /*Constantes utilizadas para verificar se o usuário escolheu usar a camera*/
    private int REQUEST_CAMERA = 1;
    /*Valores que vão aparecer na caixa de diálogo para que o usuário escolha o que fazer.*/
    private String[] items = {"Tirar foto!", "Cancelar"};
    /*declarando variaveis*/
    private ImageView image;
    /*String que armazena a escolha feita pelo usuário*/
    private String userChoosenTask;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        image = findViewById(R.id.imageUser);

    }

    /*Função que irá exibir uma caixa de diálogo para o usuário escolher o que
    * fazer em relação ao arquivo de imagem (Tirar uma Foto)*/

    public void selecionarImagem(View v){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(DashboardActivity.this);
        alertBuilder.setTitle("Adicionar Foto!");
        alertBuilder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int item) {
                boolean result=Utility.checkPermission(DashboardActivity.this);
                if (items[item].equals("Tirar foto!")) {
                    userChoosenTask="Tirar foto!";
                    if(result)
                        cameraIntent();
                } else if (items[item].equals("Cancelar")) {
                    dialogInterface.dismiss();
                }
            }
        });

        alertBuilder.show();
    }

    /*Função que irá chamar uma instancia de camera do dispositivo*/
    private void cameraIntent()
    {
        /*Ao iniciar uma intent com MediaStore.ACTION_IMAGE_CAPTURE,
        * O android abrirá uma das aplicações de camera do dispositivo*/
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //Definimos que o usuário selecionou a requisição de camera
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    /*Função que é executada caso seja solicitada alguma permissão em tempo de execução(Camera,Arquivos, etc...)*/
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Tirar foto!"))
                        cameraIntent();
                    else {
                        //code for else
                    }
                } else {
                    //code for deny
                }
                break;
            case Utility.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Tirar foto!"))
                        cameraIntent();
                    else {
                        //code for else
                    }
                } else {
                    //code for deny
                }
                break;
        }
    }

    /*Função executada após o resultado de uma Activity (cameraIntent() ou galleryIntent()*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
            else{
                //code for else
            }
        }
    }

    /*Função chamada caso o usuário tenha selecionado a camera*/
    private void onCaptureImageResult(Intent data) {
        /*Pegamos a imagem da camera e criamos um thumb para exibição*/
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        /*Aqui criamos um arquivo para salvar a imagem obtida da camera*/
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        /*Environment.getExternalStoragePublicDirectory salvará a imagem em um diretório manipulável pelo usuário*/
        File destination = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                System.currentTimeMillis() + ".png");
        FileOutputStream fo;
        try {
            boolean result=Utility.checkPermission(DashboardActivity.this);
            if(result) {
                /*Caso tenha permissão para salvar um arquivo, será feito o processo de armazenamento local*/
                destination.createNewFile();
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());
                fo.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        image.setImageBitmap(thumbnail);
    }






}
