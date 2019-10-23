package com.example.edgar.diaeuro;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

//import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int MY_REQUEST_CODE = 7117; //Puede ser cualquiera
    private TextInputLayout tilOrigen,tilTelCasa,tilTelCelular;
    List<AuthUI.IdpConfig> providers;
    EditText lugarOrigen, telCasa, telCelular;
    Button btn_sign_out, btn_post,btn_update,btn_delete;
    TextView nombre,correo,celular;
    ImageView perfil;
    RecyclerView recyclerView;
    ViewGroup viewGroup;
    String name,email,selectedKey,escuelaRespuesta,carreraRespuesta,carreraRespuesta2,beca="",sexo="";
    Post selectedPost;
    Spinner escuela,carrera1,carrera2;
    RadioGroup opciones,sexos;

    //Firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseRecyclerOptions<Post> options;
    FirebaseRecyclerAdapter<Post,MyRecyclerViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        telCasa = (EditText)findViewById(R.id.euro_telCasa);
        lugarOrigen = (EditText)findViewById(R.id.euro_origen);
        telCelular = (EditText)findViewById(R.id.euro_telCelular);
        btn_post = (Button)findViewById(R.id.btn_post);
        //btn_update = (Button)findViewById(R.id.btn_update);
        //btn_delete = (Button)findViewById(R.id.btn_delete);
        btn_sign_out =(Button)findViewById(R.id.btn_sign_out);
        nombre = (TextView)findViewById(R.id.nombre);
        opciones = (RadioGroup)findViewById(R.id.opciones_beca);
        sexos = (RadioGroup)findViewById(R.id.opciones_sexo);
        //recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        correo = (TextView)findViewById(R.id.correo);
        celular = (TextView)findViewById(R.id.celular);
        perfil = (ImageView)findViewById(R.id.perfil);
        tilOrigen = (TextInputLayout) findViewById(R.id.til_Origen);
        tilTelCasa = (TextInputLayout) findViewById(R.id.til_TelCasa);
        tilTelCelular = (TextInputLayout) findViewById(R.id.til_TelCelular);
        escuela = (Spinner)findViewById(R.id.SpinnerEscuela);
        carrera1= (Spinner)findViewById(R.id.SpinnerCarreras);
        carrera2 = (Spinner)findViewById(R.id.SpinnerCarreras2);

        TextView condiciones = (TextView) findViewById(R.id.terminos);
        condiciones.setMovementMethod(LinkMovementMethod.getInstance());

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Día Euro");

        /*databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //displayComment();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        telCasa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                esTelefonoValido(String.valueOf(charSequence));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        lugarOrigen.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                esLugarValido(String.valueOf(charSequence));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        telCelular.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                esCelularValido(String.valueOf(charSequence));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarDatos();
            }
        });

        /*btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child(selectedKey).setValue(new Post(edt_title.getText().toString(),edt_content.getText().toString()))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this,"Updated",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });*/

        /*btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child(selectedKey)
                        .removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this,"Deleted",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });*/

        //displayComment();

        btn_sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Cierre de sesión
                AuthUI.getInstance()
                        .signOut(MainActivity.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                btn_sign_out.setEnabled(false);
                                showSignInOptions();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        //Inicio de provider
        providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(), //Email Builder
                new AuthUI.IdpConfig.PhoneBuilder().build(), //Phone Builder
                new AuthUI.IdpConfig.FacebookBuilder().build(), //Facebook Builder
                new AuthUI.IdpConfig.GoogleBuilder().build() //Google Builder
        );

        showSignInOptions();
    }

    private void validarDatos() {
        String origen = tilOrigen.getEditText().getText().toString();
        String telefono = tilTelCasa.getEditText().getText().toString();
        String celular = tilTelCelular.getEditText().getText().toString();

        boolean a = esLugarValido(origen);
        boolean b = esTelefonoValido(telefono);
        boolean c = esCelularValido(celular);

        if (a && b && c) {
            // OK, se pasa a la siguiente acción
            postComment();
            Toast.makeText(this, "Se guardó el registro", Toast.LENGTH_LONG).show();
        }
    }

    private boolean esCelularValido(String celular) {
        if (!Patterns.PHONE.matcher(celular).matches() || celular.length() != 10) {
            tilTelCelular.setError("Teléfono inválido");
            return false;
        } else {
            tilTelCelular.setError(null);
        }

        return true;
    }

    private boolean esTelefonoValido(String telefono) {
        if (!Patterns.PHONE.matcher(telefono).matches() || telefono.length() != 10) {
            tilTelCasa.setError("Teléfono inválido");
            return false;
        } else {
            tilTelCasa.setError(null);
        }

        return true;
    }

    private boolean esLugarValido(String origen) {
        //Pattern patron = Pattern.compile("^[a-zA-Z ]+$");
        Pattern patron = Pattern.compile("^[[a-zA-Z ]\\p{Punct}]+$");
        if (!patron.matcher(origen).matches() || origen.length() > 30) {
            tilOrigen.setError("Lugar de origen inválido");
            return false;
        } else {
            tilOrigen.setError(null);
        }

        return true;
    }

    @Override
    protected void onStop() {
        if(adapter != null)
            adapter.stopListening();
        super.onStop();
    }

    private void postComment() {
        String lugar = lugarOrigen.getText().toString();
        String tcasa = telCasa.getText().toString();
        String tcelular = telCelular.getText().toString();
        name = (String) nombre.getText();
        email = (String) correo.getText();
        escuelaRespuesta = escuela.getSelectedItem().toString();
        carreraRespuesta = carrera1.getSelectedItem().toString();
        carreraRespuesta2 = carrera2.getSelectedItem().toString();
        if (opciones.getCheckedRadioButtonId() == R.id.radio_si) {
            beca = "Si";
            //Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        }else{
            beca = "No";
        }
        if (sexos.getCheckedRadioButtonId() == R.id.radio_si) {
            sexo = "Masculino";
            //Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        }else{
            sexo = "Femenino";
        }

        Post post = new Post(lugar,tcasa,tcelular,name,email,escuelaRespuesta,carreraRespuesta,carreraRespuesta2,beca,sexo);

        databaseReference.push() //Usar el método para crear un id unico
        .setValue(post);

        //adapter.notifyDataSetChanged();
    }

    /*private void displayComment() {
        options = new FirebaseRecyclerOptions.Builder<Post>()
                .setQuery(databaseReference,Post.class)
                .build();

        adapter =
                new FirebaseRecyclerAdapter<Post, MyRecyclerViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull MyRecyclerViewHolder holder, int position, @NonNull Post model) {
                        holder.txt_title.setText(model.getTitle());
                        holder.txt_comment.setText(model.getContent());

                        holder.setiItemClickListener(new IItemClickListener() {
                            @Override
                            public void onClick(View view, int position) {
                                selectedPost = model;
                                selectedKey = getSnapshots().getSnapshot(position).toString();
                                Log.d("KEY Item",""+selectedKey);

                                //Bind data
                                telCasa.setText(model.getContent());
                                lugarOrigen.setText(model.getTitle());
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public MyRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View itemView = LayoutInflater.from(getBaseContext()).inflate(R.layout.post_item,viewGroup,false);
                        return new MyRecyclerViewHolder(itemView);
                    }
                };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }*/

    private void showSignInOptions() {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.MyTheme)
                        .setLogo(R.drawable.logo)
                        .setTosAndPrivacyPolicyUrls("http://ueh.edu.mx/privacidad3", "http://ueh.edu.mx/privacidad3")
                        .build(),MY_REQUEST_CODE
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MY_REQUEST_CODE){
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if(resultCode == RESULT_OK){
                //Obtener Usuario
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String name = user.getDisplayName();
                String email = user.getEmail();
                String photo = String.valueOf(user.getPhotoUrl());

                Picasso.get().load(user.getPhotoUrl()).into(perfil);
                nombre.setText(user.getDisplayName());
                correo.setText(user.getEmail());
                celular.setText(user.getPhoneNumber());
                Toast.makeText(this,""+user.getEmail(),Toast.LENGTH_SHORT).show();
                //Botón de deslogueo
                btn_sign_out.setEnabled(true);
            }
            else{
                Toast.makeText(this,""+response.getError().getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    }
}