import javax.swing.*;

public class TimeManager {
    private static volatile double timeMultiplier = 1.0;
    private static JLabel multiplierLabel = null;

    public static void setTimeMultiplier(double multiplier) {
        timeMultiplier = multiplier;
        updateLabel();
    }

    public static double getTimeMultiplier() {
        return timeMultiplier;
    }

    public static long adjustTime(long baseTimeMillis) {
        return (long)(baseTimeMillis * timeMultiplier);
    }

    public static void bindLabel(JLabel label) {
        multiplierLabel = label;
        updateLabel();
    }

    private static void updateLabel() {
        if (multiplierLabel != null) {
            SwingUtilities.invokeLater(() ->
                    multiplierLabel.setText(" (--> Slower) Current Time Scale: x" + timeMultiplier)
            );
        }
    }
}