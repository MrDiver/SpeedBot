# Rocket League bot from the RLBOT Community

This was a fun Hobbyproject about the beginning of my studies in computer science.

Here are some historic gifs what the bot looked like and things it could do.

# Some Gifs

Preview of the chaining event System. It enabled smooth transitions between command sequences for reuse of simple actions like wavedashes etc.
![Chaining Event System](gifs/AddedChainSystem.gif)

This is an example how it can be used to produce a fast kickoff.
![Kickoff](gifs/AddedRightDiagonalKickoff.gif)

For better routes on the field i implemented a grid version of A* but thist urned out to be really weird so i stomped this idea for a dynamic hybrid where the grid is created on the fly.
![](gifs/AStarSearchingForBoostpads.gif)

This turned out to be a much better and faster alternative which also enabled me to dynamically react to things on the map like enemies and deactivated boost pads.
![](gifs/NowWeAreTalkingPathFinding.gif)

If you now think i am crazy you are right! Because i created the ultimate boost chaser bot with this technique. 
![](gifs/BoostChasingBot.gif)

Another technique that can be implemented with this is stopping your enemy by locking them in to the rule.
![](gifs/IFoundAWayToStopIt.gif)

Now this system can be combined with the ball physics to produce a trajectory to always hit a shot towards the goal.
| ![](gifs/PathingTest1.gif) | ![](gifs/PathingTest2.gif)|
|-|-|
|![](gifs/PathingTest3HardCorner.gif)|![](gifs/PathingTest4Boomer.gif)|
|![](gifs/PathingTest5FightingWithMovement.gif) | ![](gifs/PathingTest6AngledShots.gif)|
|![](gifs/PathingTest7ShotCompilation.gif)| ![](gifs/WHATASHOT.gif)|

And with all this in place you can just go ham and implement some more moves which can be chained to other moves like dribbling.

![](gifs/that%20circle%20is%20there.gif)

Here is some more things from that
![](gifs/Thatswhatiwanttosee.gif)