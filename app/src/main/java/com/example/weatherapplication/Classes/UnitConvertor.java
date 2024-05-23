package com.example.weatherapplication.Classes;

import android.content.SharedPreferences;

import java.util.Locale;

public class UnitConvertor {



    public static float convertTemperature(float temperature, SharedPreferences sp) {
        // Use "°F" as the default unit if the preference is not set
        String unit = sp.getString("unit", "°F");

        return convertTemperature(temperature, unit);
    }
    public static float convertTemperature(float temperature, String unit) {
        float result;
        switch (unit) {
            case "°F":
                result = celciusToFahrenheit(temperature);
                break;
            case "°C":
                result = temperature;
                // Optional: You may want to handle this case differently.
                // backGroundDialogeCall.doDialogueExecute("no");
                break;
            case "°K":
                result = celciusToKelvin(temperature);
                // Optional: You may want to handle this case differently.
                // backGroundDialogeCall.doDialogueExecute("no");
                break;
            default:
                // Use the default Fahrenheit conversion when the unit is not recognized
                result = celciusToFahrenheit(temperature);
                break;
        }
        return result;
    }


    public static float celciusToKelvin(float celciusTemp) {
        return celciusTemp+ 273.15f;
    }

    public static float celciusToFahrenheit(float celciusTemp) {
        return ( celciusTemp* 9/5) + 32;
    }

    public static float convertRain(float rain, SharedPreferences sp) {
        String lengthUnit = sp.getString("lengthUnit", "mm");
        if (lengthUnit.equals("mm")) {
            return rain;  // Rainfall is already in millimeters
        } else {
            // Convert millimeters to inches
            return (float) (rain / 25.4);
        }
    }


    public static String getRainString(double rain, double percentOfPrecipitation, SharedPreferences sp) {
        StringBuilder sb = new StringBuilder();
        if (rain > 0) {
            sb.append(" (");
            String lengthUnit = sp.getString("lengthUnit", "mm");
            boolean isMetric = lengthUnit.equals("mm");

            if (rain < 0.1) {
                sb.append(isMetric ? "<0.1" : "<0.01");
            } else if (isMetric) {
                sb.append(String.format( Locale.ENGLISH, "%.1f %s", rain, lengthUnit));
            } else {
                sb.append(String.format(Locale.ENGLISH, "%.2f %s", rain, lengthUnit));
            }

            if (percentOfPrecipitation > 0) {
                sb.append(", ").append(percentOfPrecipitation * 100).append("%");
            }

            sb.append(")");
        }

        return sb.toString();
    }

    public static float convertPressure(float pressure, SharedPreferences sp) {
        if (sp.getString("pressureUnit", "hPa").equals("kPa")) {
            return pressure / 10;
        } else if (sp.getString("pressureUnit", "hPa").equals("mm Hg")) {
            return (float) (pressure * 0.750061561303);
        } else if (sp.getString("pressureUnit", "hPa").equals("in Hg")) {
            return (float) (pressure * 0.0295299830714);
        } else {
            return pressure;
        }
    }

    public static double convertPressure(double pressure, String unit) {
        double result;
        switch (unit) {
            case "kPa":
                result = pressure / 10;
                break;
            case "mm Hg":
                result = pressure * 0.750061561303;
                break;
            case "in Hg":
                result = pressure * 0.0295299830714;
                break;
            default:
                result = pressure;
                break;
        }
        return result;
    }

    public static double convertWind(double wind, SharedPreferences sp) {
        double result;
        String unit = sp.getString("speedUnit", "mph");
        switch (unit) {
            case "kph":
                result = wind * 3.6;
                break;
            case "mph":
                result = wind * 2.23693629205;
                break;
            case "kn":
                result = wind * 1.943844;
                break;
            case "bft":
                result = convertWindIntoBFT(wind);
                break;
            default:
                result = wind * 2.23693629205;
                break;
        }
        return result;
    }

    public static double convertWind(double wind, String unit) {
        double result;
        switch (unit) {
            case "kph":
                result = wind * 3.6;
                break;
            case "mph":
                result = wind * 2.23693629205;
                break;
            case "kn":
                result = wind * 1.943844;
                break;
            case "bft":
                result = convertWindIntoBFT(wind);
                break;
            default:
                result = wind;
                break;
        }
        return result;
    }

    private static double convertWindIntoBFT(double wind) {
        int result;
        if (wind < 0.3) {
            result = 0; // Calm
        } else if (wind < 1.5) {
            result =  1; // Light air
        } else if (wind < 3.3) {
            result =  2; // Light breeze
        } else if (wind < 5.5) {
            result =  3; // Gentle breeze
        } else if (wind < 7.9) {
            result =  4; // Moderate breeze
        } else if (wind < 10.7) {
            result =  5; // Fresh breeze
        } else if (wind < 13.8) {
            result =  6; // Strong breeze
        } else if (wind < 17.1) {
            result =  7; // High wind
        } else if (wind < 20.7) {
            result =  8; // Gale
        } else if (wind < 24.4) {
            result =  9; // Strong gale
        } else if (wind < 28.4) {
            result =  10; // Storm
        } else if (wind < 32.6) {
            result =  11; // Violent storm
        } else {
            result =  12; // Hurricane
        }
        return result;
    }



    }