# NetBeans IDE Plugin: Explore Location in OS

NetBeans IDE plugin which adds menu action to open the OS'es file explorer.

## How it works

Adds a new menu item, "Explore Location in OS", to the context menu (aka right-click menu) in the following explorer views in the IDE:

- in the Projects Explorer: All folder-type nodes which represent a real folder on the disk has the menu item.
- in the File Explorer: All folder-types nodes has the menu item.



## Screenshots

Right-click on a source code folder:

![Screenshot 2022-03-21 215312](https://user-images.githubusercontent.com/32431476/159367361-d085f45f-8788-41f8-868e-5b0269974518.png)


Right-click on a Project:

![Screenshot 2022-03-21 215339](https://user-images.githubusercontent.com/32431476/159367370-67180fd3-251e-485b-bbe1-ed57980b0d89.png)

## Installation
The plugin is available from the Official NetBeans Plugin update center under the name of "Explore Location in OS".

From within the NetBeans IDE: Go to _Tools --> Plugins --> Available Plugins_ and you will find the plugin in the list.


## How it works

The action simply uses [Desktop.open()](https://docs.oracle.com/en/java/javase/11/docs/api/java.desktop/java/awt/Desktop.html#open(java.io.File)) 
and should therefore work equally well on any platform supported by AWT/Swing.

## Heritage

The plugin is somewhat similar to some previous plugins by various authors, now no longer maintained, most notably the Explore-from-here plugin.

However, the plugin presented here is a lot simpler than its predecessors and therefore easier to maintain.
