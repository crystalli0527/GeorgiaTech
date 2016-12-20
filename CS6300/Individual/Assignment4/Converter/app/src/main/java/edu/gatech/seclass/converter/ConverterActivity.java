package edu.gatech.seclass.converter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.text.DecimalFormat;

public class ConverterActivity extends AppCompatActivity {

    private RadioButton rbFromMile;
    private RadioButton rbFromKm;
    private RadioButton rbToKm;
    private RadioButton rbToMile;

    // added for converting between more units
    private RadioButton rbFromM;
    private RadioButton rbToM;
    private RadioButton rbFromFeet;
    private RadioButton rbToFeet;
    private RadioButton rbFromInch;
    private RadioButton rbToInch;
    private RadioButton rbFromCm;
    private RadioButton rbToCm;

    private EditText distValue;
    private EditText distResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rbFromMile = (RadioButton)findViewById(R.id.rbFromMile);
        rbFromKm = (RadioButton)findViewById(R.id.rbFromKm);
        rbToMile = (RadioButton)findViewById(R.id.rbToMile);
        rbToKm = (RadioButton)findViewById(R.id.rbToKm);

        // added for converting to new units
        rbFromFeet = (RadioButton)findViewById(R.id.rbFromFeet);
        rbToFeet = (RadioButton)findViewById(R.id.rbToFeet);
        rbFromInch = (RadioButton)findViewById(R.id.rbFromInch);
        rbToInch = (RadioButton)findViewById(R.id.rbToInch);
        rbFromM = (RadioButton)findViewById(R.id.rbFromM);
        rbToM = (RadioButton)findViewById(R.id.rbToM);
        rbFromCm = (RadioButton)findViewById(R.id.rbFromCm);
        rbToCm = (RadioButton)findViewById(R.id.rbToCm);

        distValue = (EditText)findViewById(R.id.distValue);
        distResult = (EditText)findViewById(R.id.distResult);

    }

    public void handleClick(View view) {

        String unitFrom = "Mile";
        String unitTo = "Mile";

        switch (view.getId()) {
            case R.id.buttonConvert:
                String value = distValue.getText().toString();
                if (value.length() > 0) {
                    // KM
                    if (rbFromKm.isChecked()) {
                        unitFrom = "Km";
                    }
                    if (rbToKm.isChecked()) {
                        unitTo = "Km";
                    }

                    // MI
                    if (rbFromMile.isChecked()) {
                        unitFrom = "Mile";
                    }
                    if (rbToMile.isChecked()) {
                        unitTo = "Mile";
                    }

                    // FEET
                    if (rbFromFeet.isChecked()) {
                        unitFrom = "Feet";
                    }
                    if (rbToFeet.isChecked()) {
                        unitTo = "Feet";
                    }

                    // INCH
                    if (rbFromInch.isChecked()) {
                        unitFrom = "In";
                    }
                    if (rbToInch.isChecked()) {
                        unitTo = "In";
                    }

                    // METER
                    if (rbFromM.isChecked()) {
                        unitFrom = "M";
                    }
                    if (rbToM.isChecked()) {
                        unitTo = "M";
                    }

                    // CM
                    if (rbFromCm.isChecked()) {
                        unitFrom = "Cm";
                    }
                    if (rbToCm.isChecked()) {
                        unitTo = "Cm";
                    }

                    if (unitFrom.contentEquals(unitTo)) {
                        distResult.setText(value);
                    }

                    else {
                        switch (unitFrom) {
                            case "Km":
                                switch (unitTo) {
                                    case "Mile":
                                        distResult.setText(kmToM(mToMiles(value)));
                                        break;
                                    case "Feet":
                                        distResult.setText(kmToM(mToFeet(value)));
                                        break;
                                    case "In":
                                        distResult.setText(kmToM(mToInch(value)));
                                        break;
                                    case "M":
                                        distResult.setText(kmToM(value));
                                        break;
                                    case "Cm":
                                        distResult.setText(kmToM(mToCm(value)));
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            case "Mile":
                                switch (unitTo) {
                                    case "Km":
                                        distResult.setText(milesToM(mToKm(value)));
                                        break;
                                    case "Feet":
                                        distResult.setText(milesToFeet(value));
                                        break;
                                    case "In":
                                        distResult.setText(milesToM(mToInch(value)));
                                        break;
                                    case "M":
                                        distResult.setText(milesToM(value));
                                        break;
                                    case "Cm":
                                        distResult.setText(milesToM(mToCm(value)));
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            case "Feet":
                                switch (unitTo) {
                                    case "Km":
                                        distResult.setText(feetToM(mToKm(value)));
                                        break;
                                    case "Mile":
                                        distResult.setText(feetToM(mToMiles(value)));
                                        break;
                                    case "In":
                                        distResult.setText(feetToInch(value));
                                        break;
                                    case "M":
                                        distResult.setText(feetToM(value));
                                        break;
                                    case "Cm":
                                        distResult.setText(feetToM(mToCm(value)));
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            case "In":
                                switch (unitTo) {
                                    case "Km":
                                        distResult.setText(inchToM(mToKm(value)));
                                        break;
                                    case "Mile":
                                        distResult.setText(inchToM(mToMiles(value)));
                                        break;
                                    case "Feet":
                                        distResult.setText(inchToM(mToFeet(value)));
                                        break;
                                    case "M":
                                        distResult.setText(inchToM(value));
                                        break;
                                    case "Cm":
                                        distResult.setText(inchToM(mToCm(value)));
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            case "M":
                                switch (unitTo) {
                                    case "Km":
                                        distResult.setText(mToKm(value));
                                        break;
                                    case "Mile":
                                        distResult.setText(mToMiles(value));
                                        break;
                                    case "Feet":
                                        distResult.setText(mToFeet(value));
                                        break;
                                    case "In":
                                        distResult.setText(mToInch(value));
                                        break;
                                    case "Cm":
                                        distResult.setText(mToCm(value));
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            case "Cm":
                                switch (unitTo) {
                                    case "Km":
                                        distResult.setText(cmToM(mToKm(value)));
                                        break;
                                    case "Mile":
                                        distResult.setText(cmToM(mToMiles(value)));
                                        break;
                                    case "Feet":
                                        distResult.setText(cmToM(mToFeet(value)));
                                        break;
                                    case "M":
                                        distResult.setText(cmToM(value));
                                        break;
                                    case "In":
                                        distResult.setText(cmToM(mToInch(value)));
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            default:
                                break;

                        }
                       /* if (unitFrom.contentEquals("Mile")) {
                            distResult.setText(milesToKm(value));
                        }
                        else {
                            distResult.setText(kmToMiles(value));
                        }*/
                    }
                }
                else {
                    Context context = getApplicationContext();
                    CharSequence text = "Empty Value!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                break;

            case R.id.buttonReset:
                distValue.setText("");
                distResult.setText("");
                rbToMile.setChecked(true);
                rbFromMile.setChecked(true);
                break;
        }
    }

    // using to Meters as intermediate conversion
    public String milesToM(String strMiles) {
        double miles = Double.parseDouble(strMiles);
        double m = miles * 1609.344;
        DecimalFormat format = new DecimalFormat("#.############");
        return String.valueOf(format.format(m));
    }

    public String feetToM(String strFeet) {
        double feet = Double.parseDouble(strFeet);
        double m = feet * 0.3048;
        DecimalFormat format = new DecimalFormat("#.############");
        return String.valueOf(format.format(m));
    }

    public String inchToM(String strInch) {
        double inch = Double.parseDouble(strInch);
        double m = inch * .0254;
        DecimalFormat format = new DecimalFormat("#.############");
        return String.valueOf(format.format(m));
    }

    public String kmToM(String strKm) {
        double km = Double.parseDouble(strKm);
        double m = km * 1000;
        DecimalFormat format = new DecimalFormat("#.############");
        return String.valueOf(format.format(m));
    }

    public String cmToM(String strCm) {
        double cm = Double.parseDouble(strCm);
        double m = cm / 100;
        DecimalFormat format = new DecimalFormat("#.############");
        return String.valueOf(format.format(m));
    }

    // now convert meters to your final unit
    public String mToKm(String strM) {
        double m = Double.parseDouble(strM);
        double km = m * .001;
        DecimalFormat format = new DecimalFormat("#.############");
        return String.valueOf(format.format(km));
    }

    public String mToMiles(String strM) {
        double m = Double.parseDouble(strM);
        double mile = m * 0.000621371192;
        DecimalFormat format = new DecimalFormat("#.############");
        return String.valueOf(format.format(mile));
    }

    public String mToFeet(String strM) {
        double m = Double.parseDouble(strM);
        double feet = m * 3.28084;
        DecimalFormat format = new DecimalFormat("#.############");
        return String.valueOf(format.format(feet));
    }

    public String mToInch(String strM) {
        double m = Double.parseDouble(strM);
        double inch = m * 39.3701;
        DecimalFormat format = new DecimalFormat("#.############");
        return String.valueOf(format.format(inch));
    }

    public String mToCm(String strM) {
        double m = Double.parseDouble(strM);
        double cm = m * 100;
        DecimalFormat format = new DecimalFormat("#.############");
        return String.valueOf(format.format(cm));
    }

    public String milesToFeet(String strMile) {
        double mile = Double.parseDouble(strMile);
        double ft = mile * 5280;
        DecimalFormat format = new DecimalFormat("#.############");
        return String.valueOf(format.format(ft));
    }

    public String feetToInch(String strFeet) {
        double feet = Double.parseDouble(strFeet);
        double inch = feet * 12;
        DecimalFormat format = new DecimalFormat("#.############");
        return String.valueOf(format.format(inch));
    }




    // provided by instructor methods
    public String milesToKm(String strMiles) {
        double miles = Double.parseDouble(strMiles);
        double km = miles * 1.60934;
        DecimalFormat format = new DecimalFormat("#.##");
        return String.valueOf(format.format(km));
    }

    public String kmToMiles(String strKm) {
        double km = Double.parseDouble(strKm);
        double miles = km / 1.60934;
        DecimalFormat format = new DecimalFormat("#.##");
        return String.valueOf(format.format(miles));
    }
}
