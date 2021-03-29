package uk.ac.city.adbs990.tools;

import city.cs.engine.Body;
import city.cs.engine.StepEvent;
import city.cs.engine.StepListener;
import uk.ac.city.adbs990.Game;

public class DelayedDestroy implements StepListener {
    private Body body;
    private StepListener stepListener;
    private int counter;
    private float delayTime;

    //constructor for destroying Bodies
    /**
     * Creates a step listener that destroys a body after specified time
     * @param body Body to be destroyed
     * @param delayTime Time after the body is destroyed
     */
    public DelayedDestroy(Body body, float delayTime) {
        this.body = body;
        this.counter = 0;
        this.delayTime = delayTime;
    }

    //constructor for removing StepListeners
    /**
     * Creates a step listener that removes other step listener after specified time
     * @param stepListener Step Listener to be removed
     * @param delayTime Time after the step listener is removed
     */
    public DelayedDestroy(StepListener stepListener, float delayTime) {
        this.stepListener = stepListener;
        this.counter = 0;
        this.delayTime = delayTime;
    }

    /**
     * Counts frames passed
     */
    @Override
    public void preStep(StepEvent stepEvent) {
        counter++;
        if (body != null) {                                 //destruction of Body
            if (counter >= delayTime * 60) {
                body.destroy();
            }
        } else if (stepListener != null) {                  //removing StepListener
            if (counter >= delayTime * 60) {
                Game.getLevel().removeStepListener(stepListener);
            }
        }
    }

    @Override
    public void postStep(StepEvent stepEvent) {

    }
}
