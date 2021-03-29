package uk.ac.city.adbs990.tools;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;
import uk.ac.city.adbs990.bomb.Bomb;
import uk.ac.city.adbs990.bug.BugBoss;
import uk.ac.city.adbs990.worldElements.Portal;
import uk.ac.city.adbs990.worldElements.collectables.Collectables;
import uk.ac.city.adbs990.hero.Hero;

public class DynamicImages implements StepListener {
    private BodyImage image;
    private Body body;

    private Hero hero;
    private BugBoss bugBoss;

    private int counter = 0;
    private static final float pi = 3.14f;

    /**
     * Changes the BodyImage of specified body
     * <p>
     *     Depending on the body type it differently changes it: <br>
     *     - Hero or BugBoss -> cycles between body's images (harm and normal) making it 'blink' red
     *     - Portal or Collectables -> constantly rotates the image
     *     - Bomb -> gradually increases the size of the image to simulate the explosion
     * </p>
     * @param body Body that will change image
     */
    public DynamicImages(Body body) {
        this.body = body;
        if (body instanceof Hero) {                     //Hero
            this.hero = (Hero) body;
        } else if (body instanceof Portal) {            //Portal
            this.image = Portal.getImage();
        } else if (body instanceof Collectables) {      //Heart, Coin, BombToken
            this.image = Collectables.getImage();
        } else if (body instanceof Bomb) {              //Bomb
            this.image = Bomb.getImageExplosion();
        } else if (body instanceof BugBoss) {
            this.bugBoss = (BugBoss) body;
        }
    }

    @Override
    public void preStep(StepEvent stepEvent) {

    }

    /**
     * Changes BodyImage of a body
     */
    @Override
    public void postStep(StepEvent stepEvent) {
        counter++;
        if (body instanceof Portal                                        //body = Portal
                || body instanceof Collectables) {                        //body = Collectable (Coin/Heart/BombToken)

            body.removeAllImages();
            AttachedImage bodyImageRotation = new AttachedImage(
                    body,                           //body the image is attached to
                    image,                          //image
                    1,                         //scale
                    (2 * pi / 8 * counter / 30),    //rotation
                    new Vec2(0, 0));          //offset

        } else if (body instanceof Hero) {                                  //body = Hero
            //cycles between hero's images (harm and normal)
            if (counter < 10) {
                hero.setBodyImage("super-hero-" + hero.getDirection() + "-harm");
            } else if (counter > 10 && counter < 20) {
                hero.setBodyImage("super-hero-" + hero.getDirection());
            } else if (counter >= 20) {
                counter = 0;
            }

        } else if (body instanceof Bomb) {                                  //body = Bomb
            //gradually increases the size of the image

            //stop counter from increasing over 15
            if (counter >= 15) {
                counter = 15;
            }
            //add bigger image on top of the previous one
            AttachedImage bodyImageIncrease = new AttachedImage(
                    body,                           //body
                    image,                          //image
                    counter/2,                 //scale
                    0,                       //rotation
                    new Vec2(0, counter/7f));  //offset

        } else if (body instanceof BugBoss) {                               //body == BugBoss
            if (counter < 10) {
                bugBoss.setBodyImage("bugBoss-" + bugBoss.getDirection() + "-harm");
            } else if (counter > 10 && counter < 20) {
                bugBoss.setBodyImage("bugBoss-" + bugBoss.getDirection());
            } else if (counter >= 20) {
                counter = 0;
            }
        }
    }
}
