package com.example.naber;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.naber.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    MaterialEditText username, email, password;
    Button btReg;
    private List<User> mUser2;
    boolean hasTaken;

    FirebaseAuth auth;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //TOOLBAR
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Kaydol");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //TANIMLAMALAR
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btReg = findViewById(R.id.btReg);

        auth = FirebaseAuth.getInstance();
        btReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String txUser = username.getText().toString();
                final String txEmail = email.getText().toString();
                final String txPass = password.getText().toString();

                if (TextUtils.isEmpty(txUser) || TextUtils.isEmpty(txEmail) || TextUtils.isEmpty(txPass)) {
                    Toast.makeText(RegisterActivity.this, "Lütfen tüm alanları doldurunuz boşa yapmadık o kutucukları", Toast.LENGTH_SHORT).show();
                } else if (txPass.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Şifre en az 6 haneli olmalıdır", Toast.LENGTH_SHORT).show();
                } else {
                    hasTaken = false;

                    DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Users");

                    reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                User user = snapshot.getValue(User.class);


                                assert user != null;
                                if (user.getUsername().equals(username.getText().toString())) {
                                    hasTaken = true;
                                    break;
                                }
                            }
                            if (hasTaken == true) {
                                Toast.makeText(RegisterActivity.this, "Kullanıcı adı alınmış", Toast.LENGTH_SHORT).show();
                            } else {
                                register(txUser, txEmail, txPass);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }
        });
    }

    private boolean hasTaken() {
        boolean hasTa = false;


        return hasTa;
    }

    private void register(final String username, final String email, final String password) {

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String userid = firebaseUser.getUid();

                            ref = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("username", username);
                            hashMap.put("imageURL", "default");
                            hashMap.put("status", "offline");

                            ref.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegisterActivity.this, "Email veya Şifreyi kontrol edin", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }
}
