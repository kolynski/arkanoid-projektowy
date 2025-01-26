public enum Difficulty {
    EASY(3.0),    
    MEDIUM(3.0), 
    HARD(4.0);    

    private final double speed;

    Difficulty(double speed) {
        this.speed = speed;
    }

    public double getSpeed() { return speed; }
}