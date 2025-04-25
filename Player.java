import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Player extends ImageView {
    private double velocityX = 0;
    private double velocityY = 0;
    private boolean onGround = false;
    private boolean facingRight = true; // 记录角色朝向
    private boolean isFireboy;

    private static final double MAX_SPEED_X = 3;   // 最大水平速度
    private static final double ACCELERATION = 2; // 加速度
    private static final double FRICTION = 0.3;   // 摩擦力
    private static final double JUMP_FORCE = -8; // 跳跃力
    private static final double AIR_DRAG = 0;  // 空气阻力 (跳跃时的X轴减速)
    private static final double HITBOX_MARGIN = 5; // 碰撞检测边界
    private static final double JUMP_COOLDOWN_TIME = 0.5; // 1 秒冷却
    private double jumpCooldown = 0; // 跳跃冷却时间（秒）

    private Image idleImage;
    private Image walkImage;
    private Image jumpImage;

    public Player(boolean isFireboy, double startX, double startY) {
        this.isFireboy = isFireboy;
        setFitWidth(30);
        setFitHeight(45);
        setX(startX);
        setY(startY);
        
        if (isFireboy) {
            idleImage = new Image("assets/Fidle.gif");
            walkImage = new Image("assets/Fwalk.gif");
            jumpImage = new Image("assets/Fjump.gif");
        } else {
            idleImage = new Image("assets/widle.gif");
            walkImage = new Image("assets/wwalk.gif");
            jumpImage = new Image("assets/wjump.gif");
        }
        setImage(idleImage);

    }

   

    public void moveLeft() {
        velocityX = Math.max(velocityX - ACCELERATION, -MAX_SPEED_X);
        facingRight = false;
        updateAnimation();
        //System.out.println("左移: " + velocityX);
    }

    public void moveRight() {
        velocityX = Math.min(velocityX + ACCELERATION, MAX_SPEED_X);
        facingRight = true;
        updateAnimation();
        //System.out.println("右移: " + velocityX);
    }

    public void jump() {
        //System.out.println("tryjump"+jumpCooldown);
        if (onGround && jumpCooldown <= 0) { // 只有在地面上且冷却结束才能跳跃
            velocityY = JUMP_FORCE;
            onGround = false; 
            //System.out.println("startjump"+jumpCooldown);
            jumpCooldown = JUMP_COOLDOWN_TIME; // 设定冷却时间
            updateAnimation();
            //System.out.println("jump"+jumpCooldown);
        }
    }

    public void applyPhysics(double deltaTime) {
        if (!onGround) {
            velocityY += 0.5;
            velocityX *= (1 - AIR_DRAG);
        }else{
            velocityY = 0;
            velocityX *= (1 - FRICTION);
        }
        // 更新位置
        setX(getX() + velocityX);
        setY(getY() + velocityY);
        
        // 处理跳跃冷却
        if (jumpCooldown > 0) {
            jumpCooldown -= deltaTime;
            if (jumpCooldown <= 0) {
                jumpCooldown = 0; // 确保不会变负
                //System.out.println("OK to jump"+jumpCooldown);
            }
            
        }

        // 停止微小漂移
        if (Math.abs(velocityX) < 0.01) {
            velocityX = 0;
        }
    }

    public void updateAnimation() {
        if (velocityY < 0) { // 跳跃动画
            setImage(jumpImage);
        } else if (velocityX != 0) { // 行走动画
            setImage(walkImage);
        } else { // 闲置动画
            setImage(idleImage);
        }

        // 处理镜像翻转
        if (facingRight) {
            setScaleX(1); // 正常朝右
        } else {
            setScaleX(-1); // 镜像翻转朝左
        }
    }


    public void setOnGround(boolean value) {
        this.onGround = value;
        if (value) velocityY = 0; // 避免掉落时速度积累
        updateAnimation();
    }

    //参数传递
    // 获取底部碰撞区域
    public Bounds getBoundsBottom() {
        return new BoundingBox(getX() + 0.2 * getFitWidth(), getY() + getFitHeight() , 0.6 * getFitWidth() , HITBOX_MARGIN);
    }   

    // 获取顶部碰撞区域
    public Bounds getBoundsTop() {
        return new BoundingBox(getX() + 0.2 * getFitWidth(), getY() , 0.6 * getFitWidth() , HITBOX_MARGIN);
    }

    // 获取左右两侧碰撞区域
    public Bounds getBoundsLeft() {
        return new javafx.geometry.BoundingBox(getX(), getY() + 0.2 * getFitHeight(),HITBOX_MARGIN, 0.6*getFitHeight());
    }
    public Bounds getBoundsRight() {
        return new javafx.geometry.BoundingBox(getX() + getFitWidth(), getY() + 0.2 * getFitHeight(),HITBOX_MARGIN, 0.6*getFitHeight());
    }

    public double getVelocityX() {
        return velocityX;
    }
    
    public double getVelocityY() {
        return velocityY;
    }
    
    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }
    
    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }
}
