# Rocket League bot from the RLBOT Community

This was a fun Hobbyproject about the beginning of my studies in computer science.

Here are some historic recordings of what the bot looked like and things it could do.

The second version of this Bot is [Dark Bunny](https://github.com/MrDiver/DarkBunny).

Official Matches
- [Qualifier 2019 Round 1 - Speedbot vs Psyonix Allstar](https://youtu.be/g7guszgnolM?t=2932)
- [Qualifier 2019 Round 2 - Speedbot vs MechJeb](https://youtu.be/g7guszgnolM?t=5717)
- [Qualifier 2019 Round 3 - Speedbot vs Calculator](https://youtu.be/g7guszgnolM?t=10867)
- [Qualifier 2019 Round 4 - Speedbot vs Psyonix Pro](https://youtu.be/g7guszgnolM?t=13534)
- [Qualifier 2019 Round 5 - Speedbot vs Boolean Algebra Calf](https://youtu.be/g7guszgnolM?t=18914)
# Some Recordings

Preview of the chaining event System. It enabled smooth transitions between command sequences for reuse of simple actions like wavedashes etc.
<video src="gifs/AddedChainSystem.mp4" controls></video>

This is an example how it can be used to produce a fast kickoff.
<video src="gifs/AddedRightDiagonalKickoff.mp4" controls></video>

For better routes on the field i implemented a grid version of A* but thist urned out to be really weird so i stomped this idea for a dynamic hybrid where the grid is created on the fly.
<video src="gifs/AStarSearchingForBoostpads.mp4" controls></video>

This turned out to be a much better and faster alternative which also enabled me to dynamically react to things on the map like enemies and deactivated boost pads.
<video src="gifs/NowWeAreTalkingPathFinding.mp4" controls></video>

If you now think i am crazy you are right! Because i created the ultimate boost chaser bot with this technique. 
<video src="gifs/BoostChasingBot.mp4" controls></video>

Another technique that can be implemented with this is stopping your enemy by locking them in to the rule.
<video src="gifs/IFoundAWayToStopIt.mp4" controls></video>

Now this system can be combined with the ball physics to produce a trajectory to always hit a shot towards the goal.
| <video src="gifs/PathingTest1.mp4" controls></video> | <video src="gifs/PathingTest2.mp4" controls></video> |
|-|-|
| <video src="gifs/PathingTest3HardCorner.mp4" controls></video> | <video src="gifs/PathingTest4Boomer.mp4" controls></video> |
| <video src="gifs/PathingTest5FightingWithMovement.mp4" controls></video> | <video src="gifs/PathingTest6AngledShots.mp4" controls></video> |
| <video src="gifs/PathingTest7ShotCompilation.mp4" controls></video> | <video src="gifs/WHATASHOT.mp4" controls></video> |

And with all this in place you can just go ham and implement some more moves which can be chained to other moves like dribbling.

<video src="gifs/that%20circle%20is%20there.mp4" controls></video>

Here is some more things from that
<video src="gifs/Thatswhatiwanttosee.mp4" controls></video>