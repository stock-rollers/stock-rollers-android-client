package edu.cnm.deepdive.stockrollerandroidclient.controller;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import com.androidplot.util.PixelUtils;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.CatmullRomInterpolator;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PanZoom.Pan;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.StepMode;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYGraphWidget.Edge;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import edu.cnm.deepdive.stockrollerandroidclient.R;
import edu.cnm.deepdive.stockrollerandroidclient.model.entity.History;
import edu.cnm.deepdive.stockrollerandroidclient.service.StockRollersDatabase;
import edu.cnm.deepdive.stockrollerandroidclient.viewmodel.MainViewModel;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class HistoryGraphActivity extends Activity {
  private static final String SERIES_TITLE = "Signthings in USA";

  private XYPlot plot1;
  private SimpleXYSeries series;
  private StockRollersDatabase database = StockRollersDatabase.getInstance();
  private MainViewModel viewModel;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_history);
    new Thread(() -> {
    plot1 = (XYPlot) findViewById(R.id.plot);

    long id = database.getStockDao().getByName("SNAP").get().getId();
    List<History> histories = database.getHistoryDao().getHistoryByStock(id);
    List<LocalDate> dates = new LinkedList<>();
      for (History history :
          histories) {
        dates.add(history.getDate());
      }
    // these will be our domain index labels:
    Date[] years = new Date[dates.size()];
      for (int i = 0; i < years.length; i++) {
        years[i] = Date.from(dates.get(i).atStartOfDay(ZoneId.systemDefault()).toInstant());
      }

    addSeries(savedInstanceState);

    plot1.setRangeBoundaries(0, 10, BoundaryMode.FIXED);

    plot1.getGraph().getGridBackgroundPaint().setColor(Color.WHITE);
    plot1.getGraph().getDomainGridLinePaint().setColor(Color.BLACK);
    plot1.getGraph().getDomainGridLinePaint().
        setPathEffect(new DashPathEffect(new float[]{1, 1}, 1));
    plot1.getGraph().getRangeGridLinePaint().setColor(Color.BLACK);
    plot1.getGraph().getRangeGridLinePaint().
        setPathEffect(new DashPathEffect(new float[]{1, 1}, 1));
    plot1.getGraph().getDomainOriginLinePaint().setColor(Color.BLACK);
    plot1.getGraph().getRangeOriginLinePaint().setColor(Color.BLACK);

    plot1.getGraph().setPaddingRight(2);

    // draw a domain tick for each year:
    plot1.setDomainStep(StepMode.SUBDIVIDE, years.length);

    // customize our domain/range labels
    plot1.setDomainLabel("Year");
    plot1.setRangeLabel("# of Sightings");

    // get rid of decimal points in our range labels:
    plot1.getGraph().getLineLabelStyle(XYGraphWidget.Edge.LEFT).
        setFormat(new DecimalFormat("0.0"));

    plot1.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).
        setFormat(new Format() {
          private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM yyyy", Locale.US);
          @Override
          public StringBuffer format(Object obj,
              @NonNull StringBuffer toAppendTo,
              @NonNull FieldPosition pos) {
            int yearIndex = (int) Math.round(((Number) obj).doubleValue());
            return dateFormat.format(years[yearIndex], toAppendTo, pos);
          }

          @Override
          public Object parseObject(String source, @NonNull ParsePosition pos) {
            return null;

          }
        });

    }).start();
  }

  /**
   * Instantiates our XYSeries, checking the current savedInstanceState for existing series data
   * to avoid having to regenerate on each resume.  If your series data is small and easy to
   * regenerate (as it is here) then you can skip saving/restoring your series data to
   * savedInstanceState.
   * @param savedInstanceState Current saved instance state, if any; may be null.
   */
  private void addSeries(Bundle savedInstanceState) {
    Number[] yVals;

    if(savedInstanceState != null) {
      yVals = (Number[]) savedInstanceState.getSerializable(SERIES_TITLE);
    } else {
      yVals = new Number[]{5, 8, 6, 9, 3, 8, 5, 4, 7, 4};
    }

    // create our series from our array of nums:
    series = new SimpleXYSeries(Arrays.asList(yVals),
        SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, SERIES_TITLE);

    LineAndPointFormatter formatter =
        new LineAndPointFormatter(Color.rgb(0, 0, 0), Color.RED, Color.RED, null);
    formatter.getVertexPaint().setStrokeWidth(PixelUtils.dpToPix(10));
    formatter.getLinePaint().setStrokeWidth(PixelUtils.dpToPix(5));

    Paint lineFill = new Paint();
    lineFill.setAlpha(200);

    formatter.setFillPaint(lineFill);

    plot1.addSeries(series, formatter);
  }

  @Override
  public void onSaveInstanceState(Bundle bundle) {
    bundle.putSerializable(SERIES_TITLE, series.getyVals().toArray(new Number[]{}));
  }
}