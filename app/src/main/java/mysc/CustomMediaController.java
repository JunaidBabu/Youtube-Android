package mysc;


import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.MediaController;

import in.junaidbabu.R;

/**
 * Created by babu on 1/7/14.
 */
public class CustomMediaController extends MediaController {

    ImageButton mCCBtn;
    Context mContext;
    AlertDialog mLangDialog;

    public CustomMediaController(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public void setAnchorView(View view) {
        super.setAnchorView(view);

        FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        frameParams.gravity = Gravity.RIGHT|Gravity.TOP;

        View v = makeCCView();
        addView(v, frameParams);

    }

    private View makeCCView() {
        mCCBtn = new ImageButton(mContext);
        mCCBtn.setImageResource(R.drawable.ic_drawer);

        mCCBtn.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//                builder.setSingleChoiceItems(R.array.langs_Array, 0, new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //Save Preference and Dismiss the Dialog here
//                        Toast.makeText(mContext, "Which ::: " + which, Toast.LENGTH_LONG).show();
//                    }
//
//                });
//                mLangDialog = builder.create();
//                mLangDialog.show();
            }
        });

        return mCCBtn;
    }

}

