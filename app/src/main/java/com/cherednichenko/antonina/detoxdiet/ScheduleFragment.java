package com.cherednichenko.antonina.detoxdiet;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cherednichenko.antonina.detoxdiet.db.ProgramsDatabaseHelper;
import com.cherednichenko.antonina.detoxdiet.detox_diet_data.DataProcessor;
import com.cherednichenko.antonina.detoxdiet.detox_diet_data.EventInfo;
import com.cherednichenko.antonina.detoxdiet.detox_diet_data.ProgramInfo;
import com.github.tibolte.agendacalendarview.AgendaCalendarView;
import com.github.tibolte.agendacalendarview.CalendarPickerController;
import com.github.tibolte.agendacalendarview.models.BaseCalendarEvent;
import com.github.tibolte.agendacalendarview.models.CalendarEvent;
import com.github.tibolte.agendacalendarview.models.DayItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by tonya on 9/8/16.
 */
public class ScheduleFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);

        Calendar minDate = Calendar.getInstance();
        Calendar maxDate = Calendar.getInstance();

        ProgramsDatabaseHelper databaseHelper = ProgramsDatabaseHelper.getInstance(getActivity());
        List<EventInfo> dbEvents = databaseHelper.getAllEvents();

        minDate.add(Calendar.MONTH, -1);
        minDate.set(Calendar.DAY_OF_MONTH, 1);
        maxDate.add(Calendar.YEAR, 1);

        List<CalendarEvent> calendarEvents = mockList(dbEvents);


        ((AgendaCalendarView) rootView.findViewById(R.id.agenda_calendar_view)).init(calendarEvents, minDate, maxDate, Locale.getDefault(), new CalendarPickerController() {
            @Override
            public void onDaySelected(DayItem dayItem) {

            }

            @Override
            public void onEventSelected(CalendarEvent event) {

            }

            @Override
            public void onScrollToDate(Calendar calendar) {

            }
        });

        return rootView;
    }

    private List<CalendarEvent> mockList(List<EventInfo> dbEvents) {
        List<CalendarEvent> calendarEvents = new ArrayList<>();
        for (EventInfo dbEvent : dbEvents) {
            Calendar start = Calendar.getInstance();
            start.setTimeInMillis(dbEvent.getTime());

            Calendar end = Calendar.getInstance();
            ProgramInfo program = dbEvent.getProgram();
            end.add(Calendar.DAY_OF_YEAR, program.getDuration());
            BaseCalendarEvent event = new BaseCalendarEvent(program.getName(), program.getShortDescription(), String.format("%s days", program.getDuration()),
                    ContextCompat.getColor(getContext(), R.color.colorPrimary), start, end, true);
            calendarEvents.add(event);
        }
        return calendarEvents;
    }

}
