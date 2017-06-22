package net.rizov.balloondestroyer.room.play;

public enum PlayRoomEvent {
    pause,
    resume,
    restart,
    lose,
    win,
    quit,
    showLeaderBoard,
    submitScore;

    public int score;
}
