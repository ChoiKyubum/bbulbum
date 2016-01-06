package com.sonimbang.sonimbang.sonimbangadmin;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sonimbang.sonimbang.sonimbangadmin.util.DayClickListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CalendarView extends LinearLayout {

    private List<LinearLayout> rows = new ArrayList<LinearLayout>();
    private LayoutInflater inflater;
    private CalendarAdapter adapter;
    private Context context;
    private List<String> mItems;
    private List<Map<String, String>> monthlyScheduleList;
    private Date today = new Date();
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public int month = 0;
    public int year = 2014;
    String todayStr;
    int snb_orange;
    int snb_green;
    int snb_red;
    View preV;
    Drawable preDrawable;
    int pixels;

    private DayClickListener mListener;

    public void setOnDayClickLisntener(DayClickListener listener){
        mListener = listener;
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        // TODO Auto-generated constructor stub
    }

    public CalendarView(Context context) {
        super(context);
        init(context);
        // TODO Auto-generated constructor stub
    }

    public CalendarView(Context context, int month, int year,
                        List<Map<String, String>> monthlyList) {
        super(context);
        final float scale = context.getResources().getDisplayMetrics().density;
        pixels = (int) (10 * scale + 0.5f);
        this.context = context;
        inflater = LayoutInflater.from(context);
        snb_orange = context.getResources().getColor(R.color.snb_orange);
        snb_green = context.getResources().getColor(R.color.snb_green);
        snb_red = context.getResources().getColor(R.color.snb_red);
        this.month = month;
        this.year = year;
        this.monthlyScheduleList = monthlyList;
        init(context);
    }

    public void init(Context context) {
        todayStr = format.format(today);
        try {
            today = format.parse(todayStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        adapter = new CalendarAdapter(month, year);
        mItems = adapter.mItems;
        this.setOrientation(LinearLayout.VERTICAL);
        try {
            drawCalendar();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //initBar();
        this.setPadding(0, 0, 0, pixels);
    }

    public void drawCalendar() throws ParseException {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < mItems.size(); i++) {
            int barPosition = 0;
            int curRowPosition = (int) i / 7;
            int date[] = adapter.getDate(i);
            if (i % 7 == 0) {
                LinearLayout row = (LinearLayout) inflater.inflate(R.layout.calendar_item, null);
                this.addView(row);
                rows.add(row);
            }
            int dayId = getResources().getIdentifier(
                    "id/day" + Integer.toString(i - curRowPosition * 7), "id",
                    "com.sonimbang.sonimbang.sonimbangadmin");
            int barsId = getResources().getIdentifier(
                    "id/bar" + Integer.toString(i - curRowPosition * 7), "id",
                    "com.sonimbang.sonimbang.sonimbangadmin");
            TextView day = (TextView) rows.get(curRowPosition).findViewById(
                    dayId);
            LinearLayout barLayout = (LinearLayout) rows.get(curRowPosition)
                    .findViewById(barsId);
            day.setTextColor(Color.parseColor("#323232"));
            if (date == null) { //요일표시
                day.setGravity(Gravity.CENTER);
                day.setTextColor(Color.parseColor("#646464"));
                day.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.border_bottom_gray));
                if (i % 7 == 0) {
                    day.setTextColor(context.getResources().getColor(R.color.snb_orange));
                    day.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.border_bottom_orange));
                }if ((i + 1) % 7 == 0) {
                    day.setTextColor(context.getResources().getColor(R.color.snb_green));
                    day.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.border_bottom_green));
                }
                    barLayout.setVisibility(View.GONE);
            } else {
                day.setGravity(Gravity.RIGHT);
                String dateStr = Integer.toString(date[2]) + "-"
                        + Integer.toString(date[1] + 1) + "-"
                        + Integer.toString(date[0]);
                final Date postDate = format.parse(dateStr);
                if (date[1] != month) {
                    day.setTextColor(Color.parseColor("#aaaaaa"));
                    ((View)day.getParent()).setBackgroundDrawable(context.getResources().getDrawable(R.drawable.stroke));
                } else if (i % 7 == 0)
                    day.setTextColor(context.getResources().getColor(R.color.snb_orange)); //일요일
                else if ((i + 1) % 7 == 0)
                    day.setTextColor(context.getResources().getColor(R.color.snb_green)); //토요일

                for (Map<String, String> map : monthlyScheduleList) {
                    Date firstDate = format.parse(map.get("start"));
                    Date lastDate = map.get("end").compareTo("") == 0 ? firstDate : format.parse(map.get("end"));
                    if (postDate.equals(firstDate) || (postDate.after(firstDate) && !postDate.after(lastDate))){
                        if(barPosition<3) {
                            TextView scheduleText = ((TextView) barLayout.getChildAt(barPosition++));
                            scheduleText.setText(map.get("title"));
                            if(map.get("wr_5").equals("예약대기"))
                                scheduleText.setTag(snb_red);
                            else if(map.get("wr_5").equals("입금대기"))
                                scheduleText.setTag(snb_orange);
                            else
                                scheduleText.setTag(snb_green);
                            scheduleText.setTextColor(Integer.parseInt(scheduleText.getTag().toString()));
                        }else{
                            TextView scheduleText = ((TextView) barLayout.getChildAt(2));
                            scheduleText.setText("+" + ((barPosition++) - 1) + "개");
                            if(map.get("wr_5").equals("예약대기"))
                                scheduleText.setTag(snb_red);
                            else if(map.get("wr_5").equals("입금대기"))
                                scheduleText.setTag(snb_orange);
                            else
                                scheduleText.setTag(snb_green);
                            scheduleText.setTextColor(Integer.parseInt(scheduleText.getTag().toString()));
                        }
                    }
                }
                ((View)day.getParent()).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (preV != null) {
                            preV.setBackgroundDrawable(preDrawable);
                            LinearLayout bars = (LinearLayout) ((LinearLayout) preV).getChildAt(1);
                            for (int i = 0; i < bars.getChildCount(); i++) {
                                TextView textView = (TextView) (bars.getChildAt(i));
                                try {
                                    textView.setTextColor(Integer.parseInt(textView.getTag().toString()));
                                }catch (NullPointerException e){
                                    e.printStackTrace();
                                }

                            }

                        }
                        preDrawable = v.getBackground();
                        mListener.onDayClick(format.format(postDate));
                        v.setBackgroundColor(context.getResources().getColor(R.color.snb_blue_alpha));
                        LinearLayout bars = (LinearLayout) ((LinearLayout) v).getChildAt(1);
                        for (int i = 0; i < bars.getChildCount(); i++) {
                            TextView textView = (TextView) (bars.getChildAt(i));
                            textView.setTag(bars.getChildAt(i).getTag());
                            textView.setTextColor(context.getResources().getColor(R.color.snb_white));
                        }
                        preV = v;
                    }
                });

                if(postDate.equals(today))
                    ((View)day.getParent()).setBackgroundDrawable(context.getResources().getDrawable(R.drawable.stroke_blue));
            }
            day.setText(mItems.get(i));
        }
    }
}
