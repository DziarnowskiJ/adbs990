package uk.ac.city.adbs990.bug;

import city.cs.engine.StepEvent;
import city.cs.engine.StepListener;
import org.jbox2d.common.Vec2;

public class BugTracker implements StepListener {
    private int counter = 0;

    /**
     * Checks whether BugMinion is in its 'walking distance' <br>
     * When it gets to its maximum distance Bug changes direction
     */
    public void preStep(StepEvent e) {
        counter++;
        for (Bug bugInstance : Bug.getBugList()) {
            if (bugInstance instanceof BugMinion) {             //responsible for BugMinion movement
                /* check if bug is in its walking distance
                 * when it goes out of it, bug changes direction
                 */
                BugMinion bug = (BugMinion)bugInstance;
                if (bug.getHalfWalkingDistance() == 0) {
                    //make sure bug doesn't move
                    bug.startWalking(0);
                } else if (bug.getPosition().x >= bug.getStartPosition().x + bug.getHalfWalkingDistance()) {
                    //bug gets close to its most right position
                    //make it go left
                    bug.changeWalking(-3);
                } else if (bug.getPosition().x <= bug.getStartPosition().x - bug.getHalfWalkingDistance()) {
                    //bug gets close to its most left position
                    //make it go right
                    bug.changeWalking(3);
                }
            }
        }
    }

    public void postStep(StepEvent e) {

    }
}