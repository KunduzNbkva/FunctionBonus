package com.example.appbonus.customViews;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkEnabledTextView extends androidx.appcompat.widget.AppCompatTextView {


    // Pattern for gathering *140*1# from the Text
    Pattern ussdPattern = Pattern.compile("(\\*[0-9]+[\\*[0-9]+]*#)");
    private TextLinkClickListener mListener;
    private ArrayList<Hyperlink> listOfLinks;

    public LinkEnabledTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        listOfLinks = new ArrayList<Hyperlink>();

    }

    public void setText(String text) {
        SpannableString linkableText = new SpannableString(text);

        gatherLinks(listOfLinks, linkableText, ussdPattern);

        for (Hyperlink linkSpec : listOfLinks) {
            // this process here makes the Clickable Links from the text
            linkableText.setSpan(linkSpec.span, linkSpec.start, linkSpec.end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        // sets the text for the TextView with enabled links
        super.setText(linkableText);
    }

    public void setOnTextLinkClickListener(TextLinkClickListener newListener) {
        mListener = newListener;
    }

    private void gatherLinks(ArrayList<Hyperlink> links, Spannable s, Pattern pattern) {
        Matcher m = pattern.matcher(s);

        while (m.find()) {
            int start = m.start();
            int end = m.end();

            Hyperlink spec = new Hyperlink();
            spec.textSpan = s.subSequence(start, end);
            spec.span = new InternalURLSpan(spec.textSpan.toString());
            spec.start = start;
            spec.end = end;

            links.add(spec);
        }
    }

    public interface TextLinkClickListener {
        public void onTextLinkClick(View textView, String clickedString);
    }

    /**
     * Class for storing the information about the Link Location
     */
    public class InternalURLSpan extends ClickableSpan {
        private String clickedSpan;

        public InternalURLSpan(String clickedString) {
            clickedSpan = clickedString;
        }

        @Override
        public void onClick(View textView) {
            mListener.onTextLinkClick(textView, clickedSpan);
        }
    }

    class Hyperlink {
        CharSequence textSpan;
        InternalURLSpan span;
        int start;
        int end;
    }
}