package uk.ac.city.adbs990.tools;

import city.cs.engine.DynamicBody;
import city.cs.engine.StaticBody;
import org.jbox2d.common.Vec2;
import uk.ac.city.adbs990.Game;
import uk.ac.city.adbs990.bomb.Bomb;
import uk.ac.city.adbs990.bug.BugBoss;
import uk.ac.city.adbs990.bug.BugMinion;
import uk.ac.city.adbs990.hero.Hero;
import uk.ac.city.adbs990.hero.HeroCollisions;
import uk.ac.city.adbs990.levels.*;
import uk.ac.city.adbs990.tools.GameTimer;
import uk.ac.city.adbs990.worldElements.Portal;
import uk.ac.city.adbs990.worldElements.collectables.BombToken;
import uk.ac.city.adbs990.worldElements.collectables.Coin;
import uk.ac.city.adbs990.worldElements.collectables.Heart;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class GameSaveLoader {

    /**
     * Saves the current game state in a file
     * @param level Current level
     * @param fileName  Name of the file state will be saved to
     * @throws IOException
     */
    public static void save(GameLevel level, String fileName) throws IOException {
        FileWriter writer = null;
        try {
            writer = new FileWriter(fileName, true);

            String bodyType;
            String moreInfo = null;

            writer.write( Game.getLevel().getLevelName() + "\n");

            for (StaticBody body : level.getStaticBodies()) {
                //initially set bodyType to null
                //and don't save info about
                // objects with null bodyType
                bodyType = null;

                //get position of a body
                float positionX = body.getPosition().x;
                float positionY = body.getPosition().y;

                if (body instanceof Coin) {
                    bodyType = "Coin";
                }
                else if (body instanceof Heart) {
                    bodyType = "Heart";
                }
                else if (body instanceof BombToken) {
                   bodyType = "BombToken";
                   moreInfo += ((BombToken) body).getBombNumber() + ",";
                }
                else if (body instanceof Portal) {
                    bodyType = "Portal";
                }

                //save body data
                if (bodyType != null) {
                    if (moreInfo != null) {
                        writer.write(bodyType + "," + positionX + "," + positionY + moreInfo + "\n");
                    } else {
                        writer.write(bodyType + "," + positionX + "," + positionY + "\n");
                    }
                }
            }

            for (DynamicBody body : level.getDynamicBodies()) {
                //initially set bodyType to null
                //and don't save info about
                // objects with null bodyType
                bodyType = null;
                moreInfo = null;

                //get position of a body
                float positionX = body.getPosition().x;
                float positionY = body.getPosition().y;

                if (body instanceof Hero) {
                    bodyType = "Hero";

                    int currentHealth = ((Hero) body).getCurrentHealth();
                    int maxHealth = ((Hero) body).getMaxHealth();
                    int strength = ((Hero) body).getStrength();
                    int coins = ((Hero) body).getCoins();
                    int bombNumber = ((Hero) body).getBombNumber();
                    boolean lookingLeft = ((Hero) body).isLookingLeft();
                    boolean useHand = ((Hero) body).canUseHand();
                    int totalCoins = Hero.getTotalCoins();
                    Vec2 linearVelocity = body.getLinearVelocity();

                    moreInfo = "," + currentHealth + ",";
                    moreInfo += maxHealth + ",";
                    moreInfo += strength + ",";
                    moreInfo += coins + ",";
                    moreInfo += bombNumber + ",";
                    moreInfo += lookingLeft + ",";
                    moreInfo += useHand + ",";
                    moreInfo += totalCoins + ",";
                    moreInfo += linearVelocity.x + "," + linearVelocity.y + ",";
                    moreInfo += GameTimer.getTime() + ",";

                }
                else if (body instanceof BugMinion) {
                    bodyType = "BugMinion";

                    int strength = ((BugMinion) body).getStrength();
                    int side;
                    float halfWalkingDistance = ((BugMinion) body).getHalfWalkingDistance();
                    Vec2 startPosition = ((BugMinion) body).getStartPosition();

                    if (((BugMinion) body).getDirection().equals("left")) {
                        side = 1;
                    } else {
                        side = 0;
                    }

                    moreInfo = "," + strength + ",";
                    moreInfo += side + ",";
                    moreInfo += halfWalkingDistance + ",";
                    moreInfo += startPosition.x + "," + startPosition.y + ",";

                }
                else if (body instanceof BugBoss) {
                    bodyType = "BugBoss";

                    int strength = ((BugBoss) body).getStrength();
                    int health = ((BugBoss) body).getHealth();
                    int side;

                    if (((BugBoss) body).getDirection().equals("left")) {
                        side = 1;
                    } else {
                        side = 0;
                    }

                    moreInfo = "," + strength + ",";
                    moreInfo += health + ",";
                    moreInfo += side + ",";

                }
                else if (body instanceof Bomb) {
                    bodyType = "Bomb";
                }

                if (bodyType != null) {
                    if (moreInfo != null) {
                        writer.write(bodyType + "," + positionX + "," + positionY + moreInfo + "\n");
                    } else {
                        writer.write(bodyType + "," + positionX + "," + positionY + "\n");
                    }
                }
            }

        } finally {
            if (writer != null) {
                writer.close();
            }
        }

    }

    /**
     * Loads the save
     * @param game Current Game
     * @param fileName  File name the save is loaded from
     * @return Returns newly loaded level
     * @throws IOException
     */
    public static GameLevel load(Game game, String fileName) throws IOException {
        FileReader fr = null;
        BufferedReader reader = null;
        GameLevel level = null;
        try {
            fr = new FileReader(fileName);
            reader = new BufferedReader(fr);
            String line = reader.readLine();

            //first line tells name of the level
            //create level
            if (line.equals("Level1")) {
                level = new Level1(game);
            } else if (line.equals("Level2")) {
                level = new Level2(game);
            } else if (line.equals("Level3")) {
                level = new Level3(game);
            } else if (line.equals("Level4")) {
                level = new Level4(game);
            }

            line = reader.readLine();

            while (line != null) {
                // file is assumed to contain one name, score pair per line
                String[] tokens = line.split(",");
                String name = tokens[0];

                if (name.equals("Hero")) {
                    Hero hero = new Hero(level);
                    hero.setPosition(new Vec2(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2])));

                    //set current health
                    hero.setCurrentHealth(Integer.parseInt(tokens[3]));
                    //set max  health
                    hero.setMaxHealth(Integer.parseInt(tokens[4]));
                    //set strength
                    hero.setStrength(Integer.parseInt(tokens[5]));
                    //set number of coins collected
                    hero.setCoins(Integer.parseInt(tokens[6]));
                    //set number of bombs
                    hero.setBombNumber(Integer.parseInt(tokens[7]));

                    //set Hero's looking direction
                    hero.updateIsLookingLeft(Boolean.parseBoolean(tokens[8]));
                    //set whether Hero can shoot hand
                    hero.setUseHand((Boolean.parseBoolean(tokens[9])));

                    //set number of coins Hero collected on previous levels
                    //(for more info read class Hero)
                    Hero.setTotalCoins(Integer.parseInt(tokens[10]));

                    //set linear velocity
                    hero.setLinearVelocity(new Vec2(Float.parseFloat(tokens[11]), Float.parseFloat(tokens[12])));

                    //set Game Timer
                    GameTimer.setTimer(Integer.parseInt(tokens[13]));

                    //add hero to the level
                    level.setHero(hero);

                    //add collision listener
                    HeroCollisions heroCollisions = new HeroCollisions(hero, level, game);
                    hero.addCollisionListener(heroCollisions);

                }
                else if (name.equals("Coin")) {
                    Coin coin = new Coin(level);
                    coin.setPosition(new Vec2(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2])));
                }
                else if (name.equals("Heart")) {
                    Heart heart = new Heart(level);
                    heart.setPosition(new Vec2(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2])));
                }
                else if (name.equals("BombToken")) {
                    BombToken bombToken = new BombToken(level, Integer.parseInt(tokens[3]));
                    bombToken.setPosition(new Vec2(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2])));
                }
                else if (name.equals("BugMinion")) {
                    BugMinion bug = new BugMinion(level,
                            Integer.parseInt(tokens[3]),
                            Integer.parseInt(tokens[4]),
                            Float.parseFloat(tokens[5]));
                    bug.setPosition(new Vec2(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2])));
                    bug.setStartPosition(new Vec2(Float.parseFloat(tokens[6]), Float.parseFloat(tokens[7])));
                }
                else if (name.equals("BugBoss")) {
                    BugBoss bug = new BugBoss(level, Integer.parseInt(tokens[3]), Integer.parseInt(tokens[5]));
                    bug.setPosition(new Vec2(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2])));
                    bug.setHealth(Integer.parseInt(tokens[4]));
                    bug.followHero();
                }
                else if (name.equals("Bomb")) {
                    Bomb bomb = new Bomb(level, new Vec2(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2])));
                    level.setBomb(bomb);
                }
                else if (name.equals("Portal")) {
                    Portal portal = new Portal(level);
                }

                line = reader.readLine();
            }

            return level;

        } finally {
            if (reader != null) {
                reader.close();
            }
            if (fr != null) {
                fr.close();
            }
        }
    }
}
