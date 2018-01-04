package creature;
import field.Field;
import field.Position;
import javafx.scene.image.Image;

public class Huluwa implements Creature {

    public enum COLOR {
        赤, 橙, 黄, 绿, 青, 蓝, 紫
    }

    public enum SENIORITY {
        一, 二, 三, 四, 五, 六, 七
    }

    private COLOR color;
    private SENIORITY seniority;
    private Position position;
    private Boolean isDead;
    private Field field;
    private Thread thread = null;
    private Image image = null;
    private Image imageDied = null;

    public Huluwa(COLOR color, SENIORITY seiority, Field f) {
        field = f;
        isDead = false;
        this.color = color;
        this.seniority = seiority;
        switch (this.seniority){
            case 一:
                image = new Image(this.getClass().getResourceAsStream("/Red.png"));
                imageDied = new Image(this.getClass().getResourceAsStream("/RedDead.png"));
                break;
            case 二:
                image = new Image(this.getClass().getResourceAsStream("/Orange.png"));
                imageDied = new Image(this.getClass().getResourceAsStream("/OrangeDead.png"));
                break;
            case 三:
                image = new Image(this.getClass().getResourceAsStream("/Yellow.png"));
                imageDied = new Image(this.getClass().getResourceAsStream("/YellowDead.png"));
                break;
            case 四:
                image = new Image(this.getClass().getResourceAsStream("/Green.png"));
                imageDied = new Image(this.getClass().getResourceAsStream("/GreenDead.png"));
                break;
            case 五:
                image = new Image(this.getClass().getResourceAsStream("/Cyan.png"));
                imageDied = new Image(this.getClass().getResourceAsStream("/CyanDead.png"));
                break;
            case 六:
                image = new Image(this.getClass().getResourceAsStream("/Blue.png"));
                imageDied = new Image(this.getClass().getResourceAsStream("/BlueDead.png"));
                break;
            case 七:
                image = new Image(this.getClass().getResourceAsStream("/Purple.png"));
                imageDied = new Image(this.getClass().getResourceAsStream("/PurpleDead.png"));
                break;
            default://error
                break;
        }
    }

    @Override
    public Thread getThread() {
        return thread;
    }

    @Override
    public void setThread(Thread t){
        thread = t;
    }

    public char getNum(){
        switch (this.seniority){
            case 一:
                return (!isDead())?'Q':'I';
            case 二:
                return (!isDead())?'W':'O';
            case 三:
                return (!isDead())?'E':'P';
            case 四:
                return (!isDead())?'R':'A';
            case 五:
                return (!isDead())?'T':'S';
            case 六:
                return (!isDead())?'Y':'D';
            case 七:
                return (!isDead())?'U':'F';
            default://e
                return '_';
        }
    }

    @Override
    public void setDead(Boolean dead) {
        isDead = dead;
    }

    @Override
    public Boolean isDead(){
        return isDead;
    }

    public COLOR getColor() {
        return color;
    }

    public SENIORITY getSeniority() {
        return seniority;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public void setPosition(Position position) {
        this.position = position;
        position.setHolder(this);
    }

    @Override
    public int getSide(){
        return 1;
    }

    @Override
    public Image report() {
        if(!isDead())
            return image;
        else
            return imageDied;
    }

    @Override
    public void run(){
        while (field.getRunAllThread()) {
            //System.out.println("Huluwa " + color + " Move");
            //找到最近的一个对手
            int len = 9999;
            int x = -1;
            int y = -1;
            for (int i = 0; i < field.sizeX; ++i) {
                for (int j = 0; j < field.sizeY; ++j) {
                    if (field.getCreatures()[i][j].getSide() == -this.getSide() && field.getCreatures()[i][j].isDead() == false) {
                        int distance = Math.abs(position.getX() - i) + Math.abs(position.getY() - j);
                        if (distance < len) {
                            len = distance;
                            x = i;
                            y = j;
                        }
                    }
                }
            }
            //距离小于1就打起来
            if (len == 1) {
                if (Math.random() > 0.6)
                    this.setDead(true);
                else
                    field.getCreatures()[x][y].setDead(true);
            }
            //否则看这一排还有没有对手
            else if (field.isRowHaveEnemy(this)) {
                field.Delete(position.getX(), position.getY());
                if (position.getX() > x) {
                    if (position.getX() - 1 < 0 || !field.getCreatures()[position.getX() - 1][position.getY()].getClass().getSimpleName().equals("Space"))
                        field.Add(position.getX(), position.getY(), this);
                    else
                        field.Add(position.getX() - 1, position.getY(), this);
                } else {
                    if (position.getX() + 1 >= field.sizeX || !field.getCreatures()[position.getX() + 1][position.getY()].getClass().getSimpleName().equals("Space"))
                        field.Add(position.getX(), position.getY(), this);
                    else
                        field.Add(position.getX() + 1, position.getY(), this);
                }
            }
            //没有就去其他排
            else {
                field.Delete(position.getX(), position.getY());
                if (position.getY() > y) {
                    if (position.getY() - 1 < 0 || !field.getCreatures()[position.getX()][position.getY() - 1].getClass().getSimpleName().equals("Space"))
                        field.Add(position.getX(), position.getY(), this);
                    else
                        field.Add(position.getX(), position.getY() - 1, this);
                } else {
                    if (position.getY() + 1 >= field.sizeY || !field.getCreatures()[position.getX()][position.getY() + 1].getClass().getSimpleName().equals("Space"))
                        field.Add(position.getX(), position.getY(), this);
                    else
                        field.Add(position.getX(), position.getY() + 1, this);
                }
            }
            if (this.isDead()) {
                System.out.println("Huluwa " + getPosition() + " Died");
                thread.interrupt();
                break;
            }
            try {
                Thread.sleep((int) (Math.random() * 1000) + 1000);
                while(field.getIsDisplaying())
                    Thread.sleep(500);
            } catch (Exception e) {
                thread.interrupt();
                break;
            }
        }
    }
}
