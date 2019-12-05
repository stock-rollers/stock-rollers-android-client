package edu.cnm.deepdive.stockrollerandroidclient.controller;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.androidplot.Plot;
import com.androidplot.ui.SeriesRenderer;
import com.androidplot.util.PixelUtils;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PanZoom;
import com.androidplot.xy.PanZoom.Pan;
import com.androidplot.xy.PanZoom.Zoom;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.SimpleXYSeries.ArrayFormat;
import com.androidplot.xy.Step;
import com.androidplot.xy.StepFormatter;
import com.androidplot.xy.StepMode;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.XYSeriesFormatter;
import edu.cnm.deepdive.stockrollerandroidclient.R;
import edu.cnm.deepdive.stockrollerandroidclient.model.entity.History;
import edu.cnm.deepdive.stockrollerandroidclient.model.entity.Stock;
import edu.cnm.deepdive.stockrollerandroidclient.service.StockRollersDatabase;
import edu.cnm.deepdive.stockrollerandroidclient.viewmodel.MainViewModel;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Fragment Class Setup to show a Stocks History
 */
public class HistoryGraphFragment extends Fragment {

  private static final String SERIES_TITLE = "Signthings in USA";

  private XYPlot plot1;
  private ProgressBar progressBar;
  private Stock stock;
  private SimpleXYSeries series;
  private StockRollersDatabase database = StockRollersDatabase.getInstance();
  private MainViewModel viewModel;
  private Long stockId;
  private List<History> histories;
  private List<LocalDate> dates;

  /**
   * This Class does may more that it should.
   * It Inflates the History Fragment.
   * Maps A Plot to the fragment.
   * Then grabs the data from the database based on the ViewModels Current Stock.
   * Then Converts Dates into positions.
   * Attaches a panZoom to the Plot.
   * Invalidates the view to re draw.
   * @param inflater gives the fragment a way to inflate the fragment
   * @param container where the view is going to reside
   * @param savedInstanceState if it had one
   * @return
   */
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_history, container, false);
    viewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
    plot1 = view.findViewById(R.id.plot);
    progressBar = view.findViewById(R.id.waiting_history);
    progressBar.setVisibility(View.VISIBLE);

    viewModel.getStock().observe(this, (stock) -> {
      stockId = stock.getId();
      this.stock = stock;
    });

    viewModel.getHistory().observe(this, (histories) -> {
      this.histories = histories;
      Date[] years = new Date[histories.size()];
      Number[] price = new Number[histories.size()];
      for (int i = 0; i < histories.size(); i++) {
        years[i] = Date.from(histories.get(i).getDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
        price[i] = histories.get(i).getClose();
      }
      int rightPointer = 0;
      int leftPointer = years.length - 1;
      Date dateTemp;
      Number priceTemp;
      while (leftPointer >= rightPointer) {
        dateTemp = years[rightPointer];
        priceTemp = price[rightPointer];
        years[rightPointer] = years[leftPointer];
        price[rightPointer] = price[leftPointer];
        years[leftPointer] = dateTemp;
        price[leftPointer] = priceTemp;
        rightPointer++;
        leftPointer--;
      }

      XYSeries series1 = new SimpleXYSeries(Arrays.asList(price), ArrayFormat.Y_VALS_ONLY, "Close Price");

      plot1.addSeries(series1, new LineAndPointFormatter(Color.DKGRAY, Color.blue(5555), Color.TRANSPARENT, null));

      plot1.setRangeBoundaries(null, null, BoundaryMode.AUTO);

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

      plot1.setTitle(stock.getNasdaqName());

      // draw a domain tick for each year:
      plot1.setDomainStep(StepMode.SUBDIVIDE, 12);
      plot1.getGraph().setLinesPerDomainLabel(10);

      // customize our domain/range labels
      plot1.setDomainLabel("Date");
      plot1.setRangeLabel("Price per Share");

      // get rid of decimal points in our range labels:
      plot1.getGraph().getLineLabelStyle(XYGraphWidget.Edge.LEFT)
          .setFormat(new DecimalFormat("0.0"));

      plot1.getOuterLimits().set(0, years.length, 0, years.length);

      plot1.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM)
//          .setFormat(new SimpleDateFormat("MMM yyyy", Locale.US));
          .setFormat(new Format() {
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
      PanZoom.attach(plot1, Pan.BOTH, Zoom.STRETCH_BOTH);
      progressBar.setVisibility(View.GONE);
      plot1.invalidate();
    });
    return view;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public void onSaveInstanceState(Bundle bundle) {
    bundle.putSerializable(SERIES_TITLE, series.getyVals().toArray(new Number[]{}));
  }
}
