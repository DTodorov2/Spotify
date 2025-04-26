# Spotify Clone - Music Streaming Platform

## Project Description

This project aims to create a **client-server** application inspired by the popular music streaming platform **Spotify**.    
The application will allow users to stream music in real-time, manage playlists, and interact with a rich library of songs through a command-line interface.

---

## Features

### 1. **User Authentication**
- Users can **register** and **log in** to the platform using their email and password.
- User credentials will be securely stored in a file system, using the SHA256 algorithm.

### 2. **Song Management**
- The server will host a collection of songs in the **.wav** format for streaming.
- The server will provide song metadata, such as title, artist and playings count, and allow users to search for songs by keywords present in the song name or artist.

### 3. **Real-Time Streaming**
- The client will stream songs from the server using **SourceDataLine** for real-time audio playback, utilizing **javax.sound.sampled**.
- Song data will be streamed in real-time, with the ability to play audio directly on the client without fully downloading the file beforehand.

### 4. **Playlist Management**
- Users can create, remove, manage, and customize **playlists**, which will be saved in files.
- Users can add songs to their playlists and view the contents of any playlist.
- Users can remove songs from their playlists.

### 5. **Top Charts**
- The server will maintain statistics on the most played songs and generate **top charts** that can be accessed by the client.

### 6. **Command-Line Interface**
The client will feature a simple and intuitive command-line interface with the following commands:
- `register <email> <password>` - Register a new user.
- `login <email> <password>` - Log in to an existing user account.
- `logout` - Log out from the current account.
- `disconnect` - Disconnect from the server.
- `search <keywords>` - Search for songs by name or artist.
- `top <number>` - Retrieve a list of the most popular songs.
- `create-playlist <name>` - Create a new playlist.
- `remove-playlist <name>` - Remove a specified playlist.
- `add-song-to <playlist_name> <song>` - Add a song to a specified playlist.
- `remove-song-from <playlist_name> <song>` - Remove a song from a specified playlist.
- `show-playlist <name>` - Show the contents of a playlist.
- `play <song>` - Play a song from the library.
- `stop` - Stop playing the song.
- `show-all-commands` - Show every possible command the user can execute.

### 7. **Error Handling and Logging**
- Proper error messages will be displayed to the user when invalid commands are entered or when issues arise, such as connection failures or problems with the server.
- Detailed **error logs** will be kept in files for troubleshooting and further development.
