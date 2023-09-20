This Android app is primarily designed for individuals with ADHD. It empowers users to create lists of tasks (and even customize templates for these lists), sort tasks and lists, modify them, copy them, and more. The app stores and retrieves data using SQLite, utilizing a 1:n relationship database format. To ensure smooth and fast scrolling, I implemented various techniques based on advice from Stack Overflow, such as performing time-expensive operations only during the initial loading of a RecyclerView row and storing data in variables to minimize redundant operations. Similarly, data retrieval from the SQLite database occurs only when opening and closing each task list, saving processing time.

Play store link:
https://play.google.com/store/apps/details?id=pac.underpackage.brainhelper

Link of the repo subfolder, where is my main source code:
https://github.com/DvorakuvPadawan/MindHelper/tree/main/app/src/main/java
