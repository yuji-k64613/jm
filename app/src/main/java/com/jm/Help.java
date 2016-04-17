package com.jm;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.jm.db.DaoFactory;
import com.jm.utility.BaseActivity;

public class Help extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);

        TextView textView1 = (TextView) findViewById(R.id.textView91);
        TextView textView2 = (TextView) findViewById(R.id.textView92);
        TextView textView3 = (TextView) findViewById(R.id.textView93);
        TextView textView4 = (TextView) findViewById(R.id.textView94);
        TextView textView5 = (TextView) findViewById(R.id.textView95);

        TextView[] textViews = {
       		textView1,
       		textView2,
       		textView3,
       		textView4,
       		textView5,
        };
        String[] messages = {
        	getString(R.string.help1),
        	getString(R.string.help2),
        	getString(R.string.help3),
        	getString(R.string.help4),
        	getString(R.string.help5)
        };
        int i = 0;
        if (DaoFactory.getInstance().getMode() == DaoFactory.Mode.ORIGINAL){
        	textViews[i++].setText(messages[0]);
        	textViews[i++].setText(messages[3]);        	
        }
        else {
        	textViews[i++].setText(messages[0]);
        	textViews[i++].setText(messages[1]);
        	textViews[i++].setText(messages[2]);
        	textViews[i++].setText(messages[3]);        	
        	textViews[i++].setText(messages[4]);        	
        }
        
        textView1.setText(getString(R.string.help1));
        
		Button button = (Button)findViewById(R.id.button91);
        button.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				finish();
			}
        });
    }

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
    	return false;
	}
    
}

