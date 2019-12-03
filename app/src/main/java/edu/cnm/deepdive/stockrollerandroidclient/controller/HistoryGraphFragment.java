package edu.cnm.deepdive.stockrollerandroidclient.controller;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.androidplot.Plot;
import com.androidplot.ui.SeriesRenderer;
import com.androidplot.util.PixelUtils;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.SimpleXYSeries.ArrayFormat;
import com.androidplot.xy.StepMode;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.XYSeriesFormatter;
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
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class HistoryGraphFragment extends Fragment {

  private static final String SERIES_TITLE = "Signthings in USA";

  private XYPlot plot1;
  private SimpleXYSeries series;
  private StockRollersDatabase database = StockRollersDatabase.getInstance();
  private MainViewModel viewModel;
  private Long stockId;
  private List<History> histories;
  private List<LocalDate> dates;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_home, container, false);
    viewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
    plot1 = (XYPlot) view.findViewById(R.id.plot);

    viewModel.getStock().observe(this, (stock) -> stockId = stock.getId());

    viewModel.getHistory().observe(this, (histories) -> {
      this.histories = histories;
      dates = new LinkedList<>();
      for (History history : histories) {
        dates.add(history.getDate());
      }
      // these will be our domain index labels:
      Date[] years = new Date[dates.size()];
      for (int i = 0; i < years.length; i++) {
        years[i] = Date.from(dates.get(i).atStartOfDay(ZoneId.systemDefault()).toInstant());
      }
      Number[] price = new Number[dates.size()];
      for (int i = 0; i < price.length; i++) {
        price[i] = histories.get(i).getClose();
      }
      XYSeries series1 = new SimpleXYSeries(Arrays.asList(price), ArrayFormat.Y_VALS_ONLY, "Close Price");
          //price, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series1");
      //XYSeries series2 = new SimpleXYSeries(
          //Arrays.asList(series2Numbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series2");

      plot1.addSeries(series1, new XYSeriesFormatter() {
        @Override
        public Class<? extends SeriesRenderer> getRendererClass() {
          return null;
        }

        @Override
        protected SeriesRenderer doGetRendererInstance(Plot plot) {
          return null;
        }
      });
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
    });

    return super.onCreateView(inflater, container, savedInstanceState);
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
