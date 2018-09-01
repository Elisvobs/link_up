package com.inosuctechnologies.linkup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private Timer timer;
    private Boolean mVerified = false;
    private String mVerificationId;
    EditText phoneNumber, code;
    Button button;
    TextView timerText;
    ImageView verify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle(R.string.log_in);

        phoneNumber = findViewById(R.id.phoneNumber);
        code = findViewById(R.id.verificationed);
        button = findViewById(R.id.sendverifybt);
        timerText = findViewById(R.id.timertv);
        verify = findViewById(R.id.verifiedSign);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d("TAG", "onVerificationCompleted:" + credential);
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w("TAG", "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Snackbar.make(findViewById(R.id.parentLayout),
                            R.string.invalid_code, Snackbar.LENGTH_LONG).show();
                }
                else if (e instanceof FirebaseTooManyRequestsException) {
                    Snackbar.make(findViewById(R.id.parentLayout),
                            R.string.many_requests, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                Log.d("TAG", "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (button.getTag().equals(getResources().getString(R.string.tag_send))) {
                    if (!phoneNumber.getText().toString().trim().isEmpty()
                            && phoneNumber.getText().toString().trim().length() >= 10) {
                        startPhoneNumberVerification(phoneNumber.getText().toString().trim());
                        mVerified = false;
                        startTimer();
                        code.setVisibility(View.VISIBLE);
                        button.setTag(getResources().getString(R.string.tag_verify));
                    } else {
                        phoneNumber.setError("Please enter valid mobile number");
                    }
                }

                if (button.getTag().equals(getResources().getString(R.string.tag_verify))) {
                    if (!code.getText().toString().trim().isEmpty() && !mVerified) {
                        Snackbar.make(findViewById(R.id.parentLayout), R.string.verification_status,
                                Snackbar.LENGTH_LONG).show();
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId,
                                code.getText().toString().trim());
                        signInWithPhoneAuthCredential(credential);
                    }
                    if (mVerified) {
                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                        finish();
                    }
                }
            }
        });

        timerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!phoneNumber.getText().toString().trim().isEmpty()
                        && phoneNumber.getText().toString().trim().length() == 10) {
                    resendVerificationCode(phoneNumber.getText().toString().trim(), mResendToken);
                    mVerified = false;
                    startTimer();
                    code.setVisibility(View.VISIBLE);
                    button.setTag(getResources().getString(R.string.tag_verify));
                    Snackbar.make(findViewById(R.id.parentLayout), R.string.code_resend,
                            Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(this,
                        new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            mVerified = true;
                            timer.cancel();
                            verify.setVisibility(View.VISIBLE);
                            timerText.setVisibility(View.INVISIBLE);
                            phoneNumber.setEnabled(false);
                            code.setVisibility(View.INVISIBLE);
                            Snackbar.make(findViewById(R.id.parentLayout), R.string.code_verified,
                                    Snackbar.LENGTH_LONG).show();
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                        } else {
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Snackbar.make(findViewById(R.id.parentLayout),R.string.invalid_code,
                                        Snackbar.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    private void signOut(){
        mAuth.signOut();
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                30,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    public void startTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {

            int second = 60;
            @Override
            public void run() {
                if (second <= 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            timerText.setText(R.string.resend_code);
                            timer.cancel();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            timerText.setText(String.format("00:%d", second--));
//                            timerText.setText(new StringBuilder().append("00:").append(second--).toString());
//                            timerText.setText(MessageFormat.format("00:0{0}", second--));
                            timerText.setText("00:" + second--);
                        }
                    });
                }
            }
        }, 0, 1000);
    }

    private void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }
}