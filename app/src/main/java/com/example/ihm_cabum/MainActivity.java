package com.example.ihm_cabum;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ihm_cabum.models.Version;
import com.example.ihm_cabum.volley.FirebaseFireAndForget;
import com.example.ihm_cabum.volley.FirebaseObject;
import com.example.ihm_cabum.volley.FirebaseResponse;
import com.example.ihm_cabum.volley.VolleyManagement;

import org.json.JSONException;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button bt1,bt2,bt3,bt4,bt5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt1 = findViewById(R.id.button);
        bt2 = findViewById(R.id.button2);
        bt3 = findViewById(R.id.button3);
        bt4 = findViewById(R.id.button4);
        bt5 = findViewById(R.id.button5);

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    (new Version(getApplicationContext())).get(new FirebaseResponse() {
                        @Override
                        public void notify(FirebaseObject result) {
                            Version version = (Version)result;
                            System.out.println(version);
                        }

                        @Override
                        public void notify(List<FirebaseObject> result) {

                        }
                    },"MW1ID0klidAoWN7tVJK4");
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    (new Version(getApplicationContext())).getAll(new FirebaseResponse() {
                        @Override
                        public void notify(FirebaseObject result) {

                        }

                        @Override
                        public void notify(List<FirebaseObject> result) {
                            System.out.println(result.size());
                            System.out.println(result.get(0));
                            System.out.println(result.get(0).getId());
                        }
                    });
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Version version = new Version(getApplicationContext(), "test 1", "test 2");
                    version.save(new FirebaseResponse() {
                        @Override
                        public void notify(FirebaseObject result) {
                            Version version = (Version)result;
                            System.out.println(version);
                        }

                        @Override
                        public void notify(List<FirebaseObject> result) {
                        }
                    });
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Version version = new Version(getApplicationContext(), "IMH S6 Polytech", "0.02");
                    version.setId("MW1ID0klidAoWN7tVJK4");
                    version.save(new FirebaseResponse() {
                        @Override
                        public void notify(FirebaseObject result) {
                            Version version = (Version)result;
                            System.out.println(version);
                        }

                        @Override
                        public void notify(List<FirebaseObject> result) {
                        }
                    });
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        bt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Version version = null;
                try {
                    version = new Version(getApplicationContext());
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                version.setId("IinBMSwNGU3ImVXIgKPm");
                try {
                    version.delete(new FirebaseFireAndForget());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}