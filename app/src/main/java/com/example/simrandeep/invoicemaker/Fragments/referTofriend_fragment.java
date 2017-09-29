package com.example.simrandeep.invoicemaker.Fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.simrandeep.invoicemaker.R;
import com.example.simrandeep.invoicemaker.SharedPrefsUtils;



public class referTofriend_fragment extends Fragment  implements View.OnClickListener  {

    private String message;
    private String playStoreLink;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.refertofriend_fragment,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Refer to Friend");



        SharedPrefsUtils sharedPrefsUtils = SharedPrefsUtils.getInstance(getActivity());
        String code = sharedPrefsUtils.getStringPreference(SharedPrefsUtils.REFERRAL_CODE);
        playStoreLink = String.format("Link", getActivity().getPackageName());
        message = String.format("Hey, Install Tache and use my referral code to earn rewards.", code);

        ClipboardManager clipboard = (ClipboardManager)getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("simple text", code);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getActivity(), "Referral code copied", Toast.LENGTH_SHORT).show();

        TextView referralCode = (TextView)getActivity().findViewById(R.id.referral_code);
        referralCode.setText(code);

        getActivity().findViewById(R.id.shareFacebook).setOnClickListener(this);
        getActivity().findViewById(R.id.shareWhatsapp).setOnClickListener(this);
        getActivity().findViewById(R.id.shareTwitter).setOnClickListener(this);
        getActivity().findViewById(R.id.shareMore).setOnClickListener(this);




    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*case R.id.shareFacebook:
                dialogFacebookShare();
                break;*/
            case R.id.shareWhatsapp:
                whatsappShare();
                break;
          /*  case R.id.shareTwitter:
                twitterShare();
                break;*/
            case R.id.shareMore:
                createChooser();
                break;
        }
    }

    private void createChooser() {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, message + "\n" + playStoreLink);
        startActivity(Intent.createChooser(share, "Share referral!"));
    }
/*
    private void twitterShare() {
        String tweetUrl ="";//= String.format("https://twitter.com/intent/tweet?text=%s&url=%s", Helper.urlEncode(message), Helper.urlEncode(playStoreLink));
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(tweetUrl));

        List<ResolveInfo> matches =getActivity().getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith("com.twitter")) {
                intent.setPackage(info.activityInfo.packageName);
            }
        }

        startActivity(intent);
    }*/


    private void whatsappShare() {
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, message + "\n" + playStoreLink);
        try {
            startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "Whatsapp has not been installed", Toast.LENGTH_SHORT).show();
        }
    }
/*
    private void dialogFacebookShare() {
        ShareDialog shareDialog = new ShareDialog(this);
        ShareLinkContent linkContent = new ShareLinkContent.Builder().setContentTitle("Tache").setContentDescription(message).setContentUrl(Uri.parse(playStoreLink)).build();
        shareDialog.show(linkContent);
    }
*/


}

