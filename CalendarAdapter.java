package com.sonimbang.sonimbang.sonimbangadmin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class CalendarAdapter{
	private GregorianCalendar mCalendar;
	private Calendar mCalendarToday;
	public List<String> mItems;
	private int mMonth;
	private int mYear;
	private int mDaysShown;
	private int mDaysLastMonth;
	private int mDaysNextMonth;
	private final String[] mDays = { "일", "월", "화", "수", "목", "금", "토" };
	private final int[] mDaysInMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30,
			31, 30, 31 };
	public int height;

	public CalendarAdapter(int month,
			int year) {
		mMonth = month;
		mYear = year;
		mCalendar = new GregorianCalendar(mYear, mMonth, 1);
		mCalendarToday = Calendar.getInstance();
		populateMonth();
	}

	/**
	 * @param date
	 *            - null if day title (0 - dd / 1 - mm / 2 - yy)
	 * @param position
	 *            - position in item list
	 * @param item
	 *            - view for date
	 */

	private void populateMonth() {
		mItems = new ArrayList<String>();
		for (String day : mDays) {
			mItems.add(day);
			mDaysShown++;
		}

		int firstDay = getDay(mCalendar.get(Calendar.DAY_OF_WEEK));
		int prevDay;
		if (mMonth == 0)
			prevDay = daysInMonth(11) - firstDay + 1;
		else
			prevDay = daysInMonth(mMonth - 1) - firstDay + 1;
		for (int i = 0; i < firstDay; i++) {
			mItems.add(String.valueOf(prevDay + i));
			mDaysLastMonth++;
			mDaysShown++;
		}

		int daysInMonth = daysInMonth(mMonth);
		for (int i = 1; i <= daysInMonth; i++) {
			mItems.add(String.valueOf(i));
			mDaysShown++;
		}

		mDaysNextMonth = 1;
		while (mDaysShown % 7 != 0) {
			mItems.add(String.valueOf(mDaysNextMonth));
			mDaysShown++;
			mDaysNextMonth++;
		}

	}

	private int daysInMonth(int month) {
		int daysInMonth = mDaysInMonth[month];
		if (month == 1 && mCalendar.isLeapYear(mYear))
			daysInMonth++;
		return daysInMonth;
	}

	private int getDay(int day) {
		switch (day) {
		case Calendar.MONDAY:
			return 1;
		case Calendar.TUESDAY:
			return 2;
		case Calendar.WEDNESDAY:
			return 3;
		case Calendar.THURSDAY:
			return 4;
		case Calendar.FRIDAY:
			return 5;
		case Calendar.SATURDAY:
			return 6;
		case Calendar.SUNDAY:
			return 0;
		default:
			return 0;
		}
	}

	private boolean isToday(int day, int month, int year) {
		if (mCalendarToday.get(Calendar.MONTH) == month
				&& mCalendarToday.get(Calendar.YEAR) == year
				&& mCalendarToday.get(Calendar.DAY_OF_MONTH) == day) {
			return true;
		}
		return false;
	}

	public int[] getDate(int position) {
		int date[] = new int[3];
		if (position <= 6) {
			return null; // day names
		} else if (position <= mDaysLastMonth + 6) {
			// previous month
			date[0] = Integer.parseInt(mItems.get(position));
			if (mMonth == 0) {
				date[1] = 11;
				date[2] = mYear - 1;
			} else {
				date[1] = mMonth - 1;
				date[2] = mYear;
			}
		} else if (position <= mDaysShown - mDaysNextMonth) {
			// current month
			date[0] = position - (mDaysLastMonth + 6);
			date[1] = mMonth;
			date[2] = mYear;
		} else {
			// next month
			date[0] = Integer.parseInt(mItems.get(position));
			if (mMonth == 11) {
				date[1] = 0;
				date[2] = mYear + 1;
			} else {
				date[1] = mMonth + 1;
				date[2] = mYear;
			}
		}
		return date;
	}
}
