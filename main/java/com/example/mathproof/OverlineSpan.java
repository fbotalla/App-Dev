package com.example.mathproof;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.style.ReplacementSpan;

/**
 * OverlineSpan class is dedicated to helping draw an acceptable square root symbol during problem
 * display.
 *
 * <p>
 *   This class accepts the size of the problem string text, sets the parameters in a new
 *   Paint class object, draws a horizontal line over the numbers in the equation, matching up
 *   with the square root symbol on the front of the equation. This creates a realistic square root
 *   problem onscreen during display.
 * </p>
 *
 * @author Fabrizio Botalla, Alberto Contreras, Jerry Lane, and Adam Tipton
 */
public class OverlineSpan extends ReplacementSpan {

  //declare our own paint space object variable
  private final Paint squareRootSymbol = new Paint();

  /**
   * The getSize method collects the data required to set the size of the drawn result.
   *
   * <p>
   *   The getSize method collects the data required to match up the resulting painted workspace
   *   with the original text, or in this case, number size. Width is the primary concern, so
   *   start minus end will determine the length of the overline drawn.
   * </p>
   *
   * @param paint
   * @param text
   * @param start
   * @param end
   * @param metrics
   * @return
   */
  @Override
  public int getSize(Paint paint, CharSequence text, int start,
                     int end, Paint.FontMetricsInt metrics) {

    //match paint space dimensions to incoming metrics.
    if (metrics != null) {

      //get font metrics and put into fontMetrics variable
      Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
      metrics.top = fontMetrics.top;
      metrics.ascent = fontMetrics.ascent;
      metrics.descent = fontMetrics.descent;
      metrics.bottom = fontMetrics.bottom;
    }

    //return the width of the text
    return (int) (paint.measureText(text, start, end) + .5f);
  }

  /**
   * The draw method will draw a line over the numbers in the string.
   *
   * <p>
   *   The draw method takes the text, or numbers in this case, determines where everything is
   *   located, draws a line just above the numbers and extends it to the left to match up
   *   with the square root symbol. The numbers are lowered slightly so the equation looks as
   *   it should when displayed onscreen.
   * </p>
   *
   *
   * @param canvas
   * @param text
   * @param start
   * @param end
   * @param x
   * @param top
   * @param y
   * @param bottom
   * @param paint
   */
  @Override
  public void draw(Canvas canvas, CharSequence text, int start,
                   int end, float x, int top, int y, int bottom, Paint paint) {

    //copy the Paint parameters
    squareRootSymbol.set(paint);

    //set a reasonable thickness for the line
    squareRootSymbol.setStrokeWidth(.09f * paint.getTextSize());

    //set the line's y to the top of tallest character so it is just above the numbers
    final Paint.FontMetricsInt paintMetricsInt = squareRootSymbol.getFontMetricsInt();
    final int newTop = (y + 11) + paintMetricsInt.ascent;

    //draw line, extend to the left over square root character
    canvas.drawLine((x - 7), newTop, x + getSize(paint, text, start, end,
      null), newTop, squareRootSymbol);

    //draw the "replaced" text, just a little lower so a gap is below the drawn line and numbers
    canvas.drawText(text, start, end, x, (y + 4), paint);
  }

}


