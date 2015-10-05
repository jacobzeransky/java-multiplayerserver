# java-multiplayerserver

Rewrite of python-multiplayerserver. Written with Eclipse Mars.

Intention is to have a application that allows you to connect with other people and play turn-based games against them. Client UI is now graphical compared to command-line based in python version, but much of the structure and methods are similar.

Threads on both server- and client-side communcation via queues, which are loaded by their respective delegator threads.

Has back-end database functionality with MySQL. Tables are user_state, user_profile, and game.

Server-side entry point is mpsServer.java, client-side is clientView.java. 
