package com.example.edgar.diaeuro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

//import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int MY_REQUEST_CODE = 7117; //Puede ser cualquiera
    List<AuthUI.IdpConfig> providers;
    EditText edt_title, edt_content;
    Button btn_sign_out, btn_post,btn_update,btn_delete;
    TextView nombre,correo,celular;
    ImageView perfil;
    RecyclerView recyclerView;
    ViewGroup viewGroup;

    //Firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Post selectedPost;
    String selectedKey;

    FirebaseRecyclerOptions<Post> options;
    FirebaseRecyclerAdapter<Post,MyRecyclerViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        edt_content = (EditText)findViewById(R.id.edt_content);
        edt_title = (EditText)findViewById(R.id.edt_title);
        btn_post = (Button)findViewById(R.id.btn_post);
        btn_update = (Button)findViewById(R.id.btn_update);
        btn_delete = (Button)findViewById(R.id.btn_delete);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btn_sign_out =(Button)findViewById(R.id.btn_sign_out);
        nombre = (TextView)findViewById(R.id.nombre);
        correo = (TextView)findViewById(R.id.correo);
        celular = (TextView)findViewById(R.id.celular);
        perfil = (ImageView)findViewById(R.id.perfil);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Día Euro");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                displayComment();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postComment();
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
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
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
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
        });

        displayComment();

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

    @Override
    protected void onStop() {
        if(adapter != null)
            adapter.stopListening();
        super.onStop();
    }

    private void postComment() {
        String title = edt_title.getText().toString();
        String content = edt_content.getText().toString();

        Post post = new Post(title,content);

        databaseReference.push() //Usar el método para crear un id unico
        .setValue(post);

        adapter.notifyDataSetChanged();
    }

    private void displayComment() {
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
                                edt_content.setText(model.getContent());
                                edt_title.setText(model.getTitle());
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
    }

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
                //String name = user.getDisplayName();
                //String email = user.getEmail();
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