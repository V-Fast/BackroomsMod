# Devlogs  
This file contains devlogs. Some features shown in them may arrive a few months after its first publication.  
**Remember that we are only 3 people working on the mod. And we all have lives besides the mod.** I (Lumaa) am not a professional Java developer, I really started learning by creating this mod.  
<sup>(btw we use this date format: DD/MM/YYYY)</sup>  

* * *

# Devlog #4 - 16/01/2023
Happy new year and welcome!  
I was very tempted to write a Devlog last week but finally, I didn't. Why? Before saying why, let me put you in the context:  
I began streaming (in French) just a few hours away of 2023, only problem is that I am a developer not a gamer, so, I stream the BackroomsMod development on [my Twitch](https://twitch.tv/u_Lumaa_). Most of the streams will be in French but if you wanna come around and talk to me in English, you totally can! (We'd actually be glad lmao).  
As I'm writing this, we currently have 269 downloads on [Modrinth](https://modrinth.com/mod/backrooms), and I never got to do it but **thank you very much** to everyone that downloaded the mod. All this went really quick and we hit 100 and 200 downloads really soon! Some mods have this chance like ours and some others have less chance, this was just supposed to be a little project and now it's at its biggest. So thank you for the 6 first months of The Backrooms Mod!!

*PS: I promise to release the Alpha A0.0.5 before March 2023*

* * *

# Devlog #3 - 28/12/2022
Hey hey.  
Quick devlog here to talk about the radio block we talked about in the [first Devlog](#devlog-1---08122022). So now, it's a total solid idea and it will be added in. AND (very important), we were allowed to use any music made by [Good Kid](https://youtube.com/@GoodKidband)! They've allowed us to use any of their musics in our mod for some credit.  
You maybe don't know who they are so you maybe can check them out!

Also while I'm at it, we have a big issue with LibuLib and will (definitely) not release for Alpha A0.0.5. Stay tuned for it though it's coming really soon <3

* * *

# Devlog #2 - 18/12/2022
Hello again!  
This devlog is kind of unexpected for me since I said that this devlog was supposed to release in 2023, but if you haven't seen it, we shown in [REPOS.txt](https://github.com/u-lumaa/BackroomsMod/blob/main/libs/REPOS.txt) my newest creation. **LibuLib**. It is, for now, my personal Minecraft library (I'll see in the future if I ever open source **LibuLib**). Why did I do this? Well first, I made it to not repeat code in projects, here's an example: [Your Name Your Death's Colors.java](https://github.com/u-lumaa/your-name-your-death/blob/main/src/main/java/com/lumaa/ynyd/utils/Colors.java) and [The Backrooms Mod's Colors.java](https://github.com/u-lumaa/BackroomsMod/blob/541ef3e3764cf9249e5c9d495d78751edb26aec2/src/main/java/com/lumination/backrooms/utils/Colors.java) are the 2 same files.  
After implementing **LibuLib**. I'll just have to import the `Colors.java` from **LibuLib**. But there will be more in the future.
 
Since [last time](#devlog-1---08122022), I fixed the Tape Player's hitbox, we reverted back to 1.19.1, the radio has been remodeled and retextured, and the original songs will come much much later in 2023. And I (almost) began working on the Level 0's labyrinth. (Fun fact: Internally, we made a lot of notes on "How can we achieve this" using Apple's latest app "[Freeform](https://apps.apple.com/app/freeform/id6443742539)" on iOS, iPadOS and macOS)
 
Thank you everyone for reading this **<3**
 
* * *

# Devlog #1 - 08/12/2022
First devlog, wooo!  
What have we been on working on? We've been trying to make the Bacteria work again, but we still fail relentlessly. We also updated the mod in 1.19.2 because of GeckoLib being inexistant in 1.19.1. But now the Tape Player lost his custom hitbox, and I don't know how to fix it lol.  
We have **songs being made** just for our mod by PatateGivree, it will be a music tape. We also thought of making a radio, when you right click on it, it scrolls through all the music tapes (without using tapes). It's been thought of and modeled, but it's unknown if it's really an idea to consider.  

I sadly still have not found a way to generate the Level 0's labyrinth. I thought of something that may work, but it's an idea, and an hard-coded way
