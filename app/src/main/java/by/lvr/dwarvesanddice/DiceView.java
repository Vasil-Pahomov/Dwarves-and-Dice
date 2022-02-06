package by.lvr.dwarvesanddice;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by vasil_000 on 10.02.2015.
 */
public class DiceView extends View {
    Paint circlePaint;
    String text;
    Rect textBounds = new Rect();
    Paint textPaint;

    public DiceView(Context paramContext)
    {
        super(paramContext);
        init();
    }

    public DiceView(Context paramContext, AttributeSet paramAttributeSet)
    {
        super(paramContext, paramAttributeSet);
        init();
    }

    private void init()
    {
        Resources res = getResources();
        this.circlePaint = new Paint();
        this.circlePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        this.circlePaint.setColor(res.getColor(R.color.black_overlay));
        this.textPaint = new Paint();
        this.textPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        this.textPaint.setColor(res.getColor(R.color.design_default_color_background));
        this.textPaint.setTextSize(20.0F);
    }

    protected void onDraw(Canvas paramCanvas)
    {
        super.onDraw(paramCanvas);
        int i = getWidth();
        int j = getHeight();
        int k = Math.min(i, j);
        paramCanvas.drawCircle(i / 2, j / 2, k / 2, this.circlePaint);
        if (this.textBounds != null && this.text != null) {
            paramCanvas.drawText(this.text, (i - this.textBounds.width()) / 2, (j - this.textBounds.height()) / 2, this.textPaint);
        }
    }

    public void setParams(int circlePaint, int textPaint, String text)
    {
        this.circlePaint.setColor(circlePaint);
        this.textPaint.setColor(textPaint);
        this.text = text;
        this.textPaint.getTextBounds(this.text, 0, this.text.length(), this.textBounds);
        invalidate();
    }

    public void resetParams()
    {
        this.circlePaint.setColor(getResources().getColor(R.color.black_overlay));
        this.text = "";
        invalidate();
    }
}
