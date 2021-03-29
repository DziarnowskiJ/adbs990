package uk.ac.city.adbs990.bug;

import city.cs.engine.DestructionEvent;
import city.cs.engine.DestructionListener;
import uk.ac.city.adbs990.tools.DynamicImages;
import uk.ac.city.adbs990.Game;
import uk.ac.city.adbs990.worldElements.Portal;

public class BugDestruction implements DestructionListener {
    /**
     * Removes Bug from the 'global bug list' <br>
     * If the list is empty (all bugs have been destroyed)
     * portal to another level appears
     */
    @Override
    public void destroy(DestructionEvent destructionEvent) {
        Bug.getBugList().remove(destructionEvent.getSource());
        //if there are no more bugs
        if (Bug.getBugList().isEmpty()) {
            Portal portal = new Portal(Game.getLevel());
        }
    }
}
