/*
package com.crm.crmapp.Registration;

import android.app.ActivityOptions;
import android.app.Dialog;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.andexert.library.RippleView;
import com.ess.ebizbarcode.ebizbarcode.R;
import com.ess.ebizbarcode.ebizbarcode.utility.UserPref;
import com.ess.ebizbarcode.ebizbarcode.utility.Util;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SplashActivity extends BaseActivity implements Animation.AnimationListener {

    private ImageView logoIV;
    private UserPref userPref;
    private Dialog tokenDialog;
    private TextView txtErrorMsg;
    private boolean mIsLoginDialogCancelled = false;
    private String ipAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        userPref = new UserPref(this);
        Animation fade_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        Animation zoom_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_out);
        ImageView background = (ImageView) findViewById(R.id.background);
        logoIV = (ImageView) findViewById(R.id.logo);
        background.startAnimation(fade_in);
        logoIV.startAnimation(zoom_out);
        fade_in.setAnimationListener(this);
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        checkCredentials(logoIV);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }

    private void checkCredentials(final ImageView imgLogo) {

        if (!userPref.getToken().isEmpty()) {
            if (userPref.getAutoLogin()) {
                Intent i = new Intent(SplashActivity.this, DashboardActivity.class);
                if (Build.VERSION.SDK_INT > 20) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this, imgLogo, "logo");
                    startActivity(i, options.toBundle());
                } else {
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
                finish();
            } else {
                Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                if (Build.VERSION.SDK_INT > 20) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this, imgLogo, "logo");
                    startActivity(i, options.toBundle());
                } else {
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
                finish();
            }
        } else {
            tokenDialog = new Dialog(SplashActivity.this, R.style.DialogSlideAnim);
            tokenDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            tokenDialog.setCancelable(false);
            tokenDialog.setContentView(R.layout.custom_dialog_token);
            tokenDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            tokenDialog.getWindow().setGravity(Gravity.CENTER);
            tokenDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            RippleView yesButton = (RippleView) tokenDialog.findViewById(R.id.btn_yes);
            RippleView noButton = (RippleView) tokenDialog.findViewById(R.id.btn_no);

            final EditText edtFirstFiveToken = (EditText) tokenDialog.findViewById(R.id.edtFirstFiveToken);
            final EditText edtSecondFiveToken = (EditText) tokenDialog.findViewById(R.id.edtSecondFiveToken);
            final EditText edtThirdFiveToken = (EditText) tokenDialog.findViewById(R.id.edtThirdFiveToken);
            final EditText edtFourthFiveToken = (EditText) tokenDialog.findViewById(R.id.edtFourthFiveToken);
            final EditText edtlastFiveToken = (EditText) tokenDialog.findViewById(R.id.edtlastFiveToken);

            txtErrorMsg = (TextView) tokenDialog.findViewById(R.id.txtErrorMsg);
            txtErrorMsg.setVisibility(View.GONE);

            edtFirstFiveToken.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (edtFirstFiveToken.getText().toString().length() == 5) {
                        enableEditText(edtSecondFiveToken);
                        edtSecondFiveToken.requestFocus();
                    } else if (txtErrorMsg.getVisibility() == View.VISIBLE) {
                        Util.makeViewGone(txtErrorMsg);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });


            edtSecondFiveToken.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (edtSecondFiveToken.getText().toString().length() == 5) {
                        enableEditText(edtThirdFiveToken);
                        edtThirdFiveToken.requestFocus();
                    } else if (txtErrorMsg.getVisibility() == View.VISIBLE) {
                        Util.makeViewGone(txtErrorMsg);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            edtThirdFiveToken.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (edtThirdFiveToken.getText().toString().length() == 5) {
                        enableEditText(edtFourthFiveToken);
                        edtFourthFiveToken.requestFocus();
                    } else if (txtErrorMsg.getVisibility() == View.VISIBLE) {
                        Util.makeViewGone(txtErrorMsg);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            edtFourthFiveToken.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (edtFourthFiveToken.getText().toString().length() == 5) {
                        enableEditText(edtlastFiveToken);
                        edtlastFiveToken.requestFocus();
                    } else if (txtErrorMsg.getVisibility() == View.VISIBLE) {
                        Util.makeViewGone(txtErrorMsg);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            edtlastFiveToken.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (edtFourthFiveToken.getText().toString().length() > 0) {
                        if (txtErrorMsg.getVisibility() == View.VISIBLE) {
                            Util.makeViewGone(txtErrorMsg);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            yesButton.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                @Override
                public void onComplete(RippleView rippleView) {
                    String tokenNumber = edtFirstFiveToken.getText().toString().trim() + edtSecondFiveToken.getText().toString().trim() + edtThirdFiveToken.getText().toString().trim() + edtFourthFiveToken.getText().toString().trim() + edtlastFiveToken.getText().toString().trim();
                    Util.makeViewVisible(txtErrorMsg);
//                    tokenNumber = "&mun7mrt7q7st9smlm"; //tally console token
//                    tokenNumber = "$ksl7kpr7k7koj9qjjm"; //150 console token
//                    tokenNumber = "$ksl7kpr7o7qr9qkjk"; //5.78:7101 Anand system token
//                    tokenNumber = "@iqj7inp7m7ikl9oihi"; //b-65 token
//                    tokenNumber = "@iqj7inp7k7nm9oihi"; //192.168.3.65:7101
//                    tokenNumber = "@ni7ij7oj7jkn9ohhk"; //61.12.72.236:7003 demo live
//                    tokenNumber = "@kl7jjn7ijm7pi9ohhk"; //34.226.125.81:7003 new demo live
//                    tokenNumber = "#jrk8joq8j8jni0piil"; //1.150:7003 new demo live
//                    tokenNumber = "@iqj7inp7m7iki9oihi"; //192.168.5.131 Sudhir b-57
//                    tokenNumber = "@iqj7inp7m7iom9ohhk"; //192.168.5.175 FC
                //    tokenNumber = "&mun7mrt7q7mou9smlm"; //192.168.5.139
                  //  tokenNumber = "@iml7oq7jll7jmh9ohhk"; //154.79.244.250
                     tokenNumber = "#jrk8joq8o8jjq0pjij"; //192.168.6.118
                     //tokenNumber = "&mlo7mms7mqr7pm9sllo"; //103.117.156.41:7003



                    if (tokenNumber.length() > 0) {
                        txtErrorMsg.setText(R.string.validating_token);
                        ipAddress = decrYptIp(tokenNumber);

                        if (ipAddress.equalsIgnoreCase("500.1.1.111:7003")) {
                            ipAddress = "alikhtyaaroffice.dyndns.org:7003";
                            mIsLoginDialogCancelled = false;
                            Util.makeViewGone(txtErrorMsg);
                            tokenDialog.dismiss();
                        } else {
                            if (ipAddress.length() > 0) {
                                new CheckIpAddress().execute(ipAddress);
                            } else {
                                txtErrorMsg.setText(getResources().getString(R.string.token_invalid));
                            }
                        }
                    } else {
                        txtErrorMsg.setText(getResources().getString(R.string.token_field_blank));
                    }
                }
            });

            noButton.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                @Override
                public void onComplete(RippleView rippleView) {
                    mIsLoginDialogCancelled = true;
                    tokenDialog.dismiss();
                }
            });

            tokenDialog.setOnDismissListener(dialogInterface -> {
                if (mIsLoginDialogCancelled) {
                    SplashActivity.this.finish();
                } else {
                    userPref.setToken("http://" + ipAddress);
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    if (Build.VERSION.SDK_INT > 20) {
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this, imgLogo, "logo");
                        startActivity(i, options.toBundle());
//                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                    } else {
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                    finish();
                }
            });

            tokenDialog.show();
        }
    }

    private class CheckIpAddress extends AsyncTask<String, Boolean, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            return isValidIpAddress(params[0]);
        }

        @Override
        protected void onPostExecute(Boolean isValidIp) {
            super.onPostExecute(isValidIp);

            if (isValidIp) {
                //for google analytics
       */
/*         mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Action")
                        .setAction("IP_DECRYPTED" + ipAddress)
                        .build());*//*


                mIsLoginDialogCancelled = false;
                Util.makeViewGone(txtErrorMsg);
                tokenDialog.dismiss();
            } else {
                txtErrorMsg.setText(getResources().getString(R.string.token_invalid));
            }
        }
    }

    private boolean isValidIpAddress(String ipAddress) {

        boolean isValidIp = false;
        if (ipAddress != null && !ipAddress.isEmpty()) {
            String ipAddressSplit[] = ipAddress.split(":");
            if (ipAddressSplit.length == 2) {
                try {
                    String IP_ADDRESS_PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

                    Pattern pattern = Pattern.compile(IP_ADDRESS_PATTERN);
                    Matcher matcher = pattern.matcher(ipAddressSplit[0]);
                    isValidIp = matcher.matches();

                    if (isValidIp) {
                        Socket socket = new Socket();
                        socket.connect(new InetSocketAddress(ipAddressSplit[0], Integer.valueOf(ipAddressSplit[1])), 70000);
                        isValidIp = socket.isConnected();
                    } else {
                        isValidIp = false;
                    }
                } catch (Exception e) {
                    isValidIp = false;
                    e.printStackTrace();
                }
            }
        }
        return isValidIp;
    }

    private void enableEditText(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
    }

   */
/* private void disableEditTex(EditText editText) {
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(false);
    }*//*


    private String decrYptIp(String encryotedIp) {
        //all the values of these arrays are predefined to get the ip
        String encryptStr = "";

        char[] dotArray = {'7', '8'};
        char[] colonArray = {'9', '0'};
        //24 character
        char[] alphabetArray = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        char[] ipAplhabets = {'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n'};
        char[] ipAplhabetValue = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

        //first we get the value of the special key at 0 position
        int keyValue = getKeyValue(encryotedIp.charAt(0));

        //this loop start from 1 because we already taken the 0 position character for key
        if (keyValue != 0) {
            for (int counter = 1; counter < encryotedIp.length(); counter++) {
                Character ipChar = encryotedIp.charAt(counter);

                //for ip numbers decryption
                for (int alphabetCounter = 0; alphabetCounter < alphabetArray.length; alphabetCounter++) {
                    if (ipChar.equals(alphabetArray[alphabetCounter])) {
                        int decryptPosition = alphabetCounter - keyValue;
                        if (decryptPosition <= 0) {
                            decryptPosition = (alphabetArray.length - 1) + decryptPosition;
                        }

                        Character decryptCharacter = alphabetArray[decryptPosition - 1];
                        for (int ipAlphabetCounter = 0; ipAlphabetCounter < ipAplhabets.length; ipAlphabetCounter++) {
                            if (decryptCharacter.equals(ipAplhabets[ipAlphabetCounter])) {
                                decryptPosition = ipAlphabetCounter;
                                break;
                            }
                        }

                        if (decryptPosition < ipAplhabetValue.length) {
                            encryptStr = encryptStr + ipAplhabetValue[decryptPosition];
                        }
                        break;
                    }
                }

                //for . decryption
                for (char aDotArray : dotArray) {
                    if (ipChar.equals(aDotArray)) {
                        encryptStr = encryptStr + ".";
                    }
                }

                //for : decryption
                for (char aColonArray : colonArray) {
                    if (ipChar.equals(aColonArray)) {
                        encryptStr = encryptStr + ":";
                    }
                }
            }
        }
//        Log.e("CLIENT IP : ", "" + encryptStr);
        return encryptStr;
    }

    //for key decryption
    private int getKeyValue(Character encryptKey) {
        int keyValue[] = {4, 2, 5, 3, 6};
        char keyArray[] = {'$', '@', '%', '#', '&'};
        int value = 0;
        for (int counter = 0; counter < keyArray.length; counter++) {
            if (encryptKey.equals(keyArray[counter])) {
                value = keyValue[counter];
                break;
            }
        }
        return value;
    }
}*/
