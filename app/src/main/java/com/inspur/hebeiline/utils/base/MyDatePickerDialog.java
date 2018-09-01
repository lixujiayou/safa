package com.inspur.hebeiline.utils.base;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.inspur.hebeiline.R;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by lixu on 2018/6/22.
 */

public class MyDatePickerDialog extends Dialog implements NumberPicker.OnValueChangeListener, View.OnClickListener {
    private Context context;
    private OnSureListener onSureListener;

    private TextView tv_title_year, tv_title_month;
    private MyNumberPicker mnp_year, mnp_month;
    private Button btn_sure, btn_cancel;

    public MyDatePickerDialog(Context context) {
        super(context);
        this.context = context;
    }

    public interface OnSureListener {
        void back(String name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_view);

        tv_title_year = (TextView) findViewById(R.id.tv_title_year);
        tv_title_month = (TextView) findViewById(R.id.tv_title_month);
        mnp_year = (MyNumberPicker) findViewById(R.id.mnp_year);
        mnp_month = (MyNumberPicker) findViewById(R.id.mnp_month);
        btn_sure = (Button) findViewById(R.id.btn_sure);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);

        mnp_year.setOnValueChangedListener(this);
        mnp_month.setOnValueChangedListener(this);

        btn_sure.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT + 08:00"));
        mnp_year.setMaxValue(2050);
        mnp_year.setMinValue(1900);
        mnp_year.setValue(calendar.get(Calendar.YEAR));
        tv_title_year.setText(String.valueOf(calendar.get(Calendar.YEAR)));

        mnp_month.setMaxValue(12);
        mnp_month.setMinValue(1);
        mnp_month.setValue(calendar.get(Calendar.MONTH) + 1);
        tv_title_month.setText(String.valueOf(calendar.get(Calendar.MONTH) + 1));

        // set NumberPicker font color and divider color
        setNumberPickerDividerColor(mnp_year, context.getResources().getColor(R.color.blue_s));
        setNumberPickerTextColor(mnp_year, context.getResources().getColor(R.color.black));
        setNumberPickerDividerColor(mnp_month, context.getResources().getColor(R.color.blue_s));
        setNumberPickerTextColor(mnp_month, context.getResources().getColor(R.color.black));

        // Prohibit rolling
        mnp_year.setWrapSelectorWheel(false);
        mnp_month.setWrapSelectorWheel(false);

    }


    public void setOnSureListener(OnSureListener onSureListener) {
        this.onSureListener = onSureListener;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_sure) {
            onSureListener.back(String.valueOf(tv_title_year.getText().toString() + "-" + tv_title_month.getText().toString()));
            this.dismiss();

        } else if (i == R.id.btn_cancel) {
            this.dismiss();

        }
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        int i = picker.getId();
        if (i == R.id.mnp_year) {
            tv_title_year.setText(String.valueOf(newVal));

        } else if (i == R.id.mnp_month) {
            tv_title_month.setText(String.valueOf(newVal));

        }
    }


    /**
     * set NumberPicker font color
     *
     * @param numberPicker：NumberPicker
     * @param color：int
     * @return boolean
     */
    public static boolean setNumberPickerTextColor(NumberPicker numberPicker, int color) {
        final int count = numberPicker.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = numberPicker.getChildAt(i);
            if (child instanceof EditText) {
                Field selectorWheelPaintField;
                try {
                    selectorWheelPaintField = numberPicker.getClass().getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    try {
                        ((Paint) selectorWheelPaintField.get(numberPicker)).setColor(color);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    ((EditText) child).setTextColor(color);
                    numberPicker.invalidate();
                    return true;
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }


    /**
     * set NumberPicker divider color
     *
     * @param numberPicker：NumberPicker
     * @param color：int
     */
    public static void setNumberPickerDividerColor(NumberPicker numberPicker, int color) {
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field SelectionDividerField : pickerFields) {
            if (SelectionDividerField.getName().equals("mSelectionDivider")) {
                SelectionDividerField.setAccessible(true);
                try {
                    SelectionDividerField.set(numberPicker, new ColorDrawable(color));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}