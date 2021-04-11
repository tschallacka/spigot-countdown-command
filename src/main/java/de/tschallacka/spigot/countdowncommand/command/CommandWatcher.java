package de.tschallacka.spigot.countdowncommand.command;

public class CommandWatcher
{
    protected final String command;
    protected final String waitMessage;
    protected final String abortedMessage;
    protected final int secondsTimer;

    public CommandWatcher(String command, String waitMessage, String abortedMessage, int secondsTimer)
    {
        this.command = command;
        this.waitMessage = waitMessage;
        this.abortedMessage = abortedMessage;
        this.secondsTimer = secondsTimer;
    }

    public String getCommand()
    {
        return command;
    }

    public String getWaitMessage()
    {
        return waitMessage;
    }

    public String getAbortedMessage()
    {
        return abortedMessage;
    }

    public int getSecondsTimer()
    {
        return secondsTimer;
    }
}
