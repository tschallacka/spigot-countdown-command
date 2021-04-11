## Spigot countdown commands

This plugin was made, so a server owner can decide which commands need a cooldown before they are executed.  
Any command specified in the config.yml will wait for the timer to run out, before executing it.
This is also the case for non-existing commands. It does not "check" if a command exists, so you could use this to spoof the existence of commands.

There is no bypass for the timeout, server operators, players, everybody goes into timeout.

This plugin was coded for minecraft 1.16.4 - 1.16.5

```yaml
# Default wait message if nothing is specified 
# at the individual command level
wait-message: "Don't move until the timer has run out."

# Default abort message if player moved nothing 
# is specified at the individual command level
aborted-message: "STOP FIDGETING!"

# Default countdown in seconds if nothing is 
# specified at the individual command level
countdown-duration-in-seconds: 5

# Define which commands need a timeout
commands:
  # just a name, you can name this anything you want.
  server:
    # The command that would be executed by the player
    command: /server
    # Duration time for this command
    countdown-duration-in-seconds: 7
    # Aborted message for this command
    aborted-message: "Countdown aborted!"
    # Wait message for this command.
    wait-message: "You'll change servers in 7 seconds, stand still."
  # just a name
  hub:
    # the command to be executed
    command: /hub
    # this command will utilize the default message
    # values defined at the top of this file.
```