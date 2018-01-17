package basil.sanity.Util;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import basil.sanity.R;
import basil.sanity.Screens.BudgetMenuScreen;
import basil.sanity.Screens.MainMenuScreen;

/**
 * Created by Andre Takhmazyan on 11/14/2017.
 */

public class ProgressBarUpdate {

    //Update the given progress bar by id to the percentage
    public static void updateProgressBar(View v, int id, double percentage, double overallSpent, double overallLimit) {
        ProgressBar budgetProgress = (ProgressBar) v.findViewById(id);
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        DecimalFormat df = new DecimalFormat("####0.00");
        overallLimit = Double.parseDouble(df.format(overallLimit));
        overallSpent = Double.parseDouble(df.format(overallSpent));

        int budgetPercent = (int) (overallSpent/overallLimit * 100.0);
        int color;
        if(overallSpent <= 0.00){//Gray
            color = 0xFF9e9e9e;
            ((TextView) v.findViewById(R.id.myTextProgress)).setTextColor(Color.BLACK);
        }
        else if(budgetPercent <= 50){//Green
            color = 0xFF33691e;
            ((TextView) v.findViewById(R.id.myTextProgress)).setTextColor(MainMenuScreen.appResources.getColor(R.color.colorTextBlack));
        }
        else if(budgetPercent > 50 && budgetPercent <= 75){//Yellow
            color = 0xFFffab00;
            ((TextView) v.findViewById(R.id.myTextProgress)).setTextColor(MainMenuScreen.appResources.getColor(R.color.colorTextBlack));
        }
        else if(budgetPercent > 75 && budgetPercent <= 100){//Red
            color = 0xFFd50000;
            ((TextView) v.findViewById(R.id.myTextProgress)).setTextColor(MainMenuScreen.appResources.getColor(R.color.colorTextBlack));
        }
        else{//Black
            color = 0xFF000000;
            ((TextView) v.findViewById(R.id.myTextProgress)).setTextColor(MainMenuScreen.appResources.getColor(R.color.colorTextLight));
        }
        budgetProgress.getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        budgetProgress.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        budgetProgress.setProgress(budgetPercent);
        budgetProgress.setScaleY(5f);

        String budgetAmountSpent = formatter.format(overallSpent);
        String budgetTotalAmount= formatter.format(overallLimit);
        Log.d("Updating overall spent", Double.toString(overallSpent));
        Log.d("Updating overall limit", Double.toString(overallLimit));
        String totalMessage = budgetAmountSpent + "/" + budgetTotalAmount;

        ((TextView) v.findViewById(R.id.myTextProgress)).setText(totalMessage);
    }
}
