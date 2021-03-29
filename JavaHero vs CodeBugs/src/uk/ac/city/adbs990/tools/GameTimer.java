package uk.ac.city.adbs990.tools;

import city.cs.engine.StepEvent;
import city.cs.engine.StepListener;

public class GameTimer implements StepListener {
    private static int counter = 0;
    private static boolean timerRunning;

    /**
     * Creates a game timer counting steps passed
     */
    public GameTimer() {
    }

    /**
     * Get number of steps passed
     * @return number of steps passed
     */
    public static int getTime() {
        return counter;
    }

    /**
     * Stop the timer
     */
    public static void stopTimer() {
        timerRunning = false;
    }

    /**
     * Start the timer
     */
    public static void startTimer() {
        timerRunning = true;
    }

    /**
     * Reset the timer
     */
    public static void resetTimer() {
        counter = 0;
    }

    /**
     * Set number of steps passed
     * <p>
     * Used during loading a save
     * </p>
     * @param counter number of steps passed
     */
    public static void setTimer(int counter) {
        GameTimer.counter = counter;
    }

    /**
     * If Timer is not stopped, increment counter every step
     */
    @Override
    public void preStep(StepEvent stepEvent) {
        if (timerRunning) {
            GameTimer.counter++;
        }
    }

    @Override
    public void postStep(StepEvent stepEvent) {

    }
}
