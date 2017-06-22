package net.rizov.balloondestroyer.room.play.entity;

public class Burst {

    private BurstType type;

    private int score;

    public Burst(BurstType type, int score) {
        super();
        this.type = type;
        this.score = score;
    }

    public BurstType getType() {
        return type;
    }

    public void setType(BurstType type) {
        this.type = type;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

}
