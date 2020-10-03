# Peer To Peer Networks File Distribution
This repository includes a networks project in college, course Computer Networks.
## Project Description
 Using peer classes (i.e. each class is both a server and a client).
 Design a protocol for peer-to-peer file request. [1 mark]
o Design format of request
o Design format of reply (must include 200 ok – 400 error)
 Number of peers and files must be dynamic (minimum 3 files and 4 peers). [1 mark]
 Each peer is directed to a folder and reads files from it.
 Peer has a hash function that is used to convert filename(s) to a hash key. Then the value of hash key and content of file are saved in a table of Key-Value pairs that refers to the files of this peer. [1 mark] [Note you can use any hash function]
o Key is a hashed filename.
o Value is the file content.
o Example:
 User 1 has File1.txt and it is hashed to 12
 User2 has File2.txt and it is hashed to 13
 User3 has File3.txt and it is hashed to 100
 Use of Tracker.txt [1 mark]
o It saves the hashed filename and the peers/ users who downloaded it.
o Available at all peers
 Login [1 mark]
o Each peer enters its username & password and send them to tracker server
 Tracker server must handle the case if a wrong username or password are entered
o Once a peer is logged in. Tracker server save its connection.
 It sends a message to all other peers stating the file(s) that it has so they will update their Tracker.txt.
 Finally peer will download the recent copy of Tracker.txt
 File download and update [2 marks]
o Ex: user4 wants to download File3
 User4 will use its hash function and convert name to hash key 100
 User4 will send to all peers until it download File3
 User4 will send a message to all the other peers “User4 has downloaded File3”
 Tracker.txt will be updated (100 user3, user4)
 User4 will update File3
 Changes made to File3 will be applied to File3 at any peer (not only user4)
 If the file is divided into to several parts and the new user wants to download the whole file (bonus). [+1 mark]
o Ex: File2.txt is hashed to 13 and is divided to two parts one in user2 and one in user3 and user1 wants to download file
o User1 will use hash function and convert File2.txt to hash key 13
o It will use Tracker.txt and it find key 13 and then 2 which mean that file is divided to two parts.
o User1 will download two files and append them to each other to make full version of the file
o User1 will update Tracker.txt with new row “13 user1”. Which means that user1 has the full version of file.

